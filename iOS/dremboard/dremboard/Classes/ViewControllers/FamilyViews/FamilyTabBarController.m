//
//  FamilyTabBarController.m
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "FamilyTabBarController.h"

@interface FamilyTabBarController ()

@end

@implementation FamilyTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.familyTabBar setDelegate:self];
    [self initFamilyVCs];
    
    [self adjustTabItems];
}

- (void)viewWillAppear:(BOOL)animated {
    [self.familyTabBar setSelectedItem:[self.familyTabBar.items objectAtIndex:0]];
    
    [self.view insertSubview:mFamilyVC.view belowSubview:self.familyTabBar];
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

- (void) initFamilyVCs {
    int myId = [[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    mFamilyVC = [self.storyboard instantiateViewControllerWithIdentifier:@"FamilyViewTab"];
    [mFamilyVC initAttributes:TYPE_FAMILY DremerId:self.mDremerId MainVC:self.mainVC];
    
    mRequestVC = [self.storyboard instantiateViewControllerWithIdentifier:@"FamilyViewTab"];
    [mRequestVC initAttributes:TYPE_FAMILY_REQUEST DremerId:self.mDremerId MainVC:self.mainVC];
    
    if (myId != self.mDremerId) {
        NSMutableArray *tabbarItems = [NSMutableArray arrayWithArray: self.familyTabBar.items];
        [tabbarItems removeObjectAtIndex: 1];
        [self.familyTabBar setItems:tabbarItems];
    }
}


- (void)adjustTabItems {
    for (UITabBarItem * item in self.familyTabBar.items)
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
            [self.view insertSubview:mFamilyVC.view belowSubview:self.familyTabBar];
            break;
        case 1:
            [self.view insertSubview:mRequestVC.view belowSubview:self.familyTabBar];
            break;
            
        default:
            break;
    }
}

@end
