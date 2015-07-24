//
//  ProfileEditController.h
//  dremboard
//
//  Created by YingLi on 7/6/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "DownPicker.h"

@interface ProfileEditController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    
    NSMutableArray* mArrayPrivacy;
}

/* Main Navigation Controller */
@property(nonatomic, assign) UINavigationController *navController;

/* Profile List */
@property (readwrite, nonatomic, strong) NSMutableArray *mArrayProfiles;

/* Combo boxes for profile privacy */
@property (strong, nonatomic) DownPicker *mCmbName;
@property (strong, nonatomic) DownPicker *mCmbGender;
@property (strong, nonatomic) DownPicker *mCmbBirthday;
@property (strong, nonatomic) DownPicker *mCmbLang;
@property (strong, nonatomic) DownPicker *mCmbCountry;
@property (strong, nonatomic) DownPicker *mCmbAddress;
@property (strong, nonatomic) DownPicker *mCmbPhoneNum;
@property (strong, nonatomic) DownPicker *mCmbFacebook;
@property (strong, nonatomic) DownPicker *mCmbGoogle;
@property (strong, nonatomic) DownPicker *mCmbTwitter;
@property (strong, nonatomic) DownPicker *mCmbBio;
@property (strong, nonatomic) DownPicker *mCmbContent;

@property (weak, nonatomic) IBOutlet UITextField *mPrivGender;
@property (weak, nonatomic) IBOutlet UITextField *mPrivBrith;
@property (weak, nonatomic) IBOutlet UITextField *mPrivLang;
@property (weak, nonatomic) IBOutlet UITextField *mPrivCountry;
@property (weak, nonatomic) IBOutlet UITextField *mPrivAddress;
@property (weak, nonatomic) IBOutlet UITextField *mPrivPhone;
@property (weak, nonatomic) IBOutlet UITextField *mPrivFacebook;
@property (weak, nonatomic) IBOutlet UITextField *mPrivGoogle;
@property (weak, nonatomic) IBOutlet UITextField *mPrivTwitter;
@property (weak, nonatomic) IBOutlet UITextField *mPrivBio;
@property (weak, nonatomic) IBOutlet UITextField *mPrivContent;

@property (weak, nonatomic) IBOutlet UIScrollView *mScrollView;


@property (strong, nonatomic) DownPicker *mCmbGenderValue;
@property (strong, nonatomic) DownPicker *mCmbLangValue;
@property (strong, nonatomic) DownPicker *mCmbCountryValue;

/* Text Field for profiles */
@property (weak, nonatomic) IBOutlet UITextField *mTxtName;
@property (weak, nonatomic) IBOutlet UITextField *mTxtGender;
@property (weak, nonatomic) IBOutlet UITextField *mTxtLang;
@property (weak, nonatomic) IBOutlet UITextField *mTxtCountry;
@property (weak, nonatomic) IBOutlet UITextField *mTxtAddress;
@property (weak, nonatomic) IBOutlet UITextField *mTxtPhone;
@property (weak, nonatomic) IBOutlet UITextField *mTxtFacebook;
@property (weak, nonatomic) IBOutlet UITextField *mTxtGoogle;
@property (weak, nonatomic) IBOutlet UITextField *mTxtTwitter;
@property (weak, nonatomic) IBOutlet UITextView *mTxtBio;
@property (weak, nonatomic) IBOutlet UITextField *mTxtContent;

- (void) setAttributes:(UINavigationController *) navController;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
