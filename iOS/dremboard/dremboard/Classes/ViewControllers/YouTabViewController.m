//
//  YouTabViewController.m
//  dremboard
//
//  Created by YingLi on 5/14/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "YouTabViewController.h"
#import "SettingTabBarController.h"
#import "ActivityTabBarController.h"
#import "ProfileTabBarController.h"
#import "FriendTabBarController.h"
#import "FamilyTabBarController.h"
#import "FollowTabBarController.h"
#import "MediaTabBarController.h"
#import "UIButton+AFNetworking.h"

@interface YouTabViewController ()

@end

@implementation YouTabViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.tableMenu setDataSource:self];
    [self.tableMenu setDelegate:self];
    self.mMenuArray = [[NSMutableArray alloc] init];
    
    [self setupNavBarItem];
    [self setupMenuItems];
    
    [self.tableMenu reloadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)setupNavBarItem {
    
    // Logo Button
    UIButton *logoButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 45, 33)];
    //    [logoButton addTarget:self action:@selector(onBackAction:) forControlEvents:UIControlEventTouchUpInside];
    [logoButton setBackgroundImage:[UIImage imageNamed:@"ic_logo.png"] forState:UIControlStateNormal];
    UIBarButtonItem *logoButtonBar = [[UIBarButtonItem alloc] initWithCustomView:logoButton];
    [self.navigationItem setLeftBarButtonItem:logoButtonBar];
    
    // User Button
    NSString *userAvatar = [[NSUserDefaults standardUserDefaults] stringForKey:@"logined_user_avatar"];
    UIButton *userButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 40, 40)];
    [userButton setBackgroundImageForState:UIControlStateNormal
                                   withURL:[NSURL URLWithString:userAvatar]
                          placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    userButton.layer.cornerRadius = userButton.frame.size.width / 2;
    userButton.layer.masksToBounds = YES;
    UIBarButtonItem *userButtonBar = [[UIBarButtonItem alloc] initWithCustomView:userButton];
    [self.navigationItem setRightBarButtonItem:userButtonBar];
}

- (void)setupMenuItems {
    [self.mMenuArray addObject:@"Activity"];
    [self.mMenuArray addObject:@"Profile"];
    [self.mMenuArray addObject:@"Notification"];
    [self.mMenuArray addObject:@"Messages"];
    [self.mMenuArray addObject:@"Friends"];
    [self.mMenuArray addObject:@"Family"];
    [self.mMenuArray addObject:@"Groups"];
    [self.mMenuArray addObject:@"Follow"];
    [self.mMenuArray addObject:@"Setting"];
    [self.mMenuArray addObject:@"Media"];
    [self.mMenuArray addObject:@"Log Out"];
}

#pragma mark - UIAlertView Delegate
- (void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    
    // click log out
    if (buttonIndex == [alertView firstOtherButtonIndex]) {
        [self.mainVC dismissViewControllerAnimated:YES completion:nil];
    }
    // click cancel
    else if (buttonIndex == [alertView cancelButtonIndex]) {
        
    }
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[self.mMenuArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"MenuCell";
    
    UITableViewCell *cell = [self.tableMenu dequeueReusableCellWithIdentifier:CellIdentifier];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    NSString *menuTitle = [self.mMenuArray objectAtIndex:[indexPath row]];
    
    cell.textLabel.text = menuTitle;
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    long rowNum = indexPath.row;
    int dremerId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    if (rowNum == 0) {
        ActivityTabBarController *activityController = [self.storyboard instantiateViewControllerWithIdentifier:@"ActivityTabController"];
        [activityController setAttributes:self.mainVC];
        [activityController setMDremerId:dremerId];
        [self.navigationController pushViewController:activityController animated:YES];
    } else if (rowNum == 1) {
        ProfileTabBarController *profileController = [self.storyboard instantiateViewControllerWithIdentifier:@"ProfileTabController"];
        [profileController setMDremerId:dremerId];
        [profileController setAttributes:self.navigationController];
        [self.navigationController pushViewController:profileController animated:YES];
    } else if (rowNum == 2) {
        UITabBarController *notificationController = [self.storyboard instantiateViewControllerWithIdentifier:@"NotificationTabController"];
        [self.navigationController pushViewController:notificationController animated:YES];
    } else if (rowNum == 3) {
        UITabBarController *messageController = [self.storyboard instantiateViewControllerWithIdentifier:@"MessageTabController"];
        [self.navigationController pushViewController:messageController animated:YES];
    } else if (rowNum == 4) {
        FriendTabBarController *friendController = [self.storyboard instantiateViewControllerWithIdentifier:@"FriendTabController"];
        [friendController setAttributes:self.mainVC];
        [friendController setMDremerId:dremerId];
        [self.navigationController pushViewController:friendController animated:YES];
    } else if (rowNum == 5) {
        FamilyTabBarController *familyController = [self.storyboard instantiateViewControllerWithIdentifier:@"FamilyTabController"];
        [familyController setAttributes:self.mainVC];
        [familyController setMDremerId:dremerId];
        [self.navigationController pushViewController:familyController animated:YES];
    } else if (rowNum == 6) {
//        UITabBarController *groupController = [self.storyboard instantiateViewControllerWithIdentifier:@"GroupTabController"];
//        [self.navigationController pushViewController:groupController animated:YES];
    } else if (rowNum == 7) {
        FollowTabBarController *followController = [self.storyboard instantiateViewControllerWithIdentifier:@"FollowTabController"];
        [followController setAttributes:self.mainVC];
        [followController setMDremerId:dremerId];
        [self.navigationController pushViewController:followController animated:YES];
    } else if (rowNum == 8) {
        SettingTabBarController *settingController = [self.storyboard instantiateViewControllerWithIdentifier:@"SettingTabController"];
        [settingController setAttributes:self.navigationController];
        [self.navigationController pushViewController:settingController animated:YES];
    } else if (rowNum == 9) {
        MediaTabBarController *mediaController = [self.storyboard instantiateViewControllerWithIdentifier:@"MediaTabController"];
        [mediaController setAttributes:self.mainVC];
        [mediaController setMDremerId:dremerId];
        [self.navigationController pushViewController:mediaController animated:YES];
    } else if (rowNum == 10) { // Log out
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Log Out"
                                                        message:@"Are you sure you want to log out?"
                                                       delegate:self
                                              cancelButtonTitle:@"Cancel"
                                              otherButtonTitles:@"Log Out", nil];
        [alert show];
    }
}


@end
