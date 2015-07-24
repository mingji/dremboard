//
//  DremerGridCell.m
//  dremboard
//
//  Created by YingLi on 6/3/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremerGridCell.h"
#import "Dremer.h"
#import "UIImageView+AFNetworking.h"

@implementation DremerGridCell
- (void)awakeFromNib {
    // Initialization code
    [self setClickListener];
}

- (void)setSelected:(BOOL)selected {
    [super setSelected:selected ];
    
    // Configure the view for the selected state
}

- (void)setDremerInfo:(DremerInfo *)dremerInfo {
    _dremerInfo = dremerInfo;
    
    self.mTxtUsername.text = dremerInfo.display_name;
    
    [self.mImgUser setImageWithURL:[NSURL URLWithString:_dremerInfo.user_avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    self.mImgUser.layer.cornerRadius = self.mImgUser.frame.size.width / 2;
    self.mImgUser.layer.masksToBounds = YES;
    
    [self setNeedsLayout];
}

- (void) setClickListener {
    
    // User Image
    UITapGestureRecognizer *imageTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickUserImg)];
    imageTap.numberOfTapsRequired = 1;
    imageTap.numberOfTouchesRequired = 1;
    [self.mImgUser addGestureRecognizer:imageTap];
    [self.mImgUser setUserInteractionEnabled:YES];
    
    // User Name
    UITapGestureRecognizer *nameTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickUserName)];
    nameTap.numberOfTapsRequired = 1;
    nameTap.numberOfTouchesRequired = 1;
    [self.mTxtUsername addGestureRecognizer:nameTap];
    [self.mTxtUsername setUserInteractionEnabled:YES];
}

- (void) setIndex:(int)index {
    self.itemIndex = index;
}

- (void) onClickUserImg {
    [self.delegate clickUser:self.itemIndex];
}

- (void) onClickUserName {
    [self.delegate clickUser:self.itemIndex];
}

@end
