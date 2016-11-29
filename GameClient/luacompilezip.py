#coding=utf8
import os
import zipfile

workdir             = None

def zip_dir():
    pass


if __name__ == '__main__':    
    workdir = os.path.split(os.path.abspath('__file__'))[0]
    print('workdir%s'%workdir)    
    _del_sounds()
    _update_assets()
    _copy_icon()
    _copy_thirdpart_files()