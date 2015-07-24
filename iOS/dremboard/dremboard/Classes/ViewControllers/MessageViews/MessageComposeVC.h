//
//  MessageComposeVC.h
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"

@interface MessageComposeVC : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;

    int mUserId;
}

@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (weak, nonatomic) IBOutlet UITextField *txtUsers;
@property (weak, nonatomic) IBOutlet UITextField *txtSubject;
@property (weak, nonatomic) IBOutlet UITextView *txtMessage;
@property (weak, nonatomic) IBOutlet UIButton *btnSend;

- (IBAction)onClickSend:(id)sender;

@end
