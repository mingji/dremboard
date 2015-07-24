//
//  DremToDremboardViewController.m
//  dremboard
//
//  Created by YingLi on 7/10/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremToDremboardViewController.h"
#import "Dremboard.h"
#import "UIViewController+MJPopupViewController.h"

@interface DremToDremboardViewController ()

@end

@implementation DremToDremboardViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    // Do any additional setup after loading the view from its nib.
    [self initAttributes];
    [self initUI];
    [self startGetDremboardsList];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)initAttributes {
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
}

- (void) setAttributes:(int)dremId {
    mDremId = dremId;
}

#pragma mark - Init UIs
- (void) initUI {
    // init dremboard lists
    mArrayDremboards = [[NSMutableArray alloc] init];
    
    self.mCmbDremboard = [[DownPicker alloc] initWithTextField:self.mTxtDremboard];
    [self.mCmbDremboard showArrowImage:YES];
    [self.mCmbDremboard setPlaceholder:@"---"];
    
    [self.mCmbDremboard addTarget:self
                          action:@selector(onSelectedDremboard:)
                forControlEvents:UIControlEventValueChanged];
}

- (void) onSelectedDremboard:(id) downpicker {
    mStrDremboard = [self.mCmbDremboard text];
}

- (void) updateDremboardList {
    NSMutableArray *arrayStrBoards = [[NSMutableArray alloc] init];
    
    for (DremboardInfo *boardInfo in mArrayDremboards) {
        [arrayStrBoards addObject:boardInfo.media_title];
    }
    
    [self.mCmbDremboard setData:arrayStrBoards];
}

- (int) getDremboardId:(NSString *)boardTitle {
    int retId = -1;
    
    if (!boardTitle)
        return retId;
    
    for (int index = 0; index < [mArrayDremboards count]; index++) {
        DremboardInfo *dremboard = [mArrayDremboards objectAtIndex:index];
        if ([dremboard.media_title isEqualToString:boardTitle]) {
            retId = dremboard.dremboard_id;
            break;
        }
    }
    
    return retId;
}

- (void) closeView {
    if (self.delegate && [self.delegate respondsToSelector:@selector(closeDremToBoardView:)]) {
        [self.delegate closeDremToBoardView:self];
    }
}

- (void) showAddSuccessAlert {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Add to Drēmboard"
                                                    message:@"THIS DRĒM HAS BEEN ADDED TO YOUR DRĒMBOARD, THANKS."
                                                   delegate:self
                                          cancelButtonTitle:nil
                                          otherButtonTitles:@"OK", nil];
    [alert show];

}

#pragma mark - UIAlertView Delegate
- (void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    
    // click OK
    if (buttonIndex == [alertView firstOtherButtonIndex]) {
        [self closeView];
    }
    // click cancel
    else if (buttonIndex == [alertView cancelButtonIndex]) {
        
    }
}

#pragma mark - onClick Buttons
- (IBAction)onClickAdd:(id)sender {
    int dremboardId = [self getDremboardId:mStrDremboard];
    
    if (dremboardId < 0)
    {
        mInfoDlg = [[ToastView alloc] init:@"Please select the dremboard." durationTime:2 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }

    [self startAddDremToDremboard:mDremId DremboardId:dremboardId];
}

- (IBAction)onClickNewDremboard:(id)sender {
    if (self.delegate && [self.delegate respondsToSelector:@selector(clickCreateDremboard)]) {
        [self.delegate clickCreateDremboard];
    }
}

- (IBAction)onClickClose:(id)sender {
    [self closeView];
}

#pragma mark - Http Task
- (void) startGetDremboardsList
{
    if(mWaitingDlg != nil){
        mWaitingDlg = nil;
    }
    
    mWaitingDlg = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:mWaitingDlg];
    
    mWaitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    mWaitingDlg.delegate = self;
    mWaitingDlg.labelText = @"Waiting ...";
    
    [mWaitingDlg show:YES];
    
    GetDremboardsParam *param = [[GetDremboardsParam alloc] init];
    param.user_id = mUserId;
    param.author_id = mUserId;
    param.search_str = @"";
    param.category = -1;
    param.last_media_id = 0;
    param.per_page = 30; // can get the dremboards list once a way.
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_DREMBOARDS Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onGetDremboardsListResult:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    if (![status isEqualToString:@"ok"])
    {
        mInfoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    GetDremboardsData *data = [result valueForKeyPath:@"data"];
    NSArray *dremboardsFromResponse = [data valueForKeyPath:@"media"];
    for (NSDictionary *attributes in dremboardsFromResponse) {
        DremboardInfo *dremboard = [[DremboardInfo alloc] initWithAttributes:attributes];
        [mArrayDremboards addObject:dremboard];
    }
    
    [self updateDremboardList];
}

- (void) startAddDremToDremboard:(int)dremId DremboardId:(int)dremboardId
{
    if(mWaitingDlg != nil){
        mWaitingDlg = nil;
    }
    
    mWaitingDlg = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:mWaitingDlg];
    
    mWaitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    mWaitingDlg.delegate = self;
    mWaitingDlg.labelText = @"Waiting ...";
    
    [mWaitingDlg show:YES];
    
    AddDremToDremboardParam *param = [[AddDremToDremboardParam alloc] init];
    param.user_id = mUserId;
    param.drem_id = dremId;
    param.dremboard_id = dremboardId;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)ADD_DREM_TO_BOARD Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onAddDremToDremboardResult:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    if (![status isEqualToString:@"ok"])
    {
        mInfoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    [self showAddSuccessAlert];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case GET_DREMBOARDS:
            [self onGetDremboardsListResult:(NSArray*)result];
            break;
            
        case ADD_DREM_TO_BOARD:
            [self onAddDremToDremboardResult:(NSArray*)result];
            break;
            
        default:
            break;
    }
}

@end
