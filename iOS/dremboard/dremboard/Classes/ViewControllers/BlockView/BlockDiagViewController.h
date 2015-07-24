//
//  BlockDiagViewController.h
//  dremboard
//
//  Created by YingLi on 5/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"

@protocol BlockViewDelegate;

@interface BlockDiagViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
}

@property int mDremerIndex;
@property int mDremerId;


@property (weak, nonatomic) IBOutlet UIButton *btnType1;
@property (weak, nonatomic) IBOutlet UIButton *btnType2;
@property (weak, nonatomic) IBOutlet UIButton *btnType3;
@property (weak, nonatomic) IBOutlet UIButton *btnAll;

@property (weak, nonatomic) IBOutlet UIButton *btnBlock;
@property (weak, nonatomic) IBOutlet UIButton *btnCancel;

@property (assign, nonatomic) id <BlockViewDelegate>delegate;

- (IBAction)onClickType1:(id)sender;
- (IBAction)onClickType2:(id)sender;
- (IBAction)onClickType3:(id)sender;
- (IBAction)onClickAll:(id)sender;

- (IBAction)onClickBlock:(id)sender;
- (IBAction)onClickCancel:(id)sender;

@end

@protocol BlockViewDelegate<NSObject>
@required
- (void)closeBlockView:(BlockDiagViewController*)blockVC;
- (void)afterBlock:(BlockDiagViewController*)blockVC BlockType:(int)blockType Index:(int)dremerIndex;
@end
