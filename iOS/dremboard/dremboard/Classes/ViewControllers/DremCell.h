//
//  DremCell.h
//  dremboard
//
//  Created by YingLi on 4/28/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DremInfo;

@protocol DremViewCellDelegate <NSObject>

- (void) setLike:(int)index;
- (void) setFavorite:(int)index;
- (void) clickShare:(int)index;
- (void) clickFlag:(int)index;
- (void) clickContent:(int)index;

@end

@interface DremViewCell : UICollectionViewCell

@property (nonatomic, strong) DremInfo *dremInfo;

@property (weak, nonatomic) IBOutlet UIImageView *imgDrem;
@property (weak, nonatomic) IBOutlet UILabel *txtCategory;
@property (weak, nonatomic) IBOutlet UIButton *btnFavorite;
@property (weak, nonatomic) IBOutlet UIButton *btnFlag;
@property (weak, nonatomic) IBOutlet UIButton *btnShare;
@property (weak, nonatomic) IBOutlet UIButton *btnLike;
@property (weak, nonatomic) IBOutlet UIButton *btnMore;
@property (weak, nonatomic) IBOutlet UIButton *btnLess;

@property int itemIndex;

@property (assign) id<DremViewCellDelegate> delegate;

- (void)setIndex:(int)index;
- (IBAction)onClickFavorite:(id)sender;
- (IBAction)onClickLike:(id)sender;
- (IBAction)onClickFlag:(id)sender;
- (IBAction)onClickShare:(id)sender;
- (IBAction)onClickMore:(id)sender;
- (IBAction)onClickLess:(id)sender;

@end
