//
//  DremboardMergeVC.h
//  dremboard
//
//  Created by YingLi on 7/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "DownPicker.h"
#import "Dremboard.h"

@protocol DremboardMergeDelegate;

@interface DremboardMergeVC : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    int mDremboardId;
    
    NSMutableArray * mArrayDremboards;
}

@property (strong, nonatomic) DownPicker *mCmbDremboard;

@property (weak, nonatomic) IBOutlet UITextField *mTxtTargetItem;

- (IBAction)onClickMerge:(id)sender;
- (IBAction)onClickClose:(id)sender;

- (void) setAttribute:(int)dremboardId;

@property (assign, nonatomic) id <DremboardMergeDelegate>delegate;

@end

@protocol DremboardMergeDelegate <NSObject>
- (void) closeDremboardMergeView;
- (void) successDremboardMerge;
@end