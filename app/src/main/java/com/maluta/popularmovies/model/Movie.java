package com.maluta.popularmovies.model;

/**
 * Created by admin on 6/3/2018.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String title;
    private String posterPath;
    private String overview;
    private Double userRating;
    private String releaseDate;
    private long voteCount;
    private static final String MOVIE_TMDB_URL = "https://image.tmdb.org/t/p/w185";
    private static final String Date_TMDB = "yyyy-MM-dd";

    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return MOVIE_TMDB_URL + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    /**
     *
     * @return the date format
     */
    public String getDateFormat(){

        return Date_TMDB;
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
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeValue(userRating);
        dest.writeValue(voteCount);
    }

    private Movie(Parcel in){
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        userRating = (double) in.readValue(double.class.getClassLoader());
        voteCount = (long) in.readValue(long.class.getClassLoader());


    }
}
