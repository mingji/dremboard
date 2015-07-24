//
//  FolllowerTabBarController.m
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "FollowTabBarController.h"

@interface FollowTabBarController ()

@end

@implementation FollowTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.followTabBar setDelegate:self];
    [self initFollowVCs];
    
    [self adjustTabItems];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.followTabBar setSelectedItem:[self.followTabBar.items objectAtIndex:0]];
    
    [self.view insertSubview:mFollowingVC.view belowSubview:self.followTabBar];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(UIViewController *)mainVC {
    
    if (!mainVC)
        self.mainVC = self;
    else
        self.mainVC = mainVC;
}

- (void) initFollowVCs {
    mFollowingVC = [self.storyboard instantiateViewControllerWithIdentifier:@"FollowViewTab"];
    [mFollowingVC initAttributes:TYPE_FOLLOWING DremerId:self.mDremerId MainVC:self.mainVC];
    
    mFollowerVC = [self.storyboard instantiateViewControllerWithIdentifier:@"FollowViewTab"];
    [mFollowerVC initAttributes:TYPE_FOLLOWER DremerId:self.mDremerId MainVC:self.mainVC];
}

- (void)adjustTabItems {
    for (UITabBarItem * item in self.followTabBar.items)
    {
        [item setTitlePositionAdjustment:UIOffsetMake(0, -10)];
        [item setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                      [UIColor lightGrayColor], NSForegroundColorAttributeName,
                                      [UIFont fontWithName:@"Helvetica" size:14.0], NSFontAttributeName, nil]
                            forState:UIControlStateNormal];
        [item setTitleTextAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                      [UIColor colorWithRed:0/255.0 green:138/255.0 blue:196/255.0 alpha:1.0], NSForegroundColorAttributeName,
                                      [UIFont fontWithName:@"Helvetica" size:14.0], NSFontAttributeName, nil]
                            forState:UIControlStateSelected];
        
    }
}

#pragma mark - UITabBarDelegate
- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item {
    switch (item.tag) {
        case 0:
            [self.view insertSubview:mFollowingVC.view belowSubview:self.followTabBar];
            break;
        case 1:
            [self.view insertSubview:mFollowerVC.view belowSubview:self.followTabBar];
            break;
        default:
            break;
    }
}

@end
