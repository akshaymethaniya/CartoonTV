package com.twoghadimoj.cartoontv;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.twoghadimoj.cartoontv.adapters.YoutubeVideoAdapter;
import com.twoghadimoj.cartoontv.constants.DeveloperKey;
import com.twoghadimoj.cartoontv.enums.CARTOON_CATEGORY;
import com.twoghadimoj.cartoontv.helpers.YTDummyData;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;
import com.twoghadimoj.cartoontv.ui.home.HomeViewModel;

import java.util.ArrayList;

public class PlayYoutubeVideo extends YouTubeBaseActivity {
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private YoutubeVideoModel playingItem;
    private YouTubePlayer mYouTubePlayer;
    private TextView video_title_label;
    private boolean showVideosOfSameCategory;
    private CardView categoryCardView;
    private YoutubeVideoAdapter youtubeVideoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_youtube_video);

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        //Get Video Details From Intent
        YoutubeVideoModel youtubeVideoModel = (YoutubeVideoModel) getIntent().getSerializableExtra("videoItem");
        this.playingItem = youtubeVideoModel;

        this.showVideosOfSameCategory = getIntent().getBooleanExtra("showVideosOfSameCategory",false);
        //set title
        video_title_label = findViewById(R.id.video_title_label);
        video_title_label.setText(youtubeVideoModel.getTitle());

        categoryCardView = findViewById(R.id.categoryImgCardView);
        categoryCardView.setForeground(CARTOON_CATEGORY.getCartoonDrawable(this,youtubeVideoModel.getCartoonCategoryName()));

        //Initialize youTubePlayerView
        youTubePlayerView.initialize(DeveloperKey.DEVELOPER_KEY,
            new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                    YouTubePlayer youTubePlayer, boolean b) {
                    mYouTubePlayer = youTubePlayer;
                    youTubePlayer.cueVideo(playingItem.getVideoId());
//                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
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

                        }

                        @Override
                        public void onVideoEnded() {
                            ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = youtubeVideoAdapter.getYoutubeVideoModelArrayList();
                            //Play video at index 0
                            playingItem = youtubeVideoModelArrayList.remove(0);
                            updateValuesForCurrentlyPlayingVideo(playingItem);
                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                    YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        setUpRecyclerView();
        populateRecyclerView();
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
        if(!showVideosOfSameCategory){
            youtubeVideoList= new YTDummyData().getRandomVideos(this);
        }else{
            youtubeVideoList = new YTDummyData().getYTVideosForCategory(this,playingItem.getCartoonCategoryName());
        }
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(this,removeCurrentVideoFromList(youtubeVideoList) , new YoutubeVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeVideoModel item) {
                updateValuesForCurrentlyPlayingVideo(item);
            }
        });
        this.youtubeVideoAdapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    private void updateValuesForCurrentlyPlayingVideo(YoutubeVideoModel item){
        playingItem = item;

        //update title
        video_title_label.setText(item.getTitle());

        categoryCardView.setForeground(CARTOON_CATEGORY.getCartoonDrawable(this,item.getCartoonCategoryName()));

        //play new video
        mYouTubePlayer.loadVideo(item.getVideoId());

        //Update recycleView
        populateRecyclerView();
    }
    private ArrayList<YoutubeVideoModel> removeCurrentVideoFromList(ArrayList<YoutubeVideoModel> youtubeVideoList){
        for(int i=0;i<youtubeVideoList.size();i++){
            YoutubeVideoModel youtubeVideoModel = youtubeVideoList.get(i);
            if(youtubeVideoModel.getVideoId().equals(playingItem.getVideoId())){
                youtubeVideoList.remove(i);
                break;
            }
        }
        return youtubeVideoList;
    }
}