//
//  CommentViewCell.h
//  dremboard
//
//  Created by YingLi on 6/5/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Activity.h"

@interface CommentViewCell : UITableViewCell

@property (nonatomic, strong) CommentInfo *commentInfo;

@property (weak, nonatomic) IBOutlet UIView *mViewContent;
@property (weak, nonatomic) IBOutlet UIImageView *mImgUser;
@property (weak, nonatomic) IBOutlet UILabel *mTxtUser;
@property (weak, nonatomic) IBOutlet UILabel *mTxtComment;
@property (weak, nonatomic) IBOutlet UILabel *mTxtTime;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightName;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightComment;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightTime;

@property int itemIndex;

- (void)setIndex:(int)index;

+ (CGFloat) heightForCellWithComment:(CommentInfo *)commentInfo;

@end
