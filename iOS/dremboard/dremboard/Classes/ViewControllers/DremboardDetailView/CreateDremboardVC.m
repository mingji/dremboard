//
//  CreateDremboardVC.m
//  dremboard
//
//  Created by YingLi on 7/14/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "CreateDremboardVC.h"
#import "AppDelegate.h"
#import "Dremboard.h"
#import "Category.h"

@interface CreateDremboardVC ()

@end

UIGestureRecognizer *tapper;

@implementation CreateDremboardVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self initAttributes];
    [self initUI];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)initAttributes {
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    mPrivacyVal = 0; // privacy - public
}

- (void) initUI {
    
    // Description
    UIColor *borderColor = [UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:204.0/255.0 alpha:1.0];
    self.mTxtDesc.layer.borderColor = borderColor.CGColor;
    self.mTxtDesc.layer.borderWidth = 1.0;
    self.mTxtDesc.layer.cornerRadius = 5.0;
    
    // Privacy Options
    self.mRadioPublic.groupButtons = @[self.mRadioPublic, self.mRadioPersonal, self.mRadioFamily, self.mRadioFriend];
    [self.mRadioPublic setSelected:YES];
    
    // Category
    [self setupCategory];
    
    // Handle Keyboard
    tapper = [[UITapGestureRecognizer alloc]
              initWithTarget:self action:@selector(handleSingleTap:)];
    tapper.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapper];
}

- (void) setupCategory {
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    mArrayCategories = [[NSMutableArray alloc] initWithArray:app.categories];
    
    NSMutableArray* arrayName = [[NSMutableArray alloc] init];
    for (CategoryItem *item in mArrayCategories) {
        NSString *categoryName = [[NSString alloc] initWithFormat:@"%@", item.category_name];

        if ([categoryName isEqualToString:@"All categories"] ||
            [categoryName isEqualToString:@"Uncategorized"]) {
            continue;
        }
        
        [arrayName addObject:categoryName];
    }
    
    self.mPickerCategory = [[DownPicker alloc] initWithTextField:self.mTxtCategory withData:arrayName];
    [self.mPickerCategory showArrowImage:YES];
    [self.mPickerCategory setPlaceholder:@"Category"];
}

- (int) getCategoryId :(NSString *) strCategory {
    int retId = -1;
    
    for (CategoryItem *item in mArrayCategories) {
        if ([item.category_name isEqualToString:strCategory]) {
            retId = item.category_id;
            break;
        }
    }
    
    return retId;
}

- (void)handleSingleTap:(UITapGestureRecognizer *) sender
{
    [self.view endEditing:YES];
}

- (void) closeView {
    if (self.delegate && [self.delegate respondsToSelector:@selector(closeCreateDremboardView:)]) {
        [self.delegate closeCreateDremboardView:self];
    }
}

#pragma mark - Click Buttons

- (IBAction)onClickClose:(id)sender {
    [self closeView];
}

- (IBAction)onClickCreate:(id)sender {
    NSString *title = [self.mTxtTtile text];
    NSString *description = [self.mTxtDesc text];
    
    int categoryId = [self getCategoryId:[self.mPickerCategory text]];
    
    // Check parameters
    if (!title || [title length] == 0) {
        mInfoDlg = [[ToastView alloc] init:@"Please input a title." durationTime:1 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    [self startCreateDremboard:title Description:description Category:categoryId Privacy:mPrivacyVal];
}

- (IBAction)onClickPrivacy:(id)sender {
    mPrivacyVal = (int)[sender tag];
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

- (void) showAddSuccessAlert {
    NSString * msg = [NSString stringWithFormat: @"%@ album created successfully.", [self.mTxtTtile text]];
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Create new Drēmboard"
                                                    message:msg
                                                   delegate:self
                                          cancelButtonTitle:nil
                                          otherButtonTitles:@"OK", nil];
    [alert show];
}


#pragma mark - HttpAPITask
- (void) startCreateDremboard:(NSString*)title Description:(NSString*)description Category:(int)categoryId Privacy:(int)privacy
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
    
    CreateDremboardParam *param = [[CreateDremboardParam alloc] init];
    param.user_id = mUserId;
    param.title = title;
    param.description = description;
    param.category_id = categoryId;
    param.privacy = privacy;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)CREATE_DREMBOARD Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onCreateDremboardResult:(NSArray *) result
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
        case CREATE_DREMBOARD:
            [self onCreateDremboardResult:(NSArray*)result];
            break;
            
        default:
            break;
    }
}


@end
