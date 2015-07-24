//
//  SettingEmailViewController.m
//  dremboard
//
//  Created by YingLi on 5/25/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "SettingEmailViewController.h"
#import "AppDelegate.h"
#import "Settings.h"

@interface SettingEmailViewController ()

@end


@implementation SettingEmailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [_mScrollView setContentSize:CGSizeMake(_mScrollView.frame.size.width, 1500)];
    
    _mArrayConf = [[NSMutableArray alloc] init];
    
    mArrayConfIds = [[NSArray alloc] initWithObjects:
                     @"notification_activity_new_mention", @"notification_activity_new_reply",
                     
                     @"notification_messages_new_message",
                     
                     @"notification_friends_friendship_request", @"notification_friends_friendship_accepted",
                     
                     @"notification_groups_invite", @"notification_groups_group_updated",
                     @"notification_groups_admin_promotion", @"notification_groups_membership_request",
                     
                     @"notification_familys_familyship_request", @"notification_familys_familyship_accepted",
                     
                     @"notification_starts_following", nil];
    
    
    [self getSettingNote];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) viewWillAppear:(BOOL)animated {
}

- (void) setAttributes:(UINavigationController *) navController {
    self.navController = navController;
    
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
}

- (UIButton *) getConfigButtonFromConfId:(NSString *) confId {
    
    if ([confId isEqualToString:[mArrayConfIds objectAtIndex:0]]) {
        return self.mBtnActivity1;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:1]]) {
        return self.mBtnActivity2;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:2]]) {
        return self.mBtnMessage;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:3]]) {
        return self.mBtnFriend1;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:4]]) {
        return self.mBtnFriend2;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:5]]) {
        return self.mBtnGroup1;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:6]]) {
        return self.mBtnGroup2;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:7]]) {
        return self.mBtnGroup3;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:8]]) {
        return self.mBtnGroup4;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:9]]) {
        return self.mBtnFamily1;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:10]]) {
        return self.mBtnFamily2;
    }
    else if ([confId isEqualToString:[mArrayConfIds objectAtIndex:11]]) {
        return self.mBtnFollow;
    }
    else {
        return nil;
    }
}

- (void) updateEmailConf {
    for (EmailNoteConf *conf in _mArrayConf) {
        UIButton *btnConf = [self getConfigButtonFromConfId:conf.conf_id];
        
        if (!btnConf)
            continue;
      
        if ([conf.value isEqualToString:@"yes"])
            [btnConf setTag:1];
        else
            [btnConf setTag:0];
        
        [self setBtnImage:btnConf];
    }
}

- (void) setBtnImage:(UIButton*) button {
    if ([button tag] == 0) {
        UIImage *imgChecked = [UIImage imageNamed:@"chk_empty"];
        [button setImage:imgChecked forState:UIControlStateNormal];
    } else {
        UIImage *imgChecked = [UIImage imageNamed:@"chk_full"];
        [button setImage:imgChecked forState:UIControlStateNormal];
    }
}

- (NSString *) makeConfJosnString {
    NSString *retStr = @"";
    
    NSMutableArray *arrayConf = [[NSMutableArray alloc] init];
    
    for (NSString * conf_id in mArrayConfIds) {
        UIButton *btnConf = [self getConfigButtonFromConfId:conf_id];
        
        if (!btnConf)
            continue;
        
        NSMutableDictionary *conf = [[NSMutableDictionary alloc] init];
        [conf setValue:conf_id forKey:@"id"];
        [conf setValue:@"" forKey:@"description"];
        
        if ([btnConf tag] == 0)
            [conf setValue:@"no" forKey:@"value"];
        else
            [conf setValue:@"yes" forKey:@"value"];
        
        [arrayConf addObject:conf];
    }
    
    NSError* error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:arrayConf
                                                       options:NSJSONWritingPrettyPrinted error:&error];
    retStr = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

    return retStr;
}

- (IBAction)OnClickSave:(id)sender {
    [self makeConfJosnString];
    
    [self setSettingNote:[self makeConfJosnString]];
}

- (IBAction)onClickConfButton:(id)sender {
    UIButton *btnConf = (UIButton*)sender;
    
    if ([btnConf tag] == 0)
        [btnConf setTag:1];
    else
        [btnConf setTag:0];
    
    [self setBtnImage:btnConf];

}

- (void) getSettingNote
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
    
    GetSettingNoteParam *param = [[GetSettingNoteParam alloc] init];
    param.user_id = mUserId;
    param.disp_user_id = mUserId;
    
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_SETTING_NOTE Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
}

- (void) onGetSettingNoteResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    GetSettingNoteData *data = [result valueForKeyPath:@"data"];
    NSArray *dataFromResponse = [data valueForKeyPath:@"notifications"];
    for (NSDictionary *attributes in dataFromResponse) {
        EmailNoteConf *conf = [[EmailNoteConf alloc] initWithAttributes:attributes];
        [_mArrayConf addObject:conf];
    }
    
    [self updateEmailConf];
    
}

- (void) setSettingNote:(NSString*) notifications
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
    
    SetSettingNoteParam *param = [[SetSettingNoteParam alloc] init];
    param.user_id = mUserId;
    param.disp_user_id = mUserId;
    param.notifications = notifications;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_SETTING_NOTE Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
}

- (void) onSetSettingNoteResult:(NSDictionary*) param Result:(NSArray *) result
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
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case GET_SETTING_NOTE:
            [self onGetSettingNoteResult:(NSDictionary *)param Result:(NSArray *)result];
            break;
            
        case SET_SETTING_NOTE:
            [self onSetSettingNoteResult:(NSDictionary *)param Result:(NSArray *)result];
            break;
            
        default:
            break;
    }
}

@end
