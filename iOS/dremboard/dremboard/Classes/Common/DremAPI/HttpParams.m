//
//  HttpParams.m
//  online-adviser
//
//  Created by vitaly on 3/30/14.
//  Copyright (c) 2014 AltairJang. All rights reserved.
//

#import "HttpParams.h"

@implementation HttpParams
- (id)init {
	self = [super init];
	if (self) {
		mParts = [[NSMutableArray alloc] init];
	}
	return self;
}

- (NSData *)getParams {
	if ([mParts count] == 0) {
		return nil;
	}
	return [[mParts componentsJoinedByString:@"&"] dataUsingEncoding:NSUTF8StringEncoding];
}

- (void)addParam:(NSString *)field VALUE:(NSString *)value {
	NSString *part = [NSString stringWithFormat:@"%@=%@", [field stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding],
					  [value stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
	[mParts addObject:part];
}
@end