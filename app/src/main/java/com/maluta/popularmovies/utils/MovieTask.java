package com.maluta.popularmovies.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.maluta.popularmovies.interfaces.OnTaskCompleted;
import com.maluta.popularmovies.model.Movie;

/**
 * Created by admin on 6/5/2018.
 */

public class MovieTask extends AsyncTask<String,Void,Movie[]> {
    private final String TAG = MovieTask.class.getName();

    private OnTaskCompleted onTaskCompleted;
    private Context mContext;

    public MovieTask(OnTaskCompleted taskCompleted, Context context){
        super();
        onTaskCompleted = taskCompleted;
        mContext = context;
    }

    @Override
    protected Movie[] doInBackground(String... strings) {
        Movie[] movies = NavUtils.fetchMovieData(mContext);

        return movies;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        onTaskCompleted.onFetchMovie(movies);
    }
}

