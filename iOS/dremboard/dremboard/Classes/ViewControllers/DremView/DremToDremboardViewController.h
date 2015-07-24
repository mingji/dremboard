//
//  DremToDremboardViewController.h
//  dremboard
//
//  Created by YingLi on 7/10/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "DownPicker.h"
#import "M13InfiniteTabBarController.h"

@protocol DremToDremoboardViewDelegate;

@interface DremToDremboardViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    int mDremId;
    NSString *mStrDremboard;    

    NSMutableArray * mArrayDremboards;
}

@property (strong, nonatomic) DownPicker *mCmbDremboard;

@property (weak, nonatomic) IBOutlet UITextField *mTxtDremboard;
@property (weak, nonatomic) IBOutlet UIButton *mBtnAdd;
@property (weak, nonatomic) IBOutlet UIButton *mBtnNewDremboard;

- (IBAction)onClickAdd:(id)sender;
- (IBAction)onClickNewDremboard:(id)sender;
- (IBAction)onClickClose:(id)sender;

- (void) setAttributes:(int)dremId;

@property (assign, nonatomic) id <DremToDremoboardViewDelegate>delegate;

@end

@protocol DremToDremoboardViewDelegate <NSObject>

- (void) closeDremToBoardView:(UIViewController *) vc;
- (void) clickCreateDremboard;
@end