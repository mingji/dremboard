//
//  DremboardEditDetailVC.m
//  dremboard
//
//  Created by YingLi on 7/16/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremboardEditDetailVC.h"


@interface DremboardEditDetailVC ()

@end

UIGestureRecognizer *tapper;

@implementation DremboardEditDetailVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self initView];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initView {
    
    [self.mTxtTitle setText:mDremboardInfo.media_title];
//    [self.mTxtDesc setText:mDremboardInfo.media_description];

    // Description
    UIColor *borderColor = [UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:204.0/255.0 alpha:1.0];
    self.mTxtDesc.layer.borderColor = borderColor.CGColor;
    self.mTxtDesc.layer.borderWidth = 1.0;
    self.mTxtDesc.layer.cornerRadius = 5.0;

    // Handle Keyboard
    tapper = [[UITapGestureRecognizer alloc]
              initWithTarget:self action:@selector(handleSingleTap:)];
    tapper.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapper];

}

- (void)handleSingleTap:(UITapGestureRecognizer *) sender
{
    [self.view endEditing:YES];
}

- (void)setAttribute:(DremboardInfo *)dremboardInfo {
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    mDremboardInfo = dremboardInfo;
}

- (void) closeView {
    if (self.delegate && [self.delegate respondsToSelector:@selector(closeBoardEditDetailView)]) {
        [self.delegate closeBoardEditDetailView];
    }
}

- (void) onSuccessEdit {
    NSString *title = [self.mTxtTitle text];
    NSString *description = [self.mTxtDesc text];
    
    if (self.delegate &&
        [self.delegate respondsToSelector:@selector(successBoardEdit:Description:)])
    {
        [self.delegate successBoardEdit:title Description:description];
    }
}

#pragma mark - Click Button
- (IBAction)onClickSave:(id)sender {
    NSString *title = [self.mTxtTitle text];
    NSString *description = [self.mTxtDesc text];
    
    // Check parameters
    if (!title || [title length] == 0) {
        mInfoDlg = [[ToastView alloc] init:@"Please input a title." durationTime:1 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    [self startEditDremboard:title Description:description];
}

- (IBAction)onClickClose:(id)sender {
    [self closeView];
}

#pragma mark - HttpAPITask
- (void) startEditDremboard:(NSString*)title Description:(NSString*)description
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
    
    EditDremboardParam *param = [[EditDremboardParam alloc] init];
    param.dremboard_id = mDremboardInfo.dremboard_id;
    param.title = title;
    param.description = description;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)EDIT_DREMBOARD Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onEditDremboardResult:(NSArray *) result
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
    
    [self onSuccessEdit];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case EDIT_DREMBOARD:
            [self onEditDremboardResult:(NSArray*)result];
            break;
            
        default:
            break;
    }
}

@end
