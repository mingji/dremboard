//
//  Actions.m
//  dremboard
//
//  Created by YingLi on 5/13/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "Actions.h"

/* SetFavoriteParam */
@implementation SetFavoriteParam

@synthesize user_id;
@synthesize activity_id;
@synthesize favorite_str;
@synthesize index;

@end

/* SetFavoriteData */
@implementation SetFavoriteData
@synthesize favorite_str;
@end

/* SetFavoriteResult */
@implementation SetFavoriteResult
@synthesize status;
@synthesize msg;
@synthesize data;

@end

/* SetLikeParam */
@implementation SetLikeParam

@synthesize user_id;
@synthesize activity_id;
@synthesize like_str;
@synthesize index;

- (instancetype)initWithAttributes:(int)userId ActivityId:(int)activityId Like:(NSString *)likeStr Index:(int)idx
{
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.user_id = userId;
    self.activity_id = activityId;
    self.like_str = likeStr;
    self.index = idx;
    
    return self;
}

@end

/* SetLikeData */
@implementation SetLikeData
@synthesize like_str;
@end

/* SetLikeResult */
@implementation SetLikeResult

@synthesize status;
@synthesize msg;
@synthesize data;

@end

/* SetFlagParam */
@implementation SetFlagParam

@synthesize user_id;
@synthesize activity_id;
@synthesize flag_slug;
@synthesize index;

@end

/* SetFlagResult */
@implementation SetFlagResult

@synthesize status;
@synthesize msg;

@end


