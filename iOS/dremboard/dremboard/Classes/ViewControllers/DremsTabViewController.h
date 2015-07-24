//
//  DremsTabViewController.h
//  dremboard
//
//  Created by YingLi on 4/28/15.
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
#import "M13InfiniteTabBarController.h"
#import "DownPicker.h"

@interface DremsTabViewController : UIViewController <HttpTaskDelegate, UICollectionViewDataSource, UISearchBarDelegate, MBProgressHUDDelegate, DremViewCellDelegate, FlagViewDelegate, DremSingleViewDelegate>
{
    HttpTask *mHttpTask;

    ToastView *mInfoDlg;
    MBProgressHUD *mWaitingDlg;
    UIRefreshControl *mRefreshControl;
    
    NSMutableArray *mArrayCategories;

    int mCategoryId;
    NSString *mStrSearch;
    int m_lastMediaId;
    int m_userId;
}

@property (strong, nonatomic) DownPicker *mPickerCategory;

@property (strong, nonatomic) M13InfiniteTabBarController *mainVC;

@property (strong, nonatomic) IBOutlet UITextField *mTxtCategory;
@property (weak, nonatomic) IBOutlet UISearchBar *mSearchBar;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionDremView;
@property (readwrite, nonatomic, strong) NSMutableArray *mArrayDrem;

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSObject *)param Result:(NSObject *)result;

@end
