//
//  FamilyViewController.m
//  dremboard
//
//  Created by YingLi on 5/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "FamilyViewController.h"
#import "UIViewController+MJPopupViewController.h"
#import "DremerDetailViewController.h"

@interface FamilyViewController ()

@end

static int PER_PAGE = 10;

@implementation FamilyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    [_mTableFamilyView setDataSource:self];
    
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetDremersList) forControlEvents:UIControlEventValueChanged];
    [self.mTableFamilyView addSubview:mRefreshControl];
    self.mTableFamilyView.alwaysBounceVertical = YES;
    
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
    
    [_mTableFamilyView reloadData];
}

- (void) setDremerFamilyship:(int)dremerId Action:(NSString*)action Index:(int)index
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
    param.index = index;
    
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
    
    // Update activity cell data
    int index = [[param valueForKey:PARAM_ITEM_INDEX] intValue];
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:index];
    [dremer setFamilyship_status: familyship_status];
    
    [_mTableFamilyView reloadData];
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
            
        case SET_FAMILYSHIP:
            [self onSetDremerFamilyshipResult:(NSDictionary*)param Result:(NSArray*)result];
            break;
            
        default:
            break;
    }
}

#pragma mark - DremerViewCellDelegate
- (void) clickFamily:(int)index{
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:index];
    
    if ([dremer.familyship_status isEqualToString:@"awaiting_response"]) {
        AcceptDiagViewController *acceptVC = [[AcceptDiagViewController alloc] initWithNibName:@"AcceptDiagViewController" bundle:nil];
        [acceptVC setDremerInfo:dremer];
        [acceptVC setDiagType:@"familyship"];
        acceptVC.mDremerIndex = index;
        acceptVC.delegate = self;
        [self presentPopupViewController:acceptVC animationType:MJPopupViewAnimationSlideLeftLeft];
        
        return;
    }
    
    NSString *action = [DremerInfo getFamilyshipAction:dremer.familyship_status];
    [self setDremerFamilyship:dremer.user_id Action:action Index:index];
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

#pragma mark - AcceptViewDelegate
- (void)afterAcceptReject:(AcceptDiagViewController*)acceptkVC Status:(NSString*)status Index:(int)dremerIndex {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideLeftLeft];
    
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:dremerIndex];
    dremer.familyship_status = status;
    [_mTableFamilyView reloadData];
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
    static NSString *CellIdentifier = @"FamilyViewCell";
    
    FamilyViewCell *cell = [_mTableFamilyView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (!cell) {
        cell = [[FamilyViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    DremerInfo *dremer = [_mArrayDremer objectAtIndex:[indexPath row]];
    [cell setIndex:(int)[indexPath row]];
    [cell setDelegate:self];
    cell.dremerInfo = dremer;
    
    return cell;
}

@end
