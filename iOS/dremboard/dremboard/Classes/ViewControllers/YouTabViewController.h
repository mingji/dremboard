//
//  YouTabViewController.h
//  dremboard
//
//  Created by YingLi on 5/14/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "M13InfiniteTabBarController.h"

@interface YouTabViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>

@property (strong, nonatomic) M13InfiniteTabBarController *mainVC;

@property (weak, nonatomic) IBOutlet UITableView *tableMenu;
@property (readwrite, nonatomic, strong) NSMutableArray *mMenuArray;

@end
