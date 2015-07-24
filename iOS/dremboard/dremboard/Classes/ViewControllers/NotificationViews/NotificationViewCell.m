//
//  NotificationViewCell.m
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "NotificationViewCell.h"
#import "UILabel+DynamicHeight.h"

@implementation NotificationViewCell

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

- (IBAction)onClickDelete:(id)sender {
    [self.delegate delNotification:self.itemIndex];
}

- (IBAction)onClickRead:(id)sender {
    [self.delegate setNotification:self.itemIndex];
}

- (void) setIndex:(int)index {
    self.itemIndex = index;
}

- (void)setNotificationInfo:(NotificationInfo *)notificationInfo {
    
    _notificationInfo = notificationInfo;
    
    self.txtNTDesc.text = notificationInfo.desc;
    self.txtNTTime.text = notificationInfo.since;
    
    if ([notificationInfo.type isEqualToString:@"read"])
        [self.btnRead setTitle:@"Undread" forState:UIControlStateNormal];
    else
        [self.btnRead setTitle:@"Read" forState:UIControlStateNormal];
    
    [self setViewsHeight];
}

- (void) setViewsHeight {
   
    // Calc Text Label's height
    CGSize sizeDesc = [self.txtNTDesc sizeOfMultiLineLabel];
    CGSize sizeTime = [self.txtNTTime sizeOfMultiLineLabel];

    self.heightDesc.constant = sizeDesc.height;
    self.heightTime.constant = sizeTime.height;
   
    // Cacl Notification View
    
    self.heightNTView.constant = sizeDesc.height + self.heightTime.constant + 65.f/*margin*/;
}

+ (CGFloat) heightForCellWithNT:(NotificationInfo *)notificationInfo {
    CGFloat retHeight = 0;
    
    // Calc User Layout
    CGFloat heightDesc = [self detailTextHeight:notificationInfo.desc Width:280.f FontSize:14.f];
    CGFloat heightTime = [self detailTextHeight:notificationInfo.since Width:275.f FontSize:13.f];
    
    CGFloat heightView = heightDesc + heightTime + 65.f;
    
    retHeight = heightView + 10.f/*margin*/;
    
    return retHeight;
}

+ (CGFloat) detailTextHeight:(NSString *)text Width:(CGFloat)width FontSize:(CGFloat)size{
    CGRect rectToFit = [text boundingRectWithSize:CGSizeMake(width, CGFLOAT_MAX) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:size]} context:nil];
    return rectToFit.size.height;
}

#pragma mark - UIView

- (void)layoutSubviews {
    [super layoutSubviews];
}


@end
