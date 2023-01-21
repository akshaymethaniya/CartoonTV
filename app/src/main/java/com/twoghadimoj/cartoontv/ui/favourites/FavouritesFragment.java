package com.twoghadimoj.cartoontv.ui.favourites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twoghadimoj.cartoontv.PlayYoutubeVideo;
import com.twoghadimoj.cartoontv.R;
import com.twoghadimoj.cartoontv.UnFavouriteCallback;
import com.twoghadimoj.cartoontv.adapters.YoutubeVideoAdapter;
import com.twoghadimoj.cartoontv.database.DBOperations;
import com.twoghadimoj.cartoontv.helpers.YTDummyData;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;
import com.twoghadimoj.cartoontv.ui.home.HomeViewModel;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FavouritesFragment extends Fragment implements UnFavouriteCallback {

    private FavouritesViewModel favouritesViewModel;
    private RecyclerView recyclerView;
    private View root;
    private YoutubeVideoAdapter youtubeVideoAdapter = null;
    private boolean orientationLand;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        favouritesViewModel =
                new ViewModelProvider(this).get(FavouritesViewModel.class);
        root = inflater.inflate(R.layout.fragment_favourites, container, false);
        orientationLand = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE? true : false;
        setUpRecyclerView();
        populateRecyclerView();
        return root;
    }
    private void setUpRecyclerView() {
        recyclerView = root.findViewById(R.id.youtube_videos);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void populateRecyclerView() {
        SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        boolean dbLoaded = sh.getBoolean("dbLoaded",false);
        ArrayList<YoutubeVideoModel> youtubeVideoList;
        if(!dbLoaded){
            //Log.d("DBLOAD","Loading data from file");
            Toast.makeText(getContext(),"No Videos Found",Toast.LENGTH_SHORT).show();
            return;
        }else {
            youtubeVideoList = new DBOperations(getContext()).getFavouriteVideos();
        }
        //No Videos
        if(youtubeVideoList.size() == 0){
            View noVideosView = root.findViewById(R.id.noVideosLayout);
            noVideosView.setVisibility(View.VISIBLE);
        }
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(getContext(), youtubeVideoList, new YoutubeVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeVideoModel item) {
//                Toast.makeText(getContext(), "Item Clicked: "+item.getVideoId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), PlayYoutubeVideo.class);
                intent.putExtra("videoItem",item);
                startActivity(intent);
            }
        },orientationLand,this::onUnfavourite);
        adapter.setHasStableIds(true);
        this.youtubeVideoAdapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onUnfavourite(int itemIndexUnfavourited) {
        youtubeVideoAdapter.getYoutubeVideoModelArrayList().remove(itemIndexUnfavourited);
        youtubeVideoAdapter.notifyDataSetChanged();
//        youtubeVideoAdapter.setYoutubeVideoModelArrayList();
        Log.d("UPDATED","ture");
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        //Update the Flag here
        orientationLand = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? true : false);
        populateRecyclerView();
    }
}