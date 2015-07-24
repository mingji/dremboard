//
//  ProfileViewController.m
//  dremboard
//
//  Created by YingLi on 5/28/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "ProfileViewController.h"
#import "ProfileViewCell.h"
#import "AppDelegate.h"

@interface ProfileViewController ()

@end

@implementation ProfileViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    [_mTableProfileView setDataSource:self];
    _mTableProfileView.delegate = self;
    
    [self startGetSingleDremer];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initAttributes:(int)dremerId{
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    mDremerId = dremerId;
}

//- (void) getProfiles {
//    
//    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
//    _mArrayProfiles = [[NSMutableArray alloc] initWithArray:app.profiles];
//}

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
//    NSDictionary *dataFromResponse = [data valueForKeyPath:@"member"];
//    DremerInfo *dremerInfo = [[DremerInfo alloc] initWithAttributes:dataFromResponse];
    
    /* get profiles */
    NSArray *profilesFromResponse = [data valueForKeyPath:@"profiles"];
    _mArrayProfiles = [[NSMutableArray alloc] init];
    for (NSDictionary *attributes in profilesFromResponse) {
        ProfileItem *profile = [[ProfileItem alloc] initWithAttributes:attributes];
        [_mArrayProfiles addObject:profile];
    }
    
    [_mTableProfileView reloadData];

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
            
        default:
            break;
    }
    
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[_mArrayProfiles count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"ProfileViewCell";
    
    ProfileViewCell *cell = [_mTableProfileView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (!cell) {
        cell = [[ProfileViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    ProfileItem *profile = [_mArrayProfiles objectAtIndex:[indexPath row]];
    
    cell.profileItem = profile;
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    ProfileItem *profile = [_mArrayProfiles objectAtIndex:[indexPath row]];
    return [ProfileViewCell heightForCellWithProfile:profile];
}

@end
