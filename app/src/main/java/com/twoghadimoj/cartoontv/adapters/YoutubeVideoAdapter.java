package com.twoghadimoj.cartoontv.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.twoghadimoj.cartoontv.enums.CARTOON_CATEGORY;
import com.twoghadimoj.cartoontv.constants.DeveloperKey;
import com.twoghadimoj.cartoontv.R;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;

import java.util.ArrayList;

/**
 * Created by sonu on 10/11/17.
 */

public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeViewHolder> {
    private static final String TAG = YoutubeVideoAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList;
    private final OnItemClickListener listener;

    public ArrayList<YoutubeVideoModel> getYoutubeVideoModelArrayList() {
        return youtubeVideoModelArrayList;
    }

    public interface OnItemClickListener {
        void onItemClick(YoutubeVideoModel item);
    }
    public YoutubeVideoAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList, OnItemClickListener listener) {
        this.context = context;
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
        this.listener = listener;
    }

    @Override
    public YoutubeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.youtube_custom_video_layout, parent, false);
        return new YoutubeViewHolder(view);
    }
    //To avoid image change on scroll issue, over-ride below two methods.
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(YoutubeViewHolder holder, final int position) {

        final YoutubeVideoModel youtubeVideoModel = youtubeVideoModelArrayList.get(position);
        holder.videoTitle.setText(youtubeVideoModel.getTitle());

        holder.categoryImgCardView.setForeground(CARTOON_CATEGORY.getCartoonDrawable(this.context,youtubeVideoModel.getCartoonCategoryName()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(youtubeVideoModel);
            }
        });
        /*  initialize the thumbnail image view , we need to pass Developer Key */
        holder.videoThumbnailImageView.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                //when initialization is sucess, set the video id to thumbnail to load
                youTubeThumbnailLoader.setVideo(youtubeVideoModel.getVideoId());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        //print or show error when thumbnail load failed
                        Log.e(TAG, "Youtube Thumbnail Error");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //print or show error when initialization failed
                Log.e(TAG, "Youtube Initialization Failure");

            }
        });

    }

    @Override
    public int getItemCount() {
        return youtubeVideoModelArrayList != null ? youtubeVideoModelArrayList.size() : 0;
    }

}