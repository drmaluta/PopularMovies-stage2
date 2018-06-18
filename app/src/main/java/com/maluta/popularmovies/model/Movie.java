package com.maluta.popularmovies.model;

/**
 * Created by admin on 6/3/2018.
 */
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movie")
public class Movie implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    private Integer id;
    @SerializedName("original_title")
    private String title;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("vote_average")
    private Double userRating;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_count")
    private Integer voteCount;


    @Ignore
    public Movie() {
    }

    public Movie(Integer id, String title, String posterPath, String overview, Double userRating, String releaseDate, Integer voteCount) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.voteCount = voteCount;
    }

    public Integer getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }


    public String getRatingAverage(){

        return String.valueOf(getUserRating()) + "/10";

    }

    public String getMovieVotes() {
        return String.valueOf(getVoteCount() + " Votes");
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeValue(userRating);
        dest.writeValue(voteCount);
    }

    private Movie(Parcel in){
        id = (Integer) in.readValue(Integer.class.getClassLoader());
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        userRating = (double) in.readValue(double.class.getClassLoader());
        voteCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }
}
