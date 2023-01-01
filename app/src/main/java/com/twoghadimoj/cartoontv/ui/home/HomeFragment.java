package com.twoghadimoj.cartoontv.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twoghadimoj.cartoontv.PlayYoutubeVideo;
import com.twoghadimoj.cartoontv.R;
import com.twoghadimoj.cartoontv.helpers.YTDummyData;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;
import com.twoghadimoj.cartoontv.adapters.YoutubeVideoAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        this.root = root;

        setUpRecyclerView();
        populateRecyclerView();
        return root;
    }
    /**
     * setup the recyclerview here
     */
    private void setUpRecyclerView() {
        recyclerView = root.findViewById(R.id.youtube_videos);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recyclerview and implement the click event here
     */
    private void populateRecyclerView() {
//        final ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = generateDummyVideoList();
        final ArrayList<YoutubeVideoModel> youtubeVideoList = new YTDummyData().getRandomVideos(getContext());
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(getContext(), youtubeVideoList, new YoutubeVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeVideoModel item) {
//                Toast.makeText(getContext(), "Item Clicked: "+item.getVideoId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), PlayYoutubeVideo.class);
                intent.putExtra("videoItem",item);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

    }
}