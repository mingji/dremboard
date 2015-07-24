//
//  DremboardManageVC.m
//  dremboard
//
//  Created by YingLi on 7/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremboardManageVC.h"
#import "UIViewController+MJPopupViewController.h"

@interface DremboardManageVC ()

@end

@implementation DremboardManageVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initUI];
    [self.mCollectionDremboard setDataSource:self];
    [self.mCollectionDremboard reloadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(DremboardInfo*)dremboardInfo Drems:(NSMutableArray*)arrayDrem {
    mDremboardInfo = dremboardInfo;
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    mArrayDrem = [[NSMutableArray alloc] initWithArray:arrayDrem];
}

- (void) initUI {
    
    // Button - Select All
    [self.mBtnSelectionAll setTag:0];
    UIImage *imgChecked = [UIImage imageNamed:@"ic_mini_chk_empty"];
    [self.mBtnSelectionAll setImage:imgChecked forState:UIControlStateNormal];
}

- (void) setAllSelection:(bool) selection {
    for (DremInfo *dremInfo in mArrayDrem) {
        dremInfo.fSelection = selection;
    }
    
    [self.mCollectionDremboard reloadData];
}

- (bool) checkSelectionCount {
    int count = 0;
    
    for (DremInfo *dremInfo in mArrayDrem) {
        if (dremInfo.fSelection)
            count++;
    }
    
    if (count <= 0) {
        mInfoDlg = [[ToastView alloc] init:@"Please select some media." durationTime:2 parent:(UIView *)self.view];
        [mInfoDlg show];
        return false;
    }
    
    return true;
}

- (NSString *) getStringSelectedIDs {
    NSString *retStr = @"";
    
    for (DremInfo *dremInfo in mArrayDrem) {
        if (dremInfo.fSelection)
            retStr = [NSString stringWithFormat:@"%@ %d,", retStr, dremInfo.drem_id];
    }
    
    return retStr;
}

- (void) finishMangeMedias {
    NSMutableArray *removedDrems = [[NSMutableArray alloc] init];
    for (DremInfo *drem in mArrayDrem) {
        if (drem.fSelection)
            [removedDrems addObject:drem];
    }
    
    if (self.delegate && [self.delegate respondsToSelector:@selector(removedDrems:)]) {
        [self.delegate removedDrems:removedDrems];
    }
    
    [self dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - Click Buttons
- (IBAction)onClickBack:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)onClickDelete:(id)sender {
    
    if ([self checkSelectionCount] == false)
        return;
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Delete Media"
                                                    message:@"Are you sure want to delete the selected medias?"
                                                   delegate:self
                                          cancelButtonTitle:@"Cancel"
                                          otherButtonTitles:@"OK", nil];
    [alert show];
}

- (IBAction)onClickMove:(id)sender {
    if ([self checkSelectionCount] == false)
        return;
    
    DremboardMoveMediaVC *moveVC = [[DremboardMoveMediaVC alloc] initWithNibName:@"DremboardMoveMediaVC" bundle:nil];
    [moveVC setAttribute:mDremboardInfo.dremboard_id];
    [moveVC setDelegate:self];
    [self presentPopupViewController:moveVC animationType:MJPopupViewAnimationSlideBottomBottom TouchDismiss:NO];
}

- (IBAction)onClickSelectAll:(id)sender {
    int tag = [self.mBtnSelectionAll tag];
    
    if (tag == 0) {
        [self.mBtnSelectionAll setTag:1];
        UIImage *imgChecked = [UIImage imageNamed:@"ic_mini_chk_full"];
        [self.mBtnSelectionAll setImage:imgChecked forState:UIControlStateNormal];
        [self setAllSelection:YES];
    } else {
        [self.mBtnSelectionAll setTag:0];
        UIImage *imgChecked = [UIImage imageNamed:@"ic_mini_chk_empty"];
        [self.mBtnSelectionAll setImage:imgChecked forState:UIControlStateNormal];
        [self setAllSelection:NO];
    }
}

#pragma mark - UIAlertView Delegate
- (void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    // click delete a dremboard
    if (buttonIndex == [alertView firstOtherButtonIndex]) {
        NSString *strIds = [self getStringSelectedIDs];
        [self startRemoveDremsFromBoard:strIds];
    }
    // click cancel
    else if (buttonIndex == [alertView cancelButtonIndex]) {
        
    }
}

#pragma mark - DremboardManageCell
- (void) clickSelection:(int)index Status:(bool)selected {
    DremInfo *drem = [mArrayDrem objectAtIndex:index];
    
    if (!drem)
        return;
    
    drem.fSelection = selected;
}

#pragma mark - DremboardMoveMediaDelegate
- (void) closeDremboardMoveView {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
}

- (void) clickMove:(int)dremboardId {
    [self dismissPopupViewControllerWithanimationType:MJPopupViewAnimationSlideBottomBottom];
    
    NSString *strIds = [self getStringSelectedIDs];
    [self startMoveDremsToBoard:strIds DremboardId:dremboardId];
}


#pragma mark - HttpTaskDelegate

- (void) startRemoveDremsFromBoard:(NSString*)strIds
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
    
    RemoveDremsFromDremboardParam *param = [[RemoveDremsFromDremboardParam alloc] init];
    param.drem_ids = strIds;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)REMOVE_DREMS_FROM_BOARD Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onRemoveDremsFromBoardResult:(NSArray *) result
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
    
    [self finishMangeMedias];
}

- (void) startMoveDremsToBoard:(NSString*)strIds DremboardId:(int)dremboardId
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
    
    MoveDremsToDremboardParam *param = [[MoveDremsToDremboardParam alloc] init];
    param.drem_ids = strIds;
    param.dremboard_id = dremboardId;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)MOVE_DREMS_TO_BOARD Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onMoveDremsToBoardResult:(NSArray *) result
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
    
    [self finishMangeMedias];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSDictionary *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case REMOVE_DREMS_FROM_BOARD:
            [self onRemoveDremsFromBoardResult:(NSArray*)result];
            break;
            
        case MOVE_DREMS_TO_BOARD:
            [self onMoveDremsToBoardResult:(NSArray*)result];
            break;
        default:
            break;
    }
}

#pragma mark - UICollectionViewDataSource
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return [mArrayDrem count];
}

// The cell that is returned must be retrieved from a call to -dequeueReusableCellWithReuseIdentifier:forIndexPath:
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    DremboardManageCell* cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"DremboardManageCell" forIndexPath:indexPath];
    
    DremInfo *drem = [mArrayDrem objectAtIndex:[indexPath row]];
    
    [cell setIndex:(int)[indexPath row]];
    [cell setDelegate:self];
    cell.dremInfo = drem;
    return cell;
}

@end
