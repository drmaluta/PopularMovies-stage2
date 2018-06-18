package com.maluta.popularmovies.utils;

/**
 * Created by admin on 6/7/2018.
 */

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeUtils {

    private static final String Date_TMDB = "yyyy-MM-dd";

    private static Date getDate(String date,String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

        return simpleDateFormat.parse(date);
    }

    public static String getLocalizedDate(Context context,String date) throws ParseException {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);

        return dateFormat.format(getDate(date, Date_TMDB));
    }
}
