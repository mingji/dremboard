//
//  BlankViewController.m
//  dremboard
//
//  Created by YingLi on 5/8/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "BlankViewController.h"
#import "UIButton+AFNetworking.h"

@interface BlankViewController ()

@end

@implementation BlankViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self setupNavBarItem];
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

- (void)onBackAction:(id)selector
{
    [self.navigationController popViewControllerAnimated:YES];
}



@end
