//
//  HttpTask.h
//  online-adviser
//
//  Created by vitaly on 4/1/14.
//  Copyright (c) 2014 AltairJang. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Constants.h"

@protocol HttpTaskDelegate;

typedef enum {
    /* User Actions */
    SIGN_UP, SIGN_IN, SIGN_OUT,
    
    GET_PROFILE, SET_PROFILE, SET_AVATAR,
    
    CHANGE_PWD, RESET_PWD,
    
    /* SNS Login */
    
    FACEBOOK_LOGIN, GOOGLE_LOGIN, TWITTER_LOGIN,
    
    /* Message */
    GET_MESSAGES, GET_SINGLE_MESSAGE, SEND_MESSAGE, REPLY_MESSAGE, SET_MESSAGE,
    
    /* Settings */
    SET_SETTING_GENERAL, GET_SETTING_NOTE, SET_SETTING_NOTE, GET_SETTING_PRIVACY, SET_SETTING_PRIVACY,
    
    /* Invite */
    SEND_INVITE,
    
    /* Notification */
    GET_NT, SET_NT,
    
    /* GET Content */
    GET_DREM_ACTIVITIES, GET_DREMS, GET_DREMERS, GET_VIDEOS, GET_SINGLE_DREMER,
    
    /* Dremboard Actions */
    GET_DREMBOARDS, CREATE_DREMBOARD, EDIT_DREMBOARD, DELETE_DREMBOARD, MERGE_DREMBOARD, ADD_DREM_TO_BOARD, REMOVE_DREMS_FROM_BOARD, MOVE_DREMS_TO_BOARD,
    
    /* Contents Actions */
    SET_COMMENT, SET_FAVORITE, SET_LIKE, SET_FLAG,
    
    /* Dremer Actions */
    SET_FRIENDSHIP, SET_FAMILYSHIP, SET_BLOCK, SET_FOLLOW,
    
    /* Other */
    GET_CATEGORY,
} HttpAPIType;

@interface HttpTask : NSObject
{
    HttpAPIType m_apiType;
    NSObject * mParamerter;
}

- (id) initWithParam:(HttpAPIType) type Parameter:(NSObject *)param;
- (void) runTask;
- (void) httpTaskBody;
- (void) runSignIn;
- (void) runGetDrems;
- (void) runGetDremers;
- (void) runGetDremboards;

//@property (nonatomic, retain) NSObject * mParamerter;
@property (nonatomic, assign) id <HttpTaskDelegate> delegate;

@end

@protocol HttpTaskDelegate <NSObject>

@optional
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end

