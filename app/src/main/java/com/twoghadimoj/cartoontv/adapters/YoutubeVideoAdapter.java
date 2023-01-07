package com.twoghadimoj.cartoontv.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.twoghadimoj.cartoontv.R;
import com.twoghadimoj.cartoontv.enums.CARTOON_CATEGORY;
import com.twoghadimoj.cartoontv.helpers.ThumbnailHelper;
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
    }
    public YoutubeVideoAdapter(Context context, ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList, OnItemClickListener listener,boolean orientationLand) {
        this.context = context;
        this.thumbnailHelper = new ThumbnailHelper(context);
        this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;
        this.originalYoutubeVideoModelArrayList = new ArrayList<>(youtubeVideoModelArrayList);
        this.listener = listener;
        this.orientationLand = orientationLand;
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

        holder.loadingPanel.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(Uri.parse("https://img.youtube.com/vi/" + youtubeVideoModel.getVideoId() + "/0.jpg"))
                .into(holder.videoThumbnailImageView1);
    }

//    private void loadThumbnail(YoutubeViewHolder holder, YoutubeVideoModel youtubeVideoModel) {
//        if (ThumbnailListAvailableInFiles.getThumbnailMap().containsKey(youtubeVideoModel.getVideoId() + ".png")) {
//            Bitmap thumbnailBitmapFromFiles = null;
//            thumbnailBitmapFromFiles = thumbnailHelper.getThumbnailFromFilesDirectory(youtubeVideoModel);
//            holder.videoThumbnailImageView1.setVisibility(View.VISIBLE);
//            holder.videoThumbnailImageView1.setImageBitmap(thumbnailBitmapFromFiles);
////            File imgFile = new File(context.getFilesDir().getAbsolutePath() + "/" + youtubeVideoModel.getVideoId() + ".png");
////            Picasso.get().load(imgFile).fit().centerCrop().into(holder.videoThumbnailImageView1);
//            Log.d("TRACE_BITMAP", "Loading Thumbnail From Files");
//        } else {
//            /*  initialize the thumbnail image view , we need to pass Developer Key */
//            holder.videoThumbnailImageView.initialize(DeveloperKey.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
//                @Override
//                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
//                    //when initialization is success, set the video id to thumbnail to load
//                    youTubeThumbnailLoader.setVideo(youtubeVideoModel.getVideoId());
//
//                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
//                        @Override
//                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
//                            holder.loadingPanel.setVisibility(View.GONE);
//
//                            //Store thumbnail in files directory
//                            Drawable drawable = youTubeThumbnailView.getDrawable();
////                            Log.d("TRACING","Started storing in files: "+youtubeVideoModel.getVideoId());
//                            thumbnailHelper.storeThumbnailInFilesDirectory(drawable, youtubeVideoModel);
//                            //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
//                            youTubeThumbnailLoader.release();
//                        }
//
//                        @Override
//                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
//                            //print or show error when thumbnail load failed
//                            Log.e(TAG, "Youtube Thumbnail Error");
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