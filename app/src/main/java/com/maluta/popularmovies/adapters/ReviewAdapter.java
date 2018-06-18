package com.maluta.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maluta.popularmovies.R;
import com.maluta.popularmovies.model.Review;

import java.util.ArrayList;

/**
 * Created by admin on 6/12/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {
    private static final String TAG = ReviewAdapter.class.getName();
    private ArrayList<Review> reviews;
    private Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void OnListItemClick(Review review);
    }

    public ReviewAdapter(Context mContext, ListItemClickListener listener) {
        this.mContext = mContext;
        mOnClickListener = listener;
    }

    public void setReviewsData (ArrayList<Review> items){
        reviews = items;
        Log.d(TAG, "trailers.size() = " + reviews.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_vh, parent,false);
        return new ReviewHolder(view, mOnClickListener, reviews);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        holder.setData(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        if (reviews == null) {
            return 0;
        }
        return reviews.size();
    }
}
