//
//  DremCell.m
//  dremboard
//
//  Created by YingLi on 4/28/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremCell.h"
#import "Drem.h"

#import "UIImageView+AFNetworking.h"

@implementation DremViewCell

- (void)awakeFromNib {
    // Initialization code
    [self setClickListener];
}

- (void)setSelected:(BOOL)selected  {
    [super setSelected:selected];
    
    // Configure the view for the selected state
}

- (void) setClickListener {
    
    // User Image
    UITapGestureRecognizer *imageTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickDremImg)];
    imageTap.numberOfTapsRequired = 1;
    imageTap.numberOfTouchesRequired = 1;
    [self.imgDrem addGestureRecognizer:imageTap];
    [self.imgDrem setUserInteractionEnabled:YES];
}

- (void)setDremInfo:(DremInfo *)dremInfo {
    _dremInfo = dremInfo;
    
    self.txtCategory.text = _dremInfo.category;
    [self.btnFavorite setTitle:_dremInfo.favorite forState:UIControlStateNormal];
    [self.btnLike setTitle:_dremInfo.like forState:UIControlStateNormal];
    
    [self.imgDrem setImageWithURL:[NSURL URLWithString:_dremInfo.guid] placeholderImage:[UIImage imageNamed:@"image_thumb.png"]];
    
    if (_dremInfo.fMoreLess) {
        [self.btnFavorite setHidden:NO];
        [self.btnFlag setHidden:NO];
        [self.btnMore setHidden:YES];
        [self.btnLess setHidden:NO];
    } else {
        [self.btnFavorite setHidden:YES];
        [self.btnFlag setHidden:YES];
        [self.btnMore setHidden:NO];
        [self.btnLess setHidden:YES];
    }
    
    [self setNeedsLayout];
}

- (void) setIndex:(int)index {
    self.itemIndex = index;
}

- (void) onClickDremImg {
    [self.delegate clickContent:self.itemIndex];
}

- (IBAction)onClickFavorite:(id)sender {
    [self.delegate setFavorite:self.itemIndex];
}

- (IBAction)onClickLike:(id)sender {
    [self.delegate setLike:self.itemIndex];
}

- (IBAction)onClickFlag:(id)sender {
    [self.delegate clickFlag:self.itemIndex];
}

- (IBAction)onClickShare:(id)sender {
    [self.delegate clickShare:self.itemIndex];
}

- (IBAction)onClickMore:(id)sender {
    _dremInfo.fMoreLess = YES;
    
    [self.btnFavorite setHidden:NO];
    [self.btnFlag setHidden:NO];
    [self.btnMore setHidden:YES];
    [self.btnLess setHidden:NO];
}

- (IBAction)onClickLess:(id)sender {
    _dremInfo.fMoreLess = NO;
    
    [self.btnFavorite setHidden:YES];
    [self.btnFlag setHidden:YES];
    [self.btnMore setHidden:NO];
    [self.btnLess setHidden:YES];

}

@end
