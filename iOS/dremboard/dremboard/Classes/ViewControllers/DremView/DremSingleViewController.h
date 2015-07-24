//
//  DremSingleViewController.h
//  dremboard
//
//  Created by YingLi on 6/3/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "Drem.h"
#import "FlagViewController.h"
#import "ShareViewController.h"
#import "DremToDremboardViewController.h"
#import "CreateDremboardVC.h"
#import "M13InfiniteTabBarController.h"

@protocol DremSingleViewDelegate;

@interface DremSingleViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, FlagViewDelegate, DremToDremoboardViewDelegate, CreateDremboardVCDelegate>
{
    HttpTask *mHttpTask;
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    DremToDremboardViewController *addDremVC;
    CreateDremboardVC *createBoardVC;

    int mUserId;
}

@property (strong, nonatomic) UIViewController *mainVC;

@property int mDremIndex;
@property (readwrite, strong, nonatomic) DremInfo * mDremInfo;

@property (weak, nonatomic) IBOutlet UIButton *mBtnClose;
@property (weak, nonatomic) IBOutlet UIImageView *mImgUser;
@property (weak, nonatomic) IBOutlet UILabel *mTxtUsername;
@property (weak, nonatomic) IBOutlet UILabel *mTxtTime;

@property (weak, nonatomic) IBOutlet UIImageView *mImgContent;
@property (weak, nonatomic) IBOutlet UIButton *mBtnLike;
@property (weak, nonatomic) IBOutlet UIButton *mBtnFlag;
@property (weak, nonatomic) IBOutlet UIButton *mBtnComment;
@property (weak, nonatomic) IBOutlet UIButton *mBtnShare;

- (void) setAttributes:(DremInfo *)dreminfo Index:(int)index MainVC:(UIViewController*)mainVC;

- (IBAction)onClickClose:(id)sender;

- (IBAction)onClickLike:(id)sender;
- (IBAction)onClickFlag:(id)sender;
- (IBAction)onClickComment:(id)sender;
- (IBAction)onClickShare:(id)sender;

@property (assign, nonatomic) id <DremSingleViewDelegate>delegate;


@end

@protocol DremSingleViewDelegate <NSObject>

- (void) closeView:(DremSingleViewController *) vc;

@end

