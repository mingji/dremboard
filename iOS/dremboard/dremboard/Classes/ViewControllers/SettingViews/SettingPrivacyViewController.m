//
//  SettingPrivacyViewController.m
//  dremboard
//
//  Created by YingLi on 5/25/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "SettingPrivacyViewController.h"
#import "Settings.h"
#import "RadioButton.h"

@interface SettingPrivacyViewController ()

@end

@implementation SettingPrivacyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.mOptionPrivate.groupButtons = @[self.mOptionPrivate, self.mOptionFamily, self.mOptionFriend, self.mOptionPublic];
    mPrivacy = 0;
    [self.mOptionPublic setSelected:YES];
    
    [self getSettingPrivacy];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes {
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
}

- (IBAction)onClickSave:(id)sender {
    [self setSettingPrivacy];
}

- (IBAction)onClickOption:(id)sender {
    int tag = (int)[sender tag];
    
    mPrivacy = tag;
}

- (void) updatePrivacyConf:(int)privacy {
    switch (privacy) {
        case 60:
            [self.mOptionPrivate setSelected:YES];
            break;

        case 50:
            [self.mOptionFamily setSelected:YES];
            break;
        
        case 40:
            [self.mOptionFriend setSelected:YES];
            break;

        default:
            [self.mOptionPublic setSelected:YES];
            break;
    }
}

- (void) getSettingPrivacy
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
    
    GetSettingPrivacyParam *param = [[GetSettingPrivacyParam alloc] init];
    param.user_id = mUserId;
    param.disp_user_id = mUserId;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_SETTING_PRIVACY Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
}

- (void) onGetSettingPrivacyResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    GetSettingPrivacyParam *data = [result valueForKeyPath:@"data"];
    int privacy = [[data valueForKeyPath:@"privacy"] intValue];
    
    [self updatePrivacyConf:privacy];
    
}

- (void) setSettingPrivacy
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
    
    SetSettingPrivacyParam *param = [[SetSettingPrivacyParam alloc] init];
    param.user_id = mUserId;
    param.disp_user_id = mUserId;
    param.privacy = mPrivacy;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_SETTING_PRIVACY Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
}

- (void) onSetSettingPrivacyResult:(NSDictionary*) param Result:(NSArray *) result
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
    
}


- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case GET_SETTING_PRIVACY:
            [self onGetSettingPrivacyResult:(NSDictionary *)param Result:(NSArray *)result];
            break;
            
        case SET_SETTING_PRIVACY:
            [self onSetSettingPrivacyResult:(NSDictionary *)param Result:(NSArray *)result];
            break;
            
        default:
            break;
    }
}

@end
