//
//  BlockDiagViewController.m
//  dremboard
//
//  Created by YingLi on 5/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "BlockDiagViewController.h"
#import "Dremer.h"

@interface BlockDiagViewController ()

@end

static int BLOCK_NONE = 1;

static int BLOCK_TYPE_1 = 2;
static int BLOCK_TYPE_2 = 3;
static int BLOCK_TYPE_3 = 5;

static int BLOCK_TYPE_ALL = 30;

@implementation BlockDiagViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self initBtns];
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) initBtns {
    [self.btnType1 setTag:BLOCK_NONE];
    [self.btnType2 setTag:BLOCK_NONE];
    [self.btnType3 setTag:BLOCK_NONE];
    
    [self setBtnImage:self.btnType1];
    [self setBtnImage:self.btnType2];
    [self setBtnImage:self.btnType3];
}

- (void) setBtnImage:(UIButton*) button {
    if ([button tag] == BLOCK_NONE) {
        UIImage *imgChecked = [UIImage imageNamed:@"chk_empty"];
        [button setImage:imgChecked forState:UIControlStateNormal];
    } else {
        UIImage *imgChecked = [UIImage imageNamed:@"chk_full"];
        [button setImage:imgChecked forState:UIControlStateNormal];
    }
}

- (IBAction)onClickType1:(id)sender {
    if ([self.btnType1 tag] == BLOCK_NONE) {
        [self.btnType1 setTag:BLOCK_TYPE_1];
    } else {
        [self.btnType1 setTag:BLOCK_NONE];
    }
    
    [self setBtnImage:self.btnType1];
}

- (IBAction)onClickType2:(id)sender {
    if ([self.btnType2 tag] == BLOCK_NONE) {
        [self.btnType2 setTag:BLOCK_TYPE_2];
    } else {
        [self.btnType2 setTag:BLOCK_NONE];
    }
    
    [self setBtnImage:self.btnType2];
}

- (IBAction)onClickType3:(id)sender {
    if ([self.btnType3 tag] == BLOCK_NONE) {
        [self.btnType3 setTag:BLOCK_TYPE_3];
    } else {
        [self.btnType3 setTag:BLOCK_NONE];
    }
    
    [self setBtnImage:self.btnType3];
}

- (IBAction)onClickAll:(id)sender {
    if ([self.btnType3 tag] == BLOCK_NONE) {
        [self.btnType1 setTag:BLOCK_TYPE_1];
        [self.btnType2 setTag:BLOCK_TYPE_2];
        [self.btnType3 setTag:BLOCK_TYPE_3];
        
        [self.btnAll setTag:BLOCK_TYPE_ALL];
    } else {
        [self.btnType1 setTag:BLOCK_NONE];
        [self.btnType2 setTag:BLOCK_NONE];
        [self.btnType3 setTag:BLOCK_NONE];
        
        [self.btnAll setTag:BLOCK_NONE];
    }
    
    [self setBtnImage:self.btnType1];
    [self setBtnImage:self.btnType2];
    [self setBtnImage:self.btnType3];
    
    [self setBtnImage:self.btnAll];
}

- (IBAction)onClickBlock:(id)sender {
    long block_type = [self.btnType1 tag] * [self.btnType2 tag] * [self.btnType3 tag];
    
    if ((int)block_type <= BLOCK_NONE) {
        mInfoDlg = [[ToastView alloc] init:@"Please select a block type." durationTime:3 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    [self setDremerBlocking:self.mDremerId Action:@"block" BlockType:(int)block_type Index:self.mDremerIndex];
}

- (IBAction)onClickCancel:(id)sender {
    [self closeView];
}

- (void) closeView {
    if (self.delegate && [self.delegate respondsToSelector:@selector(closeBlockView:)]) {
        [self.delegate closeBlockView:self];
    }
}

- (void) afterBlock:(int) blocktype {
    if (self.delegate && [self.delegate respondsToSelector:@selector(afterBlock:BlockType:Index:)]) {
        [self.delegate afterBlock:self BlockType:blocktype Index:self.mDremerIndex];
    }
}

- (void) setDremerBlocking:(int)dremerId Action:(NSString*)action BlockType:(int)blockType Index:(int)index
{
    if(mWaitingDlg != nil){
        mWaitingDlg = nil;
    }
    
    mWaitingDlg = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:mWaitingDlg];
    
    mWaitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    mWaitingDlg.delegate = self;
    mWaitingDlg.labelText = @"Waiting ...";
    
    [mWaitingDlg show:YES];
    
    SetBlockingParam *param = [[SetBlockingParam alloc] init];
    param.user_id = mUserId;
    param.dremer_id = dremerId;
    param.action = action;
    param.block_type = blockType;
    param.index = index;
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_BLOCK Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onSetDremerBlockingResult:(NSDictionary*) param Result:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    if (![status isEqualToString:@"ok"])
    {
        mInfoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    SetBlockingData *data = [result valueForKeyPath:@"data"];
    NSString * block = [data valueForKeyPath:@"block_type"];
    int block_type;
    if ([block isEqual:[NSNull null]])
        block_type = 0;
    else
        block_type = [block intValue];
    
    [self afterBlock:block_type];
}


#pragma mark - HttpTaskDelegate
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case SET_BLOCK:
            [self onSetDremerBlockingResult:(NSDictionary*) param Result:(NSArray*)result];
            break;
            
            
        default:
            break;
    }
}

@end
