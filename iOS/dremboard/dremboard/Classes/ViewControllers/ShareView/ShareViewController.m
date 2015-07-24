//
//  ShareViewController.m
//  dremboard
//
//  Created by YingLi on 5/19/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "ShareViewController.h"
#import "Constants.h"
#import <FBSDKShareKit/FBSDKShareKit.h>

@interface ShareViewController ()

@end

@implementation ShareViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onClickDrem:(id)sender {
}

- (IBAction)onClickFacebook:(id)sender {
    [self shareActivityOnFacebook];
//    NSString *url = [NSString stringWithFormat:@"http://www.facebook.com/sharer/sharer.php?u=%@/activity/%d/", HTTP_HOME, _activityIndex];
//    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
}

- (void) shareActivityOnFacebook {
    FBSDKShareLinkContent *content = [[FBSDKShareLinkContent alloc] init];
    
    NSString *strUrl = [NSString stringWithFormat:@"%@/activity/%d/", HTTP_HOME, _activityIndex];
    content.contentURL = [NSURL URLWithString:strUrl];
    content.contentTitle = @"Drēmboard";
    content.contentDescription = @"Drēm It, Believe It, Achieve It";
    
    if (self.imageUrl)
        content.imageURL = [NSURL URLWithString:self.imageUrl];
    
    [FBSDKShareDialog showFromViewController:self
                                 withContent:content
                                    delegate:nil];
}

- (IBAction)onClickTwitter:(id)sender {
    NSString *url = [NSString stringWithFormat:@"http://twitter.com/share?url=%@/activity/%d/", HTTP_HOME, _activityIndex];
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
}

- (IBAction)onClickGoogle:(id)sender {
    NSString *url = [NSString stringWithFormat:@"https://plus.google.com/share?url=%@/activity/%d/", HTTP_HOME, _activityIndex];
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
}

- (IBAction)onClickEmail:(id)sender {
    NSString *subject = @"Share from Dremboard";
    NSString *content = [NSString stringWithFormat:@"%@/activity/%d/", HTTP_HOME, _activityIndex];
    NSString *mailStr = [NSString stringWithFormat:@"mailto:sb@sw.com?subject=%@&body=%@", subject, content];
    
    NSString *url = [mailStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding ];
    [[UIApplication sharedApplication]  openURL: [NSURL URLWithString: url]];
}
@end
