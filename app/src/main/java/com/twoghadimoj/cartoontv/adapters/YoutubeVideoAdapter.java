package com.twoghadimoj.cartoontv.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.twoghadimoj.cartoontv.R;
import com.twoghadimoj.cartoontv.UnFavouriteCallback;
import com.twoghadimoj.cartoontv.database.DBHandler;
import com.twoghadimoj.cartoontv.database.DBOperations;
import com.twoghadimoj.cartoontv.enums.CARTOON_CATEGORY;
import com.twoghadimoj.cartoontv.helpers.ThumbnailHelper;
import com.twoghadimoj.cartoontv.helpers.ThumbnailListAvailableInFiles;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeViewHolder> {
    private static final String TAG = YoutubeVideoAdapter.class.getSimpleName();
    private boolean orientationLand;
    private Context context;
    private ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList, originalYoutubeVideoModelArrayList;
    private final OnItemClickListener listener;
    private ThumbnailHelper thumbnailHelper;
    private DBOperations dbOperations;
    private UnFavouriteCallback unFavouriteCallback;
    public ArrayList<YoutubeVideoModel> getYoutubeVideoModelArrayList() {
        return youtubeVideoModelArrayList;
    }

    public interface OnItemClickListener {
        void onItemClick(YoutubeVideoModel item);
    }

    public YoutubeVideoAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList, OnItemClickListener listener) {
        this.context = context;
        this.thumbnailHelper = new ThumbnailHelper(context);
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
        this.originalYoutubeVideoModelArrayList = new ArrayList<>(youtubeVideoModelArrayList);
        this.listener = listener;
        dbOperations = new DBOperations(context);
    }
    public YoutubeVideoAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList, OnItemClickListener listener, boolean orientationLand) {
        this.context = context;
        this.thumbnailHelper = new ThumbnailHelper(context);
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
        this.originalYoutubeVideoModelArrayList = new ArrayList<>(youtubeVideoModelArrayList);
        this.listener = listener;
        this.orientationLand = orientationLand;
        dbOperations = new DBOperations(context);
        this.unFavouriteCallback = null;
    }

    public void setYoutubeVideoModelArrayList(ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList) {
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
        this.originalYoutubeVideoModelArrayList = new ArrayList<>(youtubeVideoModelArrayList);
    }

    public YoutubeVideoAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList, OnItemClickListener listener, boolean orientationLand, UnFavouriteCallback unFavouriteCallback) {
        this.context = context;
        this.thumbnailHelper = new ThumbnailHelper(context);
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
        this.originalYoutubeVideoModelArrayList = new ArrayList<>(youtubeVideoModelArrayList);
        this.listener = listener;
        this.orientationLand = orientationLand;
        this.unFavouriteCallback = unFavouriteCallback;
        dbOperations = new DBOperations(context);
    }
    @NotNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(orientationLand ? R.layout.youtube_custom_video_layout_landscape : R.layout.youtube_custom_video_layout, parent, false);

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

    public ArrayList<YoutubeVideoModel> getOriginalYoutubeVideoModelArrayList() {
        return new ArrayList<>(originalYoutubeVideoModelArrayList);
    }

    @Override
    public void onBindViewHolder(YoutubeViewHolder holder, final int position) {

        final YoutubeVideoModel youtubeVideoModel = youtubeVideoModelArrayList.get(position);
        holder.videoTitle.setText(youtubeVideoModel.getTitle());
        if (youtubeVideoModel.isWatchedByUser()) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progressBar.setEnabled(true);
            holder.progressBar.setMax(youtubeVideoModel.getDurationInSeconds());
            holder.progressBar.setProgress(youtubeVideoModel.getDurWatchedInSeconds());
        }
        Picasso.get()
                .load(CARTOON_CATEGORY.getDrawableIcon(context, youtubeVideoModel.getCartoonCategoryName()))
                .fit().centerCrop()
                .into(holder.categoryImgCardView);
        holder.videoDurationTextView.setText(youtubeVideoModel.getFormattedDuation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(youtubeVideoModel);
            }
        });
        holder.heartButton.setLiked(youtubeVideoModel.isFavourite());
        holder.heartButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(!youtubeVideoModel.isFavourite()){
                    dbOperations.setVideoAsFavourite(youtubeVideoModel.getVideoId());
                    youtubeVideoModel.setFavourite(true);
                    Toast.makeText(context,"Added to Favorites",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if(youtubeVideoModel.isFavourite()){
                    dbOperations.setVideoAsUnFavourite(youtubeVideoModel.getVideoId());
                    youtubeVideoModel.setFavourite(false);
                    Toast.makeText(context,"Removed from Favorites",Toast.LENGTH_SHORT).show();
                    if(unFavouriteCallback!=null)
                        unFavouriteCallback.onUnfavourite(position);
                }
            }
        });
        holder.loadingPanel.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(Uri.parse("https://img.youtube.com/vi/" + youtubeVideoModel.getVideoId() + "/0.jpg"))
                .into(holder.videoThumbnailImageView1);
//        holder.videoThumbnailImageView1.setVisibility(View.GONE);
//        holder.videoThumbnailImageView.setVisibility(View.GONE);
//        loadThumbnail(holder,youtubeVideoModel);
    }
    private String getDeveloperKey(Context context){
        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            return bundle.getString("com.twoghadimoj.cartoontv.youtubeAPIKey");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
//    private void loadThumbnail(YoutubeViewHolder holder, YoutubeVideoModel youtubeVideoModel) {
//
//        if (ThumbnailListAvailableInFiles.getThumbnailMap().containsKey(youtubeVideoModel.getVideoId() + ".png")) {
//            Bitmap thumbnailBitmapFromFiles = null;
//            thumbnailBitmapFromFiles = thumbnailHelper.getThumbnailFromFilesDirectory(youtubeVideoModel);
//            holder.videoThumbnailImageView1.setVisibility(View.VISIBLE);
//            holder.videoThumbnailImageView1.setImageBitmap(thumbnailBitmapFromFiles);
////            File imgFile = new File(context.getFilesDir().getAbsolutePath() + "/" + youtubeVideoModel.getVideoId() + ".png");
////            Picasso.get().load(imgFile).fit().centerCrop().into(holder.videoThumbnailImageView1);
//            Log.d("TRACE_BITMAP", "Loading Thumbnail From Files");
//        } else {
//            holder.videoThumbnailImageView.setVisibility(View.VISIBLE);
//            /*  initialize the thumbnail image view , we need to pass Developer Key */
//            holder.videoThumbnailImageView.initialize(getDeveloperKey(context), new YouTubeThumbnailView.OnInitializedListener() {
//                @Override
//                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
//                    //when initialization is success, set the video id to thumbnail to load
//                    youTubeThumbnailLoader.setVideo(youtubeVideoModel.getVideoId());
//
//                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//                        @Override
//                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//                            holder.loadingPanel.setVisibility(View.GONE);
//                            youTubeThumbnailLoader.release();
//                            Log.d("TRACE_BITMAP", "YT Thumbnail View");
//                            //Store thumbnail in files directory
//                            Drawable drawable = youTubeThumbnailView.getDrawable();
////                            Log.d("TRACING","Started storing in files: "+youtubeVideoModel.getVideoId());
//                            thumbnailHelper.storeThumbnailInFilesDirectory(drawable, youtubeVideoModel);
//                            //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
//                        }
//
//                        @Override
//                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//                            //print or show error when thumbnail load failed
//                            Log.e("TRACE_BITMAP", "Youtube Thumbnail Error");
//                        }
//                    });
//                }
//
//                @Override
//                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
//                    //print or show error when initialization failed
//                    Log.e(TAG, "Youtube Initialization Failure");
//
//                }
//            });
//        }
//    }

    public void filterList(ArrayList<YoutubeVideoModel> filterlist) {
        youtubeVideoModelArrayList.clear();
        youtubeVideoModelArrayList = filterlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return youtubeVideoModelArrayList != null ? youtubeVideoModelArrayList.size() : 0;
    }

}