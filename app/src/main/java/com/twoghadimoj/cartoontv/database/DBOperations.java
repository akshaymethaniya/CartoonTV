package com.twoghadimoj.cartoontv.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.twoghadimoj.cartoontv.models.YoutubeVideoList;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class DBOperations {
    private Context context;
    private DBHandler dbHandler;

    public DBOperations(Context context) {
        this.context = context;
        this.dbHandler = new DBHandler(context);
    }

    public boolean setVideoAsUnFavourite(String videoId) {
        SQLiteDatabase db = null;
        try {
            db = dbHandler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Constants.IS_FAVOURITE_VIDEO, 0);
            int noOfRowsUpdated = db.update(Constants.VIDEOS_TABLE_NAME, values, "videoId=?", new String[]{videoId});
            Log.d("DB_UPDATE", "Unlike : No of rows updated:" + noOfRowsUpdated);

        } catch (Exception e) {
            Log.d("DB_UPDATE", e.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return true;
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    public boolean setVideoAsFavourite(String videoId) {
        SQLiteDatabase db = null;
        try {
            db = dbHandler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Constants.IS_FAVOURITE_VIDEO, 1);
            values.put(Constants.FAVOURITE_AT,getDateTime());
            int noOfRowsUpdated = db.update(Constants.VIDEOS_TABLE_NAME, values, "videoId=?", new String[]{videoId});
            Log.d("DB_UPDATE", "Like : No of rows updated:" + noOfRowsUpdated);

        } catch (Exception e) {
            Log.d("DB_UPDATE", e.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return true;
    }

    public boolean updateVideoWatchedStatusAndDuration(String videoId, int watchedDurationInSeconds) {
        SQLiteDatabase db = null;
        try {
            db = dbHandler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Constants.WATCHED_BY_USER, 1);
            values.put(Constants.DURATION_WATCHED_BY_USER_IN_SECONDS, watchedDurationInSeconds);
            int noOfRowsUpdated = db.update(Constants.VIDEOS_TABLE_NAME, values, "videoId=?", new String[]{videoId});
            //Log.d("DB_UPDATE","No of rows updated:"+noOfRowsUpdated);

        } catch (Exception e) {
            Log.d("DB_UPDATE", e.getMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return true;
    }
    @SuppressLint("Range")
    public ArrayList<YoutubeVideoModel> getFavouriteVideos() {
        ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbHandler.getWritableDatabase();

            Cursor c = db.rawQuery(
                    "SELECT * FROM " + Constants.VIDEOS_TABLE_NAME + " WHERE " + Constants.IS_FAVOURITE_VIDEO + " = 1"
                    +" ORDER BY "+Constants.FAVOURITE_AT+" DESC"
//                    +" LIMIT 250"
                    , null);
            if (c.moveToFirst()) {
                do {
                    String videoId = c.getString(c.getColumnIndex(Constants.VIDEO_ID_COL));
                    String videoCategory = c.getString(c.getColumnIndex(Constants.VIDEO_CATEGORY));
                    int durationInSeconds = c.getInt(c.getColumnIndex(Constants.DURATION_IN_SECONDS));
                    int durWatchedInSeconds = c.getInt(c.getColumnIndex(Constants.DURATION_WATCHED_BY_USER_IN_SECONDS));
                    boolean watchedByUser = c.getInt(c.getColumnIndex(Constants.WATCHED_BY_USER)) == 0 ? false : true;
                    String title = c.getString(c.getColumnIndex(Constants.TITLE_COL));
                    boolean isFav = c.getInt(c.getColumnIndex(Constants.IS_FAVOURITE_VIDEO)) == 0 ? false : true;
//                    Log.d("DB_LIST",String.format("title: %s durWatchedInSeconds: %d",title,durWatchedInSeconds));
                    youtubeVideoModels.add(new YoutubeVideoModel(videoId, title, durationInSeconds, videoCategory, durWatchedInSeconds, watchedByUser, isFav));
                    // Do something Here with values
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DB_UPDATE", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
//        Collections.shuffle(youtubeVideoModels);
        return youtubeVideoModels;
    }
    @SuppressLint("Range")
    public ArrayList<YoutubeVideoModel> getAllVideos() {
        ArrayList<YoutubeVideoModel> youtubeVideoModels1 = YoutubeVideoList.getYoutubeVideoModels();
        if(youtubeVideoModels1!=null && youtubeVideoModels1.size()!=0){
            Log.d("VIDEO_LIST","Loaded from application level object");
            return youtubeVideoModels1;
        }
        ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbHandler.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT * FROM " +
                            Constants.VIDEOS_TABLE_NAME
//                    +" LIMIT 250"
                    , null);
            if (c.moveToFirst()) {
                do {
                    String videoId = c.getString(c.getColumnIndex(Constants.VIDEO_ID_COL));
                    String videoCategory = c.getString(c.getColumnIndex(Constants.VIDEO_CATEGORY));
                    int durationInSeconds = c.getInt(c.getColumnIndex(Constants.DURATION_IN_SECONDS));
                    int durWatchedInSeconds = c.getInt(c.getColumnIndex(Constants.DURATION_WATCHED_BY_USER_IN_SECONDS));
                    boolean watchedByUser = c.getInt(c.getColumnIndex(Constants.WATCHED_BY_USER)) == 0 ? false : true;
                    String title = c.getString(c.getColumnIndex(Constants.TITLE_COL));
                    boolean isFav = c.getInt(c.getColumnIndex(Constants.IS_FAVOURITE_VIDEO)) == 0 ? false : true;

                    Log.d("DB_LIST",String.format("title: %s durWatchedInSeconds: %d",title,durWatchedInSeconds));
                    youtubeVideoModels.add(new YoutubeVideoModel(videoId, title, durationInSeconds, videoCategory, durWatchedInSeconds, watchedByUser, isFav));
//                    if(youtubeVideoModels.size()==5){
//                        return youtubeVideoModels;
//                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DB_UPDATE", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        Log.d("VIDEO_LIST","Loaded from db");
        Collections.shuffle(youtubeVideoModels);
        YoutubeVideoList.setYoutubeVideoModels(youtubeVideoModels);
        return youtubeVideoModels;
    }

    public ArrayList<YoutubeVideoModel> getAllVideosOfCategory(String category_name) {
        ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbHandler.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT * FROM " +
                    Constants.VIDEOS_TABLE_NAME +
                    " WHERE " + Constants.VIDEO_CATEGORY + " = ?", new String[]{category_name});
            if (c.moveToFirst()) {
                do {
                    String videoId = c.getString(1);
                    String videoCategory = c.getString(2);
                    int durationInSeconds = c.getInt(3);
                    int durWatchedInSeconds = c.getInt(4);
                    boolean watchedByUser = c.getInt(5) == 0 ? false : true;
                    String title = c.getString(6);
                    boolean isFav = c.getInt(7) == 0 ? false : true;
//                    Log.d("DB_LIST",String.format("title: %s durWatchedInSeconds: %d",title,durWatchedInSeconds));
                    youtubeVideoModels.add(new YoutubeVideoModel(videoId, title, durationInSeconds, videoCategory, durWatchedInSeconds, watchedByUser, isFav));
                    // Do something Here with values
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DB_UPDATE", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        Collections.shuffle(youtubeVideoModels);
        return youtubeVideoModels;
    }
}
