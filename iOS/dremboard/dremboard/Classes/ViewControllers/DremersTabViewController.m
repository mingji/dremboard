//
//  DremersTabViewController.m
//  dremboard
//
//  Created by YingLi on 4/29/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremersTabViewController.h"
#import "UIViewController+MJPopupViewController.h"
#import "DremerDetailViewController.h"
#import "UIButton+AFNetworking.h"

@interface DremersTabViewController ()

@end

static int PER_PAGE = 10;

@implementation DremersTabViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    
    if (mLastPage == 0) { // first initiallize
        _mArrayDremer = [[NSMutableArray alloc] init];
        mLastPage = 1;
        mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
        
        [self.mTabBar setDelegate:self];
        [self initVCs];
        [self adjustTabItems];
        [self setupNavBarItem];
        [self startGetDremersList];
    }
}

- (void) viewWillAppear:(BOOL)animated {
    [self.mTabBar setSelectedItem:[self.mTabBar.items objectAtIndex:0]];
    
    [self.view insertSubview:mListVC.view belowSubview:self.mTabBar];
    [self.view insertSubview:mGridVC.view belowSubview:self.mTabBar];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initVCs {
    mGridVC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremersCollectionVC"];
    [mGridVC setAttributes:self ArrayDremer:self.mArrayDremer];
    [mGridVC setDelegate:self];
    
    mListVC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremersTableVC"];
    [mListVC setAttributes:self ArrayDremer:self.mArrayDremer];
    [mListVC setDelegate:self];
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

- (void)adjustTabItems {
    for (UITabBarItem * item in self.mTabBar.items)
    {
        [item setTitlePositionAdjustment:UIOffsetMake(0, -10)];
        [item setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                      [UIColor lightGrayColor], NSForegroundColorAttributeName,
                                      [UIFont fontWithName:@"Helvetica" size:15.0], NSFontAttributeName, nil]
                            forState:UIControlStateNormal];
        [item setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                      [UIColor colorWithRed:0/255.0 green:138/255.0 blue:196/255.0 alpha:1.0], NSForegroundColorAttributeName,
                                      [UIFont fontWithName:@"Helvetica" size:15.0], NSFontAttributeName, nil]
                            forState:UIControlStateSelected];
        
    }
}


- (void)onBackAction:(id)selector
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void) updateDremerView {
    [mListVC reloadTableView];
    [mGridVC reloadGridView];
}

- (void) showDremerDetail:(int)dremerId {
    UINavigationController *dremerDetailNC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailNav"];
    DremerDetailViewController *dremerDetailVC = [self.storyboard instantiateViewControllerWithIdentifier:@"DremerDetailVC"];
    [dremerDetailVC setAttributes:dremerId];
    [dremerDetailNC pushViewController:dremerDetailVC animated:YES];
    
    [self.mainVC presentViewController:dremerDetailNC animated:TRUE completion:nil];
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
    param.disp_user_id = -1;
    param.type = @"active";
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
    
    [self updateDremerView];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case GET_DREMERS:
            [self onGetDremersListResult:(NSArray*)result];
            break;

        default:
            break;
    }
}

#pragma mark - DremerDataDelegate
- (void) getDremerList {
    [self startGetDremersList];
}

#pragma mark - UITabBarDelegate
- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item {
    switch (item.tag) {
        case 0:
            [self.view insertSubview:mGridVC.view belowSubview:self.mTabBar];
            break;
        case 1:
            [self.view insertSubview:mListVC.view belowSubview:self.mTabBar];
            break;
        default:
            break;
    }
}



@end
