package com.twoghadimoj.cartoontv.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.like.LikeButton;
import com.twoghadimoj.cartoontv.R;

public class YoutubeViewHolder extends RecyclerView.ViewHolder {

//    public YouTubeThumbnailView videoThumbnailImageView;
    public TextView videoTitle,videoDurationTextView;
    public ImageView categoryImgCardView;
    public ProgressBar progressBar;
    public ImageView videoThumbnailImageView1;
    public LinearLayout loadingPanel;
    public LikeButton heartButton;
    public YoutubeViewHolder(View itemView) {
        super(itemView);
//        videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
        videoTitle = itemView.findViewById(R.id.video_title_label);
        categoryImgCardView = itemView.findViewById(R.id.categoryImgCardView);
        videoDurationTextView = itemView.findViewById(R.id.videoDurationTextView);
        progressBar = itemView.findViewById(R.id.progressBar);
        videoThumbnailImageView1 = itemView.findViewById(R.id.video_thumbnail_image_view1);
        loadingPanel = itemView.findViewById(R.id.loadingPanel);
        heartButton = itemView.findViewById(R.id.heartButton);
    }
}