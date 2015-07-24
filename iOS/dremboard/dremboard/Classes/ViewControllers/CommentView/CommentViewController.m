//
//  CommentViewController.m
//  dremboard
//
//  Created by YingLi on 6/4/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "CommentViewController.h"
#import "AppDelegate.h"
#import "NSString+Emoji.h"

@interface CommentViewController ()

@end

int _movementDistance = 0;
UIGestureRecognizer *tapper;

@implementation CommentViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view.
    [self.mTxtComment setDelegate:self];
    [self.mTableCommentView setDataSource:self];
    [self.mTableCommentView setDelegate:self];
    [self registerKeyboardNotification];
    
    mUserId = (int)[[NSUserDefaults standardUserDefaults] integerForKey:@"logined_user_id"];
    
    [self.mTableCommentView reloadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setAttributes:(int)activityId Comments:(NSMutableArray *)arrayComment {
//    self.mArrayComment = [[NSMutableArray alloc] initWithArray:arrayComment];
    self.mArrayComment = arrayComment;
    
    mActivityId = activityId;
}

- (void) registerKeyboardNotification {
    // register for keyboard notifications
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillShow:)
                                                 name:UIKeyboardWillShowNotification
                                               object:self.view.window];
    // register for keyboard notifications
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillHide:)
                                                 name:UIKeyboardWillHideNotification
                                               object:self.view.window];

    // Handle Kayboard
    tapper = [[UITapGestureRecognizer alloc]
              initWithTarget:self action:@selector(handleSingleTap:)];
    tapper.cancelsTouchesInView = NO;
    [self.view addGestureRecognizer:tapper];
}

- (void)handleSingleTap:(UITapGestureRecognizer *) sender
{
    [self.view endEditing:YES];
}

- (void)keyboardWillHide:(NSNotification *)n
{
}

- (void)keyboardWillShow:(NSNotification *)n
{
    NSDictionary* userInfo = [n userInfo];
    
    // get the size of the keyboard
    CGSize keyboardSize = [[userInfo objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    _movementDistance = keyboardSize.height;
}

- (IBAction)onClickBack:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)onClickPost:(id)sender {
    NSString *strComment = [[self.mTxtComment text] stringByReplacingEmojiUnicodeWithCheatCodes];
    
    [self setComment:strComment];
    
//    NSData *data1 = [strComment dataUsingEncoding:NSUTF8StringEncoding];
//    NSString *strData1 = [[NSString alloc] initWithData:data1 encoding:NSNonLossyASCIIStringEncoding];
//
//    NSData *data2 = [strComment dataUsingEncoding:NSUTF32BigEndianStringEncoding];
//    NSString *strData2 = [[NSString alloc] initWithData:data2 encoding:NSUTF32BigEndianStringEncoding];
//
//    NSData *data3 = [strComment dataUsingEncoding:NSUTF16BigEndianStringEncoding];
//    NSString *strData3 = [[NSString alloc] initWithData:data2 encoding:NSUTF16BigEndianStringEncoding];

}


- (void) animateTextField: (BOOL) up
{
    const float movementDuration = 0.3f; // tweak as needed
    
    int movement;
    
    movement = up ? -_movementDistance : _movementDistance;
    
    [UIView beginAnimations: @"anim" context: nil];
    [UIView setAnimationBeginsFromCurrentState: YES];
    [UIView setAnimationDuration: movementDuration];
    
    // resize table view's height
    CGRect frameTable = self.mTableCommentView.frame;
    frameTable.size.height += movement;
    self.mTableCommentView.frame = frameTable;
    
    // move up/down the post view
    self.mViewPost.frame = CGRectOffset(self.mViewPost.frame, 0, movement);
    
    [UIView commitAnimations];
}

#pragma mark - HttpTaskDelegate

- (void) setComment:(NSString*)comment
{
    if(mWaitingDlg != nil){
        mWaitingDlg = nil;
    }
    
    mWaitingDlg = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:mWaitingDlg];
    
    mWaitingDlg.dimBackground = YES;
    
    // Regiser for HUD callbacks so we can remove it from the window at the right time
    mWaitingDlg.delegate = self;
    mWaitingDlg.labelText = @"Waiting ...";
    
    [mWaitingDlg show:YES];
    
    SetCommentParam *param = [[SetCommentParam alloc] init];
    param.user_id = mUserId;
    param.activity_id = mActivityId;
    param.comment = comment;
    param.photo = @"tmp.png";
    
    mHttpTask = [[HttpTask alloc] initWithParam:(HttpAPIType)SET_COMMENT Parameter:param];
    mHttpTask.delegate = self;
    [mHttpTask runTask];
    
}

- (void) onSetCommentResult:(NSDictionary*) param Result:(NSArray *) result
{
    if (result == nil)
        return;
    
    NSString *status = [result valueForKeyPath:@"status"];
    NSString *msg = [result valueForKeyPath:@"msg"];
    
    if (![status isEqualToString:@"ok"])
    {
        mInfoDlg = [[ToastView alloc] init:msg durationTime:1 parent:(UIView *)self.view];
        [mInfoDlg show];
        return;
    }
    
    SetCommentData *data = [result valueForKeyPath:@"data"];
    NSDictionary *attributes = [data valueForKeyPath:@"comment"];
    CommentInfo *comment = [[CommentInfo alloc] initWithAttributes:attributes];
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    [comment setAuthor_name:app.currentDremer.display_name];
    
    [_mArrayComment addObject:comment];
    [_mTableCommentView reloadData];
    
    [self.mTxtComment setText:@""];
}

- (void) onDremApiResult:(HttpAPIType)type Paramerter:(NSDictionary *)param Result:(NSObject *)result
{
    if (mWaitingDlg != nil)
    {
        [mWaitingDlg hide:YES];
    }
    
    switch (type) {
        case SET_COMMENT:
            [self onSetCommentResult:(NSDictionary*)param Result:(NSArray*)result];
            break;
        default:
            break;
    }
}

#pragma mark - UITextViewDelegate
- (void)textViewDidBeginEditing:(UITextView *)textView {
    [self animateTextField:YES];
}

- (void)textViewDidEndEditing:(UITextView *)textView {
    [self animateTextField:NO];
}

- (void)textViewDidChange:(UITextView *)textView {
    NSString *strText = [textView text];
    
    // set Post Button enable/disable
    if ([strText length] == 0)
        [self.mBtnPost setEnabled:NO];
    else
        [self.mBtnPost setEnabled:YES];
}

#pragma mark - UITableViewDataSource
- (NSInteger)tableView:(__unused UITableView *)tableView
 numberOfRowsInSection:(__unused NSInteger)section
{
    return (NSInteger)[_mArrayComment count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"CommentViewCell";
    
    CommentViewCell *cell = [_mTableCommentView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (!cell) {
        cell = [[CommentViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
    }
    
    CommentInfo *comment = [_mArrayComment objectAtIndex:[indexPath row]];
    [cell setIndex:[indexPath row]];
    cell.commentInfo = comment;
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return [self heightForBasicCellAtIndexPath:indexPath];
}

- (CGFloat)heightForBasicCellAtIndexPath:(NSIndexPath *)indexPath {
    
    CommentInfo *comment = [_mArrayComment objectAtIndex:[indexPath row]];
    return [CommentViewCell heightForCellWithComment:comment];
}

@end
