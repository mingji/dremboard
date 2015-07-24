//
//  ToastView.m
//  SMSRouter
//
//  Created by hana on 2/13/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ToastView.h"

@implementation  ToastView

- (id) init:(NSString*) msg durationTime:(int)time parent:(UIView *)parent
{
    if (self = [super init])
    {
        m_duration = time;
        m_parent = parent;
        m_msgText = msg;
        
        m_view = [[MBProgressHUD alloc] initWithView:parent];
        [parent addSubview:m_view];
        
        m_view.mode = MBProgressHUDModeText;
        m_view.margin = 7.f;
        m_view.yOffset = 100.f;
        //m_view.dimBackground = YES;
        
        // Regiser for HUD callbacks so we can remove it from the window at the right time
        //m_view.delegate = self;
        m_view.labelText = msg;
    }

    return self;
}

- (void) show
{
    [m_view showWhileExecuting:@selector(delay) onTarget:self withObject:nil animated:YES];
}

- (void) hide
{
    [m_view hide:YES];
}

- (void) delay
{
    sleep(m_duration);
}

//- (void) dealloc
//{
//    [m_view release];
//    [super dealloc];
//}

@end
