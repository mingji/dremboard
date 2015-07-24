//
//  SnsLogin.m
//  dremboard
//
//  Created by YingLi on 6/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "SnsLogin.h"

@implementation SnsLogin

@end

/*      FaceBook      */
@implementation FBLogInParam
@synthesize fb_token;
@end

@implementation FBLogInResult
@synthesize status;
@synthesize msg;
@synthesize wp_user_id;
@synthesize user_login;
@end

/*      Goolge Plus      */
@implementation GoogleLogInParam
@synthesize google_token;
@end

@implementation GoogleLogInResult
@synthesize status;
@synthesize msg;
@synthesize wp_user_id;
@synthesize user_login;
@end

/*      Twitter        */
@implementation TwitterLogInParam
@synthesize consumer_key;
@synthesize consumer_secret;
@synthesize access_token;
@synthesize access_token_secret;
@end

@implementation TwitterLogInResult
@synthesize status;
@synthesize msg;
@synthesize wp_user_id;
@synthesize user_login;
@end

