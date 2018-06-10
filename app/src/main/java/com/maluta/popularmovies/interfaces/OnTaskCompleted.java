package com.maluta.popularmovies.interfaces;

import com.maluta.popularmovies.model.Movie;

/**
 * Created by admin on 6/5/2018.
 */

public interface OnTaskCompleted {
    void onFetchMovie(Movie[] movies);
}
