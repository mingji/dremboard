//
//  Dremboard.m
//  dremboard
//
//  Created by YingLi on 5/8/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "Dremboard.h"

/* DremboardInfo */
@implementation DremboardInfo

@synthesize dremboard_id;
@synthesize media_author_id;
@synthesize media_type;
@synthesize media_title;
@synthesize media_author_avatar;
@synthesize media_author_name;
@synthesize guid;
@synthesize album_count;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.dremboard_id = (int)[[attributes valueForKeyPath:@"id"] integerValue];
    self.media_author_id = (int)[[attributes valueForKeyPath:@"media_author_id"] integerValue];
    self.media_title = [attributes valueForKeyPath:@"media_title"];
    self.media_type = [attributes valueForKeyPath:@"media_type"];
    self.media_author_avatar = [attributes valueForKeyPath:@"media_author_avatar"];
    self.media_author_name = [attributes valueForKeyPath:@"media_author_name"];
    self.guid = [attributes valueForKeyPath:@"guid"];
    self.album_count = (int)[[attributes valueForKeyPath:@"album_count"] integerValue];
    
    return self;
}

@end

/*****************************************************************
                            Get Dremboards
 *****************************************************************/

/* GetDremboardsParam */
@implementation GetDremboardsParam

@synthesize user_id;
@synthesize author_id;
@synthesize category;
@synthesize last_media_id;
@synthesize per_page;
@synthesize search_str;

@end

/* GetDremboardsData */
@implementation GetDremboardsData
@synthesize count;
@synthesize media;
@end

/* GetDremboardsResult */
@implementation GetDremboardsResult
@synthesize status;
@synthesize msg;
@synthesize data;
@end

/*****************************************************************
                        Create Dremboard
 *****************************************************************/

/* CreateDremboardParam */
@implementation CreateDremboardParam
@synthesize user_id;
@synthesize title;
@synthesize description;
@synthesize category_id;
@synthesize privacy;
@end

/* CreateDremboardResult */
@implementation CreateDremboardResult
@synthesize status;
@synthesize msg;
@end

/*****************************************************************
                        Edit Dremboard
 *****************************************************************/

/* EditDremboardParam */
@implementation EditDremboardParam
@synthesize title;
@synthesize description;
@synthesize dremboard_id;
@end

/* EditDremboardResult */
@implementation EditDremboardResult
@synthesize status;
@synthesize msg;
@end

/*****************************************************************
                        Delete Dremboard
 *****************************************************************/

/* DeleteDremboardParam */
@implementation DeleteDremboardParam
@synthesize dremboard_id;
@end

/* DeleteDremboardResult */
@implementation DeleteDremboardResult
@synthesize status;
@synthesize msg;
@end


/*****************************************************************
                        Merge Dremboard
 *****************************************************************/

/* MergeDremboardParam */
@implementation MergeDremboardParam
@synthesize source_id;
@synthesize target_id;
@end

/* MergeDremboardResult */
@implementation MergeDremboardResult
@synthesize status;
@synthesize msg;
@end

/*****************************************************************
                        Add drem to Dremboard
 *****************************************************************/

/* AddDremToDremboardParam */
@implementation AddDremToDremboardParam
@synthesize user_id;
@synthesize drem_id;
@synthesize dremboard_id;
@end

/* AddDremToDremboardResult */
@implementation AddDremToDremboardResult
@synthesize status;
@synthesize msg;
@end

/*****************************************************************
                        Remove drems from Dremboard
 *****************************************************************/

/* RemoveDremsFromDremboardParam */
@implementation RemoveDremsFromDremboardParam
@synthesize drem_ids;
@end

/* RemoveDremsFromDremboardResult */
@implementation RemoveDremsFromDremboardResult
@synthesize status;
@synthesize msg;
@end

/*****************************************************************
                        Move drems to Dremboard
 *****************************************************************/

/* MoveDremsToDremboardParam */
@implementation MoveDremsToDremboardParam
@synthesize drem_ids;
@synthesize dremboard_id;
@end

/* MoveDremsToDremboardResult */
@implementation MoveDremsToDremboardResult
@synthesize status;
@synthesize msg;
@end

