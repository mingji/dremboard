//
//  MediaTabBarController.h
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MediaDremViewController.h"
#import "MediaDremboardViewController.h"
#import "MediaDremcastViewController.h"

@interface MediaTabBarController : UIViewController <UITabBarDelegate>
{
    MediaDremViewController *mDremVC;
    MediaDremboardViewController *mDremboardVC;
    MediaDremcastViewController *mDremcastVC;
 }

@property(nonatomic, assign) UIViewController *mainVC;

@property (weak, nonatomic) IBOutlet UITabBar *mediaTabBar;

@property (nonatomic) int mDremerId;

- (void) setAttributes:(UIViewController *)mainVC;

@end
