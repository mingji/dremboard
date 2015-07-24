//
//  ShareViewController.h
//  dremboard
//
//  Created by YingLi on 5/19/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ShareViewController : UIViewController
{
}

@property int activityIndex;
@property NSString *imageUrl;

//- (void)setActivityIndex:(int)index;

- (IBAction)onClickDrem:(id)sender;
- (IBAction)onClickFacebook:(id)sender;
- (IBAction)onClickTwitter:(id)sender;
- (IBAction)onClickGoogle:(id)sender;
- (IBAction)onClickEmail:(id)sender;

@end
