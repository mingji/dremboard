//
//  RegistViewController.h
//  Dremboard
//
//  Created by vitaly on 4/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ComboBox.h"

@interface RegistViewController : UIViewController
{
    ComboBox* comboContent;
    ComboBox* comboGender;
    ComboBox* comboLanguage;
    ComboBox* comboCountry;
}

@property (weak, nonatomic) IBOutlet UIScrollView *mViewScroll;

@property (weak, nonatomic) IBOutlet UILabel *mLblContentView;
@property (weak, nonatomic) IBOutlet UILabel *mLblGender;
@property (weak, nonatomic) IBOutlet UILabel *mLblLanguage;
@property (weak, nonatomic) IBOutlet UILabel *mLblCountry;

- (IBAction)onClickComplete:(id)sender;

- (void)setupComboView:(UIView*)relativeView :(ComboBox*)combobox Width:(int)width;

@end
