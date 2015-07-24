//
//  ActivityViewCellTableViewCell.m
//  dremboard
//
//  Created by YingLi on 5/4/15.
//  Copyright (c) 2015 dremboard. All rights reserved.
//

#import "ActivityViewCell.h"
#import "UIImageView+AFNetworking.h"
#import "NSString+Emoji.h"

static CGFloat MEDIA_DEFULAT_HEIGHT = 240.f;

@implementation ActivityViewCell
- (id)initWithStyle:(UITableViewCellStyle)style
    reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (!self) {
        return nil;
    }
    
    self.selectionStyle = UITableViewCellSelectionStyleGray;
    
    return self;
}

- (void)awakeFromNib {
    // Initialization code
    [self setClickListener];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void) setClickListener {
    
    // User Image
    UITapGestureRecognizer *imageTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickUserImg)];
    imageTap.numberOfTapsRequired = 1;
    imageTap.numberOfTouchesRequired = 1;
    [self.imgUser addGestureRecognizer:imageTap];
    [self.imgUser setUserInteractionEnabled:YES];
    
    // User Name
    UITapGestureRecognizer *nameTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onClickUserAction)];
    nameTap.numberOfTapsRequired = 1;
    nameTap.numberOfTouchesRequired = 1;
    [self.txtUsername addGestureRecognizer:nameTap];
    [self.txtUsername setUserInteractionEnabled:YES];
}

- (void) setIndex:(int)index {
    self.itemIndex = index;
}

- (void)setActivityInfo:(ActivityInfo *)activityInfo {
    _activityInfo = activityInfo;
  
    self.txtUsername.text = activityInfo.action;
    self.txtTime.text = [ActivityInfo convert2RelativeDate:activityInfo.last_modified];
    self.txtDescription.text = [activityInfo.description stringByReplacingEmojiCheatCodesWithUnicode];
    
    [self.imgUser setImageWithURL:[NSURL URLWithString:activityInfo.author_avatar] placeholderImage:[UIImage imageNamed:@"empty_man.png"]];
    self.imgUser.layer.cornerRadius = self.imgUser.frame.size.width / 2;
    self.imgUser.layer.masksToBounds = YES;
    
    [self.btnFavorite setTitle:activityInfo.favorite forState:UIControlStateNormal];
    [self.btnLike setTitle:activityInfo.like forState:UIControlStateNormal];
    
    int cntComment = (int)[activityInfo.comment_list count];
    if (cntComment > 0)
        [self.btnComment setTitle:[NSString stringWithFormat:@"Comment (%d)", cntComment] forState:UIControlStateNormal];
    else
        [self.btnComment setTitle:@"Comment" forState:UIControlStateNormal];;
    
    if ([activityInfo.media_list count] > 0)
    {
        for (int i = 0; i < [activityInfo.media_list count] ; i++)
        {
            MediaInfo *media = [activityInfo.media_list objectAtIndex:i];
            
            if (i == 0) {
                [self setMediaImage:self.imgMedia1 Url:media.media_guid];
            }
            else if (i == 1)
                [self setMediaImage:self.imgMedia2 Url:media.media_guid];
            
            if (i >= 1)
                break;
            
        }
    }
    
    [self calcCellViewHeight];
    
    self.viewUser.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.viewUser.layer.borderWidth = 1.0f;
    
    self.viewContent.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.viewContent.layer.borderWidth = 1.0f;
}

- (void) finishMediaImage {
    [self.delegate finishMediaImage:self.itemIndex];
}

- (void) setMediaImage:(UIImageView *)imgView Url:(NSString *)url {
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:url]];
    
    UIImage *cachedImg = [[UIImageView sharedImageCache] cachedImageForRequest:request];
    
    if (!cachedImg) {
        
        [imgView setImageWithURLRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]
                   placeholderImage:[UIImage imageNamed:@"image_thumb.png"]
                            success:^(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image) {
                                UIImage *scaledImage = [ActivityViewCell getScaledImage:image scaledToMaxWidth:287.f];
                                [self.imgMedia1 setImage:scaledImage];
                                [self finishMediaImage];
                            }
                            failure:^(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error) {
                                ;
                            }];
//        [imgView setImage:[UIImage imageNamed:@"image_thumb.png"]];
    } else {
        UIImage *scaledImage = [ActivityViewCell getScaledImage:cachedImg scaledToMaxWidth:287.f];
        [imgView setImage:scaledImage];
    }
}

- (CGFloat) calcCellViewHeight {
    CGFloat retHeight = 0;
    NSString *strTime = [ActivityInfo convert2RelativeDate:self.activityInfo.last_modified];
    
    // Calc User Layout
    CGFloat heightName = [ActivityViewCell detailTextHeight:self.activityInfo.action Width:215.f FontSize:16.f];
    CGFloat heightTime = [ActivityViewCell detailTextHeight:strTime Width:220.f FontSize:13.f];
    
    self.heightUserName.constant = heightName + 5.f;
    self.heightTime.constant = heightTime;
    
    CGFloat heightImgUser = 60.f;
    CGFloat heightTxts = heightName + heightTime + 8.f/*top margin*/;
    
    if (heightImgUser > heightTxts)
        self.heightUserLay.constant = heightImgUser + 15.f/*bottom margin*/;
    else
        self.heightUserLay.constant = heightTxts + 15.f/*bottom margin*/;
    
    // Cacl Content Layout
    CGFloat heightDesc = [ActivityViewCell detailTextHeight:self.activityInfo.description Width:285.f FontSize:14.f];
    heightDesc += 20;
    if ([self.activityInfo.description length] == 0)
        heightDesc = 0;
    else if (heightDesc < 25.f)
        heightDesc = 25.f;
    self.heightDes.constant = heightDesc;
    
    if ([_activityInfo.media_list count] > 0)
    {
        if ([_activityInfo.media_list count] == 1) {
            self.imgMedia1.hidden = NO;
            self.imgMedia2.hidden = YES;
            
            self.heightImg1.constant = self.imgMedia1.image.size.height;
            self.heightImg2.constant = 0.f;
        } else if ([_activityInfo.media_list count] > 1) {
            self.imgMedia1.hidden = NO;
            self.imgMedia2.hidden = NO;
            
            self.heightImg1.constant = self.imgMedia1.image.size.height;
            self.heightImg2.constant = self.imgMedia2.image.size.height;
        }
    } else {
        self.imgMedia1.hidden = YES;
        self.imgMedia2.hidden = YES;
        
        self.heightImg1.constant = 0.f;
        self.heightImg2.constant = 0.f;
    }
    
    self.heightContentLay.constant = heightDesc + self.heightImg1.constant + self.heightImg2.constant + 25.f/*middle margin*/ + 45.f/*button lay*/ + 5.f/*bottom margin*/;
    
    retHeight = self.heightUserLay.constant + self.heightContentLay.constant + 10.f/*margin*/;
    
    return retHeight;
}

+ (UIImage *)getScaledImage:(UIImage *)image scaledToMaxWidth:(CGFloat)width {
    CGFloat oldWidth = image.size.width;
    CGFloat oldHeight = image.size.height;
    
    CGFloat scaleFactor=1;
    
    if (oldWidth > width) {
        scaleFactor = width / oldWidth;
    } else  // if oldWidth<width and height==0 then return not scaled image
        return image;
    
    CGFloat newHeight = oldHeight * scaleFactor;
    CGFloat newWidth = oldWidth * scaleFactor;
    CGSize newSize = CGSizeMake(newWidth, newHeight);
    
    UIGraphicsBeginImageContext(newSize);
    [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return newImage;
    
}

+ (CGFloat) heightForCellWithActivity:(ActivityInfo *)activityInfo {
    CGFloat retHeight = 0;
    NSString *strTime = [ActivityInfo convert2RelativeDate:activityInfo.last_modified];
    
    // Calc User Layout
    CGFloat heightName = [self detailTextHeight:activityInfo.action Width:215.f FontSize:16.f];
    CGFloat heightTime = [self detailTextHeight:strTime Width:220.f FontSize:13.f];
    
    CGFloat heightImgUser = 60.f;
    CGFloat heightTxts = heightName + heightTime + 8.f/*top margin*/;
    
    CGFloat heightUserLay;
    if (heightImgUser > heightTxts)
        heightUserLay = heightImgUser + 15.f/*bottom margin*/;
    else
        heightUserLay = heightTxts + 15.f/*bottom margin*/;
    
    // Cacl Content Layout
    CGFloat heightDesc = [self detailTextHeight:activityInfo.description Width:285.f FontSize:14.f];
    heightDesc += 20;
    if ([activityInfo.description length] == 0)
        heightDesc = 0;
    else if (heightDesc < 25.f)
        heightDesc = 25.f;
    
    CGFloat heightImg = 0.f;
    for (int i = 0; i < [activityInfo.media_list count]; i++) {
        if (i > 1)
            break;
        
        MediaInfo *media = [activityInfo.media_list objectAtIndex:i];
        heightImg += [self getMediaImageHeight:media];
    }    
   
    CGFloat heightContentLay = heightDesc + heightImg + 25.f/*middle margin*/ + 45.f/*button lay*/ + 5.f/*bottom margin*/;
    
    retHeight = heightUserLay + heightContentLay + 20.f/*margin*/;
    
    return retHeight;
}

+ (CGFloat) getMediaImageHeight:(MediaInfo *)media {
    
    CGFloat retHeight;
    
    UIImage *cachedImage = [[UIImageView sharedImageCache] cachedImageForRequest:
                            [NSURLRequest requestWithURL:[NSURL URLWithString:media.media_guid]]];
    if (cachedImage) {
        UIImage *scacledImg = [self getScaledImage:cachedImage scaledToMaxWidth:287.f];
        retHeight = scacledImg.size.height;
    } else
        retHeight = MEDIA_DEFULAT_HEIGHT;
    
    return retHeight;
}

+ (CGFloat) detailTextHeight:(NSString *)text Width:(CGFloat)width FontSize:(CGFloat)size{
    CGRect rectToFit = [text boundingRectWithSize:CGSizeMake(width, CGFLOAT_MAX) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: [UIFont systemFontOfSize:size]} context:nil];
    return rectToFit.size.height;
}

#pragma mark - UIView

- (void)layoutSubviews {
    [super layoutSubviews];
}

- (void) onClickUserImg {
    [self.delegate clickUser:self.itemIndex];
}

- (void) onClickUserAction {
    [self.delegate clickUser:self.itemIndex];
}

- (IBAction)onClikeLike:(id)sender {
    [self.delegate setLike:self.itemIndex];
}

- (IBAction)onClickFavorite:(id)sender {
    [self.delegate setFavorite:self.itemIndex];
}

- (IBAction)onClickShare:(id)sender {
    [self.delegate clickShare:self.itemIndex];
}

- (IBAction)onClickFlag:(id)sender {
    [self.delegate clickFlag:self.itemIndex];
}

- (IBAction)onClickComment:(id)sender {
    [self.delegate clickComment:self.itemIndex];
}

@end
