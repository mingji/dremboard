//
//  Actions.h
//  dremboard
//
//  Created by YingLi on 5/13/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

/* ===================================
           Set Favorite
   =================================== */

/* SetFavoriteParam */
@interface SetFavoriteParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int activity_id;
@property (retain, nonatomic) NSString * favorite_str;

// extend field - is not parameter...
@property (nonatomic) int index;

@end

/* SetFavoriteData */
@interface SetFavoriteData : NSObject
@property (retain, nonatomic) NSString * favorite_str;
@end

/* SetFavoriteResult */
@interface SetFavoriteResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) SetFavoriteData * data;

@end

/* ===================================
           Set Like
 =================================== */
/* SetLikeParam */
@interface SetLikeParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int activity_id;
@property (retain, nonatomic) NSString * like_str;

// extend field - is not parameter...
@property (nonatomic) int index;

- (instancetype)initWithAttributes:(int)userId ActivityId:(int)activityId Like:(NSString *)likeStr Index:(int)idx;
@end

/* SetLikeData */
@interface SetLikeData : NSObject
@property (retain, nonatomic) NSString * like_str;
@end

/* SetLikeResult */
@interface SetLikeResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) SetLikeData * data;

@end

/* ===================================
             Set Flag
 =================================== */
/* SetFlagParam */
@interface SetFlagParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int activity_id;
@property (retain, nonatomic) NSString * flag_slug;

// extend field - is not parameter...
@property (nonatomic) int index;
@end

/* SetLikeResult */
@interface SetFlagResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;

@end



