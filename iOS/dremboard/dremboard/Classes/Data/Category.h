//
//  Category.h
//  dremboard
//
//  Created by YingLi on 5/31/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <Foundation/Foundation.h>

/* Category */
@interface CategoryItem : NSObject

@property (nonatomic) int category_id;
@property (retain, nonatomic) NSString * category_name;

- (instancetype)initWithAttributes:(int)categoryId Name:(NSString*)name;

@end

/* GetCategoryParam */
@interface GetCategoryParam : NSObject

@property (nonatomic) int user_id;

@end

/* GetCategoryData */
@interface GetCategoryData : NSObject
// category : {"keys": ,"values": }
@end

/* GetCategoryResult */
@interface GetCategoryResult : NSObject

@property (retain, nonatomic) NSString * status;
@property (retain, nonatomic) NSString * msg;
@property (retain, nonatomic) GetCategoryData * data;

@end
