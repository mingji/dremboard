//
//  ActivityTabBarController.m
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "ActivityTabBarController.h"
#import "ActivityViewController.h"

@interface ActivityTabBarController ()

@end

@implementation ActivityTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.activityTabBar setDelegate:self];
    [self initActivityVCs];
    
    [self adjustTabItems];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.activityTabBar setSelectedItem:[self.activityTabBar.items objectAtIndex:0]];

    [self.view insertSubview:mPersonalActivityVC.view belowSubview:self.activityTabBar];
}

- (void) setAttributes:(UIViewController *)mainVC {
    
    if (!mainVC)
        self.mainVC = self;
    else
        self.mainVC = mainVC;
}

- (void) initActivityVCs {
    mPersonalActivityVC = [self.storyboard instantiateViewControllerWithIdentifier:@"ActivityViewTab"];
    [mPersonalActivityVC setAttributes:SCOPE_PERSONAL DremerId:self.mDremerId MainVC:self.mainVC];
    
    mMentionActivityVC = [self.storyboard instantiateViewControllerWithIdentifier:@"ActivityViewTab"];
    [mMentionActivityVC setAttributes:SCOPE_MENTIONS DremerId:self.mDremerId MainVC:self.mainVC];
    
    mFollowingActivityVC = [self.storyboard instantiateViewControllerWithIdentifier:@"ActivityViewTab"];
    [mFollowingActivityVC setAttributes:SCOPE_FOLLOWING DremerId:self.mDremerId MainVC:self.mainVC];
    
    mFavoriteActivityVC = [self.storyboard instantiateViewControllerWithIdentifier:@"ActivityViewTab"];
    [mFavoriteActivityVC setAttributes:SCOPE_FAVORITES DremerId:self.mDremerId MainVC:self.mainVC];
    
    mFriendActivityVC = [self.storyboard instantiateViewControllerWithIdentifier:@"ActivityViewTab"];
    [mFriendActivityVC setAttributes:SCOPE_FRIENDS DremerId:self.mDremerId MainVC:self.mainVC];
    
    mGroupActivityVC = [self.storyboard instantiateViewControllerWithIdentifier:@"ActivityViewTab"];
    [mGroupActivityVC setAttributes:SCOPE_GROUPS DremerId:self.mDremerId MainVC:self.mainVC];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)adjustTabItems {
    for (UITabBarItem * item in self.activityTabBar.items)
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
            [self.view insertSubview:mPersonalActivityVC.view belowSubview:self.activityTabBar];
            break;
        case 1:
            [self.view insertSubview:mMentionActivityVC.view belowSubview:self.activityTabBar];
            break;
        case 2:
            [self.view insertSubview:mFollowingActivityVC.view belowSubview:self.activityTabBar];
            break;
        case 3:
            [self.view insertSubview:mFavoriteActivityVC.view belowSubview:self.activityTabBar];
            break;
        case 4:
            [self.view insertSubview:mFriendActivityVC.view belowSubview:self.activityTabBar];
            break;
        case 5:
            [self.view insertSubview:mGroupActivityVC.view belowSubview:self.activityTabBar];
            break;
            
        default:
            break;
    }
}

@end
