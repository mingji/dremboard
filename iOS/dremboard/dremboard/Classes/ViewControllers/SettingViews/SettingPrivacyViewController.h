//
//  SettingPrivacyViewController.h
//  dremboard
//
//  Created by YingLi on 5/25/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"

@class RadioButton;

@interface SettingPrivacyViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    int mPrivacy;
}

@property (weak, nonatomic) IBOutlet RadioButton *mOptionPrivate;
@property (weak, nonatomic) IBOutlet RadioButton *mOptionFamily;
@property (weak, nonatomic) IBOutlet RadioButton *mOptionFriend;
@property (weak, nonatomic) IBOutlet RadioButton *mOptionPublic;

- (void) setAttributes;
- (IBAction)onClickSave:(id)sender;
- (IBAction)onClickOption:(id)sender;

@end
