//
//  AcceptDiagViewController.h
//  dremboard
//
//  Created by YingLi on 5/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Dremer.h"
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"

@protocol AcceptViewDelegate;

@interface AcceptDiagViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    
    int mUserId;
    
    NSString *mDiagType; // Friend request or Family request
}

@property int mDremerIndex;

@property (nonatomic, strong) DremerInfo *mDremerInfo;

@property (weak, nonatomic) IBOutlet UILabel *txtTitle;
@property (weak, nonatomic) IBOutlet UIImageView *imgUser;
@property (weak, nonatomic) IBOutlet UILabel *txtUserName;
@property (weak, nonatomic) IBOutlet UILabel *txtTime;
@property (weak, nonatomic) IBOutlet UIButton *bntAccept;
@property (weak, nonatomic) IBOutlet UIButton *btnReject;

@property (assign, nonatomic) id <AcceptViewDelegate>delegate;

- (void) setDremerInfo:(DremerInfo *)dremerInfo;
- (void) setDiagType:(NSString *) type;

- (IBAction)onClickAccept:(id)sender;
- (IBAction)onClickReject:(id)sender;

@end

@protocol AcceptViewDelegate<NSObject>
@required
- (void)afterAcceptReject:(AcceptDiagViewController*)acceptkVC Status:(NSString*)status Index:(int)dremerIndex;
@end