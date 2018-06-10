package com.maluta.popularmovies.utils;

/**
 * Created by admin on 6/4/2018.
 */

import com.maluta.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
    private static final String TAG = JsonUtils.class.getName();
    private static final String KEY_RESULTS = "results";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_VOTE_COUNT = "vote_count";

    public static Movie[] getMovieFromJson(String json) throws JSONException{
        // Get the array containing the movies found
        JSONObject moviesJson = new JSONObject(json);
        JSONArray resultsArray = moviesJson.getJSONArray(KEY_RESULTS);

        // Create array of Movie objects that stores data from the JSON string
        Movie[] movies = new Movie[resultsArray.length()];

        // Traverse through movies one by one and get data
        for (int i = 0; i < resultsArray.length(); i++) {
            // Initialize each object before it can be used
            movies[i] = new Movie();

            // Object contains all tags we're looking for
            JSONObject movieInfo = resultsArray.getJSONObject(i);

            movies[i].setTitle(movieInfo.getString(KEY_ORIGINAL_TITLE));
            movies[i].setPosterPath(movieInfo.getString(KEY_POSTER_PATH));
            movies[i].setOverview(movieInfo.getString(KEY_OVERVIEW));
            movies[i].setUserRating(movieInfo.getDouble(KEY_VOTE_AVERAGE));
            movies[i].setReleaseDate(movieInfo.getString(KEY_RELEASE_DATE));
            movies[i].setVoteCount(movieInfo.getLong(KEY_VOTE_COUNT));
        }
        return movies;
    }
}
