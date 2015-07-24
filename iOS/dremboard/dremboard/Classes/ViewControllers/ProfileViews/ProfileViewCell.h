//
//  ProfileViewCell.h
//  dremboard
//
//  Created by YingLi on 5/28/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Dremer.h"

@class ProfileItem;

@interface ProfileViewCell : UITableViewCell

@property (nonatomic, strong) ProfileItem *profileItem;

@property (weak, nonatomic) IBOutlet UILabel *mTxtField;
@property (weak, nonatomic) IBOutlet UILabel *mTxtValue;


@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightField;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightValue;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightView;

+ (CGFloat) heightForCellWithProfile:(ProfileItem *)profileItem;

@end
