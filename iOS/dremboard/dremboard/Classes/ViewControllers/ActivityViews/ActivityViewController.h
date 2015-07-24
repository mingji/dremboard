//
//  ActivityViewController.h
//  dremboard
//
//  Created by YingLi on 5/20/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "ActivityViewCell.h"
#import "FlagViewController.h"

@interface ActivityViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, UITableViewDataSource, UITableViewDelegate, ActivityViewCellDelegate, FlagViewDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    UIRefreshControl *mRefreshControl;
    
    NSMutableArray * mArrayActivity;
 
    NSString * mActivityScope;
    int mLastActivity;
    int mUserId;
    int mDremerId;
}

@property(nonatomic, assign) UIViewController *mainVC;

@property (weak, nonatomic) IBOutlet UITableView *mTableActivityView;

- (void) setAttributes:(NSString *)scope DremerId:(int)dremerId MainVC:(UIViewController *)mainVC;
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
