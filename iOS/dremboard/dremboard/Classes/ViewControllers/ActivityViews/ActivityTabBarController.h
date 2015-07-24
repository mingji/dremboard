//
//  ActivityTabBarController.h
//  dremboard
//
//  Created by YingLi on 5/18/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ActivityViewController.h"

@interface ActivityTabBarController : UIViewController <UITabBarDelegate>
{
    ActivityViewController *mPersonalActivityVC;
    ActivityViewController *mMentionActivityVC;
    ActivityViewController *mFollowingActivityVC;
    ActivityViewController *mFavoriteActivityVC;
    ActivityViewController *mFriendActivityVC;
    ActivityViewController *mGroupActivityVC;
    
}

@property(nonatomic, assign) UIViewController *mainVC;

@property (weak, nonatomic) IBOutlet UITabBar *activityTabBar;

@property (nonatomic) int mDremerId;

- (void) setAttributes:(UIViewController *)mainVC;

@end
