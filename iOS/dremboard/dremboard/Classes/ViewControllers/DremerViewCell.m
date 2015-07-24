//
//  DremerViewCell.m
//  dremboard
//
//  Created by YingLi on 4/29/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremerViewCell.h"
#import "Dremer.h"
#import "UIImageView+AFNetworking.h"

@implementation DremerViewCell

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
    UITapGestureRecognizer *nameTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickUserName)];
    nameTap.numberOfTapsRequired = 1;
    nameTap.numberOfTouchesRequired = 1;
    [self.txtUsername addGestureRecognizer:nameTap];
    [self.txtUsername setUserInteractionEnabled:YES];
}

- (void)setDremerInfo:(DremerInfo *)dremerInfo {
    _dremerInfo = dremerInfo;
    
    self.txtUsername.text = dremerInfo.display_name;
    self.txtLoginTime.text = dremerInfo.last_activity;
    
    self.txtComment.text = dremerInfo.latest_update.content;
    if (dremerInfo.latest_update.content_id == -1)
        self.txtViewLastContent.hidden = YES;
    else
        self.txtViewLastContent.hidden = NO;
    
    [self.btnFriend setTitle:[DremerInfo getFriendshipStr:dremerInfo.friendship_status] forState:UIControlStateNormal];
    self.btnFriend.titleLabel.adjustsFontSizeToFitWidth = YES;
    if (dremerInfo.block_type == 0)
        [self.btnBlock setTitle:@"Block" forState:UIControlStateNormal];
    else
        [self.btnBlock setTitle:@"Unblock" forState:UIControlStateNormal];
    if (dremerInfo.is_following == 0)
        [self.btnFollow setTitle:@"Follow" forState:UIControlStateNormal];
    else
        [self.btnFollow setTitle:@"Stop Following" forState:UIControlStateNormal];
    
    [self.imgUser setImageWithURL:[NSURL URLWithString:_dremerInfo.user_avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    self.imgUser.layer.cornerRadius = self.imgUser.frame.size.width / 2;
    self.imgUser.layer.masksToBounds = YES;
    
    int userId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    if (userId == dremerInfo.user_id) {
        self.btnFriend.hidden = YES;
        self.btnBlock.hidden = YES;
        self.btnFollow.hidden = YES;
    } else {
        self.btnFriend.hidden = NO;
        self.btnBlock.hidden = NO;
        self.btnFollow.hidden = NO;
    }
    
    [self setNeedsLayout];
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

- (IBAction)onClickFriend:(id)sender {
    [self.delegate clickFriend:self.itemIndex];
}

- (IBAction)onClickBlocking:(id)sender {
    [self.delegate clickBlocking:self.itemIndex];
}

- (IBAction)onClickFollowing:(id)sender {
    [self.delegate clickFollowing:self.itemIndex];
}



@end
