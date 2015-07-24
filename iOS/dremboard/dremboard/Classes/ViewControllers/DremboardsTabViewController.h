//
//  DremboardsTabViewController.h
//  dremboard
//
//  Created by YingLi on 5/8/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpTask.h"
#import "ToastView.h"
#import "MBProgressHUD.h"
#import "M13InfiniteTabBarController.h"
#import "DownPicker.h"
#import "DremboardViewCell.h"
#import "DremboardDetailVC.h"

@interface DremboardsTabViewController : UIViewController <HttpTaskDelegate, UISearchBarDelegate, UICollectionViewDataSource, MBProgressHUDDelegate, DremerboardCellDelegate, DremboardDetailDelegate>
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
}

@property (strong, nonatomic) DownPicker *mPickerCategory;

@property (strong, nonatomic) M13InfiniteTabBarController *mainVC;

@property (strong, nonatomic) IBOutlet UISearchBar *mSearchBar;
@property (strong, nonatomic) IBOutlet UITextField *mTxtCategory;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionDremboardView;
@property (readwrite, nonatomic, strong) NSMutableArray *mArrayDremboard;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
