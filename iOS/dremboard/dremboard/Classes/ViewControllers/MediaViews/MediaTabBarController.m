//
//  MediaTabBarController.m
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "MediaTabBarController.h"

@interface MediaTabBarController ()

@end

@implementation MediaTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.mediaTabBar setDelegate:self];
    [self initVCs];
    
    [self adjustTabItems];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.mediaTabBar setSelectedItem:[self.mediaTabBar.items objectAtIndex:0]];

    [self.view insertSubview:mDremVC.view belowSubview:self.mediaTabBar];
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

- (void) initVCs {
    mDremVC = [self.storyboard instantiateViewControllerWithIdentifier:@"MediaDremTab"];
    [mDremVC setAttributes:self.mDremerId MainVC:self.mainVC];
    
    mDremboardVC = [self.storyboard instantiateViewControllerWithIdentifier:@"MediaDremboardTab"];
    [mDremboardVC setAttributes:self.mDremerId MainVC:self.mainVC];
    
    mDremcastVC = [self.storyboard instantiateViewControllerWithIdentifier:@"MediaDremcastTab"];
}


- (void)adjustTabItems {
    for (UITabBarItem * item in self.mediaTabBar.items)
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
            [self.view insertSubview:mDremVC.view belowSubview:self.mediaTabBar];
            break;
        case 1:
            [self.view insertSubview:mDremboardVC.view belowSubview:self.mediaTabBar];
            break;
            
        case 2:
            [self.view insertSubview:mDremcastVC.view belowSubview:self.mediaTabBar];
            break;
            
        default:
            break;
    }
}

@end
