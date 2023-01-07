package com.twoghadimoj.cartoontv.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.twoghadimoj.cartoontv.helpers.YTDummyData;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;

import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + VIDEOS_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VIDEO_ID_COL + " TEXT,"
                + VIDEO_CATEGORY + " TEXT,"
                + DURATION_IN_SECONDS + " INTEGER,"
                + DURATION_WATCHED_BY_USER_IN_SECONDS + " INTEGER DEFAULT 0,"
                + WATCHED_BY_USER + " BOOLEAN DEFAULT 0,"
                + TITLE_COL + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean loadData(){
        //Log.d("DBLOAD","started loading");
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            db.delete(VIDEOS_TABLE_NAME,null,null);
            ArrayList<YoutubeVideoModel> youtubeVideoModels = new YTDummyData().getRandomVideos(context);
            for (YoutubeVideoModel video : youtubeVideoModels) {
                ContentValues values = new ContentValues();
                values.put(VIDEO_ID_COL, video.getVideoId());
                values.put(DURATION_IN_SECONDS, video.getDurationInSeconds());
                values.put(DURATION_WATCHED_BY_USER_IN_SECONDS, 0);
                values.put(TITLE_COL, video.getTitle());
                values.put(WATCHED_BY_USER, false);
                values.put(VIDEO_CATEGORY,video.getCartoonCategoryName());
                db.insert(VIDEOS_TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
            //Log.d("DBLOAD","completed loading");

        }catch (Exception e){
            Log.d("DB_LOAD",e.getMessage());
            return false;
        }finally {
            if(db!=null) {
                db.endTransaction();
                db.close();
            }
        }
        return true;
    }
}
