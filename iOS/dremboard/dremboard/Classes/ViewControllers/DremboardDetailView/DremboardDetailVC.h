//
//  DremboardDetailVC.h
//  dremboard
//
//  Created by YingLi on 6/4/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "Dremboard.h"
#import "DremCell.h"
#import "FlagViewController.h"
#import "ShareViewController.h"
#import "DremSingleViewController.h"
#import "DremboardEditDetailVC.h"
#import "DremboardOptionVC.h"
#import "DremboardMergeVC.h"
#import "DremboardManageVC.h"

@protocol DremboardDetailDelegate;

@interface DremboardDetailVC : UIViewController <HttpTaskDelegate, UICollectionViewDataSource, MBProgressHUDDelegate, DremViewCellDelegate, FlagViewDelegate, DremSingleViewDelegate, BoardEditDetailVCDelegate, DremboardOptionDelegate, DremboardMergeDelegate, DremboardManageViewDelegate>
{
    HttpTask *mHttpTask;
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    UIRefreshControl *mRefreshControl;
    
    int mLastMediaId;
    int mUserId;
    int mIndex;
    
    DremboardInfo *mDremboardInfo;
    DremboardOptionVC *mOptionVC;
}

@property (weak, nonatomic) IBOutlet UIView *mViewBoard;
@property (weak, nonatomic) IBOutlet UIView *mViewUser;
@property (weak, nonatomic) IBOutlet UILabel *mTxtTitle;
@property (weak, nonatomic) IBOutlet UIImageView *mImgUser;
@property (weak, nonatomic) IBOutlet UILabel *mTxtUsername;
@property (weak, nonatomic) IBOutlet UICollectionView *mCollectionDremView;

@property (weak, nonatomic) IBOutlet UIButton *mBtnOption;
- (IBAction)onClickOption:(id)sender;

@property (readwrite, nonatomic, strong) NSMutableArray *mArrayDrem;

- (void) setAttributes:(DremboardInfo *) drmeboardInfo Index:(int)index;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@property (assign, nonatomic) id <DremboardDetailDelegate>delegate;

@end

@protocol DremboardDetailDelegate <NSObject>

- (void) deletedDremboard:(int)index;
- (void) updateDremboard:(int)index Title:(NSString*)title Count:(int)count;

@end
