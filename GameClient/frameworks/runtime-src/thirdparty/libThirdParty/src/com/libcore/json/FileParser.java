

package com.libcore.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.cocos2dx.lib.Cocos2dxHelper;

import com.base.ApplicationBase;
import com.base.GameActivityBase;

import android.content.res.AssetManager;
import android.util.Log;


public class FileParser {
    private static final String TAG = FileParser.class.getSimpleName();
    private static String mContent = null;

    public FileParser(String fileName){
        mContent = readFile(fileName);
    }

    public String readFromFile() {
        return mContent;
    }
   
    private String readFile(String fileName) {
        String jsonString = "";
        BufferedReader reader = null;
        try{
            AssetManager assertManager = GameActivityBase.app.getAssets();
            Log.i(TAG, "assertManager:"  + assertManager);
            InputStreamReader inputStreamReader = new InputStreamReader(assertManager.open(fileName), "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null){
                jsonString += tempString;
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        Log.i(TAG, "readFile: fileName:" + fileName + " content:" + jsonString);
        return jsonString;
    }
}
