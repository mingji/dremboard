//
//  MoreTabViewController.h
//  dremboard
//
//  Created by YingLi on 5/14/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MoreTabViewController : UIViewController <UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *tableMoreMenu;
@property (readwrite, nonatomic, strong) NSMutableArray *mMoreMenuArray;

@end
