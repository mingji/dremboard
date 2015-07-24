//
//  FamilyTabBarController.h
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FamilyViewController.h"

@interface FamilyTabBarController : UIViewController <UITabBarDelegate>
{
    FamilyViewController *mFamilyVC;
    FamilyViewController *mRequestVC;
}

@property(nonatomic, assign) UIViewController *mainVC;

@property (weak, nonatomic) IBOutlet UITabBar *familyTabBar;

@property (nonatomic) int mDremerId;

- (void) setAttributes:(UIViewController *)mainVC;

@end
