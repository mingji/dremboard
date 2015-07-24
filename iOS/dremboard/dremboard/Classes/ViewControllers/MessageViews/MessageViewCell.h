//
//  MessageViewCell.h
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Message.h"

@class MessageInfo;

@interface MessageViewCell : UITableViewCell

@property (nonatomic, strong) MessageInfo *messageInfo;

@property (weak, nonatomic) IBOutlet UIImageView *imgUser;
@property (weak, nonatomic) IBOutlet UILabel *txtPartnerCaption;
@property (weak, nonatomic) IBOutlet UILabel *txtUserNames;
@property (weak, nonatomic) IBOutlet UILabel *txtTime;
@property (weak, nonatomic) IBOutlet UILabel *txtMsgTitle;
@property (weak, nonatomic) IBOutlet UILabel *txtMsgExcerpt;


@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightNames;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightMsgView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightMsgExcerpt;

+ (CGFloat) heightForCellWithMsg:(MessageInfo *)messageInfo;

@end
