//
//  DremersGridVC.m
//  dremboard
//
//  Created by YingLi on 6/2/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremersGridVC.h"
#import "DremerGridCell.h"
#import "Dremer.h"
#import "DremersTabViewController.h"

@interface DremersGridVC ()

@end

@implementation DremersGridVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.mGridDremerView setDataSource:self];
    [self setupRefreshControl];
    
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(UIViewController *)parentVC ArrayDremer:(NSMutableArray *)arrayDremer {
    self.mParentVC = parentVC;
    self.mArrayDremer = arrayDremer;
}

- (void) setupRefreshControl {
    
    mRefreshControl = [[UIRefreshControl alloc] init];
    mRefreshControl.tintColor = [UIColor grayColor];
    [mRefreshControl addTarget:self action:@selector(startGetDremersList) forControlEvents:UIControlEventValueChanged];
    [self.mGridDremerView addSubview:mRefreshControl];
    self.mGridDremerView.alwaysBounceVertical = YES;
}

- (void) reloadGridView {
    [self.mGridDremerView reloadData];
}

- (void) startGetDremersList
{
    [self.delegate getDremerList];
    [mRefreshControl endRefreshing];
}


#pragma mark - DremerGridCellDelegate
- (void) clickUser:(int)index {
    DremerInfo *dremer = [self.mArrayDremer objectAtIndex:index];
    
    if (dremer.user_id == mUserId)
        return;
    
    [self.delegate showDremerDetail:dremer.user_id];
}

#pragma mark - UICollectionViewDataSource
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return [self.mArrayDremer count];
}

// The cell that is returned must be retrieved from a call to -dequeueReusableCellWithReuseIdentifier:forIndexPath:
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    DremerGridCell* cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"DremerGridCell" forIndexPath:indexPath];
    
    DremerInfo *dremer = [self.mArrayDremer objectAtIndex:[indexPath row]];
    
    [cell setIndex:(int)[indexPath row]];
    [cell setDelegate:self];
    cell.dremerInfo = dremer;
    return cell;
}


@end
