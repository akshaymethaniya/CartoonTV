package com.twoghadimoj.cartoontv.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.twoghadimoj.cartoontv.helpers.YTDummyData;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.twoghadimoj.cartoontv.database.Constants.*;
public class DBHandler  extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    Context context;

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    //Called when Freshly installed
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + VIDEOS_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VIDEO_ID_COL + " TEXT,"
                + VIDEO_CATEGORY + " TEXT,"
                + DURATION_IN_SECONDS + " INTEGER,"
                + DURATION_WATCHED_BY_USER_IN_SECONDS + " INTEGER DEFAULT 0,"
                + WATCHED_BY_USER + " BOOLEAN DEFAULT 0,"
                + TITLE_COL + " TEXT,"
                + IS_FAVOURITE_VIDEO + " BOOLEAN DEFAULT 0,"
                + FAVOURITE_AT + " DATETIME DEFAULT NULL)";
        sqLiteDatabase.execSQL(query);
    }

    //onUpgrade is called whenever the app is upgraded and launched and the database version is not the same.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        if (oldVersion == 1 && newVersion == 2) {
//            Log.d("DB_UPGRADE","Executed");
//            sqLiteDatabase.execSQL("ALTER TABLE " + VIDEOS_TABLE_NAME + " ADD COLUMN " + IS_FAVOURITE_VIDEO + " BOOLEAN DEFAULT 0");
//            sqLiteDatabase.execSQL("ALTER TABLE " + VIDEOS_TABLE_NAME + " ADD COLUMN " + FAVOURITE_AT + " DATETIME DEFAULT NULL");
//
//        }
        if(oldVersion < newVersion){
            loadData();
        }
    }
    public boolean loadData(){
        Log.d("DB_LOAD","started loading");
        SQLiteDatabase db = null;
        try {
            //Read existing data
            HashMap<String, YoutubeVideoModel> existingVideoRows = this.createHashMap(this.readAllData());
            db = this.getWritableDatabase();
            db.beginTransaction();

            //delete existing data
            db.delete(VIDEOS_TABLE_NAME,null,null);
            ArrayList<YoutubeVideoModel> youtubeVideoModels = new YTDummyData().getRandomVideos(context);
            for (YoutubeVideoModel video : youtubeVideoModels) {
                ContentValues values = new ContentValues();
                values.put(VIDEO_ID_COL, video.getVideoId());
                values.put(DURATION_IN_SECONDS, video.getDurationInSeconds());
                values.put(TITLE_COL, video.getTitle());
                values.put(VIDEO_CATEGORY,video.getCartoonCategoryName());

                //Use watch details from existing db instead of setting it as default values
                if(existingVideoRows.containsKey(video.getVideoId())){
//                    Log.d("DB_LOAD","fOUND eXISTING VIDEO");
                    YoutubeVideoModel youtubeVideoModel = existingVideoRows.get(video.getVideoId());
                    if(youtubeVideoModel.isFavourite()){
                        Log.d("DB_LOAD","FOUND FAVORITE VIDEO");
                        values.put(IS_FAVOURITE_VIDEO, 1);
                        values.put(FAVOURITE_AT,getFormattedDate(youtubeVideoModel.getFavoriteAt()));
                    }
                    if(youtubeVideoModel.isWatchedByUser()){
                        Log.d("DB_LOAD","FOUND WATCHED VIDEO");
                        values.put(DURATION_WATCHED_BY_USER_IN_SECONDS, youtubeVideoModel.getDurWatchedInSeconds());
                        values.put(WATCHED_BY_USER, true);
                    }
                }else {
                    values.put(IS_FAVOURITE_VIDEO, 0);
                    values.put(DURATION_WATCHED_BY_USER_IN_SECONDS, 0);
                    values.put(WATCHED_BY_USER, false);
                }
                db.insert(VIDEOS_TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
            Log.d("DB_LOAD","completed loading");

        }catch (Exception e){
            Log.d("DB_LOAD",e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            if(db!=null) {
                db.endTransaction();
                db.close();
            }
        }
        return true;
    }
    public String getFormattedDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
    public Date getDate(String d){
        if(d == null || d.isEmpty()){
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @SuppressLint("Range")
    public ArrayList<YoutubeVideoModel> readAllData(){
        SQLiteDatabase db = null;
        ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>();
        try {
            db = this.getWritableDatabase();

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
                    Date favAt = getDate(c.getString(c.getColumnIndex(FAVOURITE_AT)));
//                    Log.d("DB_LOAD",String.format("title: %s durWatchedInSeconds: %d",title,durWatchedInSeconds));
                    youtubeVideoModels.add(new YoutubeVideoModel(videoId, title, durationInSeconds, videoCategory, durWatchedInSeconds, watchedByUser, isFav,favAt));
                } while (c.moveToNext());
            }
            return youtubeVideoModels;
        } catch(Exception e){
            Log.d("DB_LOAD",e.getMessage());
            Log.d("DB_LOAD",e.getStackTrace().toString());
            e.printStackTrace();
        }finally {
            if(db!=null) {
                db.close();
            }
        }
        Log.d("DB_LOAD","No of existing entries: "+youtubeVideoModels.size());
        return youtubeVideoModels;
    }
    public HashMap<String,YoutubeVideoModel> createHashMap(ArrayList<YoutubeVideoModel> youtubeVideoModels){
        HashMap<String,YoutubeVideoModel> map = new HashMap<>();
        for(YoutubeVideoModel youtubeVideoModel:youtubeVideoModels){
            map.put(youtubeVideoModel.getVideoId(),youtubeVideoModel);
        }
        return map;
    }
}
