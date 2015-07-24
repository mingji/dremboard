//
//  Activity.h
//  dremboard
//
//  Created by YingLi on 4/30/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

/* Activity Info */
@interface ActivityInfo : NSObject

@property (nonatomic) int activity_id;
@property (nonatomic) int author_id;
@property (retain, nonatomic) NSString * author_avatar;
@property (retain, nonatomic) NSString * action;
@property (retain, nonatomic) NSString * last_modified;
@property (retain, nonatomic) NSString * description;
@property (retain, nonatomic) NSString * like;
@property (retain, nonatomic) NSString * favorite;

@property (retain, nonatomic) NSMutableArray * media_list;
@property (retain, nonatomic) NSMutableArray * comment_list;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;
+ (NSString *) convert2RelativeDate:(NSString *)strDate;
@end

/* MediaInfo */
@interface MediaInfo : NSObject

@property (retain, nonatomic) NSString * media_type;
@property (retain, nonatomic) NSString * media_guid;

@end

/* CommentInfo */
@interface CommentInfo : NSObject

@property (nonatomic) int activity_id;
@property (nonatomic) int author_id;
@property (retain, nonatomic) NSString * author_avatar;
@property (retain, nonatomic) NSString * author_name;
@property (retain, nonatomic) NSString * description;
@property (retain, nonatomic) NSString * media_guid;
@property (retain, nonatomic) NSString * last_modified;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;

@end

/* GetActivitiesParam */
@interface GetActivitiesParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int disp_user_id;
@property (nonatomic) int last_activity_id;
@property (nonatomic) int per_page;
@property (retain, nonatomic) NSString * acitvity_scope;
@property (retain, nonatomic) NSString * type;

@end

/* GetActivitiesData */
@interface GetActivitiesData : NSObject
@property (nonatomic) int count;
@property (retain, nonatomic) NSMutableArray * activity;
@end

/* GetActiviesResult */
@interface GetActivitiesResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetActivitiesData * data;

@end

// Set Comment
/* SetCommentParam */
@interface SetCommentParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int activity_id;
@property (retain, nonatomic) NSString * comment;
@property (retain, nonatomic) NSString * photo;

@end

/* SetCommentData */
@interface SetCommentData : NSObject
@property (retain, nonatomic) CommentInfo * comment;
@end

/* SetCommentResult */
@interface SetCommentResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) SetCommentData * data;

@end

