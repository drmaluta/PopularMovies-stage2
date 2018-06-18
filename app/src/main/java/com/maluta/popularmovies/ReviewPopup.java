package com.maluta.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ScrollView;
import android.widget.TextView;

import com.maluta.popularmovies.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by admin on 6/12/2018.
 */

public class ReviewPopup extends Activity {
    @BindView(R.id.popup_scrollView)
    ScrollView mScrollView;
    @BindView(R.id.tv_rv_author)
    TextView authorRvTv;
    @BindView(R.id.tv_rv_content)
    TextView contentRvTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_review);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int hight = dm.heightPixels;

        getWindow().setLayout((int)(width * .8), (int)(hight * .8));

        ButterKnife.bind(this);
        Intent intent = getIntent();
        Review review = intent.getParcelableExtra(getString(R.string.review_parcel));
        authorRvTv.setText(review.getAuthor());
        contentRvTv.setText(review.getContent());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("SCROLL_POSITION", new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});
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
    }
}
