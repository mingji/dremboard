//
//  LoginViewController.m
//  Dremboard
//
//  Created by vitaly on 4/22/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "AppDelegate.h"
#import "LoginViewController.h"
#import "ViewController.h"
#import "RegistViewController.h"
#import "PulsingRequiresAttentionView.h"
#import "SignIn.h"
#import "SnsLogin.h"
#import "Dremer.h"
#import "Category.h"

#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <GooglePlus/GooglePlus.h>
#import <GoogleOpenSource/GoogleOpenSource.h>
#import <TwitterKit/TwitterKit.h>

#import "HomeTabViewController.h"
#import "DremsTabViewController.h"
#import "DremersTabViewController.h"
#import "DremboardsTabViewController.h"
#import "YouTabViewController.h"


@interface LoginViewController ()
@end

@implementation LoginViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self initGoogle];    

    // Do any additional setup after loading the view.
    NSUserDefaults *myPrefs = [NSUserDefaults standardUserDefaults];
    int rememberTag = (int)[myPrefs integerForKey:@"prev_remember"];
    if (rememberTag == 100) {
        [_mBtnRemember setTag: rememberTag];
        
        UIImage *imgChecked = [UIImage imageNamed:@"chk_full"];
        [_mBtnRemember setImage:imgChecked forState:UIControlStateNormal];
        
        NSString *prevName = [myPrefs stringForKey:@"prev_user_name"];
        NSString *prevPwd = [myPrefs stringForKey:@"prev_user_pwd"];
        [self.mTxtUsername setText:prevName];
        [self.mTxtPassword setText:prevPwd];
    } else {
        [_mBtnRemember setTag: 0];
        
        UIImage *imgChecked = [UIImage imageNamed:@"chk_empty"];
        [_mBtnRemember setImage:imgChecked forState:UIControlStateNormal];
    }
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController.navigationBar setHidden:YES];
    
}

- (void) initGoogle {
    
    self.mBtnGoogle.style = kGPPSignInButtonStyleIconOnly;
    
    [GPPSignInButton class];
    
    GPPSignIn *signIn = [GPPSignIn sharedInstance];
    signIn.shouldFetchGooglePlusUser = YES;
    signIn.shouldFetchGoogleUserEmail = YES;
    signIn.shouldFetchGoogleUserID = YES;
    
    signIn.clientID = GOOGLE_CLIENT_ID;
    
    signIn.scopes = @[ kGTLAuthScopePlusUserinfoProfile ];
    
    signIn.delegate = self;
}

- (IBAction)changeRememberConf:(id)sender {
    
    if ([_mBtnRemember tag] == 0) {
        
        [_mBtnRemember setTag: 100];
        
        UIImage *imgChecked = [UIImage imageNamed:@"chk_full"];
        [_mBtnRemember setImage:imgChecked forState:UIControlStateNormal];
    }
    else if ([_mBtnRemember tag] == 100) {
        
        [_mBtnRemember setTag:0];
        
        UIImage *imgChecked = [UIImage imageNamed:@"chk_empty"];
        [_mBtnRemember setImage:imgChecked forState:UIControlStateNormal];
    }
    
}

- (BOOL) checkInputData
{
    NSString* errorStr = nil;
    if (m_userName == nil || [m_userName length] == 0) {
        errorStr = NSLocalizedString(@"Please insert User Name.", @"aa");
    } else if (m_userPwd == nil || [m_userPwd length] == 0) {
        errorStr = NSLocalizedString(@"Please insert User password.", @"aa");
    }
    
    if (errorStr != nil && [errorStr length] > 0) {
        m_infoDlg = nil;
        m_infoDlg = [[ToastView alloc] init:errorStr durationTime:1 parent:(UIView *)self.view];
        [m_infoDlg show];
        
        return FALSE;
    }
    
    return YES;
}

- (void) saveUserInfo:(NSArray *) result
{
    SignInData *data = [result valueForKeyPath:@"data"];
    int userId = (int)[[data valueForKeyPath:@"id"] integerValue];
    NSString *userName = [data valueForKeyPath:@"user_login"];
    NSString *userAvatar = [data valueForKeyPath:@"avatar"];
    
    NSUserDefaults *myPrefs = [NSUserDefaults standardUserDefaults];
    
    [myPrefs setInteger:userId forKey:@"logined_user_id"];
    [myPrefs setObject:userName forKey:@"logined_user_name"];
    [myPrefs setObject:userAvatar forKey:@"logined_user_avatar"];
    [myPrefs setObject:m_userPwd forKey:@"logined_user_pwd"];
    
    if ([_mBtnRemember tag] ==  100) {
        [myPrefs setInteger:100 forKey:@"prev_remember"];
        [myPrefs setObject:m_userName forKey:@"prev_user_name"];
        [myPrefs setObject:m_userPwd forKey:@"prev_user_pwd"];
    } else if ([_mBtnRemember tag] ==  0) {
        [myPrefs setInteger:0 forKey:@"prev_remember"];
        [myPrefs setObject:@"" forKey:@"prev_user_name"];
        [myPrefs setObject:@"" forKey:@"prev_user_pwd"];
    }
    
    [myPrefs synchronize];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.view endEditing:YES];
}

- (void) showWaitingDiag {
    if(m_waitingDlg != nil){
        m_waitingDlg = nil;
    }
    
    m_waitingDlg = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
    [self.navigationController.view addSubview:m_waitingDlg];
    
    m_waitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    m_waitingDlg.delegate = self;
    m_waitingDlg.labelText = @"Waiting ...";
    
    [m_waitingDlg show:YES];
}

- (IBAction)onClickRegister:(id)sender {
    RegistViewController *regController = [self.storyboard instantiateViewControllerWithIdentifier:@"Register"];
    [self.navigationController pushViewController:regController animated:YES];
}

#pragma mark - Google Delegate
- (void)finishedWithAuth: (GTMOAuth2Authentication *)auth
                   error: (NSError *) error {
    
    NSLog(@"Received error %@ and auth object %@",error, auth);
    if (m_waitingDlg != nil)
        [m_waitingDlg hide:YES];

    if (error) {
        m_infoDlg = [[ToastView alloc] init:[error localizedDescription] durationTime:3 parent:(UIView *)self.view];
        [m_infoDlg show];
        
        return;
    } else {
       
        NSString* accessToken = [auth accessToken];
        
        if (!accessToken) {
            m_infoDlg = [[ToastView alloc] init:@"Can't connect to Google." durationTime:3 parent:(UIView *)self.view];
            [m_infoDlg show];
            return;
        }
        
        [self startLoginWithGoogle:accessToken];
    }
}

- (IBAction)onClickGoogleBtn:(id)sender {
    [self showWaitingDiag];
}


- (IBAction)onClickBtnFB:(id)sender {
    FBSDKLoginManager *login = [[FBSDKLoginManager alloc] init];
    [login logInWithReadPermissions:@[@"public_profile", @"email"] handler:^(FBSDKLoginManagerLoginResult *result, NSError *error) {
        if (error) {
            // Process error
        } else if (result.isCancelled) {
            // Handle cancellations
        } else {
            // If you ask for multiple permissions at once, you
            // should check if specific permissions missing
            if ([result.grantedPermissions containsObject:@"email"]) {
                // Do work
            }
            
            FBSDKAccessToken *token = [FBSDKAccessToken currentAccessToken];
            if (!token) {
                return ;
            }
            
            NSString *strToken = [token tokenString];
            
            [self startLoginWithFB:strToken];
        }
    }];
}

- (IBAction)onClickTwitterBtn:(id)sender {
    [self showWaitingDiag];
    
    [[Twitter sharedInstance] logInWithCompletion:^
     (TWTRSession *session, NSError *error) {
         if (m_waitingDlg != nil)
             [m_waitingDlg hide:YES];
         
         if (session) {
             NSLog(@"signed in as name : %@, authToken %@, authTokenSecret %@, userId %@, ",
                   [session userName],
                   [session authToken],
                   [session authTokenSecret],
                   [session userID]
                   );
             
             [self startLoginWithTwitter:[session authToken] TokenSecret:[session authTokenSecret]];

         } else {
             NSLog(@"error: %@", [error localizedDescription]);
             m_infoDlg = [[ToastView alloc] init:[error localizedDescription] durationTime:3 parent:(UIView *)self.view];
             [m_infoDlg show];
             return;
         }
     }];
}

- (IBAction)onClickLogin:(id)sender {
    [_mTxtUsername resignFirstResponder];
    [_mTxtPassword resignFirstResponder];
    
    m_userName = [_mTxtUsername text];
    m_userPwd = [_mTxtPassword text];
    
    if ([self checkInputData] == FALSE)
        return;
    
    [self startLogin];
}

- (void) startLoginWithFB:(NSString *)token
{
    [self showWaitingDiag];
    
    FBLogInParam *param = [[FBLogInParam alloc] init];
    param.fb_token = [[NSString alloc] initWithString:token];

    m_httpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)FACEBOOK_LOGIN Parameter:param];
    m_httpTask.delegate = self;
    [m_httpTask runTask];
    
}

- (void) onLogInWithFBResult:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    
    if (![status isEqualToString:@"ok"])
    {
        NSString *msg = [result valueForKeyPath:@"error"];
        m_infoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [m_infoDlg show];
        return;
    }
    
    int userId = (int)[[result valueForKeyPath:@"wp_user_id"] integerValue];
    NSString *userName = [result valueForKeyPath:@"user_login"];
    
    FBSDKAccessToken *token = [FBSDKAccessToken currentAccessToken];
    NSString *strFBUserId = [token userID];
    NSString *userAvatar = [NSString stringWithFormat: @"http://graph.facebook.com/%@/picture?type=large", strFBUserId];
    
    NSUserDefaults *myPrefs = [NSUserDefaults standardUserDefaults];
    
    [myPrefs setInteger:userId forKey:@"logined_user_id"];
    [myPrefs setObject:userName forKey:@"logined_user_name"];
    [myPrefs setObject:userAvatar forKey:@"logined_user_avatar"];
    
    [self startGetSingleDremer];
}


- (void) startLoginWithGoogle:(NSString *)token
{
    [self showWaitingDiag];
    
    GoogleLogInParam *param = [[GoogleLogInParam alloc] init];
    param.google_token = [[NSString alloc] initWithString:token];
    
    m_httpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GOOGLE_LOGIN Parameter:param];
    m_httpTask.delegate = self;
    [m_httpTask runTask];
    
}

- (void) onLogInWithGoogleResult:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    
    
    if (![status isEqualToString:@"ok"])
    {
        NSString *msg = [result valueForKeyPath:@"error"];
        m_infoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [m_infoDlg show];
        return;
    }
    
    int userId = (int)[[result valueForKeyPath:@"wp_user_id"] integerValue];
    NSString *userName = [result valueForKeyPath:@"user_login"];
    
    NSString *userID = [[GPPSignIn sharedInstance] userID];
    NSString *userAvatar = [NSString stringWithFormat: @"http://plus.google.com/s2/photos/profile/%@?sz=150", userID];

    NSUserDefaults *myPrefs = [NSUserDefaults standardUserDefaults];
    
    [myPrefs setInteger:userId forKey:@"logined_user_id"];
    [myPrefs setObject:userName forKey:@"logined_user_name"];
    [myPrefs setObject:userAvatar forKey:@"logined_user_avatar"];

    // remove the google login data
    [[GPPSignIn sharedInstance] signOut];
    
    [self startGetSingleDremer];
}

- (void) startLoginWithTwitter:(NSString *)access_token TokenSecret:(NSString *)access_token_secret
{
    [self showWaitingDiag];
    
    TwitterLogInParam *param = [[TwitterLogInParam alloc] init];
    param.consumer_key = TWITTER_CONSUMER_KEY;
    param.consumer_secret = TWITTER_CONSUMER_SECRET;
    param.access_token = [[NSString alloc] initWithString:access_token];
    param.access_token_secret = [[NSString alloc] initWithString:access_token_secret];
    
    m_httpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)TWITTER_LOGIN Parameter:param];
    m_httpTask.delegate = self;
    [m_httpTask runTask];
    
}

- (void) onLogInWithTwitterResult:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    
    
    if (![status isEqualToString:@"ok"])
    {
        NSString *msg = [result valueForKeyPath:@"error"];
        m_infoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [m_infoDlg show];
        return;
    }
    
    int userId = (int)[[result valueForKeyPath:@"wp_user_id"] integerValue];
    NSString *userName = [result valueForKeyPath:@"user_login"];
    
//    NSString *userAvatar = [NSString stringWithFormat: @"http://plus.google.com/s2/photos/profile/%@?sz=150", userID];
    
    NSUserDefaults *myPrefs = [NSUserDefaults standardUserDefaults];
    
    [myPrefs setInteger:userId forKey:@"logined_user_id"];
    [myPrefs setObject:userName forKey:@"logined_user_name"];
//    [myPrefs setObject:userAvatar forKey:@"logined_user_avatar"];
    
    // remove twitter login data
    [[Twitter sharedInstance] logOut];
    
    [self startGetSingleDremer];
}

- (void) startLogin
{
    [self showWaitingDiag];
    
    SignInParam *param = [[SignInParam alloc] init];
    param.username = [[NSString alloc] initWithString:m_userName];
    param.password = [[NSString alloc] initWithString:m_userPwd];
    
    m_httpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SIGN_IN Parameter:param];
    m_httpTask.delegate = self;
    [m_httpTask runTask];
}

- (void) onSignInResult:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    if (![status isEqualToString:@"ok"])
    {
        m_infoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [m_infoDlg show];
        return;
    }
  
    [self saveUserInfo:result];
    [self startGetSingleDremer];
}

- (void) startGetSingleDremer
{
    [self showWaitingDiag];

    int userId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    GetSingleDremerParam *param = [[GetSingleDremerParam alloc] init];
    param.user_id = userId;
    param.disp_user_id = userId;
    
    m_httpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_SINGLE_DREMER Parameter:param];
    m_httpTask.delegate = self;
    [m_httpTask runTask];
    
}

- (void) onGetSingleDremerResult:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    if (![status isEqualToString:@"ok"])
    {
        m_infoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [m_infoDlg show];
        return;
    }
    
    /* get member properties */
    GetSingleDremerData *data = [result valueForKeyPath:@"data"];
    NSDictionary *dataFromResponse = [data valueForKeyPath:@"member"];
    DremerInfo *dremer = [[DremerInfo alloc] initWithAttributes:dataFromResponse];
    
    /* get profiles */
    NSArray *profilesFromResponse = [data valueForKeyPath:@"profiles"];
    NSMutableArray *profiles = [[NSMutableArray alloc] init];
    for (NSDictionary *attributes in profilesFromResponse) {
        ProfileItem *profile = [[ProfileItem alloc] initWithAttributes:attributes];
        [profiles addObject:profile];
    }
    
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    app.currentDremer = dremer;
    app.profiles = profiles;
    
    [self startGetCategory];
}

- (void) startGetCategory
{
    [self showWaitingDiag];
    
    int userId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    GetCategoryParam *param = [[GetCategoryParam alloc] init];
    param.user_id = userId;
    
    m_httpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)GET_CATEGORY Parameter:param];
    m_httpTask.delegate = self;
    [m_httpTask runTask];
    
}

- (void) onGetCategoryResult:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    if (![status isEqualToString:@"ok"])
    {
        m_infoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [m_infoDlg show];
        return;
    }
    
    /* get categories */
    GetCategoryData *data = [result valueForKeyPath:@"data"];
    NSDictionary *categoryromResponse = [data valueForKeyPath:@"category"];
    
    NSArray *keysResponse = [categoryromResponse valueForKeyPath:@"keys"];
    NSArray *namesResponse = [categoryromResponse valueForKeyPath:@"values"];
    
    NSMutableArray *categories = [[NSMutableArray alloc] init];
    for (int i = 0; i < [keysResponse count]; i++) {
        int categoryId = [[keysResponse objectAtIndex:i] intValue];
        NSString *categoryName = [namesResponse objectAtIndex:i];
        
        CategoryItem *categoryItem = [[CategoryItem alloc] initWithAttributes:categoryId Name:categoryName];
        [categories addObject:categoryItem];
    }
    
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    app.categories = categories;
    
    [self startMainView];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result
{
    if (m_waitingDlg != nil)
    {
        [m_waitingDlg hide:YES];
    }
    
    switch (type) {
        case SIGN_IN:
            [self onSignInResult:(NSArray *)result];
            break;
            
        case FACEBOOK_LOGIN:
            [self onLogInWithFBResult:(NSArray *)result];
            break;
            
        case GOOGLE_LOGIN:
            [self onLogInWithGoogleResult:(NSArray *)result];
            break;
            
        case TWITTER_LOGIN:
            [self onLogInWithTwitterResult:(NSArray *)result];
            break;
            
        case GET_SINGLE_DREMER:
            [self onGetSingleDremerResult:(NSArray *)result];
            break;
            
        case GET_CATEGORY:
            [self onGetCategoryResult:(NSArray *)result];
            break;
            
        default:
            break;
    }
    
}

- (void) startMainView {
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    M13InfiniteTabBarController *mainViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"MainTabBarController"];
    //        mainViewController.tabBarPosition = M13InfiniteTabBarPositionTop;
    mainViewController.delegate = self;
    mainViewController.enableInfiniteScrolling = NO;
    //Set the requires user attention background
    mainViewController.requiresAttentionBackgroundView = [[PulsingRequiresAttentionView alloc] init];
    //A view controller requires user attention
    //        [mainViewController viewControllerAtIndex:5 requiresUserAttentionWithImportanceLevel:0];
    [mainViewController setSelectedIndex:0];
    
    [self presentViewController:mainViewController animated:YES completion:nil];
}

#pragma mark - M13InfiniteTabBarDelegate

- (NSArray *)infiniteTabBarControllerRequestingViewControllersToDisplay:(M13InfiniteTabBarController *)tabBarController
{
    //Load the view controllers from the storyboard and add them to the array.
    UIStoryboard *storyboard = self.storyboard;
    
//    UINavigationController *ncHome = [storyboard instantiateViewControllerWithIdentifier:@"NavigationHomeTab"];
    HomeTabViewController *vcHome = [storyboard instantiateViewControllerWithIdentifier:@"HomeVC"];
    [vcHome setMainVC:tabBarController];
//    [ncHome pushViewController:vcHome animated:YES];
    
    UINavigationController *ncDrems = [storyboard instantiateViewControllerWithIdentifier:@"NavigationDremsTab"];
    DremsTabViewController *vcDrems = [storyboard instantiateViewControllerWithIdentifier:@"DremsVC"];
    [vcDrems setMainVC:tabBarController];
    [ncDrems pushViewController:vcDrems animated:YES];
    
    UINavigationController *ncDremers = [storyboard instantiateViewControllerWithIdentifier:@"NavigationDremersTab"];
    DremersTabViewController *vcDremers = [storyboard instantiateViewControllerWithIdentifier:@"DremersVC"];
    [vcDremers setMainVC:tabBarController];
    [ncDremers pushViewController:vcDremers animated:YES];
    
    UINavigationController *ncDremboards = [storyboard instantiateViewControllerWithIdentifier:@"NavigationDremboardsTab"];
    DremboardsTabViewController *vcDremboards = [storyboard instantiateViewControllerWithIdentifier:@"DremboardsVC"];
    [vcDremboards setMainVC:tabBarController];
    [ncDremboards pushViewController:vcDremboards animated:YES];
    
    UINavigationController *ncDremcast = [storyboard instantiateViewControllerWithIdentifier:@"NavigationDremcastTab"];
    ViewController *vcDremcast = [storyboard instantiateViewControllerWithIdentifier:@"DremcastVC"];
    [ncDremcast pushViewController:vcDremcast animated:YES];
    
    UINavigationController *ncYou = [storyboard instantiateViewControllerWithIdentifier:@"NavigationYouTab"];
    YouTabViewController *vcYou = [storyboard instantiateViewControllerWithIdentifier:@"YouVC"];
    [vcYou setMainVC:tabBarController];
    [ncYou pushViewController:vcYou animated:YES];
    
    UINavigationController *ncMore = [storyboard instantiateViewControllerWithIdentifier:@"NavigationMoreTab"];
    ViewController *vcMore = [storyboard instantiateViewControllerWithIdentifier:@"MoreVC"];
    [ncMore pushViewController:vcMore animated:YES];

    //You probably want to set this on the UIViewController initalization, from within the UIViewController subclass. I'm just doing it here since each tab inherits from the same subclass.
    [vcHome setInfiniteTabBarItem:[[M13InfiniteTabBarItem alloc] initWithTitle:@"Home" selectedIconMask:[UIImage imageNamed:@"chk_full.png"] unselectedIconMask:[UIImage imageNamed:@"chk_empty.png"]]];
    [ncDrems setInfiniteTabBarItem:[[M13InfiniteTabBarItem alloc] initWithTitle:@"Drēms" selectedIconMask:[UIImage imageNamed:@"chk_full.png"] unselectedIconMask:[UIImage imageNamed:@"chk_empty.png"]]];
    [ncDremers setInfiniteTabBarItem:[[M13InfiniteTabBarItem alloc] initWithTitle:@"Drēmers" selectedIconMask:[UIImage imageNamed:@"chk_full.png"] unselectedIconMask:[UIImage imageNamed:@"chk_empty.png"]]];
    [ncDremboards setInfiniteTabBarItem:[[M13InfiniteTabBarItem alloc] initWithTitle:@"Drēmboards" selectedIconMask:[UIImage imageNamed:@"chk_full.png"] unselectedIconMask:[UIImage imageNamed:@"chk_empty.png"]]];
    [ncDremcast setInfiniteTabBarItem:[[M13InfiniteTabBarItem alloc] initWithTitle:@"Drēmcast" selectedIconMask:[UIImage imageNamed:@"chk_full.png"] unselectedIconMask:[UIImage imageNamed:@"chk_empty.png"]]];
    [ncYou setInfiniteTabBarItem:[[M13InfiniteTabBarItem alloc] initWithTitle:@"You" selectedIconMask:[UIImage imageNamed:@"chk_full.png"] unselectedIconMask:[UIImage imageNamed:@"chk_empty.png"]]];
    [ncMore setInfiniteTabBarItem:[[M13InfiniteTabBarItem alloc] initWithTitle:@"More" selectedIconMask:[UIImage imageNamed:@"chk_full.png"] unselectedIconMask:[UIImage imageNamed:@"chk_empty.png"]]];
    
    return @[vcHome, ncDrems, ncDremers, ncDremboards, ncDremcast, ncYou, ncMore];
}

//Delegate Protocol
- (BOOL)infiniteTabBarController:(M13InfiniteTabBarController *)tabBarController shouldSelectViewContoller:(UIViewController *)viewController
{
    if ([viewController.title isEqualToString:@"Search"]) { //Prevent selection
        return NO;
    } else {
        return YES;
    }
}

- (void)infiniteTabBarController:(M13InfiniteTabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController
{
    //Do nothing
}

@end
