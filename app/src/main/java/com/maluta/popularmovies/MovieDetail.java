package com.maluta.popularmovies;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.maluta.popularmovies.model.Movie;
import com.maluta.popularmovies.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetail extends AppCompatActivity {
    private static final String TAG = MovieDetail.class.getName();

    @BindView(R.id.textview_overview)
    TextView tvOverview;
    @BindView(R.id.textview_vote_average)
    TextView voteAverage;
    @BindView(R.id.textview_vote_average_title)
    TextView voteCount;
    @BindView(R.id.textview_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.imageview_poster)
    ImageView imageViewPoster;
    @BindView(R.id.textview_release_date_title)
    TextView releaseDateTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.movie_parcel));
        setTitle(movie.getTitle());

        float vote = movie.getUserRating().floatValue();
        AppCompatRatingBar ratingBar = findViewById(R.id.rating);
        ratingBar.setIsIndicator(true);
        ratingBar.setStepSize(0.1f);
        ratingBar.setRating(vote);

        //originalTitle.setText(movie.getTitle());
        releaseDateTitle.setText(getResources().getString(R.string.date));

        Picasso.with(this)
                .load(movie.getPosterPath())
                .resize(187,285)
                .placeholder(R.drawable.loading)
                .error(R.drawable.alert_circle_outline)
                .into(imageViewPoster);

        String overView = movie.getOverview();
        if(overView == null){
            tvOverview.setTypeface(null, Typeface.BOLD_ITALIC);
            overView = getResources().getString(R.string.no_overview_available);
        }
        tvOverview.setText(overView);
        voteAverage.setText(movie.getRatingAverage());
        voteCount.setText(movie.getMovieVotes());

        String releaseDate = movie.getReleaseDate();

        if(releaseDate != null){

            try{
                releaseDate = TimeUtils.getLocalizedDate(this, releaseDate,movie.getDateFormat());
            }catch (ParseException pe){
                Log.e(TAG,String.valueOf(R.string.release_date_error),pe);
            }
        }else {

            releaseDate = getResources().getString(R.string.no_date_found);

        }
        tvReleaseDate.setText(releaseDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
