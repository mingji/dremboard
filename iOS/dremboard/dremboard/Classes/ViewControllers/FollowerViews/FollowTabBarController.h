//
//  FollowTabBarController.h
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FollowViewController.h"

@interface FollowTabBarController : UIViewController <UITabBarDelegate>
{
    FollowViewController *mFollowingVC;
    FollowViewController *mFollowerVC;
}

@property(nonatomic, assign) UIViewController *mainVC;

@property (weak, nonatomic) IBOutlet UITabBar *followTabBar;

@property (nonatomic) int mDremerId;

- (void) setAttributes:(UIViewController *)mainVC;

@end
