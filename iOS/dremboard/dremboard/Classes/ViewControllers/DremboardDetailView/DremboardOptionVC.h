//
//  DremboardOptionVC.h
//  dremboard
//
//  Created by YingLi on 7/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol DremboardOptionDelegate;

@interface DremboardOptionVC : UIViewController


- (IBAction)onClickEdit:(id)sender;
- (IBAction)onClickDelete:(id)sender;
- (IBAction)onClickMerge:(id)sender;
- (IBAction)onClickManage:(id)sender;

@property (assign, nonatomic) id <DremboardOptionDelegate>delegate;

@end

@protocol DremboardOptionDelegate <NSObject>
- (void) onClickEdit;
- (void) onClickManage;
- (void) onClickDelete;
- (void) onClickMerge;
@end