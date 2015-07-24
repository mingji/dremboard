//
//  RegistViewController.m
//  Dremboard
//
//  Created by vitaly on 4/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "RegistViewController.h"

@interface RegistViewController ()

@end

@implementation RegistViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    
    self.automaticallyAdjustsScrollViewInsets = NO;
    
    return self;
}

UIGestureRecognizer *tapper;

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    UIButton *backButton = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 33, 33)];
    [backButton addTarget:self action:@selector(onBackAction:) forControlEvents:UIControlEventTouchUpInside];
    [backButton setBackgroundImage:[UIImage imageNamed:@"back_arrow.png"] forState:UIControlStateNormal];
    UIBarButtonItem *backButtonBar = [[UIBarButtonItem alloc] initWithCustomView:backButton];
    [self.navigationItem setLeftBarButtonItem:backButtonBar];
    
    NSMutableArray* contentsArray = [[NSMutableArray alloc] init];
    [contentsArray addObject:@"---"];
    [contentsArray addObject:@"Public"];
    [contentsArray addObject:@"Faimily and Friends"];
    [contentsArray addObject:@"Only myself"];
    
    NSMutableArray* gendersArray = [[NSMutableArray alloc] init];
    [gendersArray addObject:@"---"];
    [gendersArray addObject:@"Female"];
    [gendersArray addObject:@"Male"];
    
    NSMutableArray* langArray = [[NSMutableArray alloc] init];
    [langArray addObject:@"---"];
    [langArray addObject:@"English"];
    [langArray addObject:@"French"];
    
    NSMutableArray* countryArray = [[NSMutableArray alloc] init];
    [countryArray addObject:@"---"];
    [countryArray addObject:@"United State"];
    [countryArray addObject:@"Canada"];
    
    comboContent = [[ComboBox alloc] init];
    [comboContent setComboData:contentsArray];  //Assign the array to ComboBox
    [self.view addSubview:comboContent.view];
    [self setupComboView:_mLblContentView :comboContent Width:150];
    
    comboGender = [[ComboBox alloc] init];
    [comboGender setComboData:gendersArray];  //Assign the array to ComboBox
    [self.view addSubview:comboGender.view];
    [self setupComboView:_mLblGender :comboGender Width:150];
    
    comboLanguage = [[ComboBox alloc] init];
    [comboLanguage setComboData:langArray];  //Assign the array to ComboBox
    [self.view addSubview:comboLanguage.view];
    [self setupComboView:_mLblLanguage :comboLanguage Width:150];
    
    comboCountry = [[ComboBox alloc] init];
    [comboCountry setComboData:countryArray];  //Assign the array to ComboBox
    [self.view addSubview:comboCountry.view];
    [self setupComboView:_mLblCountry :comboCountry Width:150];
    
    [_mViewScroll setContentSize:CGSizeMake(_mViewScroll.frame.size.width, 2050)];
    
    // Handle Kayboard
    tapper = [[UITapGestureRecognizer alloc]
              initWithTarget:self action:@selector(handleSingleTap:)];
    tapper.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapper];
}

- (void)setupComboView:(UIView*)relativeView :(ComboBox*)combobox Width:(int)width
{
    if (!relativeView)
        return;
    
    int cord_x = relativeView.frame.origin.x;
    int cord_y = relativeView.frame.origin.y + 25;
    
    [_mViewScroll addSubview:combobox.view];
    combobox.view.frame = CGRectMake(cord_x, cord_y, width, 31);
}

- (void)viewWillAppear:(BOOL)animated
{
    [self.navigationController.navigationBar setHidden:NO];
    [self.navigationController.navigationBar setBarTintColor:[UIColor colorWithRed:19.0f/255.0f green:95.0f/255.0f blue:142.0f/255.0f alpha:1.0f]];
    // Custom initialization
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectZero];
    label.backgroundColor = [UIColor clearColor];
    label.font = [UIFont systemFontOfSize:25];
    label.textAlignment = NSTextAlignmentCenter;
    // ^-Use UITextAlignmentCenter for older SDKs.
    label.textColor = [UIColor whiteColor]; // change this color
    self.navigationItem.titleView = label;
    label.text = NSLocalizedString(@"Register", @"");
    [label sizeToFit];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)handleSingleTap:(UITapGestureRecognizer *) sender
{
    [self.view endEditing:YES];
}

- (void)onBackAction:(id)selector
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onClickComplete:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

//- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
//{
//    [self.view endEditing:YES];
//    [_mViewScroll endEditing:YES];
//}


@end
