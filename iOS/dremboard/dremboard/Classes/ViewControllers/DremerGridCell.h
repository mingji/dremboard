//
//  DremerGridCell.h
//  dremboard
//
//  Created by YingLi on 6/3/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DremerInfo;

@protocol DremerGirdCellDelegate <NSObject>

- (void) clickUser:(int)index;

@end

@interface DremerGridCell : UICollectionViewCell

@property (nonatomic, strong) DremerInfo *dremerInfo;

@property (weak, nonatomic) IBOutlet UIImageView *mImgUser;
@property (weak, nonatomic) IBOutlet UILabel *mTxtUsername;

@property int itemIndex;

@property (assign) id<DremerGirdCellDelegate> delegate;

- (void)setIndex:(int)index;

@end
