//
//  Dremer.m
//  dremboard
//
//  Created by YingLi on 4/29/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "Dremer.h"


/* DremerInfo */
@implementation DremerInfo : NSObject

@synthesize user_id;
@synthesize user_registered;
@synthesize user_login;
@synthesize user_niceman;
@synthesize display_name;
@synthesize fullname;
@synthesize user_email;
@synthesize friendship_status;
@synthesize familyship_status;
@synthesize is_following;
@synthesize block_type;
@synthesize user_avatar;
@synthesize last_activity;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.user_id = [[attributes valueForKeyPath:@"user_id"] intValue];
    self.user_registered = [attributes valueForKeyPath:@"user_registered"];
    self.user_login = [attributes valueForKeyPath:@"user_login"];
    self.user_niceman = [attributes valueForKeyPath:@"user_niceman"];
    self.display_name = [attributes valueForKeyPath:@"display_name"];
    self.fullname = [attributes valueForKeyPath:@"fullname"];
    self.user_email = [attributes valueForKeyPath:@"user_email"];
    self.friendship_status = [attributes valueForKeyPath:@"friendship_status"];
    self.familyship_status = [attributes valueForKeyPath:@"familyship_status"];
    self.is_following = [[attributes valueForKeyPath:@"is_following"] intValue];
    NSString * block = [attributes valueForKeyPath:@"block_type"];
    if ([block isEqual:[NSNull null]])
        self.block_type = 0;
    else
        self.block_type = [block intValue];
    self.user_avatar = [attributes valueForKeyPath:@"user_avatar"];
    self.last_activity = [attributes valueForKeyPath:@"last_activity"];
    
    self.latest_update = [[DremerLastContent alloc] initWithAttributes:[attributes valueForKey:@"latest_update"]];
    
    return self;
}

+ (NSString*) getFriendshipAction:(NSString*)status {
    NSString * retStr = @"";
    
    if (status == nil)
        return retStr;
    
    if ([status isEqualToString:@"pending"])
        retStr = @"cancel";
    else if ([status isEqualToString:@"not_friends"])
        retStr = @"add-friend";
    else if ([status isEqualToString:@"is_friend"])
        retStr = @"remove-friend";
    else
        retStr = @"";
    
    return retStr;

}

+ (NSString*) getFriendshipStr:(NSString*)status {
    NSString * retStr = @"";
    
    if (status == nil)
        return retStr;
    
    if ([status isEqualToString:@"pending"])
        retStr = @"Cancel Friend Request";
    else if ([status isEqualToString:@"not_friends"])
        retStr = @"Add Friend";
    else if ([status isEqualToString:@"awaiting_response"])
        retStr = @"Friendship Requested";
    else if ([status isEqualToString:@"is_friend"])
        retStr = @"Cancel Friendship";
    
    return retStr;
    
}

+ (NSString*) getFamilyshipAction:(NSString*)status {
    NSString * retStr = @"";
    
    if (status == nil)
        return retStr;
    
    if ([status isEqualToString:@"pending"])
        retStr = @"cancel";
    else if ([status isEqualToString:@"not_familys"])
        retStr = @"add-family";
    else if ([status isEqualToString:@"is_family"])
        retStr = @"remove-family";
    else
        retStr = @"";
    
    return retStr;
    
}

+ (NSString*) getFamilyshipStr:(NSString*)status {
    NSString * retStr = @"";
    
    if (status == nil)
        return retStr;
    
    if ([status isEqualToString:@"pending"])
        retStr = @"Cancel Family Request";
    else if ([status isEqualToString:@"not_familys"])
        retStr = @"Add Family";
    else if ([status isEqualToString:@"awaiting_response"])
        retStr = @"Familyship Requested";
    else if ([status isEqualToString:@"is_family"])
        retStr = @"Cancel Familyship";
    
    return retStr;
    
}

@end

/* GetDremersParam */
@implementation DremerLastContent : NSObject

@synthesize content_id;
@synthesize content;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.content_id = (int)[[attributes valueForKeyPath:@"id"] integerValue];
    self.content = [attributes valueForKeyPath:@"content"];
    
    return self;
}
@end

/* Profile Item */
@implementation ProfileItem

@synthesize profile_id;
@synthesize name;
@synthesize value;
@synthesize visibility;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.profile_id = [[attributes valueForKeyPath:@"id"] intValue];
    self.name = [attributes valueForKeyPath:@"name"];
    self.value = [attributes valueForKeyPath:@"value"];
    self.visibility = [attributes valueForKeyPath:@"visibility"];
    
    return self;
}

@end

/************************** Get Single Dremer  ****************************/

/* GetSingleDremerParam */
@implementation GetSingleDremerParam

@synthesize user_id;
@synthesize disp_user_id;

@end

/* GetSingleDremerData */
@implementation GetSingleDremerData
@synthesize member;
@synthesize profiles;
@end

/* GetSingleDremersResult */
@implementation GetSingleDremersResult

@synthesize status;
@synthesize msg;
@synthesize data;

@end


/************************** Get Dremer List  ****************************/

/* GetDremersParam */
@implementation GetDremersParam : NSObject

@synthesize user_id;
@synthesize disp_user_id;
@synthesize page;
@synthesize per_page;
@synthesize search_str;
@synthesize type;

@end

/* GetDremersData */
@implementation GetDremersData : NSObject

@synthesize count;
@synthesize member;

@end

/* GetDremersResult */
@implementation GetDremersResult : NSObject

@synthesize status;
@synthesize msg;
@synthesize data;

@end

/************************** Dremer Actions ****************************/

/* ===================================
          Set Friendship
 =================================== */
/* SetFriendParam */
@implementation SetFriendParam : NSObject

@synthesize user_id;
@synthesize dremer_id;
@synthesize action;

@synthesize index;
@end

/* SetFriendData */
@implementation SetFriendData : NSObject
@synthesize friendship_status;
@end

/* SetLikeResult */
@implementation SetFriendResult : NSObject

@synthesize status;
@synthesize msg;
@synthesize data;

@end

/* ===================================
         Set Familyship
 =================================== */
/* SetFamilyParam */
@implementation SetFamilyParam : NSObject

@synthesize user_id;
@synthesize dremer_id;
@synthesize action;

@synthesize index;
@end

/* SetFamilyData */
@implementation SetFamilyData : NSObject
@synthesize familyship_status;
@end

/* SetFamilyResult */
@implementation SetFamilyResult : NSObject

@synthesize status;
@synthesize msg;
@synthesize data;

@end

/* ===================================
          Set Following
 =================================== */
/* SetFollowingParam */
@implementation SetFollowingParam : NSObject

@synthesize user_id;
@synthesize dremer_id;
@synthesize action;

@synthesize index;
@end

/* SetFollowingData */
@implementation SetFollowingData : NSObject
@synthesize is_follow;
@end

/* SetFollowingResult */
@implementation SetFollowingResult : NSObject

@synthesize status;
@synthesize msg;
@synthesize data;

@end

/* ===================================
          Set Blocking
 =================================== */
/* SetBlockingParam */
@implementation SetBlockingParam : NSObject

@synthesize user_id;
@synthesize dremer_id;
@synthesize block_type;
@synthesize action;

@synthesize index;
@end

/* SetBlockingData */
@implementation SetBlockingData : NSObject
@synthesize block_type;
@end

/* SetBlockingResult */
@implementation SetBlockingResult : NSObject

@synthesize status;
@synthesize msg;
@synthesize data;

@end


