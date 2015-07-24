//
//  Drem.h
//  dremboard
//
//  Created by YingLi on 4/27/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

/* DremInfo */
@interface DremInfo : NSObject

@property (nonatomic) int drem_id;
@property (nonatomic) int activity_id;
@property (retain, nonatomic) NSString * media_title;
@property (retain, nonatomic) NSString * category;
@property (retain, nonatomic) NSString * media_type;
@property (retain, nonatomic) NSString * guid;
@property (retain, nonatomic) NSString * favorite;
@property (retain, nonatomic) NSString * like;
@property (retain, nonatomic) NSMutableArray * comment_list;

@property (nonatomic) BOOL fMoreLess; // yes : more, no : less
@property (nonatomic) BOOL fSelection; // yes : selected, no : unselected - in manage dremboard media page

- (instancetype)initWithAttributes:(NSDictionary *)attributes;

@end

/* GetDremsParam */
@interface GetDremsParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int album_id;
@property (nonatomic) int author_id;
@property (nonatomic) int category;
@property (nonatomic) int last_media_id;
@property (nonatomic) int per_page;
@property (retain, nonatomic) NSString * search_str;

@end

/* GetDremsData */
@interface GetDremsData : NSObject
@property (nonatomic) int count;
@property (retain, nonatomic) NSMutableArray * media;
@end

/* GetDremsResult */
@interface GetDremsResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetDremsData * data;
@end