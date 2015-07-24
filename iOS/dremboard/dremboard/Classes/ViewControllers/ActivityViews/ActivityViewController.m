//
//  ActivityViewController.m
//  dremboard
//
//  Created by YingLi on 5/20/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "ActivityViewController.h"
#import "Activity.h"
#import "Actions.h"
#import "UIViewController+MJPopupViewController.h"
#import "DremerDetailViewController.h"
#import "ShareViewController.h"
#import "CommentViewController.h"

@interface ActivityViewController ()

@end

static int PER_PAGE = 10;
static NSString *CellIdentifier = @"ActivityViewCell";

@implementation ActivityViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self.mTableActivityView setDataSource:self];
    _mTableActivityView.delegate = self;
    
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetActivitiesList) forControlEvents:UIControlEventValueChanged];
    [self.mTableActivityView addSubview:mRefreshControl];
    self.mTableActivityView.alwaysBounceVertical = YES;
    
    mArrayActivity = [[NSMutableArray alloc] init];
   
    [self startGetActivitiesList];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setAttributes:(NSString *)scope DremerId:(int)dremerId MainVC:(UIViewController *)mainVC {
    mLastActivity = 0;
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];

    mActivityScope = [NSString stringWithString:scope];
    
    if (mUserId == dremerId)
        mDremerId = -1; // non-used value
    else
        mDremerId = dremerId;
    
    self.mainVC = mainVC;
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
    param.acitvity_scope = mActivityScope;
    param.disp_user_id = mDremerId;
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
        [mArrayActivity addObject:activity];
        mLastActivity = activity.activity_id;
    }
    
    [_mTableActivityView reloadData];
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
    ActivityInfo *activity = [mArrayActivity objectAtIndex:index];
    activity.like = like_str;
    
    [self.mTableActivityView reloadData];
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
    ActivityInfo *activity = [mArrayActivity objectAtIndex:index];
    activity.favorite = favorite_str;
    
    [self.mTableActivityView reloadData];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
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
            [self onSetLikeResult:(NSDictionary *)param Result:(NSArray *)result];
            break;
            
        case SET_FAVORITE:
            [self onSetFavoriteResult:(NSDictionary *)param Result:(NSArray *)result];
            break;
            
        default:
            break;
    }
}

#pragma mark - ActivityViewCellDelegate
- (void) setLike:(int)index {
    ActivityInfo *activity = [mArrayActivity objectAtIndex:index];
    [self setLikeActivity:activity Index:index];
}

- (void) setFavorite:(int)index {
    ActivityInfo *activity = [mArrayActivity objectAtIndex:index];
    [self setFavoriteActivity:activity Index:index];
}

- (void) clickShare:(int)index {
    ShareViewController *shareVC = [[ShareViewController alloc] initWithNibName:@"ShareView" bundle:nil];
    
    ActivityInfo *activity = [mArrayActivity objectAtIndex:index];
    [shareVC setActivityIndex:index];
    if ([activity.media_list count] > 0) {
        MediaInfo *media = [activity.media_list objectAtIndex:0];
        [shareVC setImageUrl:media.media_guid];
    }
    
    [self presentPopupViewController:shareVC animationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) clickFlag:(int)index {
    ActivityInfo *activity = [mArrayActivity objectAtIndex:index];
    
    FlagViewController *flagVC = [[FlagViewController alloc] initWithNibName:@"FlagViewController" bundle:nil];
    [flagVC setActivityId:activity.activity_id];
    [flagVC setActivityIndex:index];
    flagVC.delegate = self;
    [self presentPopupViewController:flagVC animationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) finishMediaImage:(int)index {
    [self.mTableActivityView beginUpdates];
    //    [self.mTableActivityView reloadData];
    [self.mTableActivityView reloadRowsAtIndexPaths:
                [NSArray arrayWithObject:[NSIndexPath indexPathForRow:index inSection:0]] withRowAnimation:UITableViewRowAnimationAutomatic];
    [self.mTableActivityView endUpdates];
}

- (void) clickUser:(int)index {
    ActivityInfo *activity = [mArrayActivity objectAtIndex:index];
    
    if (activity.author_id == mUserId)
        return;
    
    UINavigationController *dremerDetailNC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailNav"];
    DremerDetailViewController *dremerDetailVC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailVC"];
    [dremerDetailVC setAttributes:activity.author_id];
    [dremerDetailNC pushViewController:dremerDetailVC animated:YES];
    
    [self.mainVC presentViewController:dremerDetailNC animated:TRUE completion:nil];
}

- (void) clickComment:(int)index {
    ActivityInfo *activity = [mArrayActivity objectAtIndex:index];
    
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
    
    [mArrayActivity removeObjectAtIndex:activityIndex];
    [self.mTableActivityView reloadData];
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[mArrayActivity count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [self basicCellAtIndexPath:indexPath];
}

- (ActivityViewCell *)basicCellAtIndexPath:(NSIndexPath *)indexPath {
    ActivityViewCell *cell = [_mTableActivityView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    if (!cell) {
        cell = [[ActivityViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    [self configureBasicCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)configureBasicCell:(ActivityViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    ActivityInfo *activity = [mArrayActivity objectAtIndex:[indexPath row]];
    
    [cell setDelegate:self];
    [cell setIndex:(int)[indexPath row]];
    cell.activityInfo = activity;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return [self heightForBasicCellAtIndexPath:indexPath];
}

- (CGFloat)heightForBasicCellAtIndexPath:(NSIndexPath *)indexPath {
    ActivityInfo *activity = [mArrayActivity objectAtIndex:[indexPath row]];
    return [ActivityViewCell heightForCellWithActivity:activity];
}

@end
