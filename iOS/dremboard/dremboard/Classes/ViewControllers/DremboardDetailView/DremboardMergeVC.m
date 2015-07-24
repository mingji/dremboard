//
//  DremboardMergeVC.m
//  dremboard
//
//  Created by YingLi on 7/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremboardMergeVC.h"

@interface DremboardMergeVC ()

@end

@implementation DremboardMergeVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self initUI];
    [self startGetDremboardsList];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttribute:(int)dremboadId {
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    mDremboardId = dremboadId;
}

- (void) initUI {
    // init dremboard lists
    mArrayDremboards = [[NSMutableArray alloc] init];
    
    self.mCmbDremboard = [[DownPicker alloc] initWithTextField:self.mTxtTargetItem];
    [self.mCmbDremboard showArrowImage:YES];
    [self.mCmbDremboard setPlaceholder:@"---"];
}

- (void) updateDremboardList {
    NSMutableArray *arrayStrBoards = [[NSMutableArray alloc] init];
    
    for (DremboardInfo *boardInfo in mArrayDremboards) {
        if (boardInfo.dremboard_id == mDremboardId)
            continue;
        
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

#pragma mark - Click Buttons
- (IBAction)onClickMerge:(id)sender {
    NSString *strTarget = [self.mCmbDremboard text];
    
    int targetId = [self getDremboardId:strTarget];
    
    if (targetId < 0)
    {
        mInfoDlg = [[ToastView alloc] init:@"Please select the dremboard." durationTime:2 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    [self startMergeDremboard:mDremboardId TargetId:targetId];
}

- (IBAction)onClickClose:(id)sender {
    if (self.delegate && [self.delegate respondsToSelector:@selector(closeDremboardMergeView)]) {
        [self.delegate closeDremboardMergeView];
    }
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

- (void) startMergeDremboard:(int)sourceId TargetId:(int)targetId
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
    
    MergeDremboardParam *param = [[MergeDremboardParam alloc] init];
    param.source_id = sourceId;
    param.target_id = targetId;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)MERGE_DREMBOARD Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onMergeDremboardResult:(NSArray *) result
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
    
    if (self.delegate && [self.delegate respondsToSelector:@selector(successDremboardMerge)]) {
        [self.delegate successDremboardMerge];
    }
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
          
        case MERGE_DREMBOARD:
            [self onMergeDremboardResult:(NSArray*)result];
            break;
            
        default:
            break;
    }
}

@end
