//
//  Settings.h
//  dremboard
//
//  Created by YingLi on 5/26/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

/************************ Settings General ***************************/

/* SetSettingGeneralParam */
@interface SetSettingGeneralParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int disp_user_id;
@property (retain, nonatomic) NSString * email;
@property (retain, nonatomic) NSString * password;

@end

/* SetSettingGeneralResult */
@interface SetSettingGeneralResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;

@end

/************************ Get Settings Email Note ***************************/

/* EmaiNoteConf */
@interface EmailNoteConf : NSObject

@property (retain, nonatomic) NSString * conf_id;
@property (retain, nonatomic) NSString * description;
@property (retain, nonatomic) NSString * value;

- (instancetype)initWithAttributes:(NSDictionary *)attributes;

@end

/* GetSettingNoteParam */
@interface GetSettingNoteParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int disp_user_id;

@end

/* GetSettingNoteData */
@interface GetSettingNoteData : NSObject

@property (retain, nonatomic) NSMutableArray * notifications;

@end

/* GetSettingNoteResult */
@interface GetSettingNoteResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetSettingNoteData * data;

@end

/************************ Set Settings Email Note ***************************/

/* SetSettingNoteParam */
@interface SetSettingNoteParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int disp_user_id;
@property (retain, nonatomic) NSString * notifications;

@end

/* SetSettingNoteResult */
@interface SetSettingNoteResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;

@end

/***************************** Get Settings Default Privacy ***************************/

/* GetSettingPrivacyParam */
@interface GetSettingPrivacyParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int disp_user_id;

@end

/* GetSettingPrivacyData */
@interface GetSettingPrivacyData : NSObject

@property (retain, nonatomic) NSString * privacy;

@end

/* GetSettingPrivacyResult */
@interface GetSettingPrivacyResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetSettingPrivacyData * data;

@end

/***************************** Set Settings Default Privacy ***************************/

/* SetSettingPrivacyParam */
@interface SetSettingPrivacyParam : NSObject

@property (nonatomic) int user_id;
@property (nonatomic) int disp_user_id;
@property (nonatomic) int privacy;

@end

/* SetSettingPrivacyResult */
@interface SetSettingPrivacyResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;

@end

