//
//  MessageTabBarController.h
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MessageViewController.h"
#import "MessageComposeVC.h"

@interface MessageTabBarController : UIViewController <UITabBarDelegate>
{
    MessageViewController *mInboxVC;
    MessageViewController *mSentboxVC;
    MessageComposeVC *mComposeVC;
}

@property (weak, nonatomic) IBOutlet UITabBar *messageTabBar;
@end
