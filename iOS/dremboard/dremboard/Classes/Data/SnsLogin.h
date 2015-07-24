//
//  SnsLogin.h
//  dremboard
//
//  Created by YingLi on 6/17/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SnsLogin : NSObject

@end

// Google Client ID created by YingLi
#define GOOGLE_CLIENT_ID      @"1082018926296-lo5aj6ud924eg6bb0hm2b76v64s0hl7t.apps.googleusercontent.com"

// Twitter secret key, created by YingLi
#define TWITTER_CONSUMER_KEY      @"IjCj8wIfK6gkqFiWelwRy8HV9"
#define TWITTER_CONSUMER_SECRET      @"Xb8pJTx6td8sTi5uaufZzUVAoxAm7c5Mq0Fiz9FOwco5P6Crxz"

/* FBLogInParam */
@interface FBLogInParam : NSObject

@property (retain, nonatomic) NSString * fb_token;

@end

/* FBLogInResult */
@interface FBLogInResult : NSObject

@property (nonatomic, retain) NSString * status;
@property (nonatomic, retain) NSString * msg;
@property (nonatomic) int wp_user_id;
@property (nonatomic, retain) NSString * user_login;

@end

/* GoogleLogInParam */
@interface GoogleLogInParam : NSObject

@property (retain, nonatomic) NSString * google_token;

@end

/* GoogleLogInResult */
@interface GoogleLogInResult : NSObject

@property (nonatomic, retain) NSString * status;
@property (nonatomic, retain) NSString * msg;
@property (nonatomic) int wp_user_id;
@property (nonatomic, retain) NSString * user_login;

@end

/* TwitterLogInParam */
@interface TwitterLogInParam : NSObject

@property (retain, nonatomic) NSString * consumer_key;
@property (retain, nonatomic) NSString * consumer_secret;
@property (retain, nonatomic) NSString * access_token;
@property (retain, nonatomic) NSString * access_token_secret;

@end

/* TwitterLogInResult */
@interface TwitterLogInResult : NSObject

@property (nonatomic, retain) NSString * status;
@property (nonatomic, retain) NSString * msg;
@property (nonatomic) int wp_user_id;
@property (nonatomic, retain) NSString * user_login;

@end