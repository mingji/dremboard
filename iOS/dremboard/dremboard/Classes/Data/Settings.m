//
//  Settings.m
//  dremboard
//
//  Created by YingLi on 5/26/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "Settings.h"

/************************ Settings General ***************************/

/* SetSettingGeneralParam */
@implementation SetSettingGeneralParam

@synthesize user_id;
@synthesize disp_user_id;
@synthesize email;
@synthesize password;

@end

/* SetSettingGeneralResult */
@implementation SetSettingGeneralResult

@synthesize status;
@synthesize msg;

@end

/************************ Get Settings Email Note ***************************/

/* EmaiNoteConf */
@implementation EmailNoteConf

@synthesize conf_id;
@synthesize description;
@synthesize value;

- (instancetype)initWithAttributes:(NSDictionary *)attributes {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.conf_id = [attributes valueForKeyPath:@"id"];
    self.description = [attributes valueForKeyPath:@"description"];
    self.value = [attributes valueForKeyPath:@"value"];
    
    return self;
}

@end

/* GetSettingNoteParam */
@implementation GetSettingNoteParam

@synthesize user_id;
@synthesize disp_user_id;

@end

/* GetSettingNoteData */
@implementation GetSettingNoteData

@synthesize notifications;

@end

/* GetSettingNoteResult */
@implementation GetSettingNoteResult

@synthesize status;
@synthesize msg;
@synthesize data;

@end

/************************ Set Settings Email Note ***************************/

/* SetSettingNoteParam */
@implementation SetSettingNoteParam

@synthesize user_id;
@synthesize disp_user_id;
@synthesize notifications;

@end

/* SetSettingNoteResult */
@implementation SetSettingNoteResult

@synthesize status;
@synthesize msg;

@end

/***************************** Get Settings Default Privacy ***************************/

/* GetSettingPrivacyParam */
@implementation GetSettingPrivacyParam

@synthesize user_id;
@synthesize disp_user_id;

@end

/* GetSettingPrivacyData */
@implementation GetSettingPrivacyData

@synthesize privacy;

@end

/* GetSettingPrivacyResult */
@implementation GetSettingPrivacyResult

@synthesize status;
@synthesize msg;
@synthesize data;

@end

/***************************** Set Settings Default Privacy ***************************/

/* SetSettingPrivacyParam */
@implementation SetSettingPrivacyParam

@synthesize user_id;
@synthesize disp_user_id;
@synthesize privacy;

@end

/* SetSettingPrivacyResult */
@implementation SetSettingPrivacyResult

@synthesize status;
@synthesize msg;

@end

