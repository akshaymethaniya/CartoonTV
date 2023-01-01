package com.twoghadimoj.cartoontv.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.twoghadimoj.cartoontv.database.Constants.*;
public class DBHandler  extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.


    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + VIDEOS_TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + VIDEO_ID_COL + " TEXT,"
                + WATCHED_COL + " BOOLEAN,"
                + TITLE_COL + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
