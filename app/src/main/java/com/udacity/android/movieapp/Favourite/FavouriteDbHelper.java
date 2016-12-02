package com.udacity.android.movieapp.Favourite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dr mahmoud on 11/24/2016.
 */

public class FavouriteDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favourite.db";

    public FavouriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + FavouriteContract.MovieEntry.TABLE_NAME + " (" +
                FavouriteContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouriteContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavouriteContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavouriteContract.MovieEntry.COLUMN_MOVIE_BACK_DROP + " TEXT NOT NULL, " +
                FavouriteContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                FavouriteContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                FavouriteContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL NOT NULL, " +
                FavouriteContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL);";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


}
