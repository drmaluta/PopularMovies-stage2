package com.maluta.popularmovies.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.maluta.popularmovies.R;
import com.maluta.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 6/4/2018.
 */

public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Movie> mMovies;


    public  MovieAdapter(Context context){
        mContext = context;
    }

    public void setData(ArrayList<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mMovies == null || mMovies.size() == 0){
            return 0;
        }
        return mMovies.size();
    }

    @Override
    public Object getItem(int position) {
        if(mMovies == null || mMovies.size()== 0){
            return  null;
        }
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView movieImageView;

        if(convertView == null){
            movieImageView = new ImageView(mContext);
            movieImageView.setAdjustViewBounds(true);
        }else {
            movieImageView = (ImageView) convertView;
        }
        Picasso.with(mContext)
                .load("https://image.tmdb.org/t/p/w185" + mMovies.get(position).getPosterPath())
                .resize(187,285)
                .error(R.drawable.alert_circle_outline)//when we get an error
                .placeholder(R.drawable.loading)// as the image loads
                .into(movieImageView);// the target

        return movieImageView;
    }
}
