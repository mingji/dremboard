//
//  DremboardManageVC.h
//  dremboard
//
//  Created by YingLi on 7/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "DremboardManageCell.h"
#import "Dremboard.h"
#import "Drem.h"
#import "DremboardMoveMediaVC.h"

@protocol DremboardManageViewDelegate <NSObject>
- (void) removedDrems:(NSMutableArray *)arrayDrems;
@end

@interface DremboardManageVC : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, UICollectionViewDataSource, DremboardManageCellDelegate, DremboardMoveMediaDelegate>
{
    HttpTask *mHttpTask;
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    
    DremboardInfo *mDremboardInfo;
    NSMutableArray *mArrayDrem;
}

@property (weak, nonatomic) IBOutlet UICollectionView *mCollectionDremboard;
@property (weak, nonatomic) IBOutlet UIButton *mBtnSelectionAll;
@property (weak, nonatomic) IBOutlet UIButton *mBtnBack;

- (IBAction)onClickBack:(id)sender;
- (IBAction)onClickDelete:(id)sender;
- (IBAction)onClickMove:(id)sender;
- (IBAction)onClickSelectAll:(id)sender;

- (void) setAttributes:(DremboardInfo*)dremboardInfo Drems:(NSMutableArray*)arrayDrem;

@property (assign) id<DremboardManageViewDelegate> delegate;

@end
