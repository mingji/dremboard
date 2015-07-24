//
//  DremboardViewCell.h
//  dremboard
//
//  Created by YingLi on 5/8/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Dremboard.h"

@protocol DremerboardCellDelegate <NSObject>

- (void) clickUser:(int)index;
- (void) clickContent:(int)index;

@end

@interface DremboardViewCell : UICollectionViewCell

@property (nonatomic, strong) DremboardInfo *dremboardInfo;

@property (weak, nonatomic) IBOutlet UIImageView *imgUser;
@property (weak, nonatomic) IBOutlet UILabel *txtUsername;
@property (weak, nonatomic) IBOutlet UIImageView *imgBoard;
@property (weak, nonatomic) IBOutlet UILabel *txtBoardname;
@property (weak, nonatomic) IBOutlet UILabel *txtBoardCnt;

@property int itemIndex;

@property (assign) id<DremerboardCellDelegate> delegate;

- (void)setIndex:(int)index;

@end
