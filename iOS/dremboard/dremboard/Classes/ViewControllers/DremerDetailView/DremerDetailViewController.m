//
//  DremerDetailViewController.m
//  dremboard
//
//  Created by YingLi on 5/29/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremerDetailViewController.h"
#import "UIImageView+AFNetworking.h"
#import "UIViewController+MJPopupViewController.h"
#import "ActivityTabBarController.h"
#import "ProfileTabBarController.h"
#import "FriendTabBarController.h"
#import "FamilyTabBarController.h"
#import "FollowTabBarController.h"
#import "MediaTabBarController.h"
#import "UIButton+AFNetworking.h"

@interface DremerDetailViewController ()

@end

@implementation DremerDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    [self.mTableMenu setDataSource:self];
    [self.mTableMenu setDelegate:self];
    self.mArrayMenu = [[NSMutableArray alloc] init];
    
    [self setupNavBarItem];
    
    [self setupMenuItems];
    [self.mTableMenu reloadData];
    
    [self startGetSingleDremer];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(int)dremerId {
    mDremerId = dremerId;
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
}

- (void)setupMenuItems {
    [self.mArrayMenu addObject:@"Activity"];
    [self.mArrayMenu addObject:@"Profile"];
    [self.mArrayMenu addObject:@"Friends"];
    [self.mArrayMenu addObject:@"Family"];
    [self.mArrayMenu addObject:@"Follow"];
    [self.mArrayMenu addObject:@"Groups"];
    [self.mArrayMenu addObject:@"Media"];
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

- (void) updateViewUserInfo {
    
    if (!mDremerInfo || !mProfiles)
        return;
    
    self.mTxtName.text = mDremerInfo.display_name;
    self.mTxtTime.text = mDremerInfo.last_activity;
    [self.mImgUser setImageWithURL:[NSURL URLWithString:mDremerInfo.user_avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    self.mImgUser.layer.cornerRadius = self.mImgUser.frame.size.width / 2;
    self.mImgUser.layer.masksToBounds = YES;
    
    NSString *strBio = @"";
    for (ProfileItem *item in mProfiles) {
        if ([item.name isEqualToString:@"bio"]) {
            strBio = [NSString stringWithFormat:@"%@", item.value];
            break;
        }
    }
    self.mTxtBio.text = strBio;
    
    [self.mBtnFriend setTitle:[DremerInfo getFriendshipStr:mDremerInfo.friendship_status] forState:UIControlStateNormal];
    self.mBtnFriend.titleLabel.adjustsFontSizeToFitWidth = YES;
    
    [self.mBtnFamily setTitle:[DremerInfo getFamilyshipStr:mDremerInfo.familyship_status] forState:UIControlStateNormal];
    self.mBtnFamily.titleLabel.adjustsFontSizeToFitWidth = YES;
    
    if (mDremerInfo.is_following == 0)
        [self.mBtnFollow setTitle:@"Follow" forState:UIControlStateNormal];
    else
        [self.mBtnFollow setTitle:@"Stop Following" forState:UIControlStateNormal];
}

- (IBAction)onClickFriend:(id)sender {
    if ([mDremerInfo.friendship_status isEqualToString:@"awaiting_response"]) {
        AcceptDiagViewController *acceptVC = [[AcceptDiagViewController alloc] initWithNibName:@"AcceptDiagViewController" bundle:nil];
        [acceptVC setDremerInfo:mDremerInfo];
        [acceptVC setDiagType:@"friendship"];
        acceptVC.mDremerIndex = -1;
        acceptVC.delegate = self;
        [self presentPopupViewController:acceptVC animationType:MJPopupViewAnimationSlideLeftLeft];
        
        return;
    }
    
    NSString *action = [DremerInfo getFriendshipAction:mDremerInfo.friendship_status];
    [self setDremerFriendship:mDremerInfo.user_id Action:action];
}

- (IBAction)onClickPubMsg:(id)sender {
}

- (IBAction)onClickPrivMsg:(id)sender {
}

- (IBAction)onClickFaimly:(id)sender {
}

- (IBAction)onClickFollow:(id)sender {
    NSString *action;
    
    if (mDremerInfo.is_following == 0)
        action = @"start";
    else
        action = @"stop";
    
    [self setDremerFollowing:mDremerInfo.user_id Action:action];
}

- (void) startGetSingleDremer
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
    
    GetSingleDremerParam *param = [[GetSingleDremerParam alloc] init];
    param.user_id = mUserId;
    param.disp_user_id = mDremerId;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_SINGLE_DREMER Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
}

- (void) onGetSingleDremerResult:(NSArray *) result
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
    
    /* get member properties */
    GetSingleDremerData *data = [result valueForKeyPath:@"data"];
    NSDictionary *dataFromResponse = [data valueForKeyPath:@"member"];
    mDremerInfo = [[DremerInfo alloc] initWithAttributes:dataFromResponse];
    
    /* get profiles */
    NSArray *profilesFromResponse = [data valueForKeyPath:@"profiles"];
    mProfiles = [[NSMutableArray alloc] init];
    for (NSDictionary *attributes in profilesFromResponse) {
        ProfileItem *profile = [[ProfileItem alloc] initWithAttributes:attributes];
        [mProfiles addObject:profile];
    }
    
    [self updateViewUserInfo];
}

- (void) setDremerFriendship:(int)dremerId Action:(NSString*)action
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
    
    SetFriendParam *param = [[SetFriendParam alloc] init];
    param.user_id = mUserId;
    param.dremer_id = dremerId;
    param.action = action;
    param.index = -1; // non-used value
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_FRIENDSHIP Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onSetDremerFriendshipResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    SetFriendData *data = [result valueForKeyPath:@"data"];
    NSString *friendship_status = [data valueForKeyPath:@"friendship_status"];
    
    // Update dremer data
    [mDremerInfo setFriendship_status: friendship_status];
    
    [self updateViewUserInfo];
}

- (void) setDremerFamilyship:(int)dremerId Action:(NSString*)action
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
    
    SetFamilyParam *param = [[SetFamilyParam alloc] init];
    param.user_id = mUserId;
    param.dremer_id = dremerId;
    param.action = action;
    param.index = -1; // non-used value
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_FAMILYSHIP Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onSetDremerFamilyshipResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    SetFamilyData *data = [result valueForKeyPath:@"data"];
    NSString *familyship_status = [data valueForKeyPath:@"familyship_status"];
    
    // Update dremer data
    [mDremerInfo setFamilyship_status: familyship_status];
    
    [self updateViewUserInfo];
}

- (void) setDremerFollowing:(int)dremerId Action:(NSString*)action
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
    
    SetFollowingParam *param = [[SetFollowingParam alloc] init];
    param.user_id = mUserId;
    param.dremer_id = dremerId;
    param.action = action;
    param.index = -1;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_FOLLOW Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onSetDremerFollowingResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    SetFollowingData *data = [result valueForKeyPath:@"data"];
    int is_follow = (int)[[data valueForKeyPath:@"is_follow"] integerValue];
    
    // Update dremer data
    [mDremerInfo setIs_following:is_follow];
    
    [self updateViewUserInfo];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case GET_SINGLE_DREMER:
            [self onGetSingleDremerResult:(NSArray *)result];
            break;
            
        case SET_FRIENDSHIP:
            [self onSetDremerFriendshipResult:(NSDictionary*)param Result:(NSArray*)result];
            break;
            
        case SET_FAMILYSHIP:
            [self onSetDremerFamilyshipResult:(NSDictionary*)param Result:(NSArray*)result];
            break;
            
        case SET_FOLLOW:
            [self onSetDremerFollowingResult:(NSDictionary*)param Result:(NSArray*)result];
            break;
            
        default:
            break;
    }
    
}

#pragma mark - BlockViewDelegate
- (void) closeBlockView:(BlockDiagViewController*)blockVC {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) afterBlock:(BlockDiagViewController *)blockVC BlockType:(int)blockType Index:(int)dremerIndex {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
    
    mDremerInfo.block_type = blockType;
    
    [self updateViewUserInfo];
}

#pragma mark - AcceptViewDelegate
- (void)afterAcceptReject:(AcceptDiagViewController*)acceptkVC Status:(NSString*)status Index:(int)dremerIndex {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
    
    mDremerInfo.friendship_status = status;
    
    [self updateViewUserInfo];
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[self.mArrayMenu count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"MenuCell";
    
    UITableViewCell *cell = [self.mTableMenu dequeueReusableCellWithIdentifier:CellIdentifier];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    NSString *menuTitle = [self.mArrayMenu objectAtIndex:[indexPath row]];
    
    cell.textLabel.text = menuTitle;
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    long rowNum = indexPath.row;
    
    if (rowNum == 0) {
        ActivityTabBarController *activityController = [self.storyboard instantiateViewControllerWithIdentifier:@"ActivityTabController"];
        [activityController setAttributes:nil];
        [activityController setMDremerId:mDremerId];
        [self.navigationController pushViewController:activityController animated:YES];
    } else if (rowNum == 1) {
        ProfileTabBarController *profileController = [self.storyboard instantiateViewControllerWithIdentifier:@"ProfileTabController"];
        [profileController setMDremerId:mDremerId];
        [self.navigationController pushViewController:profileController animated:YES];
    } else if (rowNum == 2) {
        FriendTabBarController *friendController = [self.storyboard instantiateViewControllerWithIdentifier:@"FriendTabController"];
        [friendController setAttributes:nil];
        [friendController setMDremerId:mDremerId];
        [self.navigationController pushViewController:friendController animated:YES];
    } else if (rowNum == 3) {
        FamilyTabBarController *familyController = [self.storyboard instantiateViewControllerWithIdentifier:@"FamilyTabController"];
        [familyController setAttributes:nil];
        [familyController setMDremerId:mDremerId];
        [self.navigationController pushViewController:familyController animated:YES];
    } else if (rowNum == 4) {
        FollowTabBarController *followController = [self.storyboard instantiateViewControllerWithIdentifier:@"FollowTabController"];
        [followController setAttributes:nil];
        [followController setMDremerId:mDremerId];
        [self.navigationController pushViewController:followController animated:YES];
    } else if (rowNum == 5) {
        //        UITabBarController *groupController = [self.storyboard instantiateViewControllerWithIdentifier:@"GroupTabController"];
        //        [self.navigationController pushViewController:groupController animated:YES];
    } else if (rowNum == 6) {
        MediaTabBarController *mediaController = [self.storyboard instantiateViewControllerWithIdentifier:@"MediaTabController"];
        [mediaController setAttributes:nil];
        [mediaController setMDremerId:mDremerId];
        [self.navigationController pushViewController:mediaController animated:YES];
    }

}
@end
