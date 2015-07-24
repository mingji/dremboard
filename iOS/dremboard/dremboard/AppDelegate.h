//
//  AppDelegate.h
//  dremboard
//
//  Created by vitaly on 4/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Dremer.h"

@interface AppDelegate : UIResponder <UIApplicationDelegate>
{
    DremerInfo *currentDremer;
    NSMutableArray * profiles;
}

@property (nonatomic, strong) DremerInfo *currentDremer;
@property (nonatomic, strong) NSMutableArray * profiles;
@property (nonatomic, strong) NSMutableArray * categories;
           
@property (strong, nonatomic) UIWindow *window;

@end
