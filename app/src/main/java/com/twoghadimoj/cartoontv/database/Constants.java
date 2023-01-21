package com.twoghadimoj.cartoontv.database;

public class Constants {

    //Database name
    public static final String DB_NAME = "videosDB";

    // below int is our database version
    public static final int DB_VERSION = 2;

    // VERSION-2 -> Updated video details of SPD POWER RANGERS
    // DATA NEEDS TO BE RELOADED TO DB
    public static final String VIDEOS_TABLE_NAME = "videos";

    //columns
    public static final String ID_COL="id";
    public static final String VIDEO_ID_COL = "videoId";
    public static final String TITLE_COL = "title";
    public static final String DURATION_IN_SECONDS = "durationInSeconds";
    public static final String DURATION_WATCHED_BY_USER_IN_SECONDS ="durWatchedInSeconds" ;
    public static final String WATCHED_BY_USER = "watchedByUser";
    public static final String VIDEO_CATEGORY = "videoCategory";
    public static final String IS_FAVOURITE_VIDEO = "isFavoriteVideo";
    public static final String FAVOURITE_AT = "favouritedAt";
}
