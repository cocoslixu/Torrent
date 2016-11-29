//
//  XYSDKDelegate.h
//  xxz
//
//  Created by  on 15/6/23.
//
//

#ifndef __xxz__XYSDKDelegate__
#define __xxz__XYSDKDelegate__

#include <stdio.h>


#import <UIKit/UIKit.h>


@interface MPDelegate :NSObject
{
    UIViewController* m_pView;
}

+(id)SharedInstance;

-(void) initDelegate:(UIViewController*)pView;

@property(nonatomic, readonly) UIViewController* m_pView;

@end

#endif /* defined(__xxz__XYSDKDelegate__) */

