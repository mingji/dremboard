//
//  DremerViewCell.h
//  dremboard
//
//  Created by YingLi on 4/29/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DremerInfo;

@protocol DremerViewCellDelegate <NSObject>

- (void) clickFriend:(int)index;
- (void) clickFollowing:(int)index;
- (void) clickBlocking:(int)index;
- (void) clickUser:(int)index;

@end

@interface DremerViewCell : UITableViewCell

@property (nonatomic, strong) DremerInfo *dremerInfo;

@property (weak, nonatomic) IBOutlet UIImageView *imgUser;
@property (weak, nonatomic) IBOutlet UILabel *txtUsername;
@property (weak, nonatomic) IBOutlet UILabel *txtComment;
@property (weak, nonatomic) IBOutlet UILabel *txtViewLastContent;
@property (weak, nonatomic) IBOutlet UILabel *txtLoginTime;
@property (weak, nonatomic) IBOutlet UIButton *btnFriend;
@property (weak, nonatomic) IBOutlet UIButton *btnBlock;
@property (weak, nonatomic) IBOutlet UIButton *btnFollow;

@property int itemIndex;

@property (assign) id<DremerViewCellDelegate> delegate;

- (void)setIndex:(int)index;
- (IBAction)onClickFriend:(id)sender;
- (IBAction)onClickBlocking:(id)sender;
- (IBAction)onClickFollowing:(id)sender;

@end
