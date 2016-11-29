# -*- coding: cp936 -*-
import os
import sys
import shutil
import json

workdir             = None

def get_dir(*pathes):
    if pathes:
        return os.path.join(workdir, *pathes)
    return workdir

def get_proj_android(*pathes):
    return get_dir('proj.android', *pathes)

def get_provider():   
    print("[get_provider]:start")
    third_json = get_dir('Resources/thirdparty.json')
    if not os.path.exists(third_json):
        raise FileNotFoundError('[get_provider]:The file [%s] is not exists!'%third_json)
    with open(third_json, 'r') as fin:
        third_json_data = json.load(fin)
    fin.close()
    print("third_json_data:\n%s"%third_json_data)
    print("install:%s"%third_json_data["install"])
    providers = []
    list_int = []      
    for item in third_json_data["install"]:
        providers.append({
                    'enum':item,                                       
                    'folder':third_json_data[str(item)]["folder"]                    
                    })
    print("providers:%s"%providers)
    return providers

def _copy_thirdpart_files():
    provider=get_provider()
    for item in provider:
        assets_dir=get_dir(item["folder"]+"/assets")
        print("copy from:%s  to  %s"%(assets_dir,get_proj_android("assets")))
        if os.path.exists(assets_dir):
            copy_files(assets_dir,get_proj_android("assets"))
        manifest_dir=get_dir(item["folder"]+'/data/AndroidManifest.xml')
        if os.path.exists(manifest_dir):
            shutil.copy(manifest_dir,get_proj_android("AndroidManifest.xml"))
        android_support_v4_dir=get_dir(item["folder"]+'/data/android-support-v4.jar')
        if os.path.exists(android_support_v4_dir):
            shutil.copy(android_support_v4_dir,get_proj_android("libs/android-support-v4.jar"))
        src=get_dir(item["folder"]+'/data/res')
        if os.path.exists(src):
            des=get_proj_android('res')
            copy_files(src, des)
        
    print('\n_copy_thirdpart_files Done!')

def copy_files(src, dst):
    for item in os.listdir(src):
        path = os.path.join(src, item)
        # Android can not package the file that ends with ".gz"
        if not item.startswith('.') and not item.endswith('.gz') and os.path.isfile(path):
            shutil.copy(path, dst)
        if os.path.isdir(path):
            new_dst = os.path.join(dst, item)
            if not os.path.exists(new_dst):
                os.mkdir(new_dst)
            copy_files(path, new_dst)
            
def _copy_icon():
    src=get_dir('deploy/res')
    des=get_proj_android('res')
    copy_files(src, des)
    src=get_dir('deploy/libs')
    des=get_proj_android('libs')
    copy_files(src, des)
    
    
def _update_assets():
    assets_dir = get_proj_android('assets')
    if os.path.isdir(assets_dir):
        print('Remove [%s]...'%assets_dir)
        shutil.rmtree(assets_dir)
    os.mkdir(assets_dir)

    resources_dir = get_dir('Resources')   
    # 复制 Resource 中的资源到 assets 文件夹
    print('Copy [%s] to [%s]...'%(resources_dir, assets_dir))
    copy_files(resources_dir, assets_dir)

    print('\n_update_assets Done!')

def _del_sounds():
    sounds_dir=get_dir('Resources/sounds')
    if os.path.isdir(sounds_dir):        
        shutil.rmtree(sounds_dir)
    sounds_ios_dir=get_dir('Resources/sounds_ios')
    if os.path.isdir(sounds_ios_dir):        
        shutil.rmtree(sounds_ios_dir)
    sounds_plist=get_dir('Resources/sounds.plist')
    if os.path.isfile(sounds_plist):
        os.remove(sounds_plist)
    music_plist=get_dir('Resources/music.plist')
    if os.path.isfile(music_plist):
        os.remove(music_plist)
    music_ios_plist=get_dir('Resources/music_ios.plist')
    if os.path.isfile(music_ios_plist):
        os.remove(music_ios_plist)
    sounds_ios_plist=get_dir('Resources/sounds_ios.plist')
    if os.path.isfile(sounds_ios_plist):
        os.remove(sounds_ios_plist)

if __name__ == '__main__':    
    workdir = os.path.split(os.path.abspath('__file__'))[0]
    print('workdir%s'%workdir)    
    _del_sounds()
    _update_assets()
    _copy_icon()
    _copy_thirdpart_files()
   


