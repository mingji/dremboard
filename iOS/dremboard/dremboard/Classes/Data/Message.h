//
//  Message.h
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Constants.h"

@class Partner;

/* Message Info */
@interface MessageInfo : NSObject

@property (nonatomic) int msg_id;
@property (nonatomic) int unread_count;
@property (retain, nonatomic) NSString * type;
@property (retain, nonatomic) NSString * post_date;
@property (retain, nonatomic) NSString * title;
@property (retain, nonatomic) NSString * excerpt;

@property (retain, nonatomic) Partner * from;
@property (retain, nonatomic) NSMutableArray * to;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;

@end

/* Message Partner */
@interface Partner : NSObject

@property (nonatomic) int user_id;
@property (retain, nonatomic) NSString * name;
@property (retain, nonatomic) NSString * avatar;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;

@end

/* GetMessagesParam */
@interface GetMessagesParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int page;
@property (nonatomic) int per_page;
@property (retain, nonatomic) NSString * type;

@end

/* GetMessagesData */
@interface GetMessagesData : NSObject
@property (retain, nonatomic) NSMutableArray * messages;
@end

/* GetMessagesResult */
@interface GetMessagesResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetMessagesData * data;

@end

/***************************** Send Message *******************************/

/* SendMessageParam */
@interface SendMessageParam : NSObject

@property (nonatomic) int user_id;
@property (retain, nonatomic) NSString * recipients;
@property (retain, nonatomic) NSString * subject;
@property (retain, nonatomic) NSString * content;
@property (retain, nonatomic) NSString * is_notice;

@end

/* SendMessageResult */
@interface SendMessageResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;

@end
