//
//  DremsTabViewController.m
//  dremboard
//
//  Created by YingLi on 4/28/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremsTabViewController.h"
#import "AppDelegate.h"
#import "Drem.h"
#import "Category.h"
#import "Actions.h"
#import "Constants.h"
#import "UIViewController+MJPopupViewController.h"
#import "UIButton+AFNetworking.h"

@interface DremsTabViewController ()

@end

static int PER_PAGE = 15;

@implementation DremsTabViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do view setup here.
    [self.collectionDremView setDataSource:self];
    [self.mSearchBar setDelegate:self];
   
    [self setupRefreshControl];
    [self setupNavBarItem];
    
    self.mArrayDrem = [[NSMutableArray alloc] init];
    
    m_lastMediaId = 0;
    m_userId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    mCategoryId = -1;
    mStrSearch = @"";
    
    [self startGetDremsList];
}

- (void) viewDidAppear:(BOOL)animated {
    [self setupCategory];
}

- (void)setupNavBarItem {
    
    // Logo Button
    UIButton *logoButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 45, 33)];
    [logoButton setBackgroundImage:[UIImage imageNamed:@"ic_logo.png"] forState:UIControlStateNormal];
    UIBarButtonItem *logoButtonBar = [[UIBarButtonItem alloc] initWithCustomView:logoButton];
    [self.navigationItem setLeftBarButtonItem:logoButtonBar];
    
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
    [self.navigationController popViewControllerAnimated:YES];
}

- (void) setupRefreshControl {
    
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetDremsList) forControlEvents:UIControlEventValueChanged];
    [self.collectionDremView addSubview:mRefreshControl];
    self.collectionDremView.alwaysBounceVertical = YES;
}

- (void) resetDremsData {
    m_lastMediaId = 0;
    [self.mArrayDrem removeAllObjects];
    [self.collectionDremView reloadData];
}

- (void) setupCategory {
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    mArrayCategories = [[NSMutableArray alloc] initWithArray:app.categories];

    NSMutableArray* arrayName = [[NSMutableArray alloc] init];
    for (CategoryItem *item in mArrayCategories) {
        NSString *categoryName = [[NSString alloc] initWithFormat:@"%@", item.category_name];
        [arrayName addObject:categoryName];
    }
    
    self.mPickerCategory = [[DownPicker alloc] initWithTextField:self.mTxtCategory withData:arrayName];
    [self.mPickerCategory showArrowImage:YES];
    [self.mPickerCategory setPlaceholder:@"Category"];
    
    [self.mPickerCategory addTarget:self
                            action:@selector(onSelectedCategory:)
                  forControlEvents:UIControlEventValueChanged];
}

- (int) getCategoryId :(NSString *) strCategory {
    int retId = -1;
    
    for (CategoryItem *item in mArrayCategories) {
        if ([item.category_name isEqualToString:strCategory]) {
            retId = item.category_id;
            break;
        }
    }
    
    return retId;
}

- (void) onSelectedCategory:(id) downpicker {
    NSString *strCategory = [self.mPickerCategory text];
    
    if ([strCategory length] == 0)
    {
        mInfoDlg = [[ToastView alloc] init:@"Please select the category." durationTime:2 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    if ( mCategoryId == [self getCategoryId:strCategory])
        return;
    
    mCategoryId = [self getCategoryId:strCategory];
    
    [self resetDremsData];
    [self startGetDremsList];

}

- (void) startGetDremsList
{
    if(mWaitingDlg != nil){
        mWaitingDlg = nil;
    }
    
    mWaitingDlg = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
    [self.navigationController.view addSubview:mWaitingDlg];
    
    mWaitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    mWaitingDlg.delegate = self;
    mWaitingDlg.labelText = @"Waiting ...";
    
    [mWaitingDlg show:YES];
    
    GetDremsParam *param = [[GetDremsParam alloc] init];
    param.user_id = m_userId;
    param.search_str = mStrSearch;
    param.category = mCategoryId;
    param.album_id = -1;
    param.author_id = -1;
    param.last_media_id = m_lastMediaId;
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
        m_lastMediaId = drem.drem_id;
    }
   
    [_collectionDremView reloadData];
}

- (void) setLikeDrem:(DremInfo * ) drem Index:(int)index
{
    if(mWaitingDlg != nil){
        mWaitingDlg = nil;
    }
    
    mWaitingDlg = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
    [self.navigationController.view addSubview:mWaitingDlg];
    
    mWaitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    mWaitingDlg.delegate = self;
    mWaitingDlg.labelText = @"Waiting ...";
    
    [mWaitingDlg show:YES];
    
    NSString *like_str = @"Like";
    if ([drem.like rangeOfString:@"Unlike"].location != NSNotFound)
        like_str = @"Unlike";
    
    SetLikeParam *paramSetLike = [[SetLikeParam alloc] init];
    paramSetLike.user_id = m_userId;
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
    
    [_collectionDremView reloadData];
}

- (void) setFavoriteDrem:(DremInfo * )drem Index:(int)index
{
    if(mWaitingDlg != nil){
        mWaitingDlg = nil;
    }
    
    mWaitingDlg = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
    [self.navigationController.view addSubview:mWaitingDlg];
    
    mWaitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    mWaitingDlg.delegate = self;
    mWaitingDlg.labelText = @"Waiting ...";
    
    [mWaitingDlg show:YES];
    
    NSString *favorite_str = @"Favorite";
    if ([drem.favorite rangeOfString:@"Unfavorite"].location != NSNotFound)
        favorite_str = @"Unfavorite";
    
    SetFavoriteParam *paramSetLike = [[SetFavoriteParam alloc] init];
    [paramSetLike setUser_id: m_userId];
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
    
    [_collectionDremView reloadData];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    [mRefreshControl endRefreshing];
    
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
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

#pragma mark - UISearchBarDelegate

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar {
    [self.view endEditing:YES];
    
    NSString *strSearch = [self.mSearchBar text];
    NSString *strCategory = [self.mPickerCategory text];
    
    if ([strCategory length] == 0)
    {
        mInfoDlg = [[ToastView alloc] init:@"Please select the category." durationTime:2 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    mCategoryId = [self getCategoryId:strCategory];
    mStrSearch = strSearch;
    
    [self resetDremsData];
    [self startGetDremsList];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    [self.view endEditing:YES];
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
    [dremVC setAttributes:drem Index:index MainVC:self.mainVC];
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
    [self.collectionDremView reloadData];
}

#pragma mark - DremSingleViewDelegate
- (void) closeView:(DremSingleViewController *)vc {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
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
