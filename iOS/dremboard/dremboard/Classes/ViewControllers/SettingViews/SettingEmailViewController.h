//
//  SettingEmailViewController.h
//  dremboard
//
//  Created by YingLi on 5/25/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"

@interface SettingEmailViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    
    NSArray *mArrayConfIds;
}

@property(nonatomic, assign) UINavigationController *navController;

@property (readwrite, nonatomic, strong) NSMutableArray *mArrayConf;

@property (weak, nonatomic) IBOutlet UIScrollView *mScrollView;
@property (weak, nonatomic) IBOutlet UIButton *mBtnActivity1;
@property (weak, nonatomic) IBOutlet UIButton *mBtnActivity2;
@property (weak, nonatomic) IBOutlet UIButton *mBtnMessage;
@property (weak, nonatomic) IBOutlet UIButton *mBtnFriend1;
@property (weak, nonatomic) IBOutlet UIButton *mBtnFriend2;
@property (weak, nonatomic) IBOutlet UIButton *mBtnGroup1;
@property (weak, nonatomic) IBOutlet UIButton *mBtnGroup2;
@property (weak, nonatomic) IBOutlet UIButton *mBtnGroup3;
@property (weak, nonatomic) IBOutlet UIButton *mBtnGroup4;
@property (weak, nonatomic) IBOutlet UIButton *mBtnFamily1;
@property (weak, nonatomic) IBOutlet UIButton *mBtnFamily2;
@property (weak, nonatomic) IBOutlet UIButton *mBtnFollow;
@property (weak, nonatomic) IBOutlet UIButton *mBtnSave;
- (IBAction)OnClickSave:(id)sender;

- (IBAction)onClickConfButton:(id)sender;

- (void) setAttributes:(UINavigationController *) navController;

@end
