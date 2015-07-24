//
//  FriendTabBarController.h
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FriendViewController.h"

@interface FriendTabBarController : UIViewController <UITabBarDelegate>
{
    FriendViewController *mFriendVC;
    FriendViewController *mRequestVC;
}

@property(nonatomic, assign) UIViewController *mainVC;

@property (weak, nonatomic) IBOutlet UITabBar *friendTabBar;

@property (nonatomic) int mDremerId;

- (void) setAttributes:(UIViewController *)mainVC;

@end
