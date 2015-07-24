//
//  SettingTabBarController.h
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SettingGeneralViewController.h"
#import "SettingEmailViewController.h"
#import "SettingPrivacyViewController.h"

@interface SettingTabBarController : UIViewController <UITabBarDelegate>
{
    SettingGeneralViewController *mGeneralVC;
    SettingEmailViewController *mEmailVC;
    SettingPrivacyViewController *mPrivacyVC;
}

@property(nonatomic, assign) UINavigationController *navController;

@property (weak, nonatomic) IBOutlet UITabBar *settingTabBar;

- (void) setAttributes:(UINavigationController *) navController;

@end
