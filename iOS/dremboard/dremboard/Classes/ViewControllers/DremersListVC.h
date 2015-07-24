//
//  DremersListVC.h
//  dremboard
//
//  Created by YingLi on 6/2/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "Dremer.h"
#import "DremerViewCell.h"
#import "BlockDiagViewController.h"
#import "AcceptDiagViewController.h"
#import "M13InfiniteTabBarController.h"


@protocol DremersDataDelegate;

@interface DremersListVC : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, UITableViewDataSource, DremerViewCellDelegate, BlockViewDelegate, AcceptViewDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    UIRefreshControl *mRefreshControl;
    
    int mUserId;
}

@property (strong, nonatomic) UIViewController *mParentVC;

@property (weak, nonatomic) IBOutlet UITableView *mTableDremerView;
@property (readwrite, nonatomic, strong) NSMutableArray *mArrayDremer;

@property (assign) id<DremersDataDelegate> delegate;

- (void) setAttributes:(UIViewController *) parentVC ArrayDremer:(NSMutableArray *)arrayDremer;
- (void) reloadTableView;
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
