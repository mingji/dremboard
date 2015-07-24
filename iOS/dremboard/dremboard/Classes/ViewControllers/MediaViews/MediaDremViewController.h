//
//  MediaDremViewController.h
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "DremCell.h"
#import "FlagViewController.h"
#import "ShareViewController.h"
#import "DremSingleViewController.h"
#import "DownPicker.h"

@interface MediaDremViewController : UIViewController <HttpTaskDelegate, UICollectionViewDataSource, UISearchBarDelegate, MBProgressHUDDelegate, DremViewCellDelegate, FlagViewDelegate, DremSingleViewDelegate>
{
    HttpTask *mHttpTask;
    
    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    UIRefreshControl *mRefreshControl;
    
    NSMutableArray *mArrayCategories;
    
    int mCategoryId;
    NSString *mStrSearch;
    int mLastMediaId;
    int mUserId;
    int mAuthorId;
}

@property (strong, nonatomic) DownPicker *mPickerCategory;

@property(nonatomic, assign) UIViewController *mainVC;

@property (strong, nonatomic) IBOutlet UITextField *mTxtCategory;
@property (strong, nonatomic) IBOutlet UISearchBar *mSearchBar;
@property (weak, nonatomic) IBOutlet UICollectionView *mCollectionDremView;
@property (readwrite, nonatomic, strong) NSMutableArray *mArrayDrem;

- (void) setAttributes:(int)authorId MainVC:(UIViewController *)mainVC;
- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
