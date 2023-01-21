package com.twoghadimoj.cartoontv;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.twoghadimoj.cartoontv.adapters.YoutubeVideoAdapter;
import com.twoghadimoj.cartoontv.database.DBOperations;
import com.twoghadimoj.cartoontv.enums.CARTOON_CATEGORY;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;
import com.twoghadimoj.cartoontv.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlayYoutubeVideo extends YouTubeBaseActivity {
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private YoutubeVideoModel playingItem;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer mYouTubePlayer = null;
    private TextView video_title_label;
    private boolean showVideosOfSameCategory;
    private CardView categoryCardView;
    private YoutubeVideoAdapter youtubeVideoAdapter;
    public Timer timer = new Timer();
    private DBOperations dbOperations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_youtube_video);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        dbOperations = new DBOperations(this);

        //Get Video Details From Intent
        YoutubeVideoModel youtubeVideoModel = (YoutubeVideoModel) getIntent().getSerializableExtra("videoItem");
        this.playingItem = youtubeVideoModel;

        this.showVideosOfSameCategory = getIntent().getBooleanExtra("showVideosOfSameCategory", false);
        //set title
        video_title_label = findViewById(R.id.video_title_label);
        video_title_label.setText(youtubeVideoModel.getTitle());

        categoryCardView = findViewById(R.id.categoryImgCardView);
        categoryCardView.setForeground(CARTOON_CATEGORY.getCartoonDrawable(this, youtubeVideoModel.getCartoonCategoryName()));

        //Initialize youTubePlayerView
        initializeYoutubePlayerView();
        setUpRecyclerView();
        populateRecyclerView();
    }
    private String getDeveloperKey(Context context){
        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            return bundle.getString("com.twoghadimoj.cartoontv.youtubeAPIKey");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void initializeYoutubePlayerView() {
        youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(getDeveloperKey(this),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        mYouTubePlayer = youTubePlayer;
                        youTubePlayer.cueVideo(playingItem.getVideoId(),playingItem.getDurWatchedInSeconds()*1000);
                        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                            @Override
                            public void onLoading() {

                            }

                            @Override
                            public void onLoaded(String s) {
                                mYouTubePlayer.play();
                            }

                            @Override
                            public void onAdStarted() {

                            }

                            @Override
                            public void onVideoStarted() {
                                try {
                                    timer.scheduleAtFixedRate(new UpdateVideoProgress(), 0, 1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onVideoEnded() {
                                ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = youtubeVideoAdapter.getYoutubeVideoModelArrayList();
                                //Play video at index 0
                                YoutubeVideoModel lastPlayed = playingItem;
                                playingItem = youtubeVideoModelArrayList.remove(0);
                                youtubeVideoModelArrayList.add(lastPlayed);
                                updateValuesForCurrentlyPlayingVideo(playingItem);
                            }

                            @Override
                            public void onError(YouTubePlayer.ErrorReason errorReason) {
                                timer.cancel();
                                Toast.makeText(PlayYoutubeVideo.this,"Failed to play video",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(PlayYoutubeVideo.this,"You don't have the latest YouTube App installed on your device.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * setup the recyclerview here
     */
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.youtube_videos);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recyclerview and implement the click event here
     */
    private void populateRecyclerView() {
        ArrayList<YoutubeVideoModel> youtubeVideoList;
        if (!showVideosOfSameCategory) {
//            youtubeVideoList= new YTDummyData().getRandomVideos(this);
            Log.d("READ_DATA","Inside");
            youtubeVideoList = (ArrayList<YoutubeVideoModel>) dbOperations.getAllVideos();
//            youtubeVideoList = YoutubeVideoList.getYoutubeVideoModels();
        } else {
//            youtubeVideoList = YoutubeVideoList.getYoutubeVideoModels();
            youtubeVideoList = dbOperations.getAllVideosOfCategory(playingItem.getCartoonCategoryName());
//            youtubeVideoList = new YTDummyData().getYTVideosForCategory(this,playingItem.getCartoonCategoryName());
        }

        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(this, removeCurrentVideoFromList(playingItem,youtubeVideoList), new YoutubeVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeVideoModel item) {
                //Play video at index 0
                ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = youtubeVideoAdapter.getYoutubeVideoModelArrayList();
                removeCurrentVideoFromList(item,youtubeVideoModelArrayList);
                youtubeVideoModelArrayList.add(playingItem);
                updateValuesForCurrentlyPlayingVideo(item);
            }
        });
        adapter.setHasStableIds(true);
        this.youtubeVideoAdapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    private void updateValuesForCurrentlyPlayingVideo(YoutubeVideoModel item) {
        playingItem = item;

        //update title
        video_title_label.setText(item.getTitle());

        categoryCardView.setForeground(CARTOON_CATEGORY.getCartoonDrawable(this, item.getCartoonCategoryName()));

        //play new video
        if(mYouTubePlayer!=null)
            mYouTubePlayer.loadVideo(item.getVideoId(),item.getDurWatchedInSeconds()*1000);

        youtubeVideoAdapter.setYoutubeVideoModelArrayList(youtubeVideoAdapter.getYoutubeVideoModelArrayList());
        youtubeVideoAdapter.notifyDataSetChanged();
        //Update recycleView
//        populateRecyclerView();
    }

    private ArrayList<YoutubeVideoModel> removeCurrentVideoFromList(YoutubeVideoModel playingItem,ArrayList<YoutubeVideoModel> youtubeVideoList) {
        for (int i = 0; i < youtubeVideoList.size(); i++) {
            YoutubeVideoModel youtubeVideoModel = youtubeVideoList.get(i);
            if (youtubeVideoModel.getVideoId().equals(playingItem.getVideoId())) {
                youtubeVideoList.remove(i);
                break;
            }
        }
        return youtubeVideoList;
    }

    private class UpdateVideoProgress extends TimerTask {
        @Override
        public void run() {
            if (mYouTubePlayer != null && mYouTubePlayer.isPlaying() && (mYouTubePlayer.getCurrentTimeMillis() / 1000) > 5) {
                //Log.d("DB_UPDATE", String.format("Updating progress for video Id: %s seconds: %d", playingItem.getVideoId(), mYouTubePlayer.getCurrentTimeMillis() / 1000));
                boolean res = dbOperations.updateVideoWatchedStatusAndDuration(playingItem.getVideoId(), mYouTubePlayer.getCurrentTimeMillis() / 1000);
                playingItem.setWatchedByUser(true);
                playingItem.setDurWatchedInSeconds(mYouTubePlayer.getCurrentTimeMillis() / 1000);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        try{
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        }catch (Exception e){
            Log.d("ERR",e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        super.onBackPressed();
    }
}