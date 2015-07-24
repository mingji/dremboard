//
//  SettingGeneralViewController.m
//  dremboard
//
//  Created by YingLi on 5/25/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "SettingGeneralViewController.h"
#import "AppDelegate.h"
#import "Settings.h"

@interface SettingGeneralViewController ()

@end

@implementation SettingGeneralViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [_mScrollView setContentSize:CGSizeMake(_mScrollView.frame.size.width, 800)];
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    [self.mTxtMail setText:app.currentDremer.user_email];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(UINavigationController *) navController {
    self.navController = navController;
    
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    mCurrentPwd = [[NSUserDefaults standardUserDefaults] stringForKey:@"logined_user_pwd"];
}

- (BOOL) checkCurrentPassword {
    NSString *password = [self.mTxtCurrentPwd text];
    
    if ([mCurrentPwd isEqualToString:password])
        return true;
    
    return false;
}

- (BOOL) checkNewPassword {
    NSString *newPwd = [self.mTxtNewPwd text];
    NSString *repeatPwd = [self.mTxtRepeatPwd text];
    
    if ([newPwd length] == 0)
        return false;
    
    if ([newPwd isEqualToString:repeatPwd])
        return true;
    
    return false;
}

- (BOOL) checkEmail {
    NSString *email = [self.mTxtMail text];
    
    if ([email length] == 0)
        return false;
    
    return true;
}

- (IBAction)onClickSave:(id)sender {
    
    if (![self checkCurrentPassword])
    {
        mInfoDlg = [[ToastView alloc] init:@"Your current password is invalid." durationTime:2 parent:self.navController.view];
        [mInfoDlg show];
        return;
    }
    
    if (![self checkNewPassword])
    {
        mInfoDlg = [[ToastView alloc] init:@"Please input a correct password." durationTime:2 parent:self.navController.view];
        [mInfoDlg show];
        return;
    }
    
    if (![self checkEmail])
    {
        mInfoDlg = [[ToastView alloc] init:@"Please input Email address." durationTime:2 parent:(UIView *)self.navController.view];
        [mInfoDlg show];
        return;
    }
    
    NSString *email = [self.mTxtMail text];
    NSString *newPwd = [self.mTxtNewPwd text];
    
    [self setSettingGeneral:email Password:newPwd];
}

- (void) setSettingGeneral:(NSString * ) email Password:(NSString *)pwd
{
    if(mWaitingDlg != nil){
        mWaitingDlg = nil;
    }
    
    mWaitingDlg = [[MBProgressHUD alloc] initWithView:self.navController.view];
    [self.navController.view addSubview:mWaitingDlg];
    
    mWaitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    mWaitingDlg.delegate = self;
    mWaitingDlg.labelText = @"Waiting ...";
    
    [mWaitingDlg show:YES];
    
    SetSettingGeneralParam *param = [[SetSettingGeneralParam alloc] init];
    param.user_id = mUserId;
    param.disp_user_id = mUserId;
    param.email = email;
    param.password = pwd;
    
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_SETTING_GENERAL Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
}

- (void) onSetSettingGeneralResult:(NSDictionary*) param Result:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    if (![status isEqualToString:@"ok"])
    {
        mInfoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.navController.view];
        [mInfoDlg show];
        return;
    }
    
    NSString *email = [self.mTxtMail text];
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    [app.currentDremer setUser_email:email];
    
    NSString *newPwd = [self.mTxtNewPwd text];
    [[NSUserDefaults standardUserDefaults] setObject:newPwd forKey:@"logined_user_pwd"];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case SET_SETTING_GENERAL:
            [self onSetSettingGeneralResult:(NSDictionary *)param Result:(NSArray *)result];
            break;
            
        default:
            break;
    }
}

@end
