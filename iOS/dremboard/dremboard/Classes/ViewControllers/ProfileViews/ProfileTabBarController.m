//
//  ProfileTabBarController.m
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "ProfileTabBarController.h"

@interface ProfileTabBarController ()

@end

UIGestureRecognizer *tapper;

@implementation ProfileTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.profileTabBar setDelegate:self];
    
    [self initVCs];
    [self adjustTabItems];
    
    // Handle Kayboard
    tapper = [[UITapGestureRecognizer alloc]
              initWithTarget:self action:@selector(handleSingleTap:)];
    tapper.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapper];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.profileTabBar setSelectedItem:[self.profileTabBar.items objectAtIndex:0]];

    [self.view insertSubview:mProfileEditVC.view belowSubview:self.profileTabBar];
    [self.view insertSubview:mProfileViewVC.view belowSubview:self.profileTabBar];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(UINavigationController *) navController {
    self.navController = navController;
}

- (void) initVCs {
    int myId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    mProfileViewVC = [self.storyboard instantiateViewControllerWithIdentifier:@"ProfileViewTab"];
    [mProfileViewVC initAttributes:self.mDremerId];
    
    mProfileEditVC = [self.storyboard instantiateViewControllerWithIdentifier:@"ProfileEditTab"];
    [mProfileEditVC setAttributes:self.navController];
    
    if (myId != self.mDremerId) {
        NSMutableArray *tabbarItems = [NSMutableArray arrayWithArray: self.profileTabBar.items];
        [tabbarItems removeObjectAtIndex: 2]; // Picture View
        [tabbarItems removeObjectAtIndex: 1]; // Edit View
        [self.profileTabBar setItems:tabbarItems];
    }
}

- (void)adjustTabItems {
    for (UITabBarItem * item in self.profileTabBar.items)
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
    UIViewController *viewController;
    switch (item.tag) {
        case 0:
            [self.view insertSubview:mProfileViewVC.view belowSubview:self.profileTabBar];
            break;
        case 1:
            [self.view insertSubview:mProfileEditVC.view belowSubview:self.profileTabBar];
            break;
            
        case 2:
            viewController = [self.storyboard instantiateViewControllerWithIdentifier:@"ProfilePictureTab"];
            [self.view insertSubview:viewController.view belowSubview:self.profileTabBar];
            break;
            
        default:
            break;
    }
}

@end
