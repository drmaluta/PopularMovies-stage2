package com.maluta.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maluta.popularmovies.R;
import com.maluta.popularmovies.model.Trailer;

import java.util.ArrayList;

/**
 * Created by admin on 6/11/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerHolder> {
    private ArrayList<Trailer> trailers = new ArrayList<>();
    private Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void OnListItemClick(Trailer trailer);
    }

    public TrailerAdapter(Context mContext, ListItemClickListener listener) {
        this.mContext = mContext;
        mOnClickListener = listener;
    }

    public void setTrailersData (ArrayList<Trailer> items){
        trailers = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_vh, parent,false);
        return new TrailerHolder(view, mContext, mOnClickListener, trailers);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        holder.setData(trailers.get(position));
    }

    @Override
    public int getItemCount() {
        if (trailers == null) {
            return 0;
        }
        return trailers.size();
    }
}
