package com.maluta.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.maluta.popularmovies.R;

/**
 * Created by admin on 6/4/2018.
 */

public class PopularMoviesPreferences {

    public static boolean isPopular(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForSort = context.getString(R.string.pref_key);
        String defaultSort = context.getString(R.string.key_sort_pop);
        String preferredSort = sp.getString(keyForSort, defaultSort);
        String sort = context.getString(R.string.key_sort_pop);

        boolean userPrefersPopular = false;
        if (sort.equals(preferredSort)) {
            userPrefersPopular = true;
        }
        return userPrefersPopular;
    }
}
