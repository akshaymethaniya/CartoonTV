package com.twoghadimoj.cartoontv.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.twoghadimoj.cartoontv.R;

public class YoutubeViewHolder extends RecyclerView.ViewHolder {

    public YouTubeThumbnailView videoThumbnailImageView;
    public TextView videoTitle;
    public CardView categoryImgCardView;

    public YoutubeViewHolder(View itemView) {
        super(itemView);
        videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
        videoTitle = itemView.findViewById(R.id.video_title_label);
        categoryImgCardView = itemView.findViewById(R.id.categoryImgCardView);
    }
}