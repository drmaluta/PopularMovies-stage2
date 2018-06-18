package com.maluta.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by admin on 6/11/2018.
 */

public class MoviesResponse {
    @SerializedName("results")
    private ArrayList<Movie> movies;

    public ArrayList<Movie> getResults() {
        return movies;
    }

    public void setResults(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
