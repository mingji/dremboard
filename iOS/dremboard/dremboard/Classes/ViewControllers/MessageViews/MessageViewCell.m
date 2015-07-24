//
//  MessageViewCell.m
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "MessageViewCell.h"
#import "UILabel+DynamicHeight.h"
#import "UIImageView+AFNetworking.h"

@implementation MessageViewCell

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

- (void)setMessageInfo:(MessageInfo *)messageInfo {
    
    _messageInfo = messageInfo;
    
    if ([messageInfo.type isEqualToString:TYPE_INBOX]) {
        self.txtPartnerCaption.text = @"From:";
        self.txtUserNames.text = messageInfo.from.name;
        [self.imgUser setImageWithURL:[NSURL URLWithString:messageInfo.from.avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    } else {
        self.txtPartnerCaption.text = @"To:";
        NSString *myAvatar = [[NSUserDefaults standardUserDefaults] stringForKey:@"logined_user_avatar"];
        [self.imgUser setImageWithURL:[NSURL URLWithString:myAvatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
        NSString *names = @"";
        for (Partner *partner in messageInfo.to) {
            names = [NSString stringWithFormat:@"%@ %@,", names, partner.name];
        }
        self.txtUserNames.text = names;
    }
    
    self.imgUser.layer.cornerRadius = self.imgUser.frame.size.width / 2;
    self.imgUser.layer.masksToBounds = YES;
    
    self.txtTime.text = messageInfo.post_date;
    self.txtMsgTitle.text = messageInfo.title;
    self.txtMsgExcerpt.text = messageInfo.excerpt;
    
    [self setViewsHeight];
}

- (void) setViewsHeight {
    
    // Calc Text Label's height
    CGSize sizeNames = [self.txtUserNames sizeOfMultiLineLabel];
    CGSize sizeMsgExcerpt = [self.txtMsgExcerpt sizeOfMultiLineLabel];
    
    self.heightNames.constant = sizeNames.height;
    self.heightMsgExcerpt.constant = sizeMsgExcerpt.height;
    
    // Cacl Message View
    self.heightMsgView.constant = sizeNames.height + sizeMsgExcerpt.height + 90.f/*margin*/;
}

+ (CGFloat) heightForCellWithMsg:(MessageInfo *)messageInfo {
    CGFloat retHeight = 0;
    NSString *names = @"";
    
    if ([messageInfo.type isEqualToString:TYPE_INBOX]) {
        names = messageInfo.from.name;
    } else {
        for (Partner *partner in messageInfo.to) {
            names = [NSString stringWithFormat:@"%@ %@,", names, partner.name];
        }
    }
    
    // Calc User Layout
    CGFloat heightNames = [self detailTextHeight:names Width:180.f FontSize:15.f];
    CGFloat heightExcerpt = [self detailTextHeight:messageInfo.excerpt Width:295.f FontSize:13.f];
    
    retHeight = heightNames + heightExcerpt + 100.f/*margin*/;
    
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
