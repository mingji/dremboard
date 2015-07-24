//
//  Notification.m
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "Notification.h"

/* NotificationInfo */
@implementation NotificationInfo : NSObject

@synthesize nt_id;
@synthesize desc;
@synthesize since;
@synthesize type;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.nt_id = [[attributes valueForKeyPath:@"id"] intValue];
    self.desc = [attributes valueForKeyPath:@"desc"];
    self.since = [attributes valueForKeyPath:@"since"];
    self.type = [attributes valueForKeyPath:@"type"];
    
    return self;
}

@end


/******************  Get Notifications  *********************/
/* GetNTsParam */
@implementation GetNTsParam : NSObject

@synthesize user_id;
@synthesize type;
@synthesize page;
@synthesize per_page;

@end

/* GetNTsData */
@implementation GetNTsData : NSObject
@synthesize notification;
@end

/* GetNTsResult */
@implementation GetNTsResult : NSObject
@synthesize status;
@synthesize msg;
@synthesize data;
@end

/******************  Set Notifications  *********************/
/* SetNTsParam */
@implementation SetNTParam : NSObject

@synthesize user_id;
@synthesize notification_id;
@synthesize action;

@synthesize index;

@end

/* SetNTResult */
@implementation SetNTResult : NSObject
@synthesize status;
@synthesize msg;
@end
