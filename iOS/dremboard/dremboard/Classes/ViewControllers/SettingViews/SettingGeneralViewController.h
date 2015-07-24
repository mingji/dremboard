//
//  SettingGeneralViewController.h
//  dremboard
//
//  Created by YingLi on 5/25/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"

@interface SettingGeneralViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    NSString *mCurrentPwd;
}

@property(nonatomic, assign) UINavigationController *navController;

@property (weak, nonatomic) IBOutlet UIScrollView *mScrollView;
@property (weak, nonatomic) IBOutlet UITextField *mTxtCurrentPwd;

@property (weak, nonatomic) IBOutlet UITextField *mTxtMail;
@property (weak, nonatomic) IBOutlet UITextField *mTxtNewPwd;
@property (weak, nonatomic) IBOutlet UITextField *mTxtRepeatPwd;
@property (weak, nonatomic) IBOutlet UIButton *mBtnSave;
- (IBAction)onClickSave:(id)sender;

- (void) setAttributes:(UINavigationController *) navController;
@end
