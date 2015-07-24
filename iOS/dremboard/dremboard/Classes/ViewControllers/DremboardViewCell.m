//
//  DremboardViewCell.m
//  dremboard
//
//  Created by YingLi on 5/8/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremboardViewCell.h"
#import "UIImageView+AFNetworking.h"

@implementation DremboardViewCell

- (void)awakeFromNib {
    // Initialization code
    [self setClickListener];
}

- (void)setSelected:(BOOL)selected {
    [super setSelected:selected ];
    
    // Configure the view for the selected state
}

- (void)setDremboardInfo:(DremboardInfo *)dremboardInfo {
    _dremboardInfo = dremboardInfo;
    
    self.txtUsername.text = _dremboardInfo.media_author_name;
    self.txtBoardname.text = _dremboardInfo.media_title;
    
    [self.imgBoard setImageWithURL:[NSURL URLWithString:_dremboardInfo.guid] placeholderImage:[UIImage imageNamed:@"image_thumb.png"]];
    [self.imgUser setImageWithURL:[NSURL URLWithString:_dremboardInfo.media_author_avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    self.imgUser.layer.cornerRadius = self.imgUser.frame.size.width / 2;
    self.imgUser.layer.masksToBounds = YES;
    
    self.txtBoardCnt.text = [NSString stringWithFormat: @"%d Drems", _dremboardInfo.album_count];
    
    [self setNeedsLayout];
}

- (void) setClickListener {
    
    // User Image
    UITapGestureRecognizer *imageTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickUserImg)];
    imageTap.numberOfTapsRequired = 1;
    imageTap.numberOfTouchesRequired = 1;
    [self.imgUser addGestureRecognizer:imageTap];
    [self.imgUser setUserInteractionEnabled:YES];
    
    // User Name
    UITapGestureRecognizer *nameTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickUserAction)];
    nameTap.numberOfTapsRequired = 1;
    nameTap.numberOfTouchesRequired = 1;
    [self.txtUsername addGestureRecognizer:nameTap];
    [self.txtUsername setUserInteractionEnabled:YES];
    
    // Content Image
    UITapGestureRecognizer *contentTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickContent)];
    contentTap.numberOfTapsRequired = 1;
    contentTap.numberOfTouchesRequired = 1;
    [self.imgBoard addGestureRecognizer:contentTap];
    [self.imgBoard setUserInteractionEnabled:YES];
}

- (void) setIndex:(int)index {
    self.itemIndex = index;
}

- (void) onClickUserImg {
    [self.delegate clickUser:self.itemIndex];
}

- (void) onClickUserAction {
    [self.delegate clickUser:self.itemIndex];
}

- (void) onClickContent {
    [self.delegate clickContent:self.itemIndex];
}

@end
