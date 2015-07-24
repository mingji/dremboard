//
//  Category.m
//  dremboard
//
//  Created by YingLi on 5/31/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "Category.h"

/* Category */
@implementation CategoryItem

@synthesize category_id;
@synthesize category_name;

- (instancetype)initWithAttributes:(int)categoryId Name:(NSString *)name {
    self = [super init];
    if (!self) {
        return nil;
    }
    
    self.category_id = categoryId;
    self.category_name = name;
    
    return self;
}

@end

/* GetCategoryParam */
@implementation GetCategoryParam

@synthesize user_id;

@end

/* GetCategoryData */
@implementation GetCategoryData
// category : {"keys": ,"values": }
@end

/* GetCategoryResult */
@implementation GetCategoryResult

@synthesize status;
@synthesize msg;
@synthesize data;

@end
