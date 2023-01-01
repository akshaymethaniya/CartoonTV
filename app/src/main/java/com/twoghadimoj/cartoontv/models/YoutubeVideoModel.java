package com.twoghadimoj.cartoontv.models;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sonu on 10/11/17.
 * class to set and get the video id, title and duration for a video
 */

public class YoutubeVideoModel implements Serializable {
    private String videoId, title;
    private String cartoonCategoryName;
    public YoutubeVideoModel() {
    }

    @Override
    public String toString() {
        return "YoutubeVideoModel{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public YoutubeVideoModel(String videoId, String title,String cartoonCategoryName) {
        this.videoId = videoId;
        this.title = title;
        this.cartoonCategoryName = cartoonCategoryName;
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