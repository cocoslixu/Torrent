# -*- coding: cp936 -*-
import os
import sys
import shutil
import json
import zipfile
workdir             = None

def get_dir(*pathes):
    if pathes:
        return os.path.join(workdir, *pathes)
    return workdir

def zip_dir(srcPath, desPath):
    zipHandle = zipfile.ZipFile(desPath, 'w', zipfile.ZIP_DEFLATED)

    for dirpath, dirs, files in os.walk(srcPath):
        for filename in files:
            zipHandle.write(os.path.join(dirpath,filename))
            print filename+" zip_succeeded"

    zipHandle.close



if __name__ == '__main__':    
    workdir = os.path.split(os.path.abspath('__file__'))[0]
    # workdir += '/src'
    print('workdir%s'%workdir)
    zip_dir('src', 'src.zip')

