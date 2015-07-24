//
//  ProfileTabBarController.h
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ProfileViewController.h"
#import "ProfileEditController.h"

@interface ProfileTabBarController : UIViewController <UITabBarDelegate>
{
    ProfileViewController *mProfileViewVC;
    ProfileEditController *mProfileEditVC;
}

@property(nonatomic, assign) UINavigationController *navController;

- (void) setAttributes:(UINavigationController *) navController;

@property (weak, nonatomic) IBOutlet UITabBar *profileTabBar;

@property (nonatomic) int mDremerId;

@end
