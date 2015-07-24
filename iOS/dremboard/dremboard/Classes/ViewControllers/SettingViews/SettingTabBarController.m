//
//  SettingTabBarController.m
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "SettingTabBarController.h"

@interface SettingTabBarController ()

@end

UIGestureRecognizer *tapper;

@implementation SettingTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.settingTabBar setDelegate:self];
    [self initVCs];
    
    [self adjustTabItems];
    
    // Handle Kayboard
    tapper = [[UITapGestureRecognizer alloc]
              initWithTarget:self action:@selector(handleSingleTap:)];
    tapper.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapper];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.settingTabBar setSelectedItem:[self.settingTabBar.items objectAtIndex:0]];
    
    [self.view insertSubview:mPrivacyVC.view belowSubview:self.settingTabBar];
    [self.view insertSubview:mEmailVC.view belowSubview:self.settingTabBar];
    [self.view insertSubview:mGeneralVC.view belowSubview:self.settingTabBar];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(UINavigationController *) navController {
    self.navController = navController;
}

- (void) initVCs {
    mGeneralVC = [self.storyboard instantiateViewControllerWithIdentifier:@"SettingGeneralTab"];
    [mGeneralVC setAttributes:self.navController];
    
    mEmailVC = [self.storyboard instantiateViewControllerWithIdentifier:@"SettingEmailTab"];
    [mEmailVC setAttributes:self.navController];
    
    mPrivacyVC = [self.storyboard instantiateViewControllerWithIdentifier:@"SettingPrivacyTab"];
    [mPrivacyVC setAttributes];
}

- (void)adjustTabItems {
    for (UITabBarItem * item in self.settingTabBar.items)
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

- (void)handleSingleTap:(UITapGestureRecognizer *) sender
{
    [self.view endEditing:YES];
}

#pragma mark - UITabBarDelegate
- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item {
    switch (item.tag) {
        case 0:
            [self.view insertSubview:mGeneralVC.view belowSubview:self.settingTabBar];
            break;
        case 1:
            [self.view insertSubview:mEmailVC.view belowSubview:self.settingTabBar];
            break;
            
        case 2:
            [self.view insertSubview:mPrivacyVC.view belowSubview:self.settingTabBar];
            break;
            
        default:
            break;
    }
}

@end
