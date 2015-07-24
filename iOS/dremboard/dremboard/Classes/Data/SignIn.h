//
//  SignIn.h
//  dremboard
//
//  Created by YingLi on 4/27/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

/* SignInParam */
@interface SignInParam : NSObject

@property (retain, nonatomic) NSString * username;
@property (retain, nonatomic) NSString * password;

@end

/* SignInData */
@interface SignInData : NSObject

@property (nonatomic) int ID;
@property (nonatomic, retain) NSString * user_login;
@property (nonatomic, retain) NSString * avatar;

@end

/* SignInResult */
@interface SignInResult : NSObject

@property (nonatomic, retain) NSString * status;
@property (nonatomic, retain) NSString * msg;
@property (nonatomic, retain) SignInData * data;

@end
