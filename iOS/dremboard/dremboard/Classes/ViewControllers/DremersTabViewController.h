//
//  DremersTabViewController.h
//  dremboard
//
//  Created by YingLi on 4/29/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "Dremer.h"
#import "M13InfiniteTabBarController.h"

#import "DremersGridVC.h"
#import "DremersListVC.h"

@protocol DremersDataDelegate <NSObject>
- (void) getDremerList;
- (void) showDremerDetail:(int)dremerId;
@end

@interface DremersTabViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, UITabBarDelegate, DremersDataDelegate>
{
    DremersGridVC *mGridVC;
    DremersListVC *mListVC;

    HttpTask *mHttpTask;
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mLastPage;
    int mUserId;
}

@property (strong, nonatomic) M13InfiniteTabBarController *mainVC;

@property (weak, nonatomic) IBOutlet UITabBar *mTabBar;
@property (readwrite, nonatomic, strong) NSMutableArray *mArrayDremer;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;
@end
