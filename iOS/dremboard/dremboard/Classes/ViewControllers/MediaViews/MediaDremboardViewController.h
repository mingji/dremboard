//
//  MediaDremboardViewController.h
//  dremboard
//
//  Created by YingLi on 5/23/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "DownPicker.h"
#import "DremboardViewCell.h"
#import "DremboardDetailVC.h"

@interface MediaDremboardViewController : UIViewController <HttpTaskDelegate, UISearchBarDelegate, UICollectionViewDataSource, MBProgressHUDDelegate, DremerboardCellDelegate, DremboardDetailDelegate>
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
@property (weak, nonatomic) IBOutlet UICollectionView *mCollectionDremboardView;
@property (readwrite, nonatomic, strong) NSMutableArray *mArrayDremboard;

- (void) setAttributes:(int)authorId MainVC:(UIViewController *)mainVC;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
