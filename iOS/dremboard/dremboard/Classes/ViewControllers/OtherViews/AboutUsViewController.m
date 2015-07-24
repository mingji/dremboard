//
//  AboutUsViewController.m
//  dremboard
//
//  Created by YingLi on 5/16/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "AboutUsViewController.h"

@interface AboutUsViewController ()

@end

@implementation AboutUsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    NSString *htmlFile = [[NSBundle mainBundle] pathForResource:@"AboutUs" ofType:@"html"];
    NSString* htmlString = [NSString stringWithContentsOfFile:htmlFile encoding:NSUTF8StringEncoding error:nil];
    [self.mWebView loadHTMLString:htmlString baseURL:nil];

}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
