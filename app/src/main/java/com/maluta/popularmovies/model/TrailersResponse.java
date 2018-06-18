package com.maluta.popularmovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by admin on 6/11/2018.
 */

public class TrailersResponse {
    @SerializedName("results")
    private ArrayList<Trailer> trailers;

    public ArrayList<Trailer> getResults() {
        return trailers;
    }

    public void setResults(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
    }
}
