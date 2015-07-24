//
//  ProfileViewCell.m
//  dremboard
//
//  Created by YingLi on 5/28/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "ProfileViewCell.h"
#import "UILabel+DynamicHeight.h"

@implementation ProfileViewCell

- (id)initWithStyle:(UITableViewCellStyle)style
    reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (!self) {
        return nil;
    }
    
    return self;
}

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setProfileItem:(ProfileItem *)profile {
    _profileItem = profile;
    
    self.mTxtField.text = profile.name;
    self.mTxtValue.text = profile.value;
    
    CGSize sizeField = [self.mTxtField sizeOfMultiLineLabel];
    CGSize sizeValue = [self.mTxtValue sizeOfMultiLineLabel];
    
    self.heightField.constant = sizeField.height;
    self.heightValue.constant = sizeValue.height;
    
    if (sizeField.height > sizeValue.height)
        self.heightView.constant = sizeField.height + 20.f;
    else
        self.heightView.constant = sizeValue.height + 20.f;

    [self setNeedsLayout];
}

+ (CGFloat) heightForCellWithProfile:(ProfileItem *)profileItem {
    CGFloat retHeight = 0;
    
    // Calc User Layout
    CGFloat heightField = [self detailTextHeight:profileItem.name Width:100.f FontSize:15.f];
    CGFloat heightValue = [self detailTextHeight:profileItem.value Width:150.f FontSize:15.f];
    
    if (heightField > heightValue)
        retHeight = heightField;
    else
        retHeight = heightValue;
    
    retHeight += 20.f;
    
    return retHeight;
}


+ (CGFloat) detailTextHeight:(NSString *)text Width:(CGFloat)width FontSize:(CGFloat)size{
    CGRect rectToFit = [text boundingRectWithSize:CGSizeMake(width, CGFLOAT_MAX) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:size]} context:nil];
    return rectToFit.size.height;
}

@end
