package com.maluta.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by admin on 6/12/2018.
 */

public class ReviewsResponse {
    @SerializedName("results")
    private ArrayList<Review> reviews;

    public ArrayList<Review> getResults() {
        return reviews;
    }

    public void setResults(ArrayList<Review> trailers) {
        this.reviews = trailers;
    }
}
