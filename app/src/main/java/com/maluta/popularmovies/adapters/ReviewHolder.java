package com.maluta.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.maluta.popularmovies.model.Review;
import com.maluta.popularmovies.adapters.ReviewAdapter.ListItemClickListener;
import com.maluta.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by admin on 6/12/2018.
 */

public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView authorTv;
    private TextView contentTv;
    private ArrayList<Review> reviews;
    private ListItemClickListener mOnClickListener;

    public ReviewHolder(View itemView, ListItemClickListener listener, ArrayList<Review> items) {
        super(itemView);
        authorTv = itemView.findViewById(R.id.tv_author);
        contentTv = itemView.findViewById(R.id.tv_content);
        mOnClickListener = listener;
        reviews = items;
        itemView.setOnClickListener(this);
    }

    public void setData (Review item) {
        authorTv.setText(item.getAuthor());
        contentTv.setText(item.getContent());
    }

    @Override
    public void onClick(View v) {
        int adapterPosition = getAdapterPosition();
        mOnClickListener.OnListItemClick(reviews.get(adapterPosition));
    }
}
