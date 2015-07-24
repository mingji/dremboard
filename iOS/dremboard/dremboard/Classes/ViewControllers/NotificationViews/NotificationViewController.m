//
//  NotificationViewController.m
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "NotificationViewController.h"
#import "Notification.h"

@interface NotificationViewController ()

@end

static int PER_PAGE = 10;
static NSString *CellIdentifier = @"NotificationViewCell";

@implementation NotificationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    [self.mTableNtView setDataSource:self];
    _mTableNtView.delegate = self;
    
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetNTsList) forControlEvents:UIControlEventValueChanged];
    [self.mTableNtView addSubview:mRefreshControl];
    self.mTableNtView.alwaysBounceVertical = YES;
    
    mArrayNt = [[NSMutableArray alloc] init];
    
    [self startGetNTsList];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setAttributes:(NSString *)type{
    mLastPage = 1;
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    mNotificationType = [NSString stringWithString:type];
}

- (void) startGetNTsList
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
    
    GetNTsParam *param = [[GetNTsParam alloc] init];
    param.user_id = mUserId;
    param.type = mNotificationType;
    param.page = mLastPage;
    param.per_page = PER_PAGE;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_NT Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onGetNTsListResult:(NSArray *) result
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
    
    GetNTsData *data = [result valueForKeyPath:@"data"];
    NSArray *ntsFromResponse = [data valueForKeyPath:@"notification"];
    for (NSDictionary *attributes in ntsFromResponse) {
        NotificationInfo *notification = [[NotificationInfo alloc] initWithAttributes:attributes];
        [mArrayNt addObject:notification];
    }
    
    if ([ntsFromResponse count] > 0)
        mLastPage++;
    
    [_mTableNtView reloadData];
}

- (void) setNT:(NotificationInfo * )notification Action:(NSString*)action Index:(int)index
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
   
    SetNTParam *param = [[SetNTParam alloc] init];
    param.user_id = mUserId;
    param.notification_id = notification.nt_id;
    param.action = action;
    param.index = index;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_NT Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
}

- (void) onSetNTResult:(NSDictionary*) param Result:(NSArray *) result
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
    
    // Update activity cell data
    int index = [[param valueForKey:PARAM_ITEM_INDEX] intValue];
    NSString *action = [param valueForKey:PARAM_ACTION];
    NotificationInfo *notification = [mArrayNt objectAtIndex:index];
    
    if ([action isEqualToString:@"delete"])
        [mArrayNt removeObjectAtIndex:index];
    else if([action isEqualToString:@"read"])
        notification.type = @"read";
    else if ([action isEqualToString:@"unread"])
        notification.type = @"unread";
    
    [self.mTableNtView reloadData];
}


- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    [mRefreshControl endRefreshing];
    
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case GET_NT:
            [self onGetNTsListResult:(NSArray*)result];
            break;
            
        case SET_NT:
            [self onSetNTResult:(NSDictionary *)param Result:(NSArray *)result];
            break;
            
        default:
            break;
    }
}

#pragma mark - NotificationViewCellDelegate
- (void) setNotification:(int)index {
    NSString *action;
    NotificationInfo *notification = [mArrayNt objectAtIndex:index];
    if ([notification.type isEqualToString:@"read"])
        action = @"unread";
    else
        action = @"read";
    
    [self setNT:notification Action:action Index:index];
}

- (void) delNotification:(int)index {
    NotificationInfo *notification = [mArrayNt objectAtIndex:index];
    [self setNT:notification Action:@"delete" Index:index];
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[mArrayNt count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [self basicCellAtIndexPath:indexPath];
}

- (NotificationViewCell *)basicCellAtIndexPath:(NSIndexPath *)indexPath {
    NotificationViewCell *cell = [_mTableNtView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    if (!cell) {
        cell = [[NotificationViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    [self configureBasicCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)configureBasicCell:(NotificationViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    NotificationInfo *notification = [mArrayNt objectAtIndex:[indexPath row]];
    
    [cell setDelegate:self];
    [cell setIndex:(int)[indexPath row]];
    cell.notificationInfo = notification;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return [self heightForBasicCellAtIndexPath:indexPath];
}

- (CGFloat)heightForBasicCellAtIndexPath:(NSIndexPath *)indexPath {
    NotificationInfo *notification = [mArrayNt objectAtIndex:[indexPath row]];
    return [NotificationViewCell heightForCellWithNT:notification];
}

@end
