//
//  Notification.h
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

/* NotificationInfo */
@interface NotificationInfo : NSObject

@property (nonatomic) int nt_id;
@property (retain, nonatomic) NSString * desc;
@property (retain, nonatomic) NSString * since;
@property (retain, nonatomic) NSString * type;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;
@end


/******************  Get Notifications  *********************/
/* GetNTsParam */
@interface GetNTsParam : NSObject

@property (nonatomic) int user_id;
@property (retain, nonatomic) NSString * type;
@property (nonatomic) int page;
@property (nonatomic) int per_page;

@end

/* GetNTsData */
@interface GetNTsData : NSObject
@property (retain, nonatomic) NSMutableArray * notification;
@end

/* GetNTsResult */
@interface GetNTsResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetNTsData * data;
@end

/******************  Set Notifications  *********************/
/* SetNTsParam */
@interface SetNTParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int notification_id;
@property (retain, nonatomic) NSString * action;

@property (nonatomic) int index;

@end

/* SetNTResult */
@interface SetNTResult : NSObject
@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@end
