//
//  DremersGridVC.h
//  dremboard
//
//  Created by YingLi on 6/2/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DremerGridCell.h"

@protocol DremersDataDelegate;

@interface DremersGridVC : UIViewController <UICollectionViewDataSource, DremerGirdCellDelegate>
{
    UIRefreshControl *mRefreshControl;
    int mUserId;
}

@property (weak, nonatomic) IBOutlet UICollectionView *mGridDremerView;

@property (strong, nonatomic) UIViewController *mParentVC;
@property (readwrite, nonatomic, strong) NSMutableArray *mArrayDremer;

@property (assign) id<DremersDataDelegate> delegate;

- (void) setAttributes:(UIViewController *) parentVC ArrayDremer:(NSMutableArray *)arrayDremer;
- (void) reloadGridView;

@end
