//
//  ToastView.h
//  SMSRouter
//
//  Created by hana on 2/13/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MBProgressHUD.h"


@interface ToastView : NSObject{
    NSString *m_msgText;
    int m_duration;
    UIView *m_parent;
    MBProgressHUD *m_view;
}

- (id) init:(NSString*)msg durationTime:(int)time parent:(UIView*) parent;
- (void) show;
- (void) hide;
- (void) delay;

@end
