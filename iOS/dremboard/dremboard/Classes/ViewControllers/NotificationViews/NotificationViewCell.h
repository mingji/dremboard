//
//  NotificationViewCell.h
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "notification.h"

@class NotificationInfo;

@protocol NTViewCellDelegate <NSObject>

- (void) setNotification:(int)index;
- (void) delNotification:(int)index;

@end

@interface NotificationViewCell : UITableViewCell

@property (nonatomic, strong) NotificationInfo *notificationInfo;

@property (weak, nonatomic) IBOutlet UILabel *txtNTDesc;
@property (weak, nonatomic) IBOutlet UILabel *txtNTTime;
@property (weak, nonatomic) IBOutlet UIButton *btnDelete;
@property (weak, nonatomic) IBOutlet UIButton *btnRead;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightDesc;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightTime;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightNTView;

@property int itemIndex;
@property (assign) id<NTViewCellDelegate> delegate;

- (void) setIndex:(int)index;

- (IBAction)onClickDelete:(id)sender;
- (IBAction)onClickRead:(id)sender;

+ (CGFloat) heightForCellWithNT:(NotificationInfo *)notifcationInfo;

@end
