//
//  Drem.m
//  dremboard
//
//  Created by YingLi on 4/27/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "Drem.h"
#import "Activity.h"

/* DremInfo */
@implementation DremInfo : NSObject

@synthesize drem_id;
@synthesize activity_id;
@synthesize media_title;
@synthesize category;
@synthesize media_type;
@synthesize guid;
@synthesize favorite;
@synthesize like;
@synthesize fMoreLess;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.drem_id = (int)[[attributes valueForKeyPath:@"id"] integerValue];
    self.activity_id = (int)[[attributes valueForKeyPath:@"activity_id"] integerValue];
    self.media_title = [attributes valueForKeyPath:@"media_title"];
    self.category = [attributes valueForKeyPath:@"category"];
    self.media_type = [attributes valueForKeyPath:@"media_type"];
    self.guid = [attributes valueForKeyPath:@"guid"];
    self.favorite = [attributes valueForKeyPath:@"favorite"];
    self.like = [attributes valueForKeyPath:@"like"];
    
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
    
    self.fMoreLess = NO;
    
    return self;
}

@end

/* GetDremsParam */
@implementation GetDremsParam : NSObject

@synthesize user_id;
@synthesize album_id;
@synthesize author_id;
@synthesize category;
@synthesize last_media_id;
@synthesize per_page;
@synthesize search_str;

@end

/* GetDremsData */
@implementation GetDremsData : NSObject

@synthesize count;
@synthesize media;

@end

/* GetDremsResult */
@implementation GetDremsResult : NSObject

@synthesize status;
@synthesize msg;
@synthesize data;

@end

