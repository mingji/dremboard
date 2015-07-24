//
//  CommentViewController.h
//  dremboard
//
//  Created by YingLi on 6/4/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "Activity.h"
#import "CommentViewCell.h"

@interface CommentViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate, UITableViewDataSource, UITableViewDelegate, UITextViewDelegate>
{
    HttpTask *mHttpTask;
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    int mActivityId;
}

@property (readwrite, nonatomic, strong) NSMutableArray *mArrayComment;

@property (weak, nonatomic) IBOutlet UIButton *mBtnBack;

@property (weak, nonatomic) IBOutlet UITableView *mTableCommentView;

@property (weak, nonatomic) IBOutlet UIView *mViewPost;
@property (weak, nonatomic) IBOutlet UITextView *mTxtComment;
@property (weak, nonatomic) IBOutlet UIButton *mBtnPost;

- (IBAction)onClickPost:(id)sender;
- (IBAction)onClickBack:(id)sender;

- (void) setAttributes:(int)activityId Comments:(NSMutableArray *)arrayComment;
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
