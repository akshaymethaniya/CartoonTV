package com.twoghadimoj.cartoontv.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;

import java.util.ArrayList;
import java.util.Collections;

public class DBOperations {
    private Context context;
    private DBHandler dbHandler;
    public DBOperations(Context context) {
        this.context = context;
        this.dbHandler = new DBHandler(context);
    }

    public boolean updateVideoWatchedStatusAndDuration(String videoId,int watchedDurationInSeconds){
        SQLiteDatabase db= null;
        try {
            db = dbHandler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Constants.WATCHED_BY_USER, 1);
            values.put(Constants.DURATION_WATCHED_BY_USER_IN_SECONDS,watchedDurationInSeconds);
             int noOfRowsUpdated = db.update(Constants.VIDEOS_TABLE_NAME, values, "videoId=?", new String[]{videoId});
            //Log.d("DB_UPDATE","No of rows updated:"+noOfRowsUpdated);

        }catch (Exception e){
            Log.d("DB_UPDATE",e.getMessage());
            return false;
        }finally {
            if(db!=null){
                db.close();
            }
        }
        return true;
    }
    public ArrayList<YoutubeVideoModel> getAllVideos(){
        ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>();
        SQLiteDatabase db= null;
        try {
            db = dbHandler.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT * FROM "+
                    Constants.VIDEOS_TABLE_NAME
//                    +" LIMIT 250"
                    , null);
            if (c.moveToFirst()) {
                do {
                    String videoId = c.getString(1);
                    String videoCategory = c.getString(2);
                    int durationInSeconds = c.getInt(3);
                    int durWatchedInSeconds = c.getInt(4);
                    boolean watchedByUser = c.getInt(5) == 0 ? false:true;
                    String title = c.getString(6);
//                    Log.d("DB_LIST",String.format("title: %s durWatchedInSeconds: %d",title,durWatchedInSeconds));
                    youtubeVideoModels.add(new YoutubeVideoModel(videoId,title,durationInSeconds,videoCategory,durWatchedInSeconds,watchedByUser));
                    // Do something Here with values
                } while(c.moveToNext());
            }
        }catch (Exception e){
            Log.d("DB_UPDATE",e.getMessage());
        }finally {
            if(db!=null){
                db.close();
            }
        }
        Collections.shuffle(youtubeVideoModels);
        return youtubeVideoModels;
    }
    public ArrayList<YoutubeVideoModel> getAllVideosOfCategory(String category_name){
        ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>();
        SQLiteDatabase db= null;
        try {
            db = dbHandler.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT * FROM "+
                    Constants.VIDEOS_TABLE_NAME+
                    " WHERE "+Constants.VIDEO_CATEGORY+" = ?", new String[]{category_name});
            if (c.moveToFirst()) {
                do {
                    String videoId = c.getString(1);
                    String videoCategory = c.getString(2);
                    int durationInSeconds = c.getInt(3);
                    int durWatchedInSeconds = c.getInt(4);
                    boolean watchedByUser = c.getInt(5) == 0 ? false:true;
                    String title = c.getString(6);
//                    Log.d("DB_LIST",String.format("title: %s durWatchedInSeconds: %d",title,durWatchedInSeconds));
                    youtubeVideoModels.add(new YoutubeVideoModel(videoId,title,durationInSeconds,videoCategory,durWatchedInSeconds,watchedByUser));
                    // Do something Here with values
                } while(c.moveToNext());
            }
        }catch (Exception e){
            Log.d("DB_UPDATE",e.getMessage());
        }finally {
            if(db!=null){
                db.close();
            }
        }
        Collections.shuffle(youtubeVideoModels);
        return youtubeVideoModels;
    }
}
