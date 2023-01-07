package com.twoghadimoj.cartoontv;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.twoghadimoj.cartoontv.database.DBHandler;
import com.twoghadimoj.cartoontv.ui.dashboard.DashboardFragment;
import com.twoghadimoj.cartoontv.ui.home.HomeFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
//    final Fragment homeFragment = new HomeFragment();
//    final Fragment catFragment = new DashboardFragment();
//    String activeFragment = "HOME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set title bar
//        getSupportActionBar().setLogo(R.mipmap.cartoon_launcher_icon_foreground);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
//                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP |
//                ActionBar.DISPLAY_USE_LOGO);
//        getSupportActionBar().setIcon(R.mipmap.cartoon_launcher_icon_foreground);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                loadDBIfNotLoaded();
//            }
//        });
//        thread.start();

        new LoadDBTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.actionSearch) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadDBIfNotLoaded() {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        boolean dbLoaded = sh.getBoolean("dbLoaded",false);
        if(!dbLoaded){
            DBHandler dbHandler = new DBHandler(this);
            boolean res = dbHandler.loadData();
            if(!res) return;
            //Log.d("DB_LOAD","Loaded db");
            SharedPreferences.Editor myEdit = sh.edit();
            myEdit.putBoolean("dbLoaded",true);
            myEdit.commit();
        }
    }

    public class LoadDBTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            loadDBIfNotLoaded();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Log.d("DBLOAD","Loaded Data");
            navController.navigate(R.id.navigation_home);
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}