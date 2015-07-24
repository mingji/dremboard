//
//  Dremer.h
//  dremboard
//
//  Created by YingLi on 4/29/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

@class DremerLastContent;

/* DremerInfo */
@interface DremerInfo : NSObject

@property (nonatomic) int user_id;
@property (retain, nonatomic) NSString * user_registered;
@property (retain, nonatomic) NSString * user_login;
@property (retain, nonatomic) NSString * user_niceman;
@property (retain, nonatomic) NSString * display_name;
@property (retain, nonatomic) NSString * fullname;
@property (retain, nonatomic) NSString * user_email;
@property (retain, nonatomic) NSString * friendship_status;
@property (retain, nonatomic) NSString * familyship_status;
@property (nonatomic) int is_following;
@property (nonatomic) int block_type;
@property (retain, nonatomic) NSString * user_avatar;
@property (retain, nonatomic) NSString * last_activity;

@property (retain, nonatomic) DremerLastContent * latest_update;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;

+ (NSString*) getFriendshipAction:(NSString*)status;
+ (NSString*) getFriendshipStr:(NSString*)status;
+ (NSString*) getFamilyshipAction:(NSString*)status;
+ (NSString*) getFamilyshipStr:(NSString*)status;

@end

/* Last content */
@interface DremerLastContent : NSObject

@property (nonatomic) int content_id;
@property (retain, nonatomic) NSString * content;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;

@end

/* Profile Item */
@interface ProfileItem : NSObject

@property (nonatomic) int profile_id;
@property (retain, nonatomic) NSString * name;
@property (retain, nonatomic) NSString * value;
@property (retain, nonatomic) NSString * visibility;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;

@end

/************************** Get Single Dremer  ****************************/

/* GetSingleDremerParam */
@interface GetSingleDremerParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int disp_user_id;

@end

/* GetSingleDremerData */
@interface GetSingleDremerData : NSObject
@property (retain, nonatomic) DremerInfo * member;
@property (retain, nonatomic) NSMutableArray * profiles;
@end

/* GetSingleDremersResult */
@interface GetSingleDremersResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetSingleDremerData * data;

@end


/************************** Get Dremer List  ****************************/

/* GetDremersParam */
@interface GetDremersParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int disp_user_id;
@property (nonatomic) int page;
@property (nonatomic) int per_page;
@property (retain, nonatomic) NSString * search_str;
@property (retain, nonatomic) NSString * type;

@end

/* GetDremersData */
@interface GetDremersData : NSObject
@property (nonatomic) int count;
@property (retain, nonatomic) NSMutableArray * member;
@end

/* GetDremersResult */
@interface GetDremersResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetDremersData * data;

@end

/************************** Dremer Actions ****************************/

/* ===================================
            Set Friendship
 =================================== */
/* SetFriendParam */
@interface SetFriendParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int dremer_id;
@property (retain, nonatomic) NSString * action;

// extend field - is not parameter...
@property (nonatomic) int index;
@end

/* SetFriendData */
@interface SetFriendData : NSObject
@property (retain, nonatomic) NSString * friendship_status;
@end

/* SetLikeResult */
@interface SetFriendResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) SetFriendData * data;

@end

/* ===================================
         Set Familyship
 =================================== */
/* SetFamilyParam */
@interface SetFamilyParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int dremer_id;
@property (retain, nonatomic) NSString * action;

// extend field - is not parameter...
@property (nonatomic) int index;
@end

/* SetFamilyData */
@interface SetFamilyData : NSObject
@property (retain, nonatomic) NSString * familyship_status;
@end

/* SetFamilyResult */
@interface SetFamilyResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) SetFamilyData * data;

@end

/* ===================================
          Set Following
 =================================== */
/* SetFollowingParam */
@interface SetFollowingParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int dremer_id;
@property (retain, nonatomic) NSString * action;

// extend field - is not parameter...
@property (nonatomic) int index;
@end

/* SetFollowingData */
@interface SetFollowingData : NSObject
@property (nonatomic) int is_follow;
@end

/* SetFollowingResult */
@interface SetFollowingResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) SetFollowingData * data;

@end

/* ===================================
            Set Blocking
 =================================== */
/* SetBlockingParam */
@interface SetBlockingParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int dremer_id;
@property (nonatomic) int block_type;
@property (retain, nonatomic) NSString * action;

// extend field - is not parameter...
@property (nonatomic) int index;
@end

/* SetBlockingData */
@interface SetBlockingData : NSObject
@property (nonatomic) int block_type;
@end

/* SetBlockingResult */
@interface SetBlockingResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) SetBlockingData * data;

@end
