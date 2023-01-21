package com.twoghadimoj.cartoontv.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by sonu on 10/11/17.
 * class to set and get the video id, title and duration for a video
 */

public class YoutubeVideoModel implements Serializable {
    private String videoId, title;
    private int durationInSeconds;
    private String cartoonCategoryName;
    private int durWatchedInSeconds;
    private boolean watchedByUser;
    private boolean isFavourite;
    private Date favoriteAt;
    public boolean isFavourite() {
        return isFavourite;
    }

    public Date getFavoriteAt() {
        return favoriteAt;
    }

    public void setFavoriteAt(Date favoriteAt) {
        this.favoriteAt = favoriteAt;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public YoutubeVideoModel() {
    }

    @Override
    public String toString() {
        return "YoutubeVideoModel{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public int getDurWatchedInSeconds() {
        return durWatchedInSeconds;
    }

    public boolean isWatchedByUser() {
        return watchedByUser;
    }

    public void setDurWatchedInSeconds(int durWatchedInSeconds) {
        this.durWatchedInSeconds = durWatchedInSeconds;
    }

    public void setWatchedByUser(boolean watchedByUser) {
        this.watchedByUser = watchedByUser;
    }
    public YoutubeVideoModel(String videoId, String title, int durationInSeconds, String cartoonCategoryName, int durWatchedInSeconds, boolean watchedByUser,boolean isFavourite,Date favoriteAt) {
        this.videoId = videoId;
        this.title = title;
        this.durationInSeconds = durationInSeconds;
        this.cartoonCategoryName = cartoonCategoryName;
        this.durWatchedInSeconds = durWatchedInSeconds;
        this.watchedByUser = watchedByUser;
        this.isFavourite = isFavourite;
        this.favoriteAt = favoriteAt;
    }
    public YoutubeVideoModel(String videoId, String title, int durationInSeconds, String cartoonCategoryName, int durWatchedInSeconds, boolean watchedByUser,boolean isFavourite) {
        this.videoId = videoId;
        this.title = title;
        this.durationInSeconds = durationInSeconds;
        this.cartoonCategoryName = cartoonCategoryName;
        this.durWatchedInSeconds = durWatchedInSeconds;
        this.watchedByUser = watchedByUser;
        this.isFavourite = isFavourite;
        this.favoriteAt = null;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public String getFormattedDuation() {
        Date d = new Date(durationInSeconds * 1000L);
        String pattern = d.getHours() == 5  ? "m:ss":"H:m:ss";
        SimpleDateFormat df = new SimpleDateFormat(pattern); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(d);
        return time;
    }

    public String getCartoonCategoryName() {
        return cartoonCategoryName;
    }

    public void setCartoonCategoryName(String cartoonCategoryName) {
        this.cartoonCategoryName = cartoonCategoryName;
    }

    public String getVideoId() {
        return videoId;
    }

    //    private String extractVideoIDFromResponse(String response){
//        //pattern to match
//        String patternString = "(\\/watch\\?)v=(.*)&list";
//        Pattern pattern = Pattern.compile(patternString);
//        Matcher matcher = pattern.matcher(response);
//
//        //find match in response
//        if(matcher.find()) {
//            String value = matcher.group(2);
////            System.out.println(value);
//            return value;
//        }
//        return null;
//    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}