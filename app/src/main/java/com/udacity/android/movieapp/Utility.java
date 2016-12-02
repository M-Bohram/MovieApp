package com.udacity.android.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.udacity.android.movieapp.Favourite.FavouriteContract;
import com.udacity.android.movieapp.Movie.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dr mahmoud on 11/25/2016.
 */

public class Utility {

    /*
    * A method to parse the movie data from the json string
     */

    final static String ID = "id";
    final static String POSTER_PATH = "poster_path";
    final static String TITLE = "title";
    final static String OVERVIEW = "overview";
    final static String BACKDROP_PATH = "backdrop_path";
    final static String VOTE_AVERAGE = "vote_average";
    final static String RELEASE_DATE = "release_date";

    public static Movie getMovieDetailsFromJsonObj (JSONArray jsonMoviesArray, int index){
        try {
            JSONObject jsonMovieObject = jsonMoviesArray.getJSONObject(index);
            int id = jsonMovieObject.getInt(ID);
            String poster_path = jsonMovieObject.getString(POSTER_PATH);
            String title = jsonMovieObject.getString(TITLE);
            String overview = jsonMovieObject.getString(OVERVIEW);
            String backdrop_path = jsonMovieObject.getString(BACKDROP_PATH);
            double vote_average = jsonMovieObject.getDouble(VOTE_AVERAGE);
            String release_date = jsonMovieObject.getString(RELEASE_DATE);

            Movie movie = new Movie();

            movie.setId(id);
            movie.setPoster_path(poster_path);
            movie.setTitle(title);
            movie.setOverview(overview);
            movie.setBack_drop(backdrop_path);
            movie.setVote_average(vote_average);
            movie.setRelease_date(release_date);

            return movie;
        }
        catch (JSONException e){
            Log.e("Utility class", e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    /*
    * A method to check if the current movie is favourite or not.
     */

    public static int isFavorited(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                FavouriteContract.MovieEntry.CONTENT_URI,
                null,   // projection
                FavouriteContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", // selection
                new String[] { Integer.toString(id) },   // selectionArgs
                null    // sort order
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }

    public static Movie getMovieDetailsFromContentProvider(Cursor cursor){
        Movie movie = new Movie();

        movie.setId(cursor.getInt(FavouriteFragment.COLUMN_MOVIE_ID));
        movie.setOverview(cursor.getString(FavouriteFragment.COLUMN_MOVIE_OVERVIEW));
        movie.setBack_drop(cursor.getString(FavouriteFragment.COLUMN_MOVIE_BACK_DROP));
        movie.setVote_average(cursor.getDouble(FavouriteFragment.COLUMN_MOVIE_VOTE_AVERAGE));
        movie.setTitle(cursor.getString(FavouriteFragment.COLUMN_MOVIE_TITLE));
        movie.setRelease_date(cursor.getString(FavouriteFragment.COLUMN_MOVIE_RELEASE_DATE));
        movie.setPoster_path(cursor.getString(FavouriteFragment.COLUMN_MOVIE_POSTER_PATH));
        return movie;
    }
}
