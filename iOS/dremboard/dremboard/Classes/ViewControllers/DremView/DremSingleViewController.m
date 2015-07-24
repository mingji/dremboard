//
//  DremSingleViewController.m
//  dremboard
//
//  Created by YingLi on 6/3/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremSingleViewController.h"
#import "CommentViewController.h"
#import "Actions.h"
#import "UIViewController+MJPopupViewController.h"
#import "UIImageView+AFNetworking.h"

@interface DremSingleViewController ()

@end

@implementation DremSingleViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view from its nib.
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    [self initView];
    
    // User Image
    UITapGestureRecognizer *imageTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickDremImg)];
    imageTap.numberOfTapsRequired = 1;
    imageTap.numberOfTouchesRequired = 1;
    [self.mImgContent addGestureRecognizer:imageTap];
    [self.mImgContent setUserInteractionEnabled:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(DremInfo *)dreminfo Index:(int)index MainVC:(UIViewController *)mainVC{
    self.mDremInfo = dreminfo;
    self.mDremIndex = index;
    
    self.mainVC = mainVC;
}

- (void) initView {
    self.mImgUser.layer.cornerRadius = self.mImgUser.frame.size.width / 2;
    self.mImgUser.layer.masksToBounds = YES;
    
    [self.mImgContent setImageWithURL:[NSURL URLWithString:self.mDremInfo.guid] placeholderImage:[UIImage imageNamed:@"image_thumb.png"]];
    
    [self.mBtnLike setTitle:self.mDremInfo.like forState:UIControlStateNormal];
    int cntComment = (int)[self.mDremInfo.comment_list count];
    if (cntComment > 0)
        [self.mBtnComment setTitle:[NSString stringWithFormat:@"Comment (%d)", cntComment] forState:UIControlStateNormal];
    else
        [self.mBtnComment setTitle:@"Comment" forState:UIControlStateNormal];
}

- (void) closeView {
    if (self.delegate && [self.delegate respondsToSelector:@selector(closeView:)]) {
        [self.delegate closeView:self];
    }
}

- (void) onClickDremImg {
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Add to Drēmboard"
                                                    message:@"Are you sure add a drem to your drēmboard?"
                                                   delegate:self
                                          cancelButtonTitle:@"Cancel"
                                          otherButtonTitles:@"Ok", nil];
    [alert show];
}

- (IBAction)onClickClose:(id)sender {
    [self closeView];
}

- (IBAction)onClickLike:(id)sender {
    [self setLikeDrem:self.mDremInfo];
}

- (IBAction)onClickFlag:(id)sender {
    FlagViewController *flagVC = [[FlagViewController alloc] initWithNibName:@"FlagViewController" bundle:nil];
    [flagVC setActivityId:self.mDremInfo.activity_id];
    [flagVC setActivityIndex:-1];
    flagVC.delegate = self;
    [self presentPopupViewController:flagVC animationType:MJPopupViewAnimationSlideLeftLeft];
}

- (IBAction)onClickComment:(id)sender {
    UIStoryboard *storybord = [UIStoryboard storyboardWithName:@"Main_iPhone" bundle:nil];
    CommentViewController *commentVC = [storybord instantiateViewControllerWithIdentifier:@"CommentVC"];
    [commentVC setAttributes:self.mDremInfo.activity_id Comments:self.mDremInfo.comment_list];
    
    [self.mainVC presentViewController:commentVC animated:TRUE completion:nil];
}

- (IBAction)onClickShare:(id)sender {
    ShareViewController *shareVC = [[ShareViewController alloc] initWithNibName:@"ShareView" bundle:nil];
    [shareVC setActivityIndex:self.mDremInfo.activity_id];
    [shareVC setImageUrl:self.mDremInfo.guid];
    [self presentPopupViewController:shareVC animationType:MJPopupViewAnimationSlideLeftLeft];
}


- (void) setLikeDrem:(DremInfo *)drem
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
    
    NSString *like_str = @"Like";
    if ([drem.like rangeOfString:@"Unlike"].location != NSNotFound)
        like_str = @"Unlike";
    
    SetLikeParam *paramSetLike = [[SetLikeParam alloc] init];
    paramSetLike.user_id = mUserId;
    paramSetLike.activity_id = drem.activity_id;
    paramSetLike.like_str = like_str;
    paramSetLike.index = self.mDremIndex;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_LIKE Parameter:paramSetLike];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
}

- (void) onSetLikeResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    SetLikeData *data = [result valueForKeyPath:@"data"];
    NSString *like_str = [data valueForKeyPath:@"like_str"];
    
    [self.mDremInfo setLike:like_str];
    [self.mBtnLike setTitle:self.mDremInfo.like forState:UIControlStateNormal];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
            
        case SET_LIKE:
            [self onSetLikeResult:(NSDictionary*)param Result:(NSArray *)result];
            break;
            
        default:
            break;
    }
}

#pragma mark - UIAlertView Delegate
- (void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    // click add to dremboard
    if (buttonIndex == [alertView firstOtherButtonIndex]) {
        addDremVC = [[DremToDremboardViewController alloc] initWithNibName:@"DremToDremboardViewController" bundle:nil];
        addDremVC.delegate = self;
        [addDremVC setAttributes:self.mDremInfo.drem_id];
        
        [self presentPopupViewController:addDremVC animationType:MJPopupViewAnimationSlideBottomBottom TouchDismiss:NO];
    }
    // click cancel
    else if (buttonIndex == [alertView cancelButtonIndex]) {
        
    }
}

#pragma mark - FlagViewDelegate
- (void) closeFlagView:(FlagViewController*)flagVC {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) afterFlaged:(FlagViewController *)flagVC Index:(int)activityIndex {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
}

#pragma mark - DremToDremboardViewDelegate
- (void) closeDremToBoardView:(UIViewController *)vc {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
}

- (void) clickCreateDremboard {
    createBoardVC = [[CreateDremboardVC alloc] initWithNibName:@"CreateDremboardVC" bundle:nil];
    createBoardVC.delegate = self;
    [self presentPopupViewController:createBoardVC animationType:MJPopupViewAnimationSlideBottomBottom TouchDismiss:NO];
}

#pragma mark - CreateDremboardVCDelegate
- (void) closeCreateDremboardView:(UIViewController *)vc {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
}

@end
