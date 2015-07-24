//
//  MessageComposeVC.m
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "MessageComposeVC.h"
#import "Message.h"

@interface MessageComposeVC ()

@end

@implementation MessageComposeVC


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL) isEmptyStr:(NSString*)checkStr {
    
    if ([checkStr length] == 0)
        return true;
    else
        return false;
}

- (void) startSendMessage : (NSString *)subject Recipient:(NSString*)recipients Content:(NSString*)content IsNotice:(NSString*)isNotice
{
    if(mWaitingDlg != nil){
        mWaitingDlg = nil;
    }
    
    mWaitingDlg = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:mWaitingDlg];
    
    mWaitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    mWaitingDlg.delegate = self;
    mWaitingDlg.labelText = @"Waiting ...";
    
    [mWaitingDlg show:YES];
    
    SendMessageParam *param = [[SendMessageParam alloc] init];
    param.user_id = mUserId;
    param.recipients = recipients;
    param.subject = subject;
    param.content = content;
    param.is_notice = isNotice;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SEND_MESSAGE Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onSendMessageResult:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    if (![status isEqualToString:@"ok"])
    {
        mInfoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    mInfoDlg = [[ToastView alloc] init:@"Message sent successfully!" durationTime:2 parent:(UIView *)self.view];
    [mInfoDlg show];
    
    self.txtUsers.text = @"";
    self.txtSubject.text = @"";
    self.txtMessage.text = @"";
    
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case SEND_MESSAGE:
            [self onSendMessageResult:(NSArray*)result];
            break;
            
        default:
            break;
    }
}

- (IBAction)onClickSend:(id)sender {
    NSString *recipients = self.txtUsers.text;
    NSString *subject = self.txtSubject.text;
    NSString *content = self.txtMessage.text;
    NSString *is_notice = @"false";
    
    if ([self isEmptyStr:recipients] ||
        [self isEmptyStr:subject] ||
        [self isEmptyStr:content]) {
        mInfoDlg = [[ToastView alloc] init:@"Please input informations" durationTime:2 parent:(UIView *)self.view];
        [mInfoDlg show];
        
        return;
    }
    
    [self startSendMessage:subject Recipient:recipients Content:content IsNotice:is_notice];
}
@end
