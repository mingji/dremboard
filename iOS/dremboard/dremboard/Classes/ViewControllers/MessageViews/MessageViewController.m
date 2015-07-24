//
//  MessageViewController.m
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "MessageViewController.h"
#import "Message.h"

@interface MessageViewController ()

@end

static int PER_PAGE = 10;
static NSString *CellIdentifier = @"MessageViewCell";

@implementation MessageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    [self.mTableMessageView setDataSource:self];
    _mTableMessageView.delegate = self;
    
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetMsgsList) forControlEvents:UIControlEventValueChanged];
    [self.mTableMessageView addSubview:mRefreshControl];
    self.mTableMessageView.alwaysBounceVertical = YES;
    
    mArrayMsg = [[NSMutableArray alloc] init];
    
    [self startGetMsgsList];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setAttributes:(NSString *)type{
    mLastPage = 1;
    mUserId = [[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    mMessageType = [NSString stringWithString:type];
}

- (void) startGetMsgsList
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
    
    GetMessagesParam *param = [[GetMessagesParam alloc] init];
    param.user_id = mUserId;
    param.type = mMessageType;
    param.page = mLastPage;
    param.per_page = PER_PAGE;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_MESSAGES Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onGetMsgsListResult:(NSArray *) result
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
    
    GetMessagesData *data = [result valueForKeyPath:@"data"];
    NSArray *msgsFromResponse = [data valueForKeyPath:@"messages"];
    for (NSDictionary *attributes in msgsFromResponse) {
        MessageInfo *message = [[MessageInfo alloc] initWithAttributes:attributes];
        [mArrayMsg addObject:message];
    }
    
    if ([msgsFromResponse count] > 0)
        mLastPage++;
    
    [_mTableMessageView reloadData];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    [mRefreshControl endRefreshing];
    
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case GET_MESSAGES:
            [self onGetMsgsListResult:(NSArray*)result];
            break;
            
        default:
            break;
    }
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[mArrayMsg count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return [self basicCellAtIndexPath:indexPath];
}

- (MessageViewCell *)basicCellAtIndexPath:(NSIndexPath *)indexPath {
    MessageViewCell *cell = [_mTableMessageView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    if (!cell) {
        cell = [[MessageViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    [self configureBasicCell:cell atIndexPath:indexPath];
    return cell;
}

- (void)configureBasicCell:(MessageViewCell *)cell atIndexPath:(NSIndexPath *)indexPath {
    MessageInfo *notification = [mArrayMsg objectAtIndex:[indexPath row]];
    
    cell.messageInfo = notification;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return [self heightForBasicCellAtIndexPath:indexPath];
}

- (CGFloat)heightForBasicCellAtIndexPath:(NSIndexPath *)indexPath {
    MessageInfo *message = [mArrayMsg objectAtIndex:[indexPath row]];
    return [MessageViewCell heightForCellWithMsg:message];
}

@end
