package com.twoghadimoj.cartoontv.models;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collections;

public class YoutubeVideoList extends Application {
    static ArrayList<YoutubeVideoModel> youtubeVideoModels = null;

    public static ArrayList<YoutubeVideoModel> getYoutubeVideoModels() {
        if(youtubeVideoModels!=null)
            Collections.shuffle(youtubeVideoModels);
        return youtubeVideoModels;
    }

    public static void setYoutubeVideoModels(ArrayList<YoutubeVideoModel> youtubeVideoModels) {
        YoutubeVideoList.youtubeVideoModels = youtubeVideoModels;
    }
    public static ArrayList<YoutubeVideoModel> getVideosOfCategory(String cartoonCategoryName){
        ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>();
        for(YoutubeVideoModel youtubeVideo:YoutubeVideoList.youtubeVideoModels){
            if(youtubeVideo.getCartoonCategoryName().equals(cartoonCategoryName)){
                youtubeVideoModels.add(youtubeVideo);
            }
        }
        return youtubeVideoModels;
    }
}
