//
//  NotificationTabBarController.m
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "NotificationTabBarController.h"

@interface NotificationTabBarController ()

@end

@implementation NotificationTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.notificationTabBar setDelegate:self];
    [self initNTVCs];
    
    [self adjustTabItems];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.notificationTabBar setSelectedItem:[self.notificationTabBar.items objectAtIndex:0]];
    
    [self.view insertSubview:mUnreadNtVC.view belowSubview:self.notificationTabBar];
}

- (void) initNTVCs {
    mUnreadNtVC = [self.storyboard instantiateViewControllerWithIdentifier:@"NotificationViewTab"];
    [mUnreadNtVC setAttributes:TYPE_UNREAD];
    
    mReadNtVC = [self.storyboard instantiateViewControllerWithIdentifier:@"NotificationViewTab"];
    [mReadNtVC setAttributes:TYPE_READ];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)adjustTabItems {
    for (UITabBarItem * item in self.notificationTabBar.items)
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
           [self.view insertSubview:mUnreadNtVC.view belowSubview:self.notificationTabBar];
            break;
        case 1:
            [self.view insertSubview:mReadNtVC.view belowSubview:self.notificationTabBar];
            break;
           
        default:
            break;
    }
}

@end
