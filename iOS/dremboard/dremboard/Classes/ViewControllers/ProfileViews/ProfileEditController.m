//
//  ProfileEditController.m
//  dremboard
//
//  Created by YingLi on 7/6/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "ProfileEditController.h"
#import "AppDelegate.h"

@interface ProfileEditController ()

@end

@implementation ProfileEditController

- (void)viewDidLoad {
    [super viewDidLoad];

    // Do any additional setup after loading the view.
    [_mScrollView setContentSize:CGSizeMake(_mScrollView.frame.size.width, 2450)];
    [self setupUI];
    [self getProfiles];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(UINavigationController *) navController {
    self.navController = navController;
    
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
}

- (void) setupUI {
    
    //Scroll View
    [_mScrollView setContentSize:CGSizeMake(_mScrollView.frame.size.width, 2450)];
    
    // Content Privacy
    mArrayPrivacy = [[NSMutableArray alloc] init];
    [mArrayPrivacy addObject:@"Everyone"];
    [mArrayPrivacy addObject:@"Only Me"];
    [mArrayPrivacy addObject:@"All Members"];
    [mArrayPrivacy addObject:@"My Friends"];
    [mArrayPrivacy addObject:@"Nobody"];
    
    // Name
    
    // Gender
    NSMutableArray *arrayGender = [[NSMutableArray alloc] initWithArray:@[@"male", @"female"]];
    
    self.mCmbGenderValue = [[DownPicker alloc] initWithTextField:self.mTxtGender withData:arrayGender];
    [self.mCmbGenderValue showArrowImage:YES];
    [self.mCmbGenderValue setPlaceholder:@"---"];
    self.mTxtGender.text = @"---";

    self.mCmbGender = [[DownPicker alloc] initWithTextField:self.mPrivGender withData:mArrayPrivacy];
    [self.mCmbGender showArrowImage:YES];
    [self.mCmbGender setPlaceholder:@"---"];
    
    // Birthday
    self.mCmbBirthday = [[DownPicker alloc] initWithTextField:self.mPrivBrith withData:mArrayPrivacy];
    [self.mCmbBirthday showArrowImage:YES];
    [self.mCmbBirthday setPlaceholder:@"---"];
    
    // Language
    /* Get Language List */
    NSArray *langIDs = [[NSUserDefaults standardUserDefaults] objectForKey:@"AppleLanguages"];
    NSMutableArray *arrayLang = [[NSMutableArray alloc] init];
    for (int i = 0; i < [langIDs count]; i++) {
        [arrayLang addObject:[[NSLocale currentLocale] displayNameForKey:NSLocaleIdentifier value:[langIDs objectAtIndex:i]]];
    }
    
    self.mCmbLangValue = [[DownPicker alloc] initWithTextField:self.mTxtLang withData:arrayLang];
    [self.mCmbLangValue showArrowImage:YES];
    [self.mCmbLangValue setPlaceholder:@"---"];
    self.mTxtLang.text = @"---";
    
    self.mCmbLang = [[DownPicker alloc] initWithTextField:self.mPrivLang withData:mArrayPrivacy];
    [self.mCmbLang showArrowImage:YES];
    [self.mCmbLang setPlaceholder:@"---"];
    
    // Country
    self.mCmbCountry = [[DownPicker alloc] initWithTextField:self.mPrivCountry withData:mArrayPrivacy];
    [self.mCmbCountry showArrowImage:YES];
    [self.mCmbCountry setPlaceholder:@"---"];
    
    // Address
    self.mCmbAddress = [[DownPicker alloc] initWithTextField:self.mPrivAddress withData:mArrayPrivacy];
    [self.mCmbAddress showArrowImage:YES];
    [self.mCmbAddress setPlaceholder:@"---"];
    
    // Phone Number
    self.mCmbPhoneNum = [[DownPicker alloc] initWithTextField:self.mPrivPhone withData:mArrayPrivacy];
    [self.mCmbPhoneNum showArrowImage:YES];
    [self.mCmbPhoneNum setPlaceholder:@"---"];
    
    // Facebook
    self.mCmbFacebook = [[DownPicker alloc] initWithTextField:self.mPrivFacebook withData:mArrayPrivacy];
    [self.mCmbFacebook showArrowImage:YES];
    [self.mCmbFacebook setPlaceholder:@"---"];
    
    // Google Plus
    self.mCmbGoogle = [[DownPicker alloc] initWithTextField:self.mPrivGoogle withData:mArrayPrivacy];
    [self.mCmbGoogle showArrowImage:YES];
    [self.mCmbGoogle setPlaceholder:@"---"];
    
    // Twitter
    self.mCmbTwitter = [[DownPicker alloc] initWithTextField:self.mPrivTwitter withData:mArrayPrivacy];
    [self.mCmbTwitter showArrowImage:YES];
    [self.mCmbTwitter setPlaceholder:@"---"];
    
    // Bio
    UIColor *borderColor = [UIColor colorWithRed:204.0/255.0 green:204.0/255.0 blue:204.0/255.0 alpha:1.0];
    self.mTxtBio.layer.borderColor = borderColor.CGColor;
    self.mTxtBio.layer.borderWidth = 1.0;
    self.mTxtBio.layer.cornerRadius = 5.0;
    
    self.mCmbBio = [[DownPicker alloc] initWithTextField:self.mPrivBio withData:mArrayPrivacy];
    [self.mCmbBio showArrowImage:YES];
    [self.mCmbBio setPlaceholder:@"---"];
    
    // Content
    self.mCmbContent = [[DownPicker alloc] initWithTextField:self.mPrivContent withData:mArrayPrivacy];
    [self.mCmbContent showArrowImage:YES];
    [self.mCmbContent setPlaceholder:@"---"];
    
}

- (void) updateProfileView:(ProfileItem *)profile {
//    NSString *profileName = profile.name;
    NSString *profileValue = profile.value;
    NSString *profileVisibility = profile.visibility;

    switch (profile.profile_id) {
        case 1: // Name
            self.mTxtName.text = profileValue;
            break;
            
        case 12: // Gender
            self.mTxtGender.text = profileValue;
            self.mPrivGender.text = profileVisibility;
            break;
            
        case 101: // Birthday
            break;
        
        case 15: // Language
            self.mTxtLang.text = profileValue;
            self.mPrivLang.text = profileVisibility;
            break;
            
        case 94: // Country
            self.mTxtCountry.text = profileValue;
            self.mPrivCountry.text = profileVisibility;
            break;
            
        case 100: // Country
            self.mTxtAddress.text = profileValue;
            self.mPrivAddress.text = profileVisibility;
            break;
            
        case 102: // Phone Number
            self.mTxtPhone.text = profileValue;
            self.mPrivPhone.text = profileVisibility;
            break;
            
        case 96: // Facebook
            self.mTxtFacebook.text = profileValue;
            self.mPrivFacebook.text = profileVisibility;
            break;
            
        case 97: // Google +
            self.mTxtGoogle.text = profileValue;
            self.mPrivGoogle.text = profileVisibility;
            break;
            
        case 98: // Twitter
            self.mTxtTwitter.text = profileValue;
            self.mPrivTwitter.text = profileVisibility;
            break;
            
        case 99: // Bio
            self.mTxtBio.text = profileValue;
            self.mPrivBio.text = profileVisibility;;
            break;
            
        case 2: // Who can view my content
            self.mTxtContent.text = profileValue;
            self.mPrivContent.text = profileVisibility;
            break;
        default:
            break;
    }
}

- (void) updateView {
    for (ProfileItem *profile in self.mArrayProfiles) {
        [self updateProfileView:profile];
    }
}

- (void) getProfiles {

    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    self.mArrayProfiles = [[NSMutableArray alloc] initWithArray:app.profiles];
    
    [self updateView];
}

#pragma mark - HttpTaskDelegate
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
            
        default:
            break;
    }
    
}

@end
