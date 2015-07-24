//
//  HomeTabViewController.h
//  dremboard
//
//  Created by vitaly on 4/25/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "ActivityViewCell.h"
#import "FlagViewController.h"
#import "M13InfiniteTabBarController.h"
#import "DownPicker.h"

@interface HomeTabViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, UITableViewDataSource, UITableViewDelegate, ActivityViewCellDelegate, FlagViewDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    UIRefreshControl *mRefreshControl;
    
    NSMutableDictionary *mDicScope;
    NSString *mStrScope;
    int mLastActivity;
    int mUserId;
}

@property (strong, nonatomic) DownPicker *mPickerScope;

@property (strong, nonatomic) M13InfiniteTabBarController *mainVC;

@property (strong, nonatomic) IBOutlet UITextField *mTxtScope;
@property (weak, nonatomic) IBOutlet UITableView *m_tableActivityView;
@property (weak, nonatomic) IBOutlet UIButton *mBtnUser;

@property (readwrite, nonatomic, strong) NSMutableArray *mArrayActivity;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
