//
//  GameUUID.m
//  diamond
//
//  Created by ff on 15/9/6.
//
//

#import "GameUUID.h"
#import <Security/Security.h>

#define DIC_KEY                        @"dic"
#define GUID_KEY                       @"GUID"

@implementation KeychainHelper

+ (NSMutableDictionary *)getKeychainQuery:(NSString *)service
{
    return [NSMutableDictionary dictionaryWithObjectsAndKeys:
            (id)kSecClassGenericPassword,(id)kSecClass,
            service, (id)kSecAttrService,
            service, (id)kSecAttrAccount,
            (id)kSecAttrAccessibleAfterFirstUnlock,(id)kSecAttrAccessible,
            nil];
}

+ (void)save:(NSString *)service data:(id)data
{
    //Get search dictionary
    NSMutableDictionary *keychainQuery = [self getKeychainQuery:service];
    //Delete old item before add new item
    SecItemDelete((CFDictionaryRef)keychainQuery);
    //Add new object to search dictionary(Attention:the data format)
    [keychainQuery setObject:[NSKeyedArchiver archivedDataWithRootObject:data] forKey:(id)kSecValueData];
    //Add item to keychain with the search dictionary
    SecItemAdd((CFDictionaryRef)keychainQuery, NULL);
}

+ (id)load:(NSString *)service
{
    id ret = nil;
    NSMutableDictionary *keychainQuery = [self getKeychainQuery:service];
    //Configure the search setting
    //Since in our simple case we are expecting only a single attribute to be returned (the password) we can set the attribute kSecReturnData to kCFBooleanTrue
    [keychainQuery setObject:(id)kCFBooleanTrue forKey:(id)kSecReturnData];
    [keychainQuery setObject:(id)kSecMatchLimitOne forKey:(id)kSecMatchLimit];
    CFDataRef keyData = NULL;
    if (SecItemCopyMatching((CFDictionaryRef)keychainQuery, (CFTypeRef *)&keyData) == noErr) {
        @try {
            ret = [NSKeyedUnarchiver unarchiveObjectWithData:(NSData *)keyData];
        } @catch (NSException *e) {
            NSLog(@"Unarchive of %@ failed: %@", service, e);
        } @finally {
        }
    }
    if (keyData)
        CFRelease(keyData);
    return ret;
}

+ (void)deleteData:(NSString *)service
{
    NSMutableDictionary *keychainQuery = [self getKeychainQuery:service];
    SecItemDelete((CFDictionaryRef)keychainQuery);
}

@end



@implementation GameUUID

+ (NSString *)packageNameString
{
    return [[NSBundle mainBundle] objectForInfoDictionaryKey:(NSString *)kCFBundleIdentifierKey];
}

+ (id)userDefaultValueForKey:(NSString *)key
{
    NSString *key_dic = [NSString stringWithFormat:@"%@.%@", [GameUUID packageNameString], DIC_KEY];
    NSMutableDictionary *dic = (NSMutableDictionary *)[KeychainHelper load:key_dic];
    return [dic objectForKey:key];
}

+ (void)saveUserDefaultValue:(id)value forKey:(NSString *)key
{
    NSString *key_dic = [NSString stringWithFormat:@"%@.%@", [GameUUID packageNameString], DIC_KEY];
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    [dic setObject:value forKey:key];
    [KeychainHelper save:key_dic data:dic];
}

+ (NSString *)uniqueAppId
{
    NSString* key = [NSString stringWithFormat:@"%@.%@", [GameUUID packageNameString], GUID_KEY];
    NSString *guidStr = [GameUUID userDefaultValueForKey:key];
    if (nil == guidStr)
    {
        CFUUIDRef uid = CFUUIDCreate(NULL);
        CFStringRef uidStr = CFUUIDCreateString(NULL, uid);
        guidStr = [[[NSString alloc] initWithString:(NSString *)uidStr] autorelease];
        guidStr = [guidStr stringByReplacingOccurrencesOfString:@"-" withString:@""];
        [GameUUID saveUserDefaultValue:guidStr forKey:key];
        CFRelease(uidStr);
        CFRelease(uid);
    }
    return guidStr;
}

+ (void) openOfficialWebSite
{
    static NSString* NSUrl = nil;
    
    if (NSUrl != nil) {
        
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:NSUrl]];
        
    }else{
        NSString *URL = [NSString stringWithFormat:@"%s", ""];
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        [request setURL:[NSURL URLWithString:URL]];
        [request setHTTPMethod:@"POST"];
        NSHTTPURLResponse *urlResponse = nil;
        NSError *error = nil;
        NSData *recervedData = [NSURLConnection sendSynchronousRequest:request returningResponse:&urlResponse error:&error];
        
        if(recervedData == nil) return ;
        
        NSDictionary *dic  = [NSJSONSerialization JSONObjectWithData:recervedData options:NSJSONReadingMutableContainers error:nil];
        NSArray *infoArray  = [dic objectForKey:@"results"];
        
        if ([infoArray count]) {
            NSDictionary *releaseInfo   = [infoArray objectAtIndex:0];
            
            NSUrl = [NSString stringWithFormat:@"%@", [releaseInfo objectForKey:@"trackViewUrl"]];
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:NSUrl]];
        }
    }
}
@end
