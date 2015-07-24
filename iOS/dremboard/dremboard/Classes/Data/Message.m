//
//  Message.m
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "Message.h"

/* Message Info */
@implementation MessageInfo

@synthesize msg_id;
@synthesize unread_count;
@synthesize type;
@synthesize post_date;
@synthesize title;
@synthesize excerpt;

@synthesize from;
@synthesize to;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.msg_id = [[attributes valueForKeyPath:@"id"] intValue];
    self.unread_count = [[attributes valueForKeyPath:@"unread_count"] intValue];
    self.type = [attributes valueForKeyPath:@"type"];
    self.post_date = [attributes valueForKeyPath:@"post_date"];
    self.title = [attributes valueForKeyPath:@"title"];
    self.excerpt = [attributes valueForKeyPath:@"excerpt"];
    
    if ([self.type isEqualToString:TYPE_INBOX]) {
        NSDictionary *fromUser = [attributes valueForKeyPath:@"from"];
        self.from = [[Partner alloc] initWithAttributes:fromUser];
    } else {
        self.to = [[NSMutableArray alloc] init];
        NSArray *toUsers = [attributes valueForKeyPath:@"to"];
        for (NSDictionary *userInfo in toUsers) {
            Partner *partner = [[Partner alloc] initWithAttributes:userInfo];
            [self.to addObject:partner];
        }
    }
    
    return self;
}

@end

/* Message Partner */
@implementation Partner

@synthesize user_id;
@synthesize name;
@synthesize avatar;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.user_id = [[attributes valueForKeyPath:@"id"] intValue];
    self.name = [attributes valueForKeyPath:@"name"];
    self.avatar = [attributes valueForKeyPath:@"avatar"];
    
    return self;
}

@end

/* GetMessagesParam */
@implementation GetMessagesParam

@synthesize user_id;
@synthesize page;
@synthesize per_page;
@synthesize type;

@end

/* GetMessagesData */
@implementation GetMessagesData
@synthesize messages;
@end

/* GetMessagesResult */
@implementation GetMessagesResult

@synthesize status;
@synthesize msg;
@synthesize data;

@end

/*************************** Send Message *******************************/
/* SendMessageParam */
@implementation SendMessageParam

@synthesize user_id;
@synthesize recipients;
@synthesize subject;
@synthesize content;
@synthesize is_notice;

@end

/* SendMessageResult */
@implementation SendMessageResult

@synthesize status;
@synthesize msg;

@end
