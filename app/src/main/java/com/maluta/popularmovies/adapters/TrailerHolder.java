package com.maluta.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maluta.popularmovies.R;
import com.maluta.popularmovies.adapters.TrailerAdapter.ListItemClickListener;
import com.maluta.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 6/11/2018.
 */

public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView trailerIv;
    private TextView nameTv;
    private TextView typeTv;

    private Context mContext;
    private ListItemClickListener mOnClickListener;
    private ArrayList<Trailer> trailers;

    public TrailerHolder(View itemView, Context context, ListItemClickListener listener, ArrayList<Trailer> items) {
        super(itemView);
        trailerIv = itemView.findViewById(R.id.iv_trailer);
        nameTv = itemView.findViewById(R.id.tv_trailer_name);
        typeTv = itemView.findViewById(R.id.tv_trailer_type);
        mContext = context;
        mOnClickListener = listener;
        trailers = items;
        itemView.setOnClickListener(this);
    }

    public void setData (Trailer item) {
       // trailer = item;
        Log.d("ImagePath data",item.getImagePath());
        Picasso.with(mContext)
                .load(item.getImagePath())
                .error(R.drawable.alert_circle_outline)//when we get an error
                .placeholder(R.drawable.loading)// as the image loads
                .into(trailerIv);
        nameTv.setText(item.getName());
        typeTv.setText(item.getType());
    }

    @Override
    public void onClick(View v) {
        int adapterPosition = getAdapterPosition();
        mOnClickListener.OnListItemClick(trailers.get(adapterPosition));
    }
}
