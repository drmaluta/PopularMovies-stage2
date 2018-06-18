package com.maluta.popularmovies;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maluta.popularmovies.adapters.ReviewAdapter;
import com.maluta.popularmovies.adapters.TrailerAdapter;
import com.maluta.popularmovies.database.AppDatabase;
import com.maluta.popularmovies.database.AppExecutors;
import com.maluta.popularmovies.model.Movie;
import com.maluta.popularmovies.model.Review;
import com.maluta.popularmovies.model.ReviewsResponse;
import com.maluta.popularmovies.model.Trailer;
import com.maluta.popularmovies.model.TrailersResponse;
import com.maluta.popularmovies.utils.ApiClient;
import com.maluta.popularmovies.utils.MovieClient;
import com.maluta.popularmovies.utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetail extends AppCompatActivity
        implements TrailerAdapter.ListItemClickListener, ReviewAdapter.ListItemClickListener{
    private static final String TAG = MovieDetail.class.getName();
    private final static String BASE_URL_TRAILER_VIDEO = "https://www.youtube.com/watch?v=";

    @BindView(R.id.detail_scrollView)
    NestedScrollView mScrollView;
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
    @BindView(R.id.tv_trailer_title)
    TextView trailerTitle;
    @BindView(R.id.textview_release_date_title)
    TextView releaseDateTitle;
    @BindView(R.id.trailers_rv)
    RecyclerView trailersRv;
    @BindView(R.id.tv_reviews_title)
    TextView reviewTitle;
    @BindView(R.id.reviews_rv)
    RecyclerView reviewsRv;
    @BindView(R.id.div_trailers)
    View divTrailers;
    @BindView(R.id.div_reviews)
    View divReviews;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    TrailerAdapter mTrailerAdapter;
    ReviewAdapter mReviewAdapter;

    private AppDatabase mDb;
    private boolean isFavorite;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(getString(R.string.movie_parcel));
        if (savedInstanceState == null) {
            isFavorite = intent.getBooleanExtra(getResources().getString(R.string.is_favorite), false);
        }

        addFavoritesIcon();
        setTitle(movie.getTitle());

        float vote = movie.getUserRating().floatValue();
        AppCompatRatingBar ratingBar = findViewById(R.id.rating);
        ratingBar.setIsIndicator(true);
        ratingBar.setStepSize(0.1f);
        ratingBar.setRating(vote);


        releaseDateTitle.setText(getResources().getString(R.string.date));

        Picasso.with(this)
                .load("https://image.tmdb.org/t/p/w185" + movie.getPosterPath())
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
                releaseDate = TimeUtils.getLocalizedDate(this, releaseDate);
            }catch (ParseException pe){
                Log.e(TAG,String.valueOf(R.string.release_date_error),pe);
            }
        }else {

            releaseDate = getResources().getString(R.string.no_date_found);

        }
        tvReleaseDate.setText(releaseDate);

        Integer id = movie.getId();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isFavorite) {
                    isFavorite = true;
                    addFavoritesIcon();
                    addMovie();
                } else {
                    isFavorite = false;
                    addFavoritesIcon();
                    deleteMovie();
                }
            }
        });

        if (networkAvailable()) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mTrailerAdapter = new TrailerAdapter(this, this);
            trailerTitle.setText(R.string.trailers);
            trailersRv.setLayoutManager(linearLayoutManager);
            trailersRv.setAdapter(mTrailerAdapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mReviewAdapter = new ReviewAdapter(this, this);
            reviewTitle.setText(R.string.reviews);
            reviewsRv.setLayoutManager(layoutManager);
            reviewsRv.setAdapter(mReviewAdapter);

            loadTrailersAndReviews(id);
        } else {
            trailerTitle.setVisibility(View.GONE);
            trailersRv.setVisibility(View.GONE);
            divTrailers.setVisibility(View.GONE);

            reviewTitle.setVisibility(View.GONE);
            reviewsRv.setVisibility(View.GONE);
            divReviews.setVisibility(View.GONE);
        }

    }


    private void loadTrailersAndReviews(Integer movieId) {
        MovieClient client = new ApiClient().getClient().create(MovieClient.class);
        Call<TrailersResponse> callTrailers = client.getTrailers(movieId, MainActivity.my_api_key);
        callTrailers.enqueue(new Callback<TrailersResponse>() {

            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                ArrayList<Trailer> trailers = response.body().getResults();
                if (trailers.size() == 0){
                    trailerTitle.setVisibility(View.GONE);
                    trailersRv.setVisibility(View.GONE);
                    divTrailers.setVisibility(View.GONE);
                }
                mTrailerAdapter.setTrailersData(trailers);
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });

        Call<ReviewsResponse> callReviews = client.getReviews(movieId, MainActivity.my_api_key);
        callReviews.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                ArrayList<Review> reviews = response.body().getResults();
                if (reviews.size() == 0){
                    reviewTitle.setVisibility(View.GONE);
                    reviewsRv.setVisibility(View.GONE);
                    divReviews.setVisibility(View.GONE);
                }
                mReviewAdapter.setReviewsData(reviews);
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });
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



    private void addMovie(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovie(movie);
            }
        });

    }

    private void deleteMovie(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovie(movie);
            }
        });
    }

    private void addFavoritesIcon( ){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(getSupportActionBar().getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(getSupportActionBar().getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.ic_action_favorite);
        imageView.setColorFilter(Color.parseColor("#FFC107"), PorterDuff.Mode.SRC_ATOP);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.END
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 8;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        if (!isFavorite) {
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void OnListItemClick(Trailer trailer) {
        this.startActivity(new Intent(Intent.ACTION_VIEW, buildVideoUrl(trailer.getKey())));
    }

    private static Uri buildVideoUrl(String key) {
        String path = BASE_URL_TRAILER_VIDEO + key;
        return Uri.parse(path);
    }

    @Override
    public void OnListItemClick(Review review) {
        Intent intent = new Intent(getApplicationContext(), ReviewPopup.class);
        intent.putExtra(getString(R.string.review_parcel), review);
        this.startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("SCROLL_POSITION", new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});
        outState.putBoolean(getResources().getString(R.string.is_favorite), isFavorite);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
        if(position != null) {
            mScrollView.postDelayed(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(position[0], position[1]);
                }
            }, 300);
        }

        isFavorite = savedInstanceState.getBoolean(getResources().getString(R.string.is_favorite));
        addFavoritesIcon();
    }
}
