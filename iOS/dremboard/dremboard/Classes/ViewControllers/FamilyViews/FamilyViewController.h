//
//  FamilyViewController.h
//  dremboard
//
//  Created by YingLi on 5/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "Dremer.h"
#import "FamilyViewCell.h"
#import "AcceptDiagViewController.h"

@interface FamilyViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, UITableViewDataSource, FamilyViewCellDelegate, AcceptViewDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    UIRefreshControl *mRefreshControl;
    
    NSString * mType;
    
    int mLastPage;
    int mUserId;
    int mDremerId;
}

@property(nonatomic, assign) UIViewController *mainVC;

@property (weak, nonatomic) IBOutlet UITableView *mTableFamilyView;
@property (readwrite, nonatomic, strong) NSMutableArray *mArrayDremer;

- (void) initAttributes:(NSString *)type DremerId:(int)dremerId MainVC:(UIViewController *)mainVC;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
