//
//  FriendTabBarController.m
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "FriendTabBarController.h"

@interface FriendTabBarController ()

@end

@implementation FriendTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.friendTabBar setDelegate:self];
    [self initFriendVCs];
    
    [self adjustTabItems];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.friendTabBar setSelectedItem:[self.friendTabBar.items objectAtIndex:0]];
    
    [self.view insertSubview:mFriendVC.view belowSubview:self.friendTabBar];
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

- (void) initFriendVCs {
    int myId = [[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    mFriendVC = [self.storyboard instantiateViewControllerWithIdentifier:@"FriendViewTab"];
    [mFriendVC initAttributes:TYPE_FRIEND DremerId:self.mDremerId MainVC:self.mainVC];
    
    mRequestVC = [self.storyboard instantiateViewControllerWithIdentifier:@"FriendViewTab"];
    [mRequestVC initAttributes:TYPE_FRIEND_REQUEST DremerId:self.mDremerId MainVC:self.mainVC];
    
    if (myId != self.mDremerId) {
        NSMutableArray *tabbarItems = [NSMutableArray arrayWithArray: self.friendTabBar.items];
        [tabbarItems removeObjectAtIndex: 1];
        [self.friendTabBar setItems:tabbarItems];
    }
}

- (void)adjustTabItems {
    for (UITabBarItem * item in self.friendTabBar.items)
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
            [self.view insertSubview:mFriendVC.view belowSubview:self.friendTabBar];
            break;
        case 1:
            [self.view insertSubview:mRequestVC.view belowSubview:self.friendTabBar];
            break;
            
        default:
            break;
    }
}

@end
