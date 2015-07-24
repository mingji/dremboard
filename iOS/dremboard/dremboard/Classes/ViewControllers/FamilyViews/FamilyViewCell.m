//
//  FamilyViewCell.m
//  dremboard
//
//  Created by YingLi on 5/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "FamilyViewCell.h"
#import "Dremer.h"
#import "UIImageView+AFNetworking.h"

@implementation FamilyViewCell

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
    [self setClickListener];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
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
    [self.txtUserName addGestureRecognizer:nameTap];
    [self.txtUserName setUserInteractionEnabled:YES];
}

- (void)setDremerInfo:(DremerInfo *)dremerInfo {
    _dremerInfo = dremerInfo;
    
    self.txtUserName.text = dremerInfo.display_name;
    self.txtTime.text = dremerInfo.last_activity;
    
    
    [self.btnFamily setTitle:[DremerInfo getFamilyshipStr:dremerInfo.familyship_status] forState:UIControlStateNormal];
    self.btnFamily.titleLabel.adjustsFontSizeToFitWidth = YES;
    
    [self.imgUser setImageWithURL:[NSURL URLWithString:_dremerInfo.user_avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    self.imgUser.layer.cornerRadius = self.imgUser.frame.size.width / 2;
    self.imgUser.layer.masksToBounds = YES;
    
    int userId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    if (userId == dremerInfo.user_id) {
        self.btnFamily.hidden = YES;
    } else {
        self.btnFamily.hidden = NO;
    }
    
    [self setNeedsLayout];
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

- (IBAction)onClickFamily:(id)sender {
    [self.delegate clickFamily:self.itemIndex];
}

@end
