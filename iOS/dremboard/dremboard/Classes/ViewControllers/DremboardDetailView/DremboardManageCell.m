//
//  DremboardManageCell.m
//  dremboard
//
//  Created by YingLi on 7/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremboardManageCell.h"
#import "UIImageView+AFNetworking.h"
#import "Drem.h"

@implementation DremboardManageCell

- (IBAction)onClickSelection:(id)sender {
    int tag = [self.mBtnSelect tag];
    bool selected = false;
    
    if (tag == 0) {
        [self.mBtnSelect setTag:1];
        UIImage *imgChecked = [UIImage imageNamed:@"ic_mini_chk_full"];
        [self.mBtnSelect setImage:imgChecked forState:UIControlStateNormal];
        
        selected = true;
    } else {
        [self.mBtnSelect setTag:0];
        UIImage *imgChecked = [UIImage imageNamed:@"ic_mini_chk_empty"];
        [self.mBtnSelect setImage:imgChecked forState:UIControlStateNormal];
        
        selected = false;
    }
    
    if (self.delegate &&
        [self.delegate respondsToSelector:@selector(clickSelection:Status:)])
    {
        [self.delegate clickSelection:mIndex Status:selected];
    }
}

- (void) setIndex:(int)index {
    mIndex = index;
}

- (void)setDremInfo:(DremInfo *)dremInfo {
    _dremInfo = dremInfo;
    
    self.mTxtCategory.text = _dremInfo.category;
    
    [self.mImgContent setImageWithURL:[NSURL URLWithString:_dremInfo.guid] placeholderImage:[UIImage imageNamed:@"image_thumb.png"]];
    
    if (_dremInfo.fSelection) {
        [self.mBtnSelect setTag:1];
        UIImage *imgChecked = [UIImage imageNamed:@"ic_mini_chk_full"];
        [self.mBtnSelect setImage:imgChecked forState:UIControlStateNormal];
    } else {
        [self.mBtnSelect setTag:0];
        UIImage *imgChecked = [UIImage imageNamed:@"ic_mini_chk_empty"];
        [self.mBtnSelect setImage:imgChecked forState:UIControlStateNormal];

    }
}

@end
