//
//  FlagViewController.h
//  dremboard
//
//  Created by YingLi on 5/19/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"

@class RadioButton;

@protocol FlagViewDelegate;

@interface FlagViewController : UIViewController <HttpTaskDelegate, MBProgressHUDDelegate>
{
    NSArray *m_arrayOptions;
    NSString *m_selectedOption;
    
    HttpTask *m_httpTask;
    
    ToastView *m_infoDlg;
    MBProgressHUD *m_waitingDlg;
    
    int m_userId;
}

@property int activityIndex;
@property int activityId;

@property (nonatomic, strong) IBOutlet RadioButton* optionReport;
@property (weak, nonatomic) IBOutlet UILabel *lblComplaint;

@property (assign, nonatomic) id <FlagViewDelegate>delegate;
- (IBAction)onSelectOption:(id)sender;

- (IBAction)onClickReport:(id)sender;
- (IBAction)onClickCancel:(id)sender;

@end


@protocol FlagViewDelegate<NSObject>
@required
- (void)closeFlagView:(FlagViewController*)flagVC;
- (void)afterFlaged:(FlagViewController*)flagVC Index:(int)activityIndex;
@end