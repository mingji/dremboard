//
//  MessageTabBarController.m
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "MessageTabBarController.h"

@interface MessageTabBarController ()

@end

UIGestureRecognizer *tapper;

@implementation MessageTabBarController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.messageTabBar setDelegate:self];
    [self initMsgVCs];
    
    [self adjustTabItems];
    // Handle Kayboard
    tapper = [[UITapGestureRecognizer alloc]
              initWithTarget:self action:@selector(handleSingleTap:)];
    tapper.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapper];

}

- (void)viewWillAppear:(BOOL)animated {
    [self.messageTabBar setSelectedItem:[self.messageTabBar.items objectAtIndex:0]];
    
    [self.view insertSubview:mInboxVC.view belowSubview:self.messageTabBar];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initMsgVCs {
    mInboxVC = [self.storyboard instantiateViewControllerWithIdentifier:@"MessageViewTab"];
    [mInboxVC setAttributes:TYPE_INBOX];
    
    mSentboxVC = [self.storyboard instantiateViewControllerWithIdentifier:@"MessageViewTab"];
    [mSentboxVC setAttributes:TYPE_SENTBOX];
    
    mComposeVC = [self.storyboard instantiateViewControllerWithIdentifier:@"MessageComposeTab"];
}

- (void)adjustTabItems {
    for (UITabBarItem * item in self.messageTabBar.items)
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
            [self.view insertSubview:mInboxVC.view belowSubview:self.messageTabBar];
            break;
        case 1:
            [self.view insertSubview:mSentboxVC.view belowSubview:self.messageTabBar];
            break;
            
        case 2:
            [self.view insertSubview:mComposeVC.view belowSubview:self.messageTabBar];
            break;
            
        default:
            break;
    }
}

@end
