//
//  ActivityViewCellTableViewCell.h
//  dremboard
//
//  Created by YingLi on 5/4/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Activity.h"

@protocol ActivityViewCellDelegate <NSObject>

- (void) clickComment:(int)index;
- (void) setLike:(int)index;
- (void) setFavorite:(int)index;
- (void) clickShare:(int)index;
- (void) clickFlag:(int)index;
- (void) finishMediaImage:(int)index;
- (void) clickUser:(int)index;
@end

@interface ActivityViewCell : UITableViewCell

@property (nonatomic, strong) ActivityInfo *activityInfo;

@property (weak, nonatomic) IBOutlet UIView *viewUser;
@property (weak, nonatomic) IBOutlet UIImageView *imgUser;
@property (weak, nonatomic) IBOutlet UILabel *txtUsername;
@property (weak, nonatomic) IBOutlet UILabel *txtTime;

@property (weak, nonatomic) IBOutlet UIView *viewContent;
@property (weak, nonatomic) IBOutlet UILabel *txtDescription;
@property (weak, nonatomic) IBOutlet UIImageView *imgMedia1;
@property (weak, nonatomic) IBOutlet UIImageView *imgMedia2;

@property (weak, nonatomic) IBOutlet UIButton *btnComment;
@property (weak, nonatomic) IBOutlet UIButton *btnFavorite;
@property (weak, nonatomic) IBOutlet UIButton *btnEdit;
@property (weak, nonatomic) IBOutlet UIButton *btnLike;
@property (weak, nonatomic) IBOutlet UIButton *btnShare;
@property (weak, nonatomic) IBOutlet UIButton *btnFlag;
@property (weak, nonatomic) IBOutlet UIButton *btnDelete;



@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightUserLay;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightUserName;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightTime;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightContentLay;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightDes;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightImg1;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightImg2;

@property int itemIndex;

@property (assign) id<ActivityViewCellDelegate> delegate;

- (void)setIndex:(int)index;

- (IBAction)onClikeLike:(id)sender;
- (IBAction)onClickFavorite:(id)sender;
- (IBAction)onClickShare:(id)sender;
- (IBAction)onClickFlag:(id)sender;
- (IBAction)onClickComment:(id)sender;

+ (CGFloat) heightForCellWithActivity:(ActivityInfo *)activityInfo;
+ (UIImage *)getScaledImage:(UIImage *)image scaledToMaxWidth:(CGFloat)width;

@end
