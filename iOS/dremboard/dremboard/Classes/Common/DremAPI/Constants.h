//
//  Constants.h
//  dremboard
//
//  Created by YingLi on 4/27/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#ifndef dremboard_Constants_h
#define dremboard_Constants_h

#define HTTP_HOME @"http://dremboard.com"
//#define HTTP_HOME @"http://10.70.126.4:8080/"
#define HTTP_BASIC_PARAM [HTTP_HOME stringByAppendingString:@"/api/rest/"]

/* Action List */
#define ACT_LOGIN               [HTTP_BASIC_PARAM stringByAppendingString:@"user_login"]
#define ACT_GET_INIT_PARAMS     [HTTP_BASIC_PARAM stringByAppendingString:@"get_init_params"]
#define ACT_GET_DREMS           [HTTP_BASIC_PARAM stringByAppendingString:@"get_drems"]
#define ACT_SET_COMMENT         [HTTP_BASIC_PARAM stringByAppendingString:@"set_activity_comment"]
#define ACT_SET_FAVORITE        [HTTP_BASIC_PARAM stringByAppendingString:@"set_favorite"]
#define ACT_SET_LIKE            [HTTP_BASIC_PARAM stringByAppendingString:@"set_like"]
#define ACT_SHARE_DREM          [HTTP_BASIC_PARAM stringByAppendingString:@"share_drem"]
//#define ACT_SET_FLAG            [HTTP_BASIC_PARAM stringByAppendingString:@"set_flag"]
#define ACT_FLAG_DREM           [HTTP_BASIC_PARAM stringByAppendingString:@"flag_drem"]
#define ACT_USER_REGISTER       [HTTP_BASIC_PARAM stringByAppendingString:@"user_register"]
#define ACT_RETRIEVE_PASSWORD   [HTTP_BASIC_PARAM stringByAppendingString:@"retrieve_password"]
#define ACT_GET_ACTIVITIES      [HTTP_BASIC_PARAM stringByAppendingString:@"get_activities"]
#define ACT_GET_DREMERS         [HTTP_BASIC_PARAM stringByAppendingString:@"get_dremers"]
#define ACT_GET_SINGLE_DREMER   [HTTP_BASIC_PARAM stringByAppendingString:@"get_single_dremer"]
#define ACT_GET_DREMBOARDS      [HTTP_BASIC_PARAM stringByAppendingString:@"get_dremboards"]
#define ACT_GET_MEMORIES        [HTTP_BASIC_PARAM stringByAppendingString:@"get_memories"]
#define ACT_SET_FRIENDSHIP      [HTTP_BASIC_PARAM stringByAppendingString:@"change_dremer_friendship"]
#define ACT_SET_FAMILYSHIP      [HTTP_BASIC_PARAM stringByAppendingString:@"change_dremer_familyship"]
#define ACT_SET_FOLLOWING       [HTTP_BASIC_PARAM stringByAppendingString:@"change_dremer_following"]
#define ACT_SET_BLOCKING        [HTTP_BASIC_PARAM stringByAppendingString:@"change_dremer_blocking"]
#define ACT_GET_NT              [HTTP_BASIC_PARAM stringByAppendingString:@"get_notifications"]
#define ACT_SET_NT              [HTTP_BASIC_PARAM stringByAppendingString:@"set_notification_action"]
#define ACT_GET_MESSAGES        [HTTP_BASIC_PARAM stringByAppendingString:@"get_messages"]
#define ACT_GET_SINGLE_MESSAGE  [HTTP_BASIC_PARAM stringByAppendingString:@"get_message_single_view"]
#define ACT_SEND_MESSAGE        [HTTP_BASIC_PARAM stringByAppendingString:@"message_compose"]
#define ACT_REPLY_MESSAGE       [HTTP_BASIC_PARAM stringByAppendingString:@"message_reply"]
#define ACT_SET_GENERAL         [HTTP_BASIC_PARAM stringByAppendingString:@"set_single_dremer_general"]
#define ACT_GET_MAIL_NOTE       [HTTP_BASIC_PARAM stringByAppendingString:@"get_single_dremer_email_note"]
#define ACT_SET_MAIL_NOTE       [HTTP_BASIC_PARAM stringByAppendingString:@"set_single_dremer_email_note"]
#define ACT_GET_DEF_PRIVACY     [HTTP_BASIC_PARAM stringByAppendingString:@"get_single_dremer_default_privacy"]
#define ACT_SET_DEF_PRIVACY     [HTTP_BASIC_PARAM stringByAppendingString:@"set_single_dremer_default_privacy"]

#define ACT_CREATE_DREMBOARD            [HTTP_BASIC_PARAM stringByAppendingString:@"create_dremboard"]
#define ACT_EDIT_DREMBOARD              [HTTP_BASIC_PARAM stringByAppendingString:@"edit_dremboard"]
#define ACT_DELETE_DREMBOARD            [HTTP_BASIC_PARAM stringByAppendingString:@"delete_dremboard"]
#define ACT_MERGE_DREMBOARD             [HTTP_BASIC_PARAM stringByAppendingString:@"merge_dremboard"]
#define ACT_ADD_DREM_TO_BOARD           [HTTP_BASIC_PARAM stringByAppendingString:@"add_drem_to_dremboard"]
#define ACT_REMOVE_DREMS_FROM_BOARD     [HTTP_BASIC_PARAM stringByAppendingString:@"remove_drems_from_dremboard"]
#define ACT_MOVE_DREMS_FROM_BOARD       [HTTP_BASIC_PARAM stringByAppendingString:@"move_drems_to_dremboard"]


/* SNS Login Action */
#define HTTP_USER_PARAM         [HTTP_HOME stringByAppendingString:@"/api/user/"]

#define ACT_LOGIN_FB            [HTTP_USER_PARAM stringByAppendingString:@"fb_connect"]
#define ACT_LOGIN_GOOGLE        [HTTP_USER_PARAM stringByAppendingString:@"google_connect"]
#define ACT_LOGIN_TWITTER       [HTTP_USER_PARAM stringByAppendingString:@"twitter_connect"]


/* Parameter List */
#define PARAM_USERNAME          @"username"
#define PARAM_PASSWORD          @"password"
#define PARAM_USER_LOGIN        @"user_login"
#define PARAM_USER_ID           @"user_id"
#define PARAM_DISP_USER_ID      @"disp_user_id"
#define PARAM_DREMER_ID         @"dremer_id"
#define PARAM_ALBUM_ID          @"album_id"
#define PARAM_AUTHOR_ID         @"author_id"
#define PARAM_CATEGORY          @"category"
#define PARAM_COMMENT           @"comment"
#define PARAM_SEARCH_STR        @"search_str"
#define PARAM_LAST_MEDIA_ID     @"last_media_id"
#define PARAM_LAST_DREMER_ID    @"last_dremer_id"
#define PARAM_PAGE              @"page"
#define PARAM_PER_PAGE          @"per_page"
#define PARAM_ACTIVITY_ID       @"activity_id"
#define PARAM_RTMEDIA_ID        @"rtmedia_id"
#define PARAM_FAVORITE_STR      @"favorite_str"
#define PARAM_LIKE_STR          @"like_str"
#define PARAM_DESCRIPTION       @"description"
#define PARAM_SHARE_USER        @"share_user"
#define PARAM_SHARE_MODE        @"share_mode"
#define PARAM_FLAG_SLUG         @"flag_slug"
#define PARAM_ACTIVITY_SCOPE    @"activity_scope"
#define PARAM_LAST_ACTIVITY_ID  @"last_activity_id"
#define PARAM_PHOTO             @"photo"
#define PARAM_ACTION            @"action"
#define PARAM_BLOCK_TYPE        @"block_type"
#define PARAM_TYPE              @"type"
#define PARAM_NOTIFICATION_ID   @"notification_id"
#define PARAM_SUBJECT           @"subject"
#define PARAM_CONTENT           @"content"
#define PARAM_RECIPIENTS        @"recipients"
#define PARAM_IS_NOTICE         @"is_notice"
#define PARAM_MESSAGE_ID        @"message_id"
#define PARAM_EMAIL             @"email"
#define PARAM_PRIVACY           @"privacy"
#define PARAM_NOTE_JSON         @"notifications_json"
#define PARAM_ACCESS_TOKEN      @"access_token"
#define PARAM_ACCESS_TOKEN_SECRET   @"access_token_secret"
#define PARAM_CONSUMER_KEY      @"consumer_key"
#define PARAM_CONSUMER_SECRET   @"consumer_secret"
#define PARAM_TITLE             @"title"
#define PARAM_DESCRIPTION       @"description"
#define PARAM_CATEGORY_ID       @"category_id"
#define PARAM_DREMBOARD_ID      @"dremboard_id"
#define PARAM_DREM_ID           @"drem_id"
#define PARAM_SOURCE_ID         @"source_id"
#define PARAM_TARGET_ID         @"target_id"
#define PARAM_DREM_IDS          @"drem_ids"

#define PARAM_ITEM_INDEX        @"item_index"

/* Activity Scope */
#define SCOPE_PERSONAL          @"just-me"
#define SCOPE_MENTIONS          @"mentions"
#define SCOPE_FOLLOWING         @"following"
#define SCOPE_FAVORITES         @"favorites"
#define SCOPE_FRIENDS           @"friends"
#define SCOPE_GROUPS            @"groups"

/* Dremer List Type */
#define TYPE_ACTIVE             @"active"
#define TYPE_FOLLOWING          @"following"
#define TYPE_FOLLOWER           @"follower"
#define TYPE_FRIEND             @"friends"
#define TYPE_FRIEND_REQUEST     @"friendship_request"
#define TYPE_FAMILY             @"family"
#define TYPE_FAMILY_REQUEST     @"familyship_request"

/* Notificaion, Message Type */
#define TYPE_READ               @"read"
#define TYPE_UNREAD             @"unread"
#define TYPE_INBOX              @"inbox"
#define TYPE_SENTBOX            @"sentbox"

#endif
