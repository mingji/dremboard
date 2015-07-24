//
//  MessageViewController.h
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "MessageViewCell.h"

@interface MessageViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, UITableViewDataSource, UITableViewDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    UIRefreshControl *mRefreshControl;
    
    NSMutableArray * mArrayMsg;
    
    NSString * mMessageType;
    int mLastPage;
    int mUserId;
}

@property (weak, nonatomic) IBOutlet UITableView *mTableMessageView;

- (void) setAttributes:(NSString *)type;
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
