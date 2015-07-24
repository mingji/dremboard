//
//  FollowViewController.m
//  dremboard
//
//  Created by YingLi on 5/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "FollowViewController.h"
#import "UIViewController+MJPopupViewController.h"
#import "DremerDetailViewController.h"

@interface FollowViewController ()

@end

static int PER_PAGE = 10;

@implementation FollowViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    [_mTableDremerView setDataSource:self];
    
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetDremersList) forControlEvents:UIControlEventValueChanged];
    [self.mTableDremerView addSubview:mRefreshControl];
    self.mTableDremerView.alwaysBounceVertical = YES;
    
    _mArrayDremer = [[NSMutableArray alloc] init];
    
    [self startGetDremersList];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initAttributes:(NSString *)type DremerId:(int)dremerId MainVC:(UIViewController *)mainVC{
    mLastPage = 1;
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];

    mType = [NSString stringWithString:type];

    mDremerId = dremerId;
    self.mainVC = mainVC;
}

- (void) startGetDremersList
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
    
    GetDremersParam *param = [[GetDremersParam alloc] init];
    param.user_id = mUserId;
    param.search_str = @"";
    param.disp_user_id = mDremerId;
    param.type = mType;
    param.page = mLastPage;
    param.per_page = PER_PAGE;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_DREMERS Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onGetDremersListResult:(NSArray *) result
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
    
    GetDremersData *data = [result valueForKeyPath:@"data"];
    NSArray *dremersFromResponse = [data valueForKeyPath:@"member"];
    for (NSDictionary *attributes in dremersFromResponse) {
        DremerInfo *dremer = [[DremerInfo alloc] initWithAttributes:attributes];
        [_mArrayDremer addObject:dremer];
    }
    
    if ([dremersFromResponse count] > 0)
        mLastPage++;
    
    [_mTableDremerView reloadData];
}

- (void) setDremerFriendship:(int)dremerId Action:(NSString*)action Index:(int)index
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
    param.index = index;
    
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
    
    // Update activity cell data
    int index = [[param valueForKey:PARAM_ITEM_INDEX] intValue];
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:index];
    [dremer setFriendship_status: friendship_status];
    
    [_mTableDremerView reloadData];
}

- (void) setDremerFollowing:(int)dremerId Action:(NSString*)action Index:(int)index
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
    param.index = index;
    
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
    
    // Update activity cell data
    int index = [[param valueForKey:PARAM_ITEM_INDEX] intValue];
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:index];
    [dremer setIs_following:is_follow];
    
    [_mTableDremerView reloadData];
}

- (void) setDremerBlocking:(int)dremerId Action:(NSString*)action BlockType:(int)blockType Index:(int)index
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
    
    SetBlockingParam *param = [[SetBlockingParam alloc] init];
    param.user_id = mUserId;
    param.dremer_id = dremerId;
    param.action = action;
    param.block_type = blockType;
    param.index = index;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_BLOCK Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onSetDremerBlockingResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    SetBlockingData *data = [result valueForKeyPath:@"data"];
    NSString * block = [data valueForKeyPath:@"block_type"];
    int block_type;
    if ([block isEqual:[NSNull null]])
        block_type = 0;
    else
        block_type = [block intValue];
    
    
    // Update activity cell data
    int index = [[param valueForKey:PARAM_ITEM_INDEX] intValue];
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:index];
    [dremer setBlock_type:block_type];
    
    [_mTableDremerView reloadData];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    [mRefreshControl endRefreshing];
    
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case GET_DREMERS:
            [self onGetDremersListResult:(NSArray*)result];
            break;
            
        case SET_FRIENDSHIP:
            [self onSetDremerFriendshipResult:(NSDictionary*)param Result:(NSArray*)result];
            break;
            
        case SET_FOLLOW:
            [self onSetDremerFollowingResult:(NSDictionary*)param Result:(NSArray*)result];
            break;
            
        case SET_BLOCK:
            [self onSetDremerBlockingResult:(NSDictionary*)param Result:(NSArray*)result];
            break;
            
        default:
            break;
    }
}

#pragma mark - DremerViewCellDelegate
- (void) clickFriend:(int)index{
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:index];
    
    if ([dremer.friendship_status isEqualToString:@"awaiting_response"]) {
        AcceptDiagViewController *acceptVC = [[AcceptDiagViewController alloc] initWithNibName:@"AcceptDiagViewController" bundle:nil];
        [acceptVC setDremerInfo:dremer];
        [acceptVC setDiagType:@"friendship"];
        acceptVC.mDremerIndex = index;
        acceptVC.delegate = self;
        [self presentPopupViewController:acceptVC animationType:MJPopupViewAnimationSlideLeftLeft];
        
        return;
    }
    
    NSString *action = [DremerInfo getFriendshipAction:dremer.friendship_status];
    [self setDremerFriendship:dremer.user_id Action:action Index:index];
}

- (void) clickFollowing:(int)index {
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:index];
    NSString *action;
    
    if (dremer.is_following == 0)
        action = @"start";
    else
        action = @"stop";
    
    [self setDremerFollowing:dremer.user_id Action:action Index:index];
}

- (void) clickBlocking:(int)index {
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:index];
    NSString *action;
    
    if (dremer.block_type == 0) {
        BlockDiagViewController *blockVC = [[BlockDiagViewController alloc] initWithNibName:@"BlockDiagViewController" bundle:nil];
        blockVC.mDremerId = dremer.user_id;
        blockVC.mDremerIndex = index;
        blockVC.delegate = self;
        [self presentPopupViewController:blockVC animationType:MJPopupViewAnimationSlideLeftLeft];
        
        return;
    } else
        action = @"unblock";
    
    [self setDremerBlocking:dremer.user_id Action:action BlockType:0 Index:index];
}

- (void) clickUser:(int)index {
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:index];
    
    if (dremer.user_id == mUserId)
        return;
    
    UINavigationController *dremerDetailNC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailNav"];
    DremerDetailViewController *dremerDetailVC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailVC"];
    [dremerDetailVC setAttributes:dremer.user_id];
    [dremerDetailNC pushViewController:dremerDetailVC animated:YES];
    
    [self.mainVC presentViewController:dremerDetailNC animated:TRUE completion:nil];
}

#pragma mark - BlockViewDelegate
- (void) closeBlockView:(BlockDiagViewController*)blockVC {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
}

- (void) afterBlock:(BlockDiagViewController *)blockVC BlockType:(int)blockType Index:(int)dremerIndex {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
    
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:dremerIndex];
    dremer.block_type = blockType;
    [_mTableDremerView reloadData];
}

#pragma mark - AcceptViewDelegate
- (void)afterAcceptReject:(AcceptDiagViewController*)acceptkVC Status:(NSString*)status Index:(int)dremerIndex {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
    
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:dremerIndex];
    dremer.friendship_status = status;
    [_mTableDremerView reloadData];
}


#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[_mArrayDremer count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"DremerViewCell";
    
    DremerViewCell *cell = [_mTableDremerView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (!cell) {
        cell = [[DremerViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:[indexPath row]];
    [cell setIndex:(int)[indexPath row]];
    [cell setDelegate:self];
    cell.dremerInfo = dremer;
    
    return cell;
}

@end
