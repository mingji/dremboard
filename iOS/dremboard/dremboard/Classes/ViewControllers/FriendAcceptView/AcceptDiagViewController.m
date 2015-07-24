//
//  AcceptDiagViewController.m
//  dremboard
//
//  Created by YingLi on 5/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "AcceptDiagViewController.h"
#import "UIImageView+AFNetworking.h"
#import "Dremer.h"

@interface AcceptDiagViewController ()

@end

@implementation AcceptDiagViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];

    self.txtUserName.text = _mDremerInfo.display_name;
    self.txtTime.text = _mDremerInfo.last_activity;
    
    [self.imgUser setImageWithURL:[NSURL URLWithString:_mDremerInfo.user_avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    
    if ([mDiagType isEqualToString:@"friendship"])
        self.txtTitle.text = @"Requested Friendship";
    else if ([mDiagType isEqualToString:@"familyship"])
        self.txtTitle.text = @"Requested Familyship";

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setDremerInfo:(DremerInfo *)dremerInfo {
    _mDremerInfo = dremerInfo;
}

- (void) setDiagType:(NSString *)type {
    // @"friendship" or @"familyship"
    mDiagType = [NSString stringWithString:type];
}

- (IBAction)onClickAccept:(id)sender {
    if ([mDiagType isEqualToString:@"friendship"])
        [self setFriendship:_mDremerInfo.user_id Action:@"accept" Index:_mDremerIndex];
    else if ([mDiagType isEqualToString:@"familyship"])
        [self setFamilyship:_mDremerInfo.user_id Action:@"accept" Index:_mDremerIndex];
}

- (IBAction)onClickReject:(id)sender {
    if ([mDiagType isEqualToString:@"friendship"])
        [self setFriendship:_mDremerInfo.user_id Action:@"reject" Index:_mDremerIndex];
    else if ([mDiagType isEqualToString:@"familyship"])
        [self setFamilyship:_mDremerInfo.user_id Action:@"reject" Index:_mDremerIndex];
}

- (void)afterAcceptReject:(NSString*)status Index:(int)dremerIndex {
    if (self.delegate && [self.delegate respondsToSelector:@selector(afterAcceptReject:Status:Index:)]) {
        [self.delegate afterAcceptReject:self Status:status Index:self.mDremerIndex];
    }
}


- (void) setFriendship:(int)dremerId Action:(NSString*)action Index:(int)index
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
    
    SetFriendParam *param = [[SetFriendParam alloc] init];
    param.user_id = mUserId;
    param.dremer_id = dremerId;
    param.action = action;
    param.index = index;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_FRIENDSHIP Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onSetFriendshipResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    SetFriendData *data = [result valueForKeyPath:@"data"];
    NSString *friendship_status = [data valueForKeyPath:@"friendship_status"];
    
    [self afterAcceptReject:friendship_status Index:self.mDremerIndex];
}

- (void) setFamilyship:(int)dremerId Action:(NSString*)action Index:(int)index
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
    
    SetFamilyParam *param = [[SetFamilyParam alloc] init];
    param.user_id = mUserId;
    param.dremer_id = dremerId;
    param.action = action;
    param.index = index;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_FAMILYSHIP Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onSetFamilyshipResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    SetFamilyData *data = [result valueForKeyPath:@"data"];
    NSString *familyship_status = [data valueForKeyPath:@"familyship_status"];
    
    [self afterAcceptReject:familyship_status Index:self.mDremerIndex];
}

#pragma mark - HttpTaskDelegate
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case SET_FRIENDSHIP:
            [self onSetFriendshipResult:(NSDictionary*) param Result:(NSArray*)result];
            break;
            
        case SET_FAMILYSHIP:
            [self onSetFamilyshipResult:(NSDictionary*) param Result:(NSArray*)result];
            break;
            
        default:
            break;
    }
}

@end
