//
//  LoginViewController.h
//  Dremboard
//
//  Created by vitaly on 4/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "M13InfiniteTabBarController.h"
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <GooglePlus/GooglePlus.h>
#import "HomeTabViewController.h"

@class GPPSignInButton;

@interface LoginViewController : UIViewController <M13InfiniteTabBarControllerDelegate, HttpTaskDelegate, MBProgressHUDDelegate, GPPSignInDelegate>
{
    NSString  *m_userName;
    NSString  *m_userPwd;
    
    HttpTask *m_httpTask;    
    ToastView *m_infoDlg;
    MBProgressHUD *m_waitingDlg;    
}
@property (weak, nonatomic) IBOutlet UITextField *mTxtUsername;
@property (weak, nonatomic) IBOutlet UITextField *mTxtPassword;
@property (weak, nonatomic) IBOutlet UIButton *mBtnRemember;
@property (weak, nonatomic) IBOutlet UIButton *mBtnRegister;


@property (weak, nonatomic) IBOutlet GPPSignInButton *mBtnGoogle;

- (IBAction)onClickBtnFB:(id)sender;
- (IBAction)onClickTwitterBtn:(id)sender;
- (IBAction)onClickGoogleBtn:(id)sender;


- (IBAction)onClickLogin:(id)sender;
- (IBAction)changeRememberConf:(id)sender;
- (IBAction)onClickRegister:(id)sender;

- (BOOL) checkInputData;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;


@end
