//
//  DremboardOptionVC.m
//  dremboard
//
//  Created by YingLi on 7/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "DremboardOptionVC.h"

@interface DremboardOptionVC ()

@end

@implementation DremboardOptionVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onClickEdit:(id)sender {
    if (self.delegate && [self.delegate respondsToSelector:@selector(onClickEdit)]) {
        [self.delegate onClickEdit];
    }
}

- (IBAction)onClickDelete:(id)sender {
    if (self.delegate && [self.delegate respondsToSelector:@selector(onClickDelete)]) {
        [self.delegate onClickDelete];
    }
}

- (IBAction)onClickMerge:(id)sender {
    if (self.delegate && [self.delegate respondsToSelector:@selector(onClickMerge)]) {
        [self.delegate onClickMerge];
    }
}

- (IBAction)onClickManage:(id)sender {
    if (self.delegate && [self.delegate respondsToSelector:@selector(onClickManage)]) {
        [self.delegate onClickManage];
    }
}
@end
