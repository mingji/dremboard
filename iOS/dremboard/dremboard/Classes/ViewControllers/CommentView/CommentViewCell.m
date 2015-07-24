//
//  CommentViewCell.m
//  dremboard
//
//  Created by YingLi on 6/5/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "CommentViewCell.h"
#import "UIImageView+AFNetworking.h"
#import "NSString+Emoji.h"

@implementation CommentViewCell

- (id)initWithStyle:(UITableViewCellStyle)style
    reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (!self) {
        return nil;
    }
    
    self.selectionStyle = UITableViewCellSelectionStyleGray;
    
    return self;
}

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void) setIndex:(int)index {
    self.itemIndex = index;
}

- (void)setCommentInfo:(CommentInfo *)commentInfo {
    
    _commentInfo = commentInfo;
    
    self.mTxtUser.text = commentInfo.author_name;
    self.mTxtTime.text = [ActivityInfo convert2RelativeDate:commentInfo.last_modified];
    self.mTxtComment.text = [commentInfo.description stringByReplacingEmojiCheatCodesWithUnicode];
    
    [self.mImgUser setImageWithURL:[NSURL URLWithString:commentInfo.author_avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    self.mImgUser.layer.cornerRadius = self.mImgUser.frame.size.width / 2;
    self.mImgUser.layer.masksToBounds = YES;
    
    [self calcCellViewHeight];
    
    self.mViewContent.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.mViewContent.layer.borderWidth = 1.0f;
    
    [self setNeedsLayout];
}

- (CGFloat) calcCellViewHeight {
    CGFloat retHeight = 0;
  
    // Comment Height
    CGFloat heightComment = [CommentViewCell detailTextHeight:self.commentInfo.description Width:240.f FontSize:14.f];
    if ([self.commentInfo.description length] == 0)
        heightComment = 0;
    else if (heightComment < 25.f)
        heightComment = 25.f;
    self.heightComment.constant = heightComment;
    
    // Cell Height
    self.heightView.constant = heightComment + 50.f/*margin*/;
    
    retHeight = self.heightView.constant;
    
    return retHeight;
}

+ (CGFloat) heightForCellWithComment:(CommentInfo *)commentInfo {
    CGFloat retHeight = 0;
    
    // Comment Height
    CGFloat heightComment = [CommentViewCell detailTextHeight:commentInfo.description Width:240.f FontSize:14.f];
    if ([commentInfo.description length] == 0)
        heightComment = 0;
    else if (heightComment < 25.f)
        heightComment = 25.f;
    
    // Cell Height
    retHeight = heightComment + 58.f/*margin*/;
    
    return retHeight;
}

+ (CGFloat) detailTextHeight:(NSString *)text Width:(CGFloat)width FontSize:(CGFloat)size{
    CGRect rectToFit = [text boundingRectWithSize:CGSizeMake(width, CGFLOAT_MAX) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:size]} context:nil];
    return rectToFit.size.height;
}

@end
