package com.twoghadimoj.cartoontv.helpers;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ThumbnailListAvailableInFiles extends Application {
    private static HashMap<String,Boolean> thumbnailMap = new HashMap<>();
    public static HashMap<String, Boolean> getThumbnailMap() {
        return thumbnailMap;
    }
//
//    public static void setThumbnailMap(HashMap<String, Boolean> thumbnailMap) {
//        ThumbnailListAvailableInFiles.thumbnailMap = thumbnailMap;
//    }
    public static void initializeThumbnailMap(Context context){
        File cacheDir = context.getFilesDir();
        File[] files = cacheDir.listFiles();
        for(File file:files){
            thumbnailMap.put(file.getName(),true);
            Log.d("CACHE_READ",file.getName());
        }
    }
}
