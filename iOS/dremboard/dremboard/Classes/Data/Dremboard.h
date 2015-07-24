//
//  Dremboard.h
//  dremboard
//
//  Created by YingLi on 5/8/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

/* DremboardInfo */
@interface DremboardInfo : NSObject

@property (nonatomic) int dremboard_id;
@property (nonatomic) int media_author_id;
@property (retain, nonatomic) NSString * media_type;
@property (retain, nonatomic) NSString * media_title;
@property (retain, nonatomic) NSString * media_author_avatar;
@property (retain, nonatomic) NSString * media_author_name;
@property (retain, nonatomic) NSString * guid;
@property (nonatomic) int album_count;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;

@end


/*****************************************************************
                      Get Dremboards
 *****************************************************************/

/* GetDremboardsParam */
@interface GetDremboardsParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int author_id;
@property (nonatomic) int category;
@property (nonatomic) int last_media_id;
@property (nonatomic) int per_page;
@property (retain, nonatomic) NSString * search_str;

@end

/* GetDremboardsData */
@interface GetDremboardsData : NSObject
@property (nonatomic) int count;
@property (retain, nonatomic) NSMutableArray * media;
@end

/* GetDremboardsResult */
@interface GetDremboardsResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetDremboardsData * data;
@end

/*****************************************************************
                        Create Dremboard
 *****************************************************************/

/* CreateDremboardParam */
@interface CreateDremboardParam : NSObject
@property (nonatomic) int user_id;
@property (retain, nonatomic) NSString * title;
@property (retain, nonatomic) NSString * description;
@property (nonatomic) int category_id;
@property (nonatomic) int privacy;
@end

/* CreateDremboardResult */
@interface CreateDremboardResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@end

/*****************************************************************
                        Edit Dremboard
 *****************************************************************/

/* EditDremboardParam */
@interface EditDremboardParam : NSObject
@property (retain, nonatomic) NSString * title;
@property (retain, nonatomic) NSString * description;
@property (nonatomic) int dremboard_id;
@end

/* EditDremboardResult */
@interface EditDremboardResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@end

/*****************************************************************
                        Delete Dremboard
 *****************************************************************/

/* DeleteDremboardParam */
@interface DeleteDremboardParam : NSObject
@property (nonatomic) int dremboard_id;
@end

/* DeleteDremboardResult */
@interface DeleteDremboardResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@end


/*****************************************************************
                        Merge Dremboard
 *****************************************************************/

/* MergeDremboardParam */
@interface MergeDremboardParam : NSObject
@property (nonatomic) int source_id;
@property (nonatomic) int target_id;
@end

/* MergeDremboardResult */
@interface MergeDremboardResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@end

/*****************************************************************
                        Add drem to Dremboard
 *****************************************************************/

/* AddDremToDremboardParam */
@interface AddDremToDremboardParam : NSObject
@property (nonatomic) int user_id;
@property (nonatomic) int drem_id;
@property (nonatomic) int dremboard_id;
@end

/* AddDremToDremboardResult */
@interface AddDremToDremboardResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@end

/*****************************************************************
                        Remove drems from Dremboard
 *****************************************************************/

/* RemoveDremsFromDremboardParam */
@interface RemoveDremsFromDremboardParam : NSObject
@property (retain, nonatomic) NSString * drem_ids;
@end

/* RemoveDremsFromDremboardResult */
@interface RemoveDremsFromDremboardResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@end

/*****************************************************************
                        Move drems to Dremboard
 *****************************************************************/

/* MoveDremsToDremboardParam */
@interface MoveDremsToDremboardParam : NSObject
@property (retain, nonatomic) NSString * drem_ids;
@property (nonatomic) int dremboard_id;
@end

/* MoveDremsToDremboardResult */
@interface MoveDremsToDremboardResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@end

