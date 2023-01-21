package com.twoghadimoj.cartoontv.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.twoghadimoj.cartoontv.PlayYoutubeVideo;
import com.twoghadimoj.cartoontv.R;
import com.twoghadimoj.cartoontv.database.DBOperations;
import com.twoghadimoj.cartoontv.helpers.YTDummyData;
import com.twoghadimoj.cartoontv.models.YoutubeVideoModel;
import com.twoghadimoj.cartoontv.adapters.YoutubeVideoAdapter;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements SearchView.OnCloseListener, SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private View root;
    private YoutubeVideoAdapter youtubeVideoAdapter = null;
    private boolean orientationLand;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        this.root = root;
        setHasOptionsMenu(true);
        orientationLand = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE? true : false;
        setUpRecyclerView();
        populateRecyclerView();
        return root;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<YoutubeVideoModel> filteredlist = new ArrayList<YoutubeVideoModel>();
        Log.d("TRACE","Total videos : "+youtubeVideoAdapter.getOriginalYoutubeVideoModelArrayList().size());
        // running a for loop to compare elements.
        for (YoutubeVideoModel item : youtubeVideoAdapter.getOriginalYoutubeVideoModelArrayList()) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            youtubeVideoAdapter.filterList(filteredlist);
        }
    }
    /**
     * setup the recyclerview here
     */
    private void setUpRecyclerView() {
        recyclerView = root.findViewById(R.id.youtube_videos);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    /**
     * populate the recyclerview and implement the click event here
     */
    private void populateRecyclerView() {
        Log.d("POPULATE","Inside");
        SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        boolean dbLoaded = sh.getBoolean("dbLoaded",false);
        ArrayList<YoutubeVideoModel> youtubeVideoList;
        if(!dbLoaded){
            //Log.d("DBLOAD","Loading data from file");
            youtubeVideoList = new YTDummyData().getRandomVideos(getContext());
        }else {
//        final ArrayList<YoutubeVideoModel> youtubeVideoList = new YTDummyData().getRandomVideos(getContext());
            youtubeVideoList = new DBOperations(getContext()).getAllVideos();
//        YoutubeVideoList.setYoutubeVideoModels(youtubeVideoList);
        }
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(getContext(), youtubeVideoList, new YoutubeVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeVideoModel item) {
//                Toast.makeText(getContext(), "Item Clicked: "+item.getVideoId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), PlayYoutubeVideo.class);
                intent.putExtra("videoItem",item);
                startActivity(intent);
            }
        },orientationLand,null);
        adapter.setHasStableIds(true);
        this.youtubeVideoAdapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(!query.isEmpty())
            filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.app_bar_menu, menu);
        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onClose() {
        youtubeVideoAdapter.filterList(youtubeVideoAdapter.getOriginalYoutubeVideoModelArrayList());
        return false;
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