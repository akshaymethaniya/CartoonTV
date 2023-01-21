package com.twoghadimoj.cartoontv.helpers;

import android.content.Context;

import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class YTDummyData {
    private HashMap<String,ArrayList<YoutubeVideoModel>> ytVideoMap;

    public YTDummyData(HashMap<String, ArrayList<YoutubeVideoModel>> ytVideoMap) {
        this.ytVideoMap = ytVideoMap;
    }

    public YTDummyData() {
        this.ytVideoMap = new HashMap<>();
    }

    public HashMap<String, ArrayList<YoutubeVideoModel>> getYtVideoMap() {
        return ytVideoMap;
    }
    public ArrayList<YoutubeVideoModel> getRandomVideos(Context context){
        ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>() ;
        HashMap<String, ArrayList<YoutubeVideoModel>> categoryWiseVideos = getCategoryWiseVideos(context);
        Iterator hmIterator = categoryWiseVideos.entrySet().iterator();
        while(hmIterator.hasNext()){
            Map.Entry mapElement
                    = (Map.Entry)hmIterator.next();
            ArrayList<YoutubeVideoModel> youtubeVideoList = (ArrayList<YoutubeVideoModel>) mapElement.getValue();
            youtubeVideoModels.addAll(youtubeVideoList);
        }
        Collections.shuffle(youtubeVideoModels);
        return youtubeVideoModels;
    }
    public ArrayList<YoutubeVideoModel> getRandomVideos(Context context,String videoCategory){
        ArrayList<YoutubeVideoModel> ytVideosForCategory = getYTVideosForCategory(context, videoCategory);
        Collections.shuffle(ytVideosForCategory);
        return ytVideosForCategory;
    }
    public YoutubeVideoModel getFirstVideoForCategory(Context context,String categoryName){
        String json = null;
        JSONObject ytData = null;
        try {
            InputStream is = context.getAssets().open("ytdata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            ytData = new JSONObject(json);

            for (Iterator<String> it = ytData.keys(); it.hasNext(); ) {
                String videoCategoryTitle = it.next();
                JSONObject videoData = (JSONObject) ytData.get(videoCategoryTitle);
                JSONArray videos = (JSONArray) videoData.get("videos");

                if(videoCategoryTitle.equals(categoryName)){
                    for(int i=0;i<videos.length();i++){
                        JSONObject videoDetails = (JSONObject) videos.get(i);
                        String videoId = videoDetails.getString("id");
                        String video_title = videoDetails.getString("title");
                        int durationInSeconds = videoDetails.getInt("length");
                       return new YoutubeVideoModel(videoId,video_title,durationInSeconds,videoCategoryTitle,0,false,false);
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<YoutubeVideoModel> getYTVideosForCategory(Context context,String categoryName){
        ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>();
        String json = null;
        JSONObject ytData = null;
        try {
            InputStream is = context.getAssets().open("ytdata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            ytData = new JSONObject(json);

            for (Iterator<String> it = ytData.keys(); it.hasNext(); ) {
                String videoCategoryTitle = it.next();
                JSONObject videoData = (JSONObject) ytData.get(videoCategoryTitle);
                JSONArray videos = (JSONArray) videoData.get("videos");

                if(videoCategoryTitle.equals(categoryName)){
                    for(int i=0;i<videos.length();i++){
                        JSONObject videoDetails = (JSONObject) videos.get(i);
                        String videoId = videoDetails.getString("id");
                        String video_title = videoDetails.getString("title");
                        int durationInSeconds = videoDetails.getInt("length");
                        YoutubeVideoModel videoModel = new YoutubeVideoModel(videoId,video_title,durationInSeconds,videoCategoryTitle,0,false,false);
                        youtubeVideoModels.add(videoModel);
                    }
                    break;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return youtubeVideoModels;
    }
    public HashMap<String, ArrayList<YoutubeVideoModel>> getCategoryWiseVideos(Context context){
        String json = null;
        JSONObject ytData = null;
        try {
            InputStream is = context.getAssets().open("ytdata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            ytData = new JSONObject(json);

            for (Iterator<String> it = ytData.keys(); it.hasNext(); ) {
                String videoCategoryTitle = it.next();
                JSONObject videoData = (JSONObject) ytData.get(videoCategoryTitle);
                JSONArray videos = (JSONArray) videoData.get("videos");

                ArrayList<YoutubeVideoModel> youtubeVideoModels = new ArrayList<>();

                for(int i=0;i<videos.length();i++){
                    JSONObject videoDetails = (JSONObject) videos.get(i);
                    String videoId = videoDetails.getString("id");
                    String video_title = videoDetails.getString("title");
                    int durationInSeconds = videoDetails.getInt("length");
                    YoutubeVideoModel videoModel = new YoutubeVideoModel(videoId,video_title,durationInSeconds,videoCategoryTitle,0,false,false);
                    youtubeVideoModels.add(videoModel);
                }

                ytVideoMap.put(videoCategoryTitle,youtubeVideoModels);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return ytVideoMap;
    }
}
