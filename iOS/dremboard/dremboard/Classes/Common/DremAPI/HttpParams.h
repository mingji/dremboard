//
//  HttpParams.h
//  online-adviser
//
//  Created by vitaly on 3/30/14.
//  Copyright (c) 2014 AltairJang. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HttpParams : NSObject{
    
	NSMutableArray *mParts;
    
}

- (NSData *)getParams;
- (void)addParam:(NSString *)field  VALUE:(NSString *)value;
@end
