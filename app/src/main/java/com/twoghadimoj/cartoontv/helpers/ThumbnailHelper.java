package com.twoghadimoj.cartoontv.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThumbnailHelper {
    private Context context;

    public ThumbnailHelper(Context context) {
        this.context = context;
    }

    public void storeThumbnailInFilesDirectory(Drawable drawable, YoutubeVideoModel youtubeVideoModel) {
        StoreThumbnailTask storeThumbnailTask = new StoreThumbnailTask(youtubeVideoModel,context);
        storeThumbnailTask.execute(drawable);
    }

//    public void storeThumbnailInFilesDirectory(Bitmap bitmap, YoutubeVideoModel youtubeVideoModel) {
//        StoreThumbnailTask storeThumbnailTask = new StoreThumbnailTask(youtubeVideoModel,context);
//        storeThumbnailTask.execute(bitmap);
//    }

    public Bitmap getThumbnailFromFilesDirectory(YoutubeVideoModel youtubeVideoModel){
        File imgFile = new File(context.getFilesDir().getAbsolutePath() + "/" + youtubeVideoModel.getVideoId() + ".png");
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return bitmap;
        }
        return null;
    }

    public class StoreThumbnailTask extends AsyncTask<Drawable, Void, Void> {
        private YoutubeVideoModel youtubeVideoModel;
        private Context context;

        public StoreThumbnailTask(YoutubeVideoModel youtubeVideoModel, Context context) {
            this.youtubeVideoModel = youtubeVideoModel;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Drawable... drawables) {
            File cacheDir = context.getFilesDir();
            String PATH = cacheDir.getAbsolutePath() + "/" + youtubeVideoModel.getVideoId() + ".png";
            try {
                File file = new File(PATH);
                FileOutputStream fos = new FileOutputStream(file);
                Bitmap bitmap = ((BitmapDrawable) drawables[0]).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                //Log.d("TRACING","Stored successfully: "+youtubeVideoModel.getVideoId());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("TRACE_BITMAP", e.getMessage());
            }
            return null;
        }
    }
}
