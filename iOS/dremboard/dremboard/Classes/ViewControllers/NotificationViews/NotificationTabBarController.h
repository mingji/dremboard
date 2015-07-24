//
//  NotificationTabBarController.h
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NotificationViewController.h"

@interface NotificationTabBarController : UIViewController <UITabBarDelegate>
{
    NotificationViewController *mUnreadNtVC;
    NotificationViewController *mReadNtVC;
}

@property (weak, nonatomic) IBOutlet UITabBar *notificationTabBar;
@end
