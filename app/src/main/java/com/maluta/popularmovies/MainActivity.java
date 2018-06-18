package com.maluta.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.maluta.popularmovies.data.PopularMoviesPreferences;
import com.maluta.popularmovies.model.MainViewModel;
import com.maluta.popularmovies.model.Movie;
import com.maluta.popularmovies.model.MoviesResponse;
import com.maluta.popularmovies.utils.ApiClient;
import com.maluta.popularmovies.utils.MovieClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    public static String my_api_key = BuildConfig.my_api;

    @BindView(R.id.grid_view)
    GridView mGridView;
    @BindView(R.id.navigationView)
    BottomNavigationView bottomNavigation;

    MovieAdapter mAdapter;
    private List<Movie> mFavoriteMovies;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    final String FAVORITE_TYPE = "favorite";
    final String OTHER_TYPE = "other";
    private String movieType = "normal type";
    private String title = "";
    private String MOVIE_TYPE_EXTRA = "movie_type";

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
                boolean isFavorite = compareToFavorite(movie);
                intent.putExtra(getResources().getString(R.string.movie_parcel), movie);
                intent.putExtra(getResources().getString(R.string.is_favorite), isFavorite);

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

        mAdapter = new MovieAdapter(this);

        if(savedInstanceState == null){
            if (networkAvailable()) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(getString(R.string.pref_key), getString(R.string.key_sort_pop));
                editor.apply();
                setupMainViewModel();
                loadMovieData(this, movieType);
            }
        } else {
            mMovies = savedInstanceState.
                    getParcelableArrayList(getString(R.string.movie_parcel));
            movieType = savedInstanceState.getString(MOVIE_TYPE_EXTRA);
            if (mMovies != null && !movieType.equals(FAVORITE_TYPE)) {
                mAdapter.setData(mMovies);
                mGridView.setAdapter(mAdapter);
            }

            title = savedInstanceState.getString("title");
            if (title.isEmpty()) {
                title = getString(R.string.app_name);
            }
            setTitle(title);
            setupMainViewModel();
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
                    title = getString(R.string.app_name);
                    setTitle(title);
                    movieType = OTHER_TYPE;
                    loadMovieData(this, movieType);
                }
                break;
            case R.id.navigation_top_rated:
                if (networkAvailable()) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(getString(R.string.pref_key), getString(R.string.key_sort_rating));
                    editor.apply();
                    title = getString(R.string.sort_rating);
                    setTitle(title);
                    movieType = OTHER_TYPE;
                    loadMovieData(this, movieType);
                }
                break;
            case R.id.navigation_favorite:
                if (networkAvailable()) {
                    title = getString(R.string.nav_favorite);
                    setTitle(title);
                    movieType = FAVORITE_TYPE;
                    loadMovieData(this, movieType);
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
            ArrayList<Movie> movies = new ArrayList<>();
            for (int i = 0; i <  mGridViewCount; i++) {
                movies.add((Movie) mGridView.getItemAtPosition(i));
            }
            outState.putParcelableArrayList(getString(R.string.movie_parcel), movies);
            outState.putString(MOVIE_TYPE_EXTRA, movieType);
            outState.putString("title", title);
        }

        super.onSaveInstanceState(outState);
    }

    /**
     * This method will load movies
     */
    private void loadMovieData (Context context, String type){
        if (type.equals(FAVORITE_TYPE)) {
            setupMainViewModel();
        } else {
            MovieClient client = new ApiClient().getClient().create(MovieClient.class);
            Call<MoviesResponse> call;
            if (PopularMoviesPreferences.isPopular(context)) {
                call = client.getPopularMovies(my_api_key);
            } else {
                call = client.getTopRatedMovies(my_api_key);
            }
            call.enqueue(new Callback<MoviesResponse>() {

                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    mMovies = response.body().getResults();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setData(mMovies);
                            mGridView.setAdapter(mAdapter);
                        }
                    });

                    //mAdapter.setMoviesData(movies);
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    //Show alert dialog
                    Log.e(LOG_TAG, "Error in retrofit " + t.getMessage());

                }
            });
        }
    }

    private void setupMainViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovies = (ArrayList<Movie>) movies;
                mFavoriteMovies = movies;
                if (movieType.equals(FAVORITE_TYPE)) {
                    Log.d(LOG_TAG, "Updating list of favorite movies from LiveData in ViewModel");
                    mAdapter.setData(mMovies);
                    mGridView.setAdapter(mAdapter);
                }
            }
        });
    }

    private boolean compareToFavorite(Movie movie) {
        if (mFavoriteMovies != null) {
            for (int i = 0; i < mFavoriteMovies.size(); i++) {
                if (movie.getId().equals(mFavoriteMovies.get(i).getId())) {
                    return true;
                }
            }
        }
        return false;
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
