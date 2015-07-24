//
//  ProfileViewController.h
//  dremboard
//
//  Created by YingLi on 5/28/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "Dremer.h"

@interface ProfileViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, UITableViewDataSource, UITableViewDataSource, UITableViewDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    int mDremerId;
}

@property (weak, nonatomic) IBOutlet UITableView *mTableProfileView;

@property (readwrite, nonatomic, strong) NSMutableArray *mArrayProfiles;

- (void) initAttributes:(int)dremerId;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
