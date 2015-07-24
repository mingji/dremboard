//
//  FlagViewController.m
//  dremboard
//
//  Created by YingLi on 5/19/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "FlagViewController.h"
#import "RadioButton.h"
#import "Actions.h"

@interface FlagViewController ()

@end

@implementation FlagViewController
@synthesize delegate;

- (void) viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    m_arrayOptions = [NSArray arrayWithObjects:
                  @"annoy",
                  @"nudity",
                  @"graphic",
                  @"attack",
                  @"improper",
                  @"spam",
                  nil];
    m_selectedOption = @"annoy";
    m_userId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    [self setComplaintUrl];
}

- (void) didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setComplaintUrl {
    self.lblComplaint.userInteractionEnabled = YES;
    
    UITapGestureRecognizer *gestureRec = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(openUrl:)];
    gestureRec.numberOfTouchesRequired = 1;
    gestureRec.numberOfTapsRequired = 1;
    [self.lblComplaint addGestureRecognizer:gestureRec];
}

- (void) openUrl:(id)sender
{
    UIGestureRecognizer *rec = (UIGestureRecognizer *)sender;
    
    id hitLabel = [self.view hitTest:[rec locationInView:self.view] withEvent:UIEventTypeTouches];
    
    if ([hitLabel isKindOfClass:[UILabel class]]) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://dremboard.com/copyright-complaint/"]];
    }
}

- (IBAction) onSelectOption:(id)sender {
    int tag = (int)[sender tag];
    m_selectedOption = [m_arrayOptions objectAtIndex:tag];
}

- (IBAction) onClickReport:(id)sender {
    [self setFlagDrem:self.activityId];
}

- (IBAction) onClickCancel:(id)sender {
    [self closeView];
}

- (void) closeView {
    if (self.delegate && [self.delegate respondsToSelector:@selector(closeFlagView:)]) {
        [self.delegate closeFlagView:self];
    }
}

- (void) afterFlaged:(int) activityIndex {
    if (self.delegate && [self.delegate respondsToSelector:@selector(afterFlaged:Index:)]) {
        [self.delegate afterFlaged:self Index:activityIndex];
    }
}

- (void) setFlagDrem:(int)activityId
{
    if(m_waitingDlg != nil){
        m_waitingDlg = nil;
    }
    
    m_waitingDlg = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:m_waitingDlg];
    
    m_waitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    m_waitingDlg.delegate = self;
    m_waitingDlg.labelText = @"Waiting ...";
    
    [m_waitingDlg show:YES];
 
    SetFlagParam *param = [[SetFlagParam alloc] init];
    [param setUser_id: m_userId];
    [param setActivity_id: activityId];
    [param setFlag_slug: m_selectedOption];
    [param setIndex: self.activityIndex];
    
    m_httpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_FLAG Parameter:param];
    m_httpTask.delegate = self;
    [m_httpTask runTask];
}

- (void) onSetFlagResult:(NSDictionary*) param Result:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    m_infoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
    [m_infoDlg show];
    
    if (![status isEqualToString:@"ok"])
    {
        [self closeView];
        return;
    }
    
    int activityIndex = [[param valueForKey:PARAM_ITEM_INDEX] intValue];
    [self afterFlaged:activityIndex];
}


#pragma mark - HttpTaskDelegate
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (m_waitingDlg != nil)
    {
        [m_waitingDlg hide:YES];
    }
    
    switch (type) {
        case SET_FLAG:
            [self onSetFlagResult:(NSDictionary*) param Result:(NSArray*)result];
            break;
            
            
        default:
            break;
    }
}

@end
