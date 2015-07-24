//
//  DremboardDetailVC.m
//  dremboard
//
//  Created by YingLi on 6/4/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremboardDetailVC.h"
#import "UIImageView+AFNetworking.h"
#import "UIViewController+MJPopupViewController.h"
#import "UIButton+AFNetworking.h"
#import "Actions.h"
#import "DremboardManageVC.h"

@interface DremboardDetailVC ()

@end

static int PER_PAGE = 15;

@implementation DremboardDetailVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    [self.mCollectionDremView setDataSource:self];
    [self setupNavBarItem];
    [self setupRefreshControl];
    [self updateBoardInfo];
    
    self.mArrayDrem = [[NSMutableArray alloc] init];
    [self startGetDremsList];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(DremboardInfo *) dremboardInfo Index:(int)index {
    mIndex = index;
    mDremboardInfo = dremboardInfo;
    mLastMediaId = 0;
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
}

- (void) updateBoardInfo {
    if (!mDremboardInfo)
        return;
    
    self.mTxtTitle.text = mDremboardInfo.media_title;
    self.mTxtUsername.text = mDremboardInfo.media_author_name;
    [self.mImgUser setImageWithURL:[NSURL URLWithString:mDremboardInfo.media_author_avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    self.mImgUser.layer.cornerRadius = self.mImgUser.frame.size.width / 2;
    self.mImgUser.layer.masksToBounds = YES;
    
    self.mViewBoard.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.mViewBoard.layer.borderWidth = 1.0f;
    
    self.mViewUser.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.mViewUser.layer.borderWidth = 1.0f;
    
    if (mUserId != mDremboardInfo.media_author_id) {
        self.mBtnOption.hidden = YES;
    } else {
        self.mBtnOption.hidden = NO;
    }
}

- (void) setupRefreshControl {
    
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetDremsList) forControlEvents:UIControlEventValueChanged];
    [self.mCollectionDremView addSubview:mRefreshControl];
    self.mCollectionDremView.alwaysBounceVertical = YES;
}

- (void)setupNavBarItem {
    
    // Back Button
    UIButton *backButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 33, 33)];
    [backButton addTarget:self action:@selector(onBackAction:) forControlEvents:UIControlEventTouchUpInside];
    [backButton setBackgroundImage:[UIImage imageNamed:@"back_arrow.png"] forState:UIControlStateNormal];
    UIBarButtonItem *backButtonBar = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    [self.navigationItem setLeftBarButtonItem:backButtonBar];
    
    // User Button
    NSString *userAvatar = [[NSUserDefaults standardUserDefaults] stringForKey:@"logined_user_avatar"];
    UIButton *userButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 40, 40)];
    [userButton setBackgroundImageForState:UIControlStateNormal
                                   withURL:[NSURL URLWithString:userAvatar]
                          placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    userButton.layer.cornerRadius = userButton.frame.size.width / 2;
    userButton.layer.masksToBounds = YES;
    UIBarButtonItem *userButtonBar = [[UIBarButtonItem alloc] initWithCustomView:userButton];
    [self.navigationItem setRightBarButtonItem:userButtonBar];
}

- (void)onBackAction:(id)selector
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - Click Button
- (IBAction)onClickOption:(id)sender {
    mOptionVC = [[DremboardOptionVC alloc] initWithNibName:@"DremboardOptionVC" bundle:nil];

    mOptionVC.delegate = self;
    [self presentPopupViewController:mOptionVC animationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) startDeleteDremboard
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
    
    DeleteDremboardParam *param = [[DeleteDremboardParam alloc] init];
    param.dremboard_id = mDremboardInfo.dremboard_id;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)DELETE_DREMBOARD Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onDeleteDremboardResult:(NSArray *) result
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
    
    if (self.delegate &&
        [self.delegate respondsToSelector:@selector(deletedDremboard:)])
    {
        [self.delegate deletedDremboard:mIndex];
    }
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void) startGetDremsList
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
    
    GetDremsParam *param = [[GetDremsParam alloc] init];
    param.user_id = mUserId;
    param.search_str = @"";
    param.category = -1;
    param.album_id = mDremboardInfo.dremboard_id;
    param.author_id = -1;
    param.last_media_id = mLastMediaId;
    param.per_page = PER_PAGE;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_DREMS Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onGetDremsListResult:(NSArray *) result
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
    
    GetDremsData *data = [result valueForKeyPath:@"data"];
    NSArray *dremsFromResponse = [data valueForKeyPath:@"media"];
    for (NSDictionary *attributes in dremsFromResponse) {
        DremInfo *drem = [[DremInfo alloc] initWithAttributes:attributes];
        [_mArrayDrem addObject:drem];
        mLastMediaId = drem.drem_id;
    }
    
    [_mCollectionDremView reloadData];
}

- (void) setLikeDrem:(DremInfo * ) drem Index:(int)index
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
    paramSetLike.index = index;
    
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
    
    // Update activity cell data
    int index = [[param valueForKey:PARAM_ITEM_INDEX] intValue];
    DremInfo *drem = [_mArrayDrem objectAtIndex:index];
    [drem setLike:like_str];
    
    [_mCollectionDremView reloadData];
}

- (void) setFavoriteDrem:(DremInfo * )drem Index:(int)index
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
    
    NSString *favorite_str = @"Favorite";
    if ([drem.favorite rangeOfString:@"Unfavorite"].location != NSNotFound)
        favorite_str = @"Unfavorite";
    
    SetFavoriteParam *paramSetLike = [[SetFavoriteParam alloc] init];
    [paramSetLike setUser_id: mUserId];
    [paramSetLike setActivity_id: drem.activity_id];
    [paramSetLike setFavorite_str: favorite_str];
    [paramSetLike setIndex: index];
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_FAVORITE Parameter:paramSetLike];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
}

- (void) onSetFavoriteResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    SetFavoriteData *data = [result valueForKeyPath:@"data"];
    NSString *favorite_str = [data valueForKeyPath:@"favorite_str"];
    
    // Update activity cell data
    int index = [[param valueForKey:PARAM_ITEM_INDEX] intValue];
    DremInfo *drem = [_mArrayDrem objectAtIndex:index];
    [drem setFavorite: favorite_str];
    
    [_mCollectionDremView reloadData];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    [mRefreshControl endRefreshing];
    
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case DELETE_DREMBOARD:
            [self onDeleteDremboardResult:(NSArray*)result];
            break;
            
        case GET_DREMS:
            [self onGetDremsListResult:(NSArray*)result];
            break;
            
        case SET_LIKE:
            [self onSetLikeResult:(NSDictionary*)param Result:(NSArray *)result];
            break;
            
        case SET_FAVORITE:
            [self onSetFavoriteResult:(NSDictionary*)param Result:(NSArray *)result];
            break;
            
        default:
            break;
    }
}

#pragma mark - DremViewCellDelegate
- (void) setLike:(int)index {
    DremInfo *drem = [_mArrayDrem objectAtIndex:index];
    [self setLikeDrem:drem Index:index];
}

- (void) setFavorite:(int)index {
    DremInfo *drem = [_mArrayDrem objectAtIndex:index];
    [self setFavoriteDrem:drem Index:index];
}

- (void) clickShare:(int)index {
    ShareViewController *shareVC = [[ShareViewController alloc] initWithNibName:@"ShareView" bundle:nil];
    
    DremInfo *drem = [_mArrayDrem objectAtIndex:index];
    [shareVC setActivityIndex:index];
    [shareVC setImageUrl:drem.guid];
    [self presentPopupViewController:shareVC animationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) clickFlag:(int)index {
    DremInfo *drem = [_mArrayDrem objectAtIndex:index];
    
    FlagViewController *flagVC = [[FlagViewController alloc] initWithNibName:@"FlagViewController" bundle:nil];
    [flagVC setActivityId:drem.activity_id];
    [flagVC setActivityIndex:index];
    flagVC.delegate = self;
    [self presentPopupViewController:flagVC animationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) clickContent:(int)index {
    DremInfo *drem = [_mArrayDrem objectAtIndex:index];
    
    DremSingleViewController *dremVC = [[DremSingleViewController alloc] initWithNibName:@"DremSingleViewController" bundle:nil];
    [dremVC setAttributes:drem Index:index MainVC:self];
    dremVC.delegate = self;
    [self presentPopupViewController:dremVC animationType:MJPopupViewAnimationSlideBottomBottom TouchDismiss:NO];
}

#pragma mark - FlagViewDelegate
- (void) closeFlagView:(FlagViewController*)flagVC {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) afterFlaged:(FlagViewController *)flagVC Index:(int)activityIndex {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
    
    [self.mArrayDrem removeObjectAtIndex:activityIndex];
    [self.mCollectionDremView reloadData];
}

#pragma mark - DremSingleViewDelegate
- (void) closeView:(DremSingleViewController *)vc {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
}

#pragma mark - BoardEditDetailVCDelegate
- (void) closeBoardEditDetailView {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
}

- (void) successBoardEdit:(NSString*)title Description:(NSString*)description {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
    
    self.mTxtTitle.text = title;
    mDremboardInfo.media_title = [[NSString alloc] initWithString:title];
    
    if (self.delegate &&
        [self.delegate respondsToSelector:@selector(updateDremboard:Title:Count:)])
    {
        [self.delegate updateDremboard:mIndex Title:title Count:[_mArrayDrem count]];
    }
}

#pragma mark - DremboardOptionDelegate
- (void) onClickEdit {
    DremboardEditDetailVC *editVC = [[DremboardEditDetailVC alloc] initWithNibName:@"DremboardEditDetailVC" bundle:nil];
    [editVC setAttribute:mDremboardInfo];
    editVC.delegate = self;
    [self presentPopupViewController:editVC animationType:MJPopupViewAnimationSlideBottomBottom TouchDismiss:NO];
}

- (void) onClickDelete {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Delete drēmboard"
                                                    message:@"Are you sure delete a drēmboard?"
                                                   delegate:self
                                          cancelButtonTitle:@"Cancel"
                                          otherButtonTitles:@"Delete", nil];
    [alert show];
}

- (void) onClickMerge {
    DremboardMergeVC *mergeVC = [[DremboardMergeVC alloc] initWithNibName:@"DremboardMergeVC" bundle:nil];
    [mergeVC setAttribute:mDremboardInfo.dremboard_id];
    mergeVC.delegate = self;
    [self presentPopupViewController:mergeVC animationType:MJPopupViewAnimationSlideBottomBottom TouchDismiss:NO];
}

- (void) onClickManage {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
    
    UIStoryboard *storybord = [UIStoryboard storyboardWithName:@"Main_iPhone" bundle:nil];
    DremboardManageVC *manageVC = [storybord instantiateViewControllerWithIdentifier:@"DremboardManageVC"];
    [manageVC setAttributes:mDremboardInfo Drems:self.mArrayDrem];
    [manageVC setDelegate:self];
    
    [self presentViewController:manageVC animated:TRUE completion:nil];
}

#pragma mark - DremboardOptionDelegate
- (void) closeDremboardMergeView {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
}

- (void) successDremboardMerge {
    if (self.delegate &&
        [self.delegate respondsToSelector:@selector(deletedDremboard:)])
    {
        [self.delegate deletedDremboard:mIndex];
    }
    [self dismissViewControllerAnimated:YES completion:nil];
    
}

#pragma mark - DremboardManageViewDelegate
- (void) removedDrems:(NSMutableArray *)arrayDrems {
    
    for (int index = 0; index < [_mArrayDrem count]; index++)
    {
        DremInfo *drem = [_mArrayDrem objectAtIndex:index];
        for (DremInfo *removeDrem in arrayDrems) {
            if (drem.drem_id == removeDrem.drem_id) {
                [_mArrayDrem removeObject:drem];
                index --;
                break;
            }
        }
    }
    
    mDremboardInfo.album_count = [_mArrayDrem count];
    
    [self.mCollectionDremView reloadData];
    
    if (self.delegate &&
        [self.delegate respondsToSelector:@selector(updateDremboard:Title:Count:)])
    {
        [self.delegate updateDremboard:mIndex Title:mDremboardInfo.media_title Count:mDremboardInfo.album_count];
    }
}


#pragma mark - UIAlertView Delegate
- (void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    // click delete a dremboard
    if (buttonIndex == [alertView firstOtherButtonIndex]) {
        [self startDeleteDremboard];
    }
    // click cancel
    else if (buttonIndex == [alertView cancelButtonIndex]) {
        
    }
}

#pragma mark - UICollectionViewDataSource
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return [_mArrayDrem count];
}

// The cell that is returned must be retrieved from a call to -dequeueReusableCellWithReuseIdentifier:forIndexPath:
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    DremViewCell* cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"DremViewCell" forIndexPath:indexPath];
    
    DremInfo *drem = [_mArrayDrem objectAtIndex:[indexPath row]];
    
    [cell setIndex:(int)[indexPath row]];
    [cell setDelegate:self];
    cell.dremInfo = drem;
    return cell;
}

@end
