//
//  Activity.m
//  dremboard
//
//  Created by YingLi on 4/30/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "Activity.h"
#import "NSDate+TimeAgo.h"

/* Activity Info */
@implementation ActivityInfo
@synthesize activity_id;
@synthesize author_id;
@synthesize author_avatar;
@synthesize action;
@synthesize last_modified;
@synthesize description;
@synthesize like;
@synthesize favorite;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.activity_id = (int)[[attributes valueForKeyPath:@"activity_id"] integerValue];
    self.author_id = (int)[[attributes valueForKeyPath:@"author_id"] integerValue];
    self.author_avatar = [attributes valueForKeyPath:@"author_avatar"];
    self.action = [self stringByStrippingHTML:[attributes valueForKeyPath:@"action"] ];
    self.last_modified = [attributes valueForKeyPath:@"last_modified"];
    self.description = [attributes valueForKeyPath:@"description"];
    self.like = [attributes valueForKeyPath:@"like"];
//    self.favorite = [attributes valueForKeyPath:@"favorite"];
    self.favorite = @"Favorite";
    
    /* Media List */
    self.media_list = [[NSMutableArray alloc] init];
    NSArray *mediaArray = [attributes valueForKeyPath:@"media_list"];
    for (NSDictionary *mediaData in mediaArray) {
        MediaInfo *media = [[MediaInfo alloc] init];
        media.media_type = [mediaData valueForKey:@"media_type"];
        media.media_guid = [mediaData valueForKey:@"media_guid"];
        
        [self.media_list addObject:media];
    }
    
    /* Comment List */
    self.comment_list = [[NSMutableArray alloc] init];
    NSArray *commentArray = [attributes valueForKeyPath:@"comment_list"];
    for (NSDictionary *commentData in commentArray) {
        CommentInfo *comment = [[CommentInfo alloc] initWithAttributes:commentData];
//        CommentInfo *comment = [[CommentInfo alloc] init];
//        comment.activity_id = (int)[[commentData valueForKeyPath:@"activity_id"] integerValue];
//        comment.author_id = (int)[[commentData valueForKeyPath:@"author_id"] integerValue];
//        comment.author_name = [commentData valueForKeyPath:@"author_name"];
//        comment.author_avatar = [commentData valueForKeyPath:@"author_avatar"];
//        comment.media_guid = [commentData valueForKeyPath:@"media_guid"];
//        comment.last_modified = [commentData valueForKeyPath:@"last_modified"];
//        comment.description = [commentData valueForKeyPath:@"description"];
        
        [self.comment_list addObject:comment];
    }
    
    return self;
}

-(NSString *) stringByStrippingHTML:(NSString *)input {
    
    NSRange r;
    NSString *s = [input copy] ;
    while ((r = [s rangeOfString:@"<[^>]+>" options:NSRegularExpressionSearch]).location != NSNotFound)
        s = [s stringByReplacingCharactersInRange:r withString:@""];
    return s;
}

+ (NSString *) convert2RelativeDate:(NSString *)strDate {
    
    NSDateFormatter *formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSTimeZone *gmt = [NSTimeZone timeZoneWithAbbreviation:@"GMT"];
    [formatter setTimeZone:gmt];
    NSDate *date = [formatter dateFromString:strDate];
    
    NSString *retStr = [date timeAgo];

    return retStr;
}


@end

/* MediaInfo */
@implementation MediaInfo

@synthesize media_type;
@synthesize media_guid;

@end

/* CommentInfo */
@implementation CommentInfo

@synthesize activity_id;
@synthesize author_id;
@synthesize author_name;
@synthesize author_avatar;
@synthesize media_guid;
@synthesize last_modified;
@synthesize description;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.activity_id = (int)[[attributes valueForKeyPath:@"activity_id"] integerValue];
    self.author_id = (int)[[attributes valueForKeyPath:@"author_id"] integerValue];
    self.author_name = [attributes valueForKeyPath:@"author_name"];
    self.author_avatar = [attributes valueForKeyPath:@"author_avatar"];
    self.media_guid = [attributes valueForKeyPath:@"media_guid"];
    self.last_modified = [attributes valueForKeyPath:@"last_modified"];
    self.description = [attributes valueForKeyPath:@"description"];
    
    return self;
}

@end

/* GetActivitiesParam */
@implementation GetActivitiesParam

@synthesize user_id;
@synthesize disp_user_id;
@synthesize last_activity_id;
@synthesize per_page;
@synthesize acitvity_scope;
@synthesize type;

@end

/* GetActivitiesData */
@implementation GetActivitiesData
@synthesize count;
@synthesize activity;
@end

/* GetActiviesResult */
@implementation GetActivitiesResult

@synthesize status;
@synthesize msg;
@synthesize data;

@end

// Set Comment
/* SetCommentParam */
@implementation SetCommentParam

@synthesize user_id;
@synthesize activity_id;
@synthesize comment;
@synthesize photo;

@end

/* SetCommentData */
@implementation SetCommentData
@synthesize comment;
@end

/* SetCommentResult */
@implementation SetCommentResult

@synthesize status;
@synthesize msg;
@synthesize data;

@end
