//
//  HomeTabViewController.m
//  dremboard
//
//  Created by vitaly on 4/25/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "HomeTabViewController.h"
#import "Activity.h"
#import "Actions.h"
#import "UIViewController+MJPopupViewController.h"
#import "DremerDetailViewController.h"
#import "CommentViewController.h"
#import "ShareViewController.h"
#import "AppDelegate.h"
#import "UIButton+AFNetworking.h"

@interface HomeTabViewController ()

@end

static int PER_PAGE = 10;
static NSString *CellIdentifier = @"ActivityViewCell";

@implementation HomeTabViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [_m_tableActivityView setDataSource:self];
    _m_tableActivityView.delegate = self;
    
    _mArrayActivity = [[NSMutableArray alloc] init];
    
    mStrScope = @"all";
    mLastActivity = 0;
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    [self setupScope];
    [self setupRefreshControl];
    [self setupTopBarItem];
    [self startGetActivitiesList];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setupRefreshControl {
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetActivitiesList) forControlEvents:UIControlEventValueChanged];
    [self.m_tableActivityView addSubview:mRefreshControl];
    self.m_tableActivityView.alwaysBounceVertical = YES;
}

- (void) resetActivitiesData {
    mLastActivity = 0;
    [self.mArrayActivity removeAllObjects];
    [self.m_tableActivityView reloadData];
}

- (void) setupScope {
    mDicScope = [[NSMutableDictionary alloc]initWithCapacity:10];
    [mDicScope setObject:@"all" forKey:@"All Members"];
    [mDicScope setObject:@"following" forKey:@"Following"];
    [mDicScope setObject:@"friends" forKey:@"My Friends"];
    [mDicScope setObject:@"groups" forKey:@"My Groups"];
    [mDicScope setObject:@"favorites" forKey:@"My Favorites"];
    [mDicScope setObject:@"mentions" forKey:@"Mentions"];
    [mDicScope setObject:@"notifications" forKey:@"Notifications"];
    
    NSMutableArray* arrayScope = [[NSMutableArray alloc] init];
    [arrayScope addObject:@"All Members"];
    [arrayScope addObject:@"Following"];
    [arrayScope addObject:@"My Friends"];
    [arrayScope addObject:@"My Groups"];
    [arrayScope addObject:@"My Favorites"];
    [arrayScope addObject:@"Mentions"];
    [arrayScope addObject:@"Notifications"];
    
    self.mPickerScope = [[DownPicker alloc] initWithTextField:self.mTxtScope withData:arrayScope];
    [self.mPickerScope showArrowImage:YES];
    [self.mPickerScope setPlaceholder:@"All Members"];
    
    [self.mPickerScope addTarget:self
                             action:@selector(onSelectedScope:)
                   forControlEvents:UIControlEventValueChanged];
}

- (NSString*) getScope :(NSString *) strScopeDesc {
    NSString *retScope = @"all";
    
    retScope = [mDicScope objectForKey:strScopeDesc];
    
    return retScope;
}

- (void) onSelectedScope:(id) downpicker {
    NSString *strScope = [self.mPickerScope text];
    
    if ([strScope length] == 0)
    {
        mInfoDlg = [[ToastView alloc] init:@"Please select the scope." durationTime:2 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    if ([mStrScope isEqualToString:[self getScope:strScope]])
         return;
    
    mStrScope = [self getScope:strScope];
    
    [self resetActivitiesData];
    [self startGetActivitiesList];
    
}

- (void)setupTopBarItem {
    
    // User Button
    NSString *userAvatar = [[NSUserDefaults standardUserDefaults] stringForKey:@"logined_user_avatar"];
    [self.mBtnUser setBackgroundImageForState:UIControlStateNormal
                                   withURL:[NSURL URLWithString:userAvatar]
                          placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    self.mBtnUser.layer.cornerRadius = self.mBtnUser.frame.size.width / 2;
    self.mBtnUser.layer.masksToBounds = YES;
}

- (void) startGetActivitiesList
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
    
    GetActivitiesParam *param = [[GetActivitiesParam alloc] init];
    param.user_id = mUserId;
    param.acitvity_scope = mStrScope;
    param.disp_user_id = -1;
    param.type = @"";
    param.last_activity_id = mLastActivity;
    param.per_page = PER_PAGE;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_DREM_ACTIVITIES Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onGetActivitiesListResult:(NSArray *) result
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
    
    GetActivitiesData *data = [result valueForKeyPath:@"data"];
    NSArray *activitiesFromResponse = [data valueForKeyPath:@"activity"];
    for (NSDictionary *attributes in activitiesFromResponse) {
        ActivityInfo *activity = [[ActivityInfo alloc] initWithAttributes:attributes];
        [_mArrayActivity addObject:activity];
        mLastActivity = activity.activity_id;
    }
    
    [_m_tableActivityView reloadData];
}

- (void) setLikeActivity:(ActivityInfo * ) activity Index:(int)index
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
    if ([activity.like rangeOfString:@"Unlike"].location != NSNotFound)
        like_str = @"Unlike";
    
    SetLikeParam *paramSetLike = [[SetLikeParam alloc] init];
    paramSetLike.user_id = mUserId;
    paramSetLike.activity_id = activity.activity_id;
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
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:index];
    activity.like = like_str;
    
    [_m_tableActivityView reloadData];
}

- (void) setFavoriteActivity:(ActivityInfo * ) activity Index:(int)index
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
    if ([activity.favorite rangeOfString:@"Unfavorite"].location != NSNotFound)
        favorite_str = @"Unfavorite";
    
    SetFavoriteParam *paramSetLike = [[SetFavoriteParam alloc] init];
    paramSetLike.user_id = mUserId;
    paramSetLike.activity_id = activity.activity_id;
    paramSetLike.favorite_str = favorite_str;
    paramSetLike.index = index;
    
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
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:index];
    activity.favorite = favorite_str;
    
    [_m_tableActivityView reloadData];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSDictionary *)param Result:(NSObject *)result
{
    [mRefreshControl endRefreshing];
    
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case GET_DREM_ACTIVITIES:
            [self onGetActivitiesListResult:(NSArray*)result];
            break;
            
        case SET_LIKE:
            [self onSetLikeResult:param Result:(NSArray *)result];
            break;
            
        case SET_FAVORITE:
            [self onSetFavoriteResult:param Result:(NSArray *)result];
            break;
            
        default:
            break;
    }
}

#pragma mark - ActivityViewCellDelegate
- (void) setLike:(int)index {
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:index];
    [self setLikeActivity:activity Index:index];
}

- (void) setFavorite:(int)index {
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:index];
    [self setFavoriteActivity:activity Index:index];
}

- (void) clickShare:(int)index {
    ShareViewController *shareVC = [[ShareViewController alloc] initWithNibName:@"ShareView" bundle:nil];
    
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:index];
    [shareVC setActivityIndex:index];
    if ([activity.media_list count] > 0) {
        MediaInfo *media = [activity.media_list objectAtIndex:0];
        [shareVC setImageUrl:media.media_guid];
    }
    
    [self presentPopupViewController:shareVC animationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) clickFlag:(int)index {
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:index];
    
    FlagViewController *flagVC = [[FlagViewController alloc] initWithNibName:@"FlagViewController" bundle:nil];
    [flagVC setActivityId:activity.activity_id];
    [flagVC setActivityIndex:index];
    flagVC.delegate = self;
    [self presentPopupViewController:flagVC animationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) finishMediaImage:(int)index {
    [self.m_tableActivityView beginUpdates];
//    [self.m_tableActivityView reloadData];
    [self.m_tableActivityView reloadRowsAtIndexPaths:
                [NSArray arrayWithObject:[NSIndexPath indexPathForRow:index inSection:0]] withRowAnimation:UITableViewRowAnimationAutomatic];
    [self.m_tableActivityView endUpdates];
}

- (void) clickUser:(int)index {
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:index];
    
    if (activity.author_id == mUserId)
        return;
    
    UINavigationController *dremerDetailNC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailNav"];
    DremerDetailViewController *dremerDetailVC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailVC"];
    [dremerDetailVC setAttributes:activity.author_id];
    [dremerDetailNC pushViewController:dremerDetailVC animated:YES];
    
    [self.mainVC presentViewController:dremerDetailNC animated:TRUE completion:nil];
}

- (void) clickComment:(int)index {
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:index];
    
    CommentViewController *commentVC = [self.storyboard instantiateViewControllerWithIdentifier:@"CommentVC"];
    [commentVC setAttributes:activity.activity_id Comments:activity.comment_list];
    
    [self.mainVC presentViewController:commentVC animated:TRUE completion:nil];
}

#pragma mark - FlagViewDelegate
- (void) closeFlagView:(FlagViewController*)flagVC {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) afterFlaged:(FlagViewController *)flagVC Index:(int)activityIndex {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
    
    [self.mArrayActivity removeObjectAtIndex:activityIndex];
    [self.m_tableActivityView reloadData];
}


#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[_mArrayActivity count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [self basicCellAtIndexPath:indexPath];
}

- (ActivityViewCell *)basicCellAtIndexPath:(NSIndexPath *)indexPath {
    ActivityViewCell *cell = [_m_tableActivityView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    if (!cell) {
        cell = [[ActivityViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }

    [self configureBasicCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)configureBasicCell:(ActivityViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:[indexPath row]];
    [cell setIndex:(int)[indexPath row]];
    [cell setDelegate:self];
    cell.activityInfo = activity;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return [self heightForBasicCellAtIndexPath:indexPath];
}

- (CGFloat)heightForBasicCellAtIndexPath:(NSIndexPath *)indexPath {
    
    ActivityInfo *activity = [_mArrayActivity objectAtIndex:[indexPath row]];
    return [ActivityViewCell heightForCellWithActivity:activity];
}

@end
