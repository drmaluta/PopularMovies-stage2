package com.maluta.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.GridView;

import com.maluta.popularmovies.adapters.MovieAdapter;
import com.maluta.popularmovies.interfaces.OnTaskCompleted;
import com.maluta.popularmovies.model.Movie;
import com.maluta.popularmovies.utils.MovieTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.grid_view)
    GridView mGridView;
    @BindView(R.id.navigationView)
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), MovieDetail.class);
                intent.putExtra(getResources().getString(R.string.movie_parcel), movie);

                startActivity(intent);
            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                handleBottomNavigationItemSelected(item);
                return true;
            }
        });

        if(savedInstanceState == null){
            if (networkAvailable()) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(getString(R.string.pref_key), getString(R.string.key_sort_pop));
                editor.apply();
                loadMovies();
            }
        }else {

            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray(getString(R.string.movie_parcel));

            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
                Movie[] movies = new Movie[numMovieObjects];
                for (int i = 0; i < numMovieObjects; i++) {
                    movies[i] = (Movie) parcelable[i];
                }
                mGridView.setAdapter(new MovieAdapter(this, movies));
            }
        }
    }


    /**
     * This method will handle BottomNavigation
     */
    private void handleBottomNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_popular:
                if (networkAvailable()) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.pref_key), getString(R.string.key_sort_pop));
                    editor.apply();
                    setTitle(R.string.app_name);
                    loadMovies();
                }
                break;
            case R.id.navigation_top_rated:
                if (networkAvailable()) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.pref_key), getString(R.string.key_sort_rating));
                    editor.apply();
                    setTitle(R.string.sort_rating);
                    loadMovies();
                }
                break;
            case R.id.navigation_refresh:
                if (networkAvailable()) {
                    loadMovies();
                }
                break;
        }
    }

    /**
     * in this method i will get the objects from gridview and save to bundle
     * @param outState save the objects to bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int mGridViewCount = mGridView.getCount();
        if ( mGridViewCount > 0) {

            Movie[] movies = new Movie[ mGridViewCount];
            for (int i = 0; i <  mGridViewCount; i++) {
                movies[i] = (Movie) mGridView.getItemAtPosition(i);
            }

            outState.putParcelableArray(getString(R.string.movie_parcel), movies);
        }

        super.onSaveInstanceState(outState);
    }

    /**
     * This method will load movies
     */
    private void loadMovies(){
        OnTaskCompleted onTaskCompleted = new OnTaskCompleted() {
            @Override
            public void onFetchMovie(Movie[] movies) {
                Log.d(LOG_TAG, "Movies size " + movies.length);
                mGridView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
            }
        };

        MovieTask movieTask = new MovieTask(onTaskCompleted, this);
        movieTask.execute();
    }

    /**
     * This method will check my internet connection
     * @return true if connected to internet and false if not
     */
    private boolean networkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
