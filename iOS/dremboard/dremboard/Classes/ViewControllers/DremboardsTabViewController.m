//
//  DremboardsTabViewController.m
//  dremboard
//
//  Created by YingLi on 5/8/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremboardsTabViewController.h"
#import "Dremboard.h"
#import "Category.h"
#import "AppDelegate.h"
#import "UIButton+AFNetworking.h"
#import "DremerDetailViewController.h"
#import "DremboardDetailVC.h"

@interface DremboardsTabViewController ()

@end

static int PER_PAGE = 10;

@implementation DremboardsTabViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    [self.collectionDremboardView setDataSource:self];
    [self.mSearchBar setDelegate:self];
    
    self.mArrayDremboard = [[NSMutableArray alloc] init];
    
    mCategoryId = -1;
    mStrSearch = @"";
    mLastMediaId = 0;
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    [self setupCategory];
    [self setupRefreshControl];
    [self setupNavBarItem];
    [self startGetDremboardsList];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (void) setupRefreshControl {
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetDremboardsList) forControlEvents:UIControlEventValueChanged];
    [self.collectionDremboardView addSubview:mRefreshControl];
    self.collectionDremboardView.alwaysBounceVertical = YES;
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
    
    [self resetDremboardsData];
    [self startGetDremboardsList];
    
}

- (void) resetDremboardsData {
    mLastMediaId = 0;
    [self.mArrayDremboard removeAllObjects];
    [self.collectionDremboardView reloadData];
}

- (void)setupNavBarItem {
    
    // Logo Button
    UIButton *logoButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 45, 33)];
    //    [logoButton addTarget:self action:@selector(onBackAction:) forControlEvents:UIControlEventTouchUpInside];
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

- (void) startGetDremboardsList
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
    
    GetDremboardsParam *param = [[GetDremboardsParam alloc] init];
    param.user_id = mUserId;
    param.author_id = -1;
    param.search_str = mStrSearch;
    param.category = mCategoryId;
    param.last_media_id = mLastMediaId;
    param.per_page = PER_PAGE;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_DREMBOARDS Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onGetDremboardsListResult:(NSArray *) result
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
    
    GetDremboardsData *data = [result valueForKeyPath:@"data"];
    NSArray *dremboardsFromResponse = [data valueForKeyPath:@"media"];
    for (NSDictionary *attributes in dremboardsFromResponse) {
        DremboardInfo *dremboard = [[DremboardInfo alloc] initWithAttributes:attributes];
        [self.mArrayDremboard addObject:dremboard];
        mLastMediaId = dremboard.dremboard_id;
    }
    
    [self.collectionDremboardView reloadData];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    [mRefreshControl endRefreshing];
    
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    if (type == GET_DREMBOARDS)
        [self onGetDremboardsListResult:(NSArray*)result];
}

#pragma mark - DremboardCellDelegate
- (void) clickUser:(int)index {
    DremboardInfo *dremboard = [self.mArrayDremboard objectAtIndex:index];
    
    if (dremboard.media_author_id == mUserId)
        return;
    
    UINavigationController *dremerDetailNC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailNav"];
    DremerDetailViewController *dremerDetailVC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailVC"];
    [dremerDetailVC setAttributes:dremboard.media_author_id];
    [dremerDetailNC pushViewController:dremerDetailVC animated:YES];
    
    [self.mainVC presentViewController:dremerDetailNC animated:TRUE completion:nil];
}

- (void) clickContent:(int)index {
    DremboardInfo *dremboard = [self.mArrayDremboard objectAtIndex:index];
    
    UINavigationController *dremboardDetailNC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremboardDetailNav"];
    DremboardDetailVC *dremboardDetailVC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremboardDetailVC"];
    dremboardDetailVC.delegate = self;
    [dremboardDetailVC setAttributes:dremboard Index:index];
    [dremboardDetailNC pushViewController:dremboardDetailVC animated:YES];
    
    [self.mainVC presentViewController:dremboardDetailNC animated:TRUE completion:nil];
}

#pragma mark - DremboardDetailDelegate
- (void) deletedDremboard:(int)index {
    [self.mArrayDremboard removeObjectAtIndex:index];
    [self.collectionDremboardView reloadData];
}

- (void) updateDremboard:(int)index Title:(NSString*)title Count:(int)count{
    DremboardInfo *dremboard = [self.mArrayDremboard objectAtIndex:index];
    
    dremboard.media_title = title;
    dremboard.album_count = count;
    
    [self.collectionDremboardView reloadData];
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
    
    [self resetDremboardsData];
    [self startGetDremboardsList];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    [self.view endEditing:YES];
}

#pragma mark - UICollectionViewDataSource
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return [self.mArrayDremboard count];
}

// The cell that is returned must be retrieved from a call to -dequeueReusableCellWithReuseIdentifier:forIndexPath:
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    DremboardViewCell* cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"DremboardViewCell" forIndexPath:indexPath];
    
    DremboardInfo *dremboard = [self.mArrayDremboard objectAtIndex:[indexPath row]];
    [cell setIndex:(int)[indexPath row]];
    [cell setDelegate:self];
    cell.dremboardInfo = dremboard;
    return cell;
}

@end
