//
//  DremboardManageCell.h
//  dremboard
//
//  Created by YingLi on 7/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DremInfo;

@protocol DremboardManageCellDelegate <NSObject>

- (void) clickSelection:(int)index Status:(bool)selected;

@end

@interface DremboardManageCell : UICollectionViewCell
{
    int mIndex;
}

@property (nonatomic, strong) DremInfo *dremInfo;

@property (weak, nonatomic) IBOutlet UILabel *mTxtCategory;
@property (weak, nonatomic) IBOutlet UIButton *mBtnSelect;
@property (weak, nonatomic) IBOutlet UIImageView *mImgContent;

- (IBAction)onClickSelection:(id)sender;

- (void) setIndex:(int)index;

@property (assign) id<DremboardManageCellDelegate> delegate;


@end

