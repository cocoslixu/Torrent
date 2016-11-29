//
//  TBSDKDelegate.h
//  xxz
//
//  Created by daily-play on 15/8/26.
//
//

#ifndef __xxz__TBSDKDelegate__
#define __xxz__TBSDKDelegate__

#import <UIKit/UIKit.h>

@interface TBDelegate :NSObject
{
          UIViewController* m_pView;
}
+(id)SharedInstance;

//-(void)InitDeleage;

-(void)InitDeleage:(UIViewController*)pView;

@property(nonatomic, readonly) UIViewController* m_pView;

@end

#endif /* defined(__xxz__TBSDKDelegate__) */
