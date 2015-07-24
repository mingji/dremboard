//
//  HttpTask.m
//  online-adviser
//
//  Created by vitaly on 4/1/14.
//  Copyright (c) 2014 AltairJang. All rights reserved.
//

#import "HttpTask.h"
#import "HttpParams.h"
#import "AFHTTPRequestOperationManager.h"

#import "SignIn.h"
#import "SnsLogin.h"
#import "Drem.h"
#import "Dremer.h"
#import "Activity.h"
#import "Dremboard.h"
#import "Actions.h"
#import "Notification.h"
#import "Message.h"
#import "Settings.h"
#import "Category.h"

@implementation HttpTask

- (id) initWithParam:(HttpAPIType) type Parameter:(NSObject *)param
{
    self = [super init];
    
    if (self != nil)
    {
        m_apiType = type;
        mParamerter = param;
    }
    
    return self;
}

- (void) runTask
{
    [self httpTaskBody];
}

- (void) httpTaskBody
{
    switch (m_apiType) {
        case GET_CATEGORY:
            [self runGetCategory];
            break;
            
        case SIGN_IN:
            [self runSignIn];
            break;
            
        case SIGN_UP:
            
            break;
            
        case SIGN_OUT:
            
            break;
            
        case FACEBOOK_LOGIN:
            [self runFacebookLogIn];
            break;
            
        case GOOGLE_LOGIN:
            [self runGoogleLogIn];
            break;
            
        case TWITTER_LOGIN:
            [self runTwitterLogIn];
            break;
            
        case GET_DREM_ACTIVITIES:
            [self runGetActivities];
            break;
            
        case GET_DREMS:
            [self runGetDrems];
            break;
            
        case SET_COMMENT:
            [self runSetComment];
            break;
            
        case GET_SINGLE_DREMER:
            [self runGetSingleDremer];
            break;
            
        case GET_DREMERS:
            [self runGetDremers];
            break;
            
        case GET_DREMBOARDS:
            [self runGetDremboards];
            break;
            
        case CREATE_DREMBOARD:
            [self runCreateDremboard];
            break;

        case EDIT_DREMBOARD:
            [self runEditDremboard];
            break;
            
        case DELETE_DREMBOARD:
            [self runDeleteDremboard];
            break;
            
        case MERGE_DREMBOARD:
            [self runMergeDremboard];
            break;
            
        case ADD_DREM_TO_BOARD:
            [self runAddDremToBoard];
            break;
            
        case REMOVE_DREMS_FROM_BOARD:
            [self runRemoveDremsFromBoard];
            break;
            
        case MOVE_DREMS_TO_BOARD:
            [self runMoveDremsToBoard];
            break;

        case SET_FAVORITE:
            [self runSetFavorite];
            break;
            
        case SET_LIKE:
            [self runSetLike];
            break;
            
        case SET_FLAG:
            [self runSetFlag];
            break;
            
        case SET_FRIENDSHIP:
            [self runSetFriendship];
            break;
            
        case SET_FAMILYSHIP:
            [self runSetFamilyship];
            break;
            
        case SET_FOLLOW:
            [self runSetFollowing];
            break;
            
        case SET_BLOCK:
            [self runSetBlocking];
            break;
            
        case GET_NT:
            [self runGetNotifications];
            break;

        case SET_NT:
            [self runSetNotification];
            break;
            
        case GET_MESSAGES:
            [self runGetMessages];
            break;
            
        case SEND_MESSAGE:
            [self runSendMessage];
            break;
            
        case SET_SETTING_GENERAL:
            [self runSetSettingGeneral];
            break;
            
        case GET_SETTING_NOTE:
            [self runGetSettingNote];
            break;
            
        case SET_SETTING_NOTE:
            [self runSetSettingNote];
            break;
            
        case GET_SETTING_PRIVACY:
            [self runGetSettingPrivacy];
            break;
            
        case SET_SETTING_PRIVACY:
            [self runSetSettingPrivacy];
            break;
            
        default:
            break;
    }
}

- (void) execAPI:(NSString*)url Parameter:(NSMutableDictionary *)parameters {
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    [manager POST:url parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        NSLog(@"JSON: %@", responseObject);
        if (self.delegate && [self.delegate respondsToSelector:@selector(onDremApiResult:Paramerter:Result:)])
            [self.delegate onDremApiResult:m_apiType Paramerter:parameters Result:responseObject];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Error: %@", error);
        if (self.delegate && [self.delegate respondsToSelector:@selector(onDremApiResult:Paramerter:Result:)])
            [self.delegate onDremApiResult:m_apiType Paramerter:parameters Result:nil];
    }];
    
}

- (void) runGetCategory
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_INIT_PARAMS];
    
    GetCategoryParam *params = (GetCategoryParam *) mParamerter;
    
    NSDictionary *dic = @{};
    NSMutableDictionary *parameters = [dic mutableCopy];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    
    [self execAPI:url Parameter:parameters];
}


- (void) runSignIn
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_LOGIN];
    
    SignInParam *params = (SignInParam *) mParamerter;
    
    NSDictionary *dic = @{};
    NSMutableDictionary *parameters = [dic mutableCopy];
    [parameters setValue:params.username forKey:PARAM_USERNAME];
    [parameters setValue:params.password forKey:PARAM_PASSWORD];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runFacebookLogIn
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_LOGIN_FB];
    
    FBLogInParam *params = (FBLogInParam *) mParamerter;
    
    NSDictionary *dic = @{};
    NSMutableDictionary *parameters = [dic mutableCopy];
    [parameters setValue:params.fb_token forKey:PARAM_ACCESS_TOKEN];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runGoogleLogIn
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_LOGIN_GOOGLE];
    
    GoogleLogInParam *params = (GoogleLogInParam *) mParamerter;
    
    NSDictionary *dic = @{};
    NSMutableDictionary *parameters = [dic mutableCopy];
    [parameters setValue:params.google_token forKey:PARAM_ACCESS_TOKEN];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runTwitterLogIn
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_LOGIN_TWITTER];
    
    TwitterLogInParam *params = (TwitterLogInParam *) mParamerter;
    
    NSDictionary *dic = @{};
    NSMutableDictionary *parameters = [dic mutableCopy];
    [parameters setValue:params.consumer_key forKey:PARAM_CONSUMER_KEY];
    [parameters setValue:params.consumer_secret forKey:PARAM_CONSUMER_SECRET];
    [parameters setValue:params.access_token forKey:PARAM_ACCESS_TOKEN];
    [parameters setValue:params.access_token_secret forKey:PARAM_ACCESS_TOKEN_SECRET];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runGetDrems
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_DREMS];
    
    GetDremsParam *params = (GetDremsParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:params.search_str forKey:PARAM_SEARCH_STR];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.category] forKey:PARAM_CATEGORY];
    if (params.album_id != -1)
        [parameters setValue:[NSString stringWithFormat:@"%d", params.album_id] forKey:PARAM_ALBUM_ID];
    if (params.author_id != -1)
        [parameters setValue:[NSString stringWithFormat:@"%d", params.author_id] forKey:PARAM_AUTHOR_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.last_media_id] forKey:PARAM_LAST_MEDIA_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.per_page] forKey:PARAM_PER_PAGE];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetComment
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_COMMENT];
    
    SetCommentParam *params = (SetCommentParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.activity_id] forKey:PARAM_ACTIVITY_ID];
    [parameters setValue:params.comment forKey:PARAM_COMMENT];
    [parameters setValue:params.photo forKey:PARAM_PHOTO];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runGetSingleDremer
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_SINGLE_DREMER];
    
    GetSingleDremerParam *params = (GetSingleDremerParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    if (params.disp_user_id != -1)
        [parameters setValue:[NSString stringWithFormat:@"%d", params.disp_user_id] forKey:PARAM_DISP_USER_ID];
    
    [self execAPI:url Parameter:parameters];
}


- (void) runGetDremers
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_DREMERS];
    
    GetDremersParam *params = (GetDremersParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:params.search_str forKey:PARAM_SEARCH_STR];
    if (params.disp_user_id != -1)
        [parameters setValue:[NSString stringWithFormat:@"%d", params.disp_user_id] forKey:PARAM_DISP_USER_ID];
    [parameters setValue:params.type forKey:PARAM_TYPE];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.page] forKey:PARAM_PAGE];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.per_page] forKey:PARAM_PER_PAGE];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runGetActivities
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_ACTIVITIES];
    
    GetActivitiesParam *params = (GetActivitiesParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    if (params.disp_user_id != -1)
        [parameters setValue:[NSString stringWithFormat:@"%d", params.disp_user_id] forKey:PARAM_DISP_USER_ID];
    [parameters setValue:params.acitvity_scope forKey:PARAM_ACTIVITY_SCOPE];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.last_activity_id] forKey:PARAM_LAST_ACTIVITY_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.per_page] forKey:PARAM_PER_PAGE];

    [self execAPI:url Parameter:parameters];
}

- (void) runGetDremboards
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_DREMBOARDS];
    
    GetDremboardsParam *params = (GetDremboardsParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    if (params.author_id != -1)
        [parameters setValue:[NSString stringWithFormat:@"%d", params.author_id] forKey:PARAM_AUTHOR_ID];
    [parameters setValue:params.search_str forKey:PARAM_SEARCH_STR];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.category] forKey:PARAM_CATEGORY];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.last_media_id] forKey:PARAM_LAST_MEDIA_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.per_page] forKey:PARAM_PER_PAGE];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runCreateDremboard
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_CREATE_DREMBOARD];
    
    CreateDremboardParam *params = (CreateDremboardParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:params.title forKey:PARAM_TITLE];
    [parameters setValue:params.description forKey:PARAM_DESCRIPTION];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.category_id] forKey:PARAM_CATEGORY_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.privacy] forKey:PARAM_PRIVACY];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runEditDremboard
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_EDIT_DREMBOARD];
    
    EditDremboardParam *params = (EditDremboardParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.dremboard_id] forKey:PARAM_DREMBOARD_ID];
    [parameters setValue:params.title forKey:PARAM_TITLE];
    [parameters setValue:params.description forKey:PARAM_DESCRIPTION];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runDeleteDremboard
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_DELETE_DREMBOARD];
    
    DeleteDremboardParam *params = (DeleteDremboardParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.dremboard_id] forKey:PARAM_DREMBOARD_ID];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runMergeDremboard
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_MERGE_DREMBOARD];
    
    MergeDremboardParam *params = (MergeDremboardParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.source_id] forKey:PARAM_SOURCE_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.target_id] forKey:PARAM_TARGET_ID];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runAddDremToBoard
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_ADD_DREM_TO_BOARD];
    
    AddDremToDremboardParam *params = (AddDremToDremboardParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.drem_id] forKey:PARAM_DREM_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.dremboard_id] forKey:PARAM_DREMBOARD_ID];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runRemoveDremsFromBoard
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_REMOVE_DREMS_FROM_BOARD];
    
    RemoveDremsFromDremboardParam *params = (RemoveDremsFromDremboardParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:params.drem_ids forKey:PARAM_DREM_IDS];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runMoveDremsToBoard
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_MOVE_DREMS_FROM_BOARD];
    
    MoveDremsToDremboardParam *params = (MoveDremsToDremboardParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:params.drem_ids forKey:PARAM_DREM_IDS];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.dremboard_id] forKey:PARAM_DREMBOARD_ID];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetFavorite
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_FAVORITE];
    
    SetFavoriteParam *params = (SetFavoriteParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.activity_id] forKey:PARAM_ACTIVITY_ID];
    [parameters setValue:params.favorite_str forKey:PARAM_FAVORITE_STR];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.index] forKey:PARAM_ITEM_INDEX];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetLike
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_LIKE];
    
    SetLikeParam *params = (SetLikeParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.activity_id] forKey:PARAM_ACTIVITY_ID];
    [parameters setValue:params.like_str forKey:PARAM_LIKE_STR];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.index] forKey:PARAM_ITEM_INDEX];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetFlag
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_FLAG_DREM];
    
    SetFlagParam *params = (SetFlagParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.activity_id] forKey:PARAM_ACTIVITY_ID];
    [parameters setValue:params.flag_slug forKey:PARAM_FLAG_SLUG];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.index] forKey:PARAM_ITEM_INDEX];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetFriendship
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_FRIENDSHIP];
    
    SetFriendParam *params = (SetFriendParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.dremer_id] forKey:PARAM_DREMER_ID];
    [parameters setValue:params.action forKey:PARAM_ACTION];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.index] forKey:PARAM_ITEM_INDEX];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetFamilyship
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_FAMILYSHIP];
    
    SetFamilyParam *params = (SetFamilyParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.dremer_id] forKey:PARAM_DREMER_ID];
    [parameters setValue:params.action forKey:PARAM_ACTION];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.index] forKey:PARAM_ITEM_INDEX];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetFollowing
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_FOLLOWING];
    
    SetFollowingParam *params = (SetFollowingParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.dremer_id] forKey:PARAM_DREMER_ID];
    [parameters setValue:params.action forKey:PARAM_ACTION];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.index] forKey:PARAM_ITEM_INDEX];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetBlocking
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_BLOCKING];
    
    SetBlockingParam *params = (SetBlockingParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.dremer_id] forKey:PARAM_DREMER_ID];
    [parameters setValue:params.action forKey:PARAM_ACTION];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.block_type] forKey:PARAM_BLOCK_TYPE];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.index] forKey:PARAM_ITEM_INDEX];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runGetNotifications
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_NT];
    
    GetNTsParam *params = (GetNTsParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:params.type forKey:PARAM_TYPE];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.page] forKey:PARAM_PAGE];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.per_page] forKey:PARAM_PER_PAGE];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetNotification
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_NT];
    
    SetNTParam *params = (SetNTParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.notification_id] forKey:PARAM_NOTIFICATION_ID];
    [parameters setValue:params.action forKey:PARAM_ACTION];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.index] forKey:PARAM_ITEM_INDEX];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runGetMessages
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_MESSAGES];
    
    GetMessagesParam *params = (GetMessagesParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:params.type forKey:PARAM_TYPE];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.page] forKey:PARAM_PAGE];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.per_page] forKey:PARAM_PER_PAGE];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSendMessage
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SEND_MESSAGE];
    
    SendMessageParam *params = (SendMessageParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:params.recipients forKey:PARAM_RECIPIENTS];
    [parameters setValue:params.subject forKey:PARAM_SUBJECT];
    [parameters setValue:params.content forKey:PARAM_CONTENT];
    [parameters setValue:params.is_notice forKey:PARAM_IS_NOTICE];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetSettingGeneral
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_GENERAL];
    
    SetSettingGeneralParam *params = (SetSettingGeneralParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.disp_user_id] forKey:PARAM_DISP_USER_ID];
    [parameters setValue:params.email forKey:PARAM_EMAIL];
    [parameters setValue:params.password forKey:PARAM_PASSWORD];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runGetSettingNote
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_MAIL_NOTE];
    
    GetSettingNoteParam *params = (GetSettingNoteParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.disp_user_id] forKey:PARAM_DISP_USER_ID];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetSettingNote
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_MAIL_NOTE];
    
    SetSettingNoteParam *params = (SetSettingNoteParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.disp_user_id] forKey:PARAM_DISP_USER_ID];
    [parameters setValue:params.notifications forKey:PARAM_NOTE_JSON];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runGetSettingPrivacy
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_GET_DEF_PRIVACY];
    
    GetSettingPrivacyParam *params = (GetSettingPrivacyParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.disp_user_id] forKey:PARAM_DISP_USER_ID];
    
    [self execAPI:url Parameter:parameters];
}

- (void) runSetSettingPrivacy
{
    if (self == nil)
        return;
    
    NSString * url = [NSString stringWithFormat: @"%@", ACT_SET_DEF_PRIVACY];
    
    SetSettingPrivacyParam *params = (SetSettingPrivacyParam *) mParamerter;
    
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.user_id] forKey:PARAM_USER_ID];
    [parameters setValue:[NSString stringWithFormat:@"%d", params.disp_user_id] forKey:PARAM_DISP_USER_ID];
    if (params.privacy == 0)
        [parameters setValue:[NSString stringWithFormat:@""] forKey:PARAM_PRIVACY];
    else
        [parameters setValue:[NSString stringWithFormat:@"%d", params.privacy] forKey:PARAM_PRIVACY];
    
    [self execAPI:url Parameter:parameters];
}


@end
