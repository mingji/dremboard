//
//  MoreTabViewController.m
//  dremboard
//
//  Created by YingLi on 5/14/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "MoreTabViewController.h"
#import "UIButton+AFNetworking.h"

@interface MoreTabViewController ()

@end

@implementation MoreTabViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.tableMoreMenu setDataSource:self];
    [self.tableMoreMenu setDelegate:self];
    self.mMoreMenuArray = [[NSMutableArray alloc] init];
    
    [self setupNavBarItem];
    [self setupMenuItems];
    
    [self.tableMoreMenu reloadData];
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
    [self.mMoreMenuArray addObject:@"About Us"];
    [self.mMoreMenuArray addObject:@"Privacy"];
    [self.mMoreMenuArray addObject:@"Terms"];
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[self.mMoreMenuArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"MoreCell";
    
    UITableViewCell *cell = [self.tableMoreMenu dequeueReusableCellWithIdentifier:CellIdentifier];
    if (!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    NSString *menuTitle = [self.mMoreMenuArray objectAtIndex:[indexPath row]];
    
    cell.textLabel.text = menuTitle;
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    long rowNum = indexPath.row;
    
    if (rowNum == 0) {
        UIViewController *aboutController = [self.storyboard instantiateViewControllerWithIdentifier:@"AboutUsVC"];
        [self.navigationController pushViewController:aboutController animated:YES];
    } else if (rowNum == 1) {
        UIViewController *privacyController = [self.storyboard instantiateViewControllerWithIdentifier:@"PrivacyVC"];
        [self.navigationController pushViewController:privacyController animated:YES];
    } else if (rowNum == 2) {
        UIViewController *termsController = [self.storyboard instantiateViewControllerWithIdentifier:@"TermsVC"];
        [self.navigationController pushViewController:termsController animated:YES];
    }
}


@end
