//
//  FamilyViewCell.h
//  dremboard
//
//  Created by YingLi on 5/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DremerInfo;

@protocol FamilyViewCellDelegate <NSObject>

- (void) clickFamily:(int)index;
- (void) clickUser:(int)index;

@end

@interface FamilyViewCell : UITableViewCell

@property (nonatomic, strong) DremerInfo *dremerInfo;

@property (weak, nonatomic) IBOutlet UILabel *txtUserName;
@property (weak, nonatomic) IBOutlet UILabel *txtTime;
@property (weak, nonatomic) IBOutlet UIImageView *imgUser;
@property (weak, nonatomic) IBOutlet UIButton *btnFamily;

- (IBAction)onClickFamily:(id)sender;

@property int itemIndex;

@property (assign) id<FamilyViewCellDelegate> delegate;

- (void)setIndex:(int)index;

@end
