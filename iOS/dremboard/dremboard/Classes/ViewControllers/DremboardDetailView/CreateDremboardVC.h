//
//  CreateDremboardVC.h
//  dremboard
//
//  Created by YingLi on 7/14/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "RadioButton.h"
#import "DownPicker.h"

@protocol CreateDremboardVCDelegate;

@interface CreateDremboardVC : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    NSMutableArray *mArrayCategories;
    
    int mUserId;
    int mPrivacyVal;
}

@property (strong, nonatomic) DownPicker *mPickerCategory;

@property (weak, nonatomic) IBOutlet UITextField *mTxtTtile;
@property (weak, nonatomic) IBOutlet UITextView *mTxtDesc;
@property (weak, nonatomic) IBOutlet UITextField *mTxtCategory;

@property (weak, nonatomic) IBOutlet RadioButton *mRadioPublic;
@property (weak, nonatomic) IBOutlet RadioButton *mRadioPersonal;
@property (weak, nonatomic) IBOutlet RadioButton *mRadioFamily;
@property (weak, nonatomic) IBOutlet RadioButton *mRadioFriend;

- (IBAction)onClickClose:(id)sender;
- (IBAction)onClickCreate:(id)sender;
- (IBAction)onClickPrivacy:(id)sender;

@property (assign, nonatomic) id <CreateDremboardVCDelegate>delegate;

@end

@protocol CreateDremboardVCDelegate <NSObject>

- (void) closeCreateDremboardView:(UIViewController *) vc;

@end