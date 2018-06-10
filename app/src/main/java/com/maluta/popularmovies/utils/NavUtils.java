package com.maluta.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.maluta.popularmovies.BuildConfig;
import com.maluta.popularmovies.data.PopularMoviesPreferences;
import com.maluta.popularmovies.model.Movie;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by admin on 6/4/2018.
 */

public class NavUtils {
    private static final String LOG_TAG = NavUtils.class.getSimpleName();

    private static String my_api_key = BuildConfig.my_api;

    public static Movie[] fetchMovieData(Context context) {
        // Create a URL object
        URL url = getURL(context);
        Movie[] movies;
        //Call HTTP request to URL and get JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request ", e);
        }

        try {
            movies = JsonUtils.getMovieFromJson(jsonResponse);
            return movies;
        } catch (JSONException e){
            Log.e(LOG_TAG, "Problem reading JSON ", e);
            return null;
        }
    }

    /**
     * Make HTTP request to the URL and return a String as the response
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        int readTimeout = 10000;
        int connectTimeout = 15000;
        int responseCode = 200;

        // If the URL is null, then return
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(readTimeout /* milliseconds */);
            urlConnection.setConnectTimeout(connectTimeout /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == responseCode) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static URL getURL (Context context) {
        URL popularMoviesURL;
        Uri uri;
        String BASE_URL;
        if (PopularMoviesPreferences.isPopular(context)) {
            BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
        } else {
            BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
        }

        String Api_Key = "api_key";

        uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(Api_Key,my_api_key)
                .build();
        try {
            popularMoviesURL =  new URL(uri.toString());
            return popularMoviesURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convert the {@link InputStream} into a String that contains JSON
     * response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
