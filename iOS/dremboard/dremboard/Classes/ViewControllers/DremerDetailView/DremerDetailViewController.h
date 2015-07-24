//
//  DremerDetailViewController.h
//  dremboard
//
//  Created by YingLi on 5/29/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "Dremer.h"
#import "BlockDiagViewController.h"
#import "AcceptDiagViewController.h"

@interface DremerDetailViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, HttpTaskDelegate, MBProgressHUDDelegate, BlockViewDelegate, AcceptViewDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    UIRefreshControl *mRefreshControl;
    
    int mUserId;
    int mDremerId;
    
    DremerInfo *mDremerInfo;
    NSMutableArray * mProfiles;
    
}

@property (readwrite, nonatomic, strong) NSMutableArray *mArrayMenu;

@property (weak, nonatomic) IBOutlet UITableView *mTableMenu;

@property (weak, nonatomic) IBOutlet UIImageView *mImgUser;
@property (weak, nonatomic) IBOutlet UILabel *mTxtName;
@property (weak, nonatomic) IBOutlet UILabel *mTxtBio;
@property (weak, nonatomic) IBOutlet UILabel *mTxtTime;
@property (weak, nonatomic) IBOutlet UIButton *mBtnFriend;
@property (weak, nonatomic) IBOutlet UIButton *mBtnFamily;
@property (weak, nonatomic) IBOutlet UIButton *mBtnFollow;
@property (weak, nonatomic) IBOutlet UIButton *mBtnPubMsg;
@property (weak, nonatomic) IBOutlet UIButton *mBtnPrivMsg;

- (void) setAttributes:(int) dremerId;

- (IBAction)onClickFriend:(id)sender;
- (IBAction)onClickFaimly:(id)sender;
- (IBAction)onClickFollow:(id)sender;
- (IBAction)onClickPubMsg:(id)sender;
- (IBAction)onClickPrivMsg:(id)sender;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
