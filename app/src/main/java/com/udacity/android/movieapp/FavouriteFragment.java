package com.udacity.android.movieapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.udacity.android.movieapp.Favourite.FavouriteContract;
import com.udacity.android.movieapp.Movie.Movie;
import com.udacity.android.movieapp.Movie.MovieAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FavouriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] MOVIE_COLUMNS = {

            FavouriteContract.MovieEntry.TABLE_NAME + "." + FavouriteContract.MovieEntry._ID,
            FavouriteContract.MovieEntry.COLUMN_MOVIE_ID,
            FavouriteContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            FavouriteContract.MovieEntry.COLUMN_MOVIE_TITLE,
            FavouriteContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            FavouriteContract.MovieEntry.COLUMN_MOVIE_BACK_DROP,
            FavouriteContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            FavouriteContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
    };
    // These indices are tied to MOVIE_COLUMNS.  If the MOVIE_COLUMNS changes, these
    // must change.
    static final int COL_ID = 0;
    static final int COLUMN_MOVIE_ID = 1;
    static final int COLUMN_MOVIE_POSTER_PATH = 2;
    static final int COLUMN_MOVIE_TITLE = 3;
    static final int COLUMN_MOVIE_OVERVIEW = 4;
    static final int COLUMN_MOVIE_BACK_DROP = 5;
    static final int COLUMN_MOVIE_VOTE_AVERAGE = 6;
    static final int COLUMN_MOVIE_RELEASE_DATE = 7;
    private static final int LOADER_ID = 0;
    // this MovieAdapter object is global to be used in both onCreateView and OnPostExecute methods.
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> MoviesList = new ArrayList<Movie>();

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movies", MoviesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_ID, Bundle.EMPTY, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        // we retreive the movies arraylist from saveInstanceState bundle without requesting again.
        if (savedInstanceState != null) {
            MoviesList = savedInstanceState.getParcelableArrayList("Movies");
        } else {
            updateMovieData();
        }

        movieAdapter = new MovieAdapter(getActivity(), MoviesList);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        if (movieAdapter != null) {
            gridView.setAdapter(movieAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int Position, long l) {
                    Movie movie = movieAdapter.getItem(Position);
                    ((Callback) getActivity()).onItemSelected(movie);
                }
            });
        }

        return rootView;
    }

    public void updateMovieData() {
//        FetchMovieFromFavouriteTask fetchTask = new FetchMovieFromFavouriteTask();
//        fetchTask.execute();
        getLoaderManager().restartLoader(LOADER_ID, Bundle.EMPTY, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (args != null) {
            return new CursorLoader(getActivity(),
                    FavouriteContract.MovieEntry.CONTENT_URI,
                    FavouriteFragment.MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            for (int index = 0; index < data.getCount(); index++) {
                Movie movie = Utility.getMovieDetailsFromContentProvider(data);
                movieAdapter.add(movie);
                if (!data.moveToNext())
                    break;
            }
        }
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.clear();
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(Movie movie);
    }

//    public class FetchMovieFromFavouriteTask extends AsyncTask<Void, Void, List<Movie>> {
//
//
//        @Override
//        protected List<Movie> doInBackground(Void... params) {
//            List<Movie> movies = new ArrayList<Movie>();
//            Cursor cursor = getActivity().getContentResolver().query(
//                    FavouriteContract.MovieEntry.CONTENT_URI,
//                    FavouriteFragment.MOVIE_COLUMNS,
//                    null,
//                    null,
//                    null
//            );
////            if(cursor!=null){
//            if(cursor.moveToFirst()) {
//                for (int index = 0; index < cursor.getCount(); index++) {
//
//                    Movie movie = Utility.getMovieDetailsFromContentProvider(cursor);
//                    movies.add(movie);
//                    if (!cursor.moveToNext())
//                        break;
//                }
//                cursor.close();
//                return movies;
////            }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(List<Movie> movies) {
//            if(movies != null) {
//                movieAdapter.clear();
//                for(Movie movie : movies){
//                    movieAdapter.add(movie);
//                }
//                movieAdapter.notifyDataSetChanged();
//            }
//        }
//    }

}
