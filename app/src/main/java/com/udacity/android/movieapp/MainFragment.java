package com.udacity.android.movieapp;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.udacity.android.movieapp.Movie.Movie;
import com.udacity.android.movieapp.Movie.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    // this MovieAdapter object is global to be used in both onCreateView and OnPostExecute methods.
    private static MovieAdapter movieAdapter;
    // default value for sorting is popularity
    private String sorted_by = "popularity.desc";
    private ArrayList<Movie> MoviesList = new ArrayList<Movie>();

    public MainFragment() {
        // Required empty public constructor
    }

    public static void updateMovieData(String sorted_by) {
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute(sorted_by);
    }

    //we save the data when rotating the device.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movies", MoviesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        if (savedInstanceState != null) {
            MoviesList = savedInstanceState.getParcelableArrayList("Movies");
        } else {
            updateMovieData(sorted_by);
        }

        movieAdapter = new MovieAdapter(getActivity(), MoviesList);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(movieAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int Position, long l) {
                Movie movie = movieAdapter.getItem(Position);
                ((Callback) getActivity()).onItemSelected(movie);

            }
        });

        return rootView;
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

    public static class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        final static String RESULTS = "results";
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        public List<Movie> getMoviesDataFromJson(String jsonString) {


            List<Movie> movies = new ArrayList<Movie>();

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                JSONArray movieResultsArray = jsonObject.getJSONArray(RESULTS);

                for (int index = 0; index < movieResultsArray.length(); index++) {

                    Movie movie = Utility.getMovieDetailsFromJsonObj(movieResultsArray, index);
                    movies.add(movie);
                }
                return movies;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected List<Movie> doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the moviedb query
                // Possible parameters are top_rated.desc or popularity.desc
                // http://themoviedb.org/
                final String MOVIEDB_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORTING = "sort_by";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORTING, params[0])
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIEDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to MovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return getMoviesDataFromJson(forecastJsonStr);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                movieAdapter.clear();
                for (Movie movie : movies) {
                    movieAdapter.add(movie);
                }
            }
        }
    }

}
