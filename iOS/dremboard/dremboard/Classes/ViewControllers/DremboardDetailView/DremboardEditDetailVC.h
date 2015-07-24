//
//  DremboardEditDetailVC.h
//  dremboard
//
//  Created by YingLi on 7/16/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "Dremboard.h"

@protocol BoardEditDetailVCDelegate;

@interface DremboardEditDetailVC : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    DremboardInfo *mDremboardInfo;
    int mUserId;
}


@property (weak, nonatomic) IBOutlet UITextField *mTxtTitle;
@property (weak, nonatomic) IBOutlet UITextView *mTxtDesc;

- (IBAction)onClickSave:(id)sender;
- (IBAction)onClickClose:(id)sender;

- (void) setAttribute:(DremboardInfo*)dremboardInfo;

@property (assign, nonatomic) id <BoardEditDetailVCDelegate>delegate;

@end

@protocol BoardEditDetailVCDelegate <NSObject>

- (void) closeBoardEditDetailView;
- (void) successBoardEdit:(NSString*)title Description:(NSString*)description;

@end