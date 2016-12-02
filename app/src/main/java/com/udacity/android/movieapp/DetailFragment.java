package com.udacity.android.movieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.android.movieapp.Movie.Movie;
import com.udacity.android.movieapp.Review.Review;
import com.udacity.android.movieapp.Review.ReviewAdapter;
import com.udacity.android.movieapp.Trailer.Trailer;
import com.udacity.android.movieapp.Trailer.TrailerAdapter;
import com.udacity.android.movieapp.Favourite.FavouriteContract;

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


public class DetailFragment extends Fragment {
    //constant strings.
    private static final String YOUTUBE_URI = "http://www.youtube.com/watch";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    //trailer objects.
    private ListView mTrailerListView;
    private TrailerAdapter mTrailerAdapter;
    private ArrayList<Trailer> mTrailers = new ArrayList<Trailer>();
    //review objects.
    private ListView mReviewListView;
    private ReviewAdapter mReviewAdapter;
    private ArrayList<Review> mReviews = new ArrayList<Review>();

    private Movie mMovie = new Movie();

    private CheckBox mCheckBox;

    public static boolean isTwoPane;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTwoPane) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_favourite) {
            Intent intent = new Intent(getActivity(), FavouriteActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ScrollView view = (ScrollView) rootView.findViewById(R.id.Main_container);
        view.smoothScrollTo(0, 0);

        Intent intent = getActivity().getIntent();
        Bundle bundle ;
        if(intent!=null){
            bundle = getArguments();
        }else{
            bundle = intent.getExtras();
        }

        if (bundle != null) {
            mMovie = bundle.getParcelable("Movie");

            final String overview = mMovie.getOverview();
            final String title = mMovie.getTitle();
            final String back_drop = "http://image.tmdb.org/t/p/w185/" + mMovie.getBack_drop();
            final String poster = "http://image.tmdb.org/t/p/w185/" + mMovie.getPoster_path();
            final String release_date = mMovie.getRelease_date();
            final String vote_average = Double.toString(mMovie.getVote_average()) + " / 10";

            RequestTrailerData(mMovie.getId());
            RequestReviewData(mMovie.getId());

            mTrailerAdapter = new TrailerAdapter(getActivity(), mTrailers);

            mReviewAdapter = new ReviewAdapter(getActivity(), mReviews);

            // resize(500,500) , resize(500,700) got them with try and error.
            ImageView backgroundImage = (ImageView) rootView.findViewById(R.id.movie_name_image);
            Picasso.with(getActivity()).load(back_drop).resize(500, 500).into(backgroundImage);
            ImageView posterImage = (ImageView) rootView.findViewById(R.id.backdrop_poster);
            Picasso.with(getActivity()).load(poster).resize(500, 700).into(posterImage);

            TextView titleView = (TextView) rootView.findViewById(R.id.movie_name);
            titleView.setText(title);
            TextView release_dateView = (TextView) rootView.findViewById(R.id.release_date);
            release_dateView.setText(release_date);
            TextView vote_averageView = (TextView) rootView.findViewById(R.id.vote_average);
            vote_averageView.setText(vote_average);
            TextView overviewView = (TextView) rootView.findViewById(R.id.overview);
            overviewView.setText(overview);


            mCheckBox = (CheckBox) rootView.findViewById(R.id.favorite_button);

            mCheckBox.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(final View v) {
                                                 if (mMovie != null) {
                                                     new AsyncTask<Void, Void, Integer>() {

                                                         @Override
                                                         protected Integer doInBackground(Void... params) {
                                                             return Utility.isFavorited(getActivity(), mMovie.getId());
                                                         }

                                                         @Override
                                                         protected void onPostExecute(Integer isFavourite) {
                                                             // if it is in favorites
                                                             if (isFavourite == 1) {
                                                                 // delete from favorites
                                                                 new AsyncTask<Void, Void, Integer>() {
                                                                     @Override
                                                                     protected Integer doInBackground(Void... params) {
                                                                         return getActivity().getContentResolver().delete(
                                                                                 FavouriteContract.MovieEntry.CONTENT_URI,
                                                                                 FavouriteContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                                                                                 new String[]{Integer.toString(mMovie.getId())}
                                                                         );
                                                                     }

                                                                     @Override
                                                                     protected void onPostExecute(Integer rowsDeleted) {
                                                                         mCheckBox.setChecked(false);
                                                                         Toast.makeText(getActivity(), "removed_from_favorites", Toast.LENGTH_SHORT)
                                                                                 .show();
                                                                     }
                                                                 }.execute();
                                                             }
                                                             // if it is not in favorites
                                                             else {
                                                                 // add to favorites
                                                                 new AsyncTask<Void, Void, Uri>() {
                                                                     @Override
                                                                     protected Uri doInBackground(Void... params) {
                                                                         final ContentValues movieValues = new ContentValues();
                                                                         movieValues.put(FavouriteContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId()); //integer
                                                                         movieValues.put(FavouriteContract.MovieEntry.COLUMN_MOVIE_TITLE, mMovie.getTitle());
                                                                         movieValues.put(FavouriteContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, mMovie.getPoster_path());
                                                                         movieValues.put(FavouriteContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, mMovie.getVote_average()); //double
                                                                         movieValues.put(FavouriteContract.MovieEntry.COLUMN_MOVIE_BACK_DROP, mMovie.getBack_drop());
                                                                         movieValues.put(FavouriteContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, mMovie.getOverview());
                                                                         movieValues.put(FavouriteContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie.getRelease_date());

                                                                         return getActivity().getContentResolver().insert(FavouriteContract.MovieEntry.CONTENT_URI,
                                                                                 movieValues);
                                                                     }

                                                                     @Override
                                                                     protected void onPostExecute(Uri returnUri) {

                                                                         mCheckBox.setChecked(true);
                                                                         Toast.makeText(getActivity(), "added_from_favorites", Toast.LENGTH_SHORT)
                                                                                 .show();
                                                                     }
                                                                 }.execute();
                                                             }
                                                         }
                                                     }.execute();
                                                 }

                                             }

                                         }
            );

            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    return Utility.isFavorited(getActivity(), mMovie.getId());
                }

                @Override
                protected void onPostExecute(Integer isFavourite) {
                    if (isFavourite == 1) {
                        mCheckBox.setChecked(true);
                    }
                }
            }.execute();

            mTrailerListView = (ListView) rootView.findViewById(R.id.listview_trailers);
            mTrailerListView.setAdapter(mTrailerAdapter);
            mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                                            Trailer trailer = mTrailerAdapter.getItem(position);
                                                            String key = trailer.getKey();
                                                            final Uri builtUri = Uri.parse(YOUTUBE_URI).buildUpon()
                                                                    .appendQueryParameter("v", key)
                                                                    .build();

                                                            Intent intent = new Intent(Intent.ACTION_VIEW, builtUri);
                                                            startActivity(intent);
                                                        }
                                                    }
            );

            mReviewListView = (ListView) rootView.findViewById(R.id.listview_reviews);
            mReviewListView.setAdapter(mReviewAdapter);

        }
        return rootView;
    }

    private void RequestTrailerData(int id) {
        FetchTrailerTask trailerTask = new FetchTrailerTask();
        trailerTask.execute(Integer.toString(id));
    }

    private void RequestReviewData(int id) {
        FetchReviewTask reviewTask = new FetchReviewTask();
        reviewTask.execute(Integer.toString(id));
    }

    public class FetchTrailerTask extends AsyncTask<String, Void, List<Trailer>> {

        final String RESULTS = "results";
        final String NAME = "name";
        final String KEY = "key";

        private List<Trailer> getTrailersDataFromJson(String jsonString) {

            ArrayList<Trailer> trailers = new ArrayList<Trailer>();


            try {

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray trailerResultsArray = jsonObject.getJSONArray(RESULTS);

                for (int index = 0; index < trailerResultsArray.length(); index++) {
                    JSONObject jsonTrailerObject = trailerResultsArray.getJSONObject(index);
                    String name = jsonTrailerObject.getString(NAME);
                    String key = jsonTrailerObject.getString(KEY);

                    Trailer trailer = new Trailer();
                    trailer.setName(name);
                    trailer.setKey(key);

                    trailers.add(trailer);
                }

                return trailers;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected List<Trailer> doInBackground(String... params) {

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
                        "http://api.themoviedb.org/3/movie/";
                final String EXTENTION = "videos";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendPath(EXTENTION)
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
            return getTrailersDataFromJson(forecastJsonStr);
        }


        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null) {
                mTrailerAdapter.clear();
                for (Trailer trailer : trailers) {
                    mTrailerAdapter.add(trailer);
                }
                mTrailerAdapter.notifyDataSetChanged();
            }
        }
    }

    public class FetchReviewTask extends AsyncTask<String, Void, List<Review>> {

        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        private final String LOG_TAG = DetailFragment.FetchTrailerTask.class.getSimpleName();

        private List<Review> getReviewsDataFromJson(String jsonString) {

            ArrayList<Review> reviews = new ArrayList<Review>();

            try {

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray reviewResultsArray = jsonObject.getJSONArray(RESULTS);

                for (int index = 0; index < reviewResultsArray.length(); index++) {
                    JSONObject jsonReviewObject = reviewResultsArray.getJSONObject(index);

                    String author = jsonReviewObject.getString(AUTHOR);
                    String content = jsonReviewObject.getString(CONTENT);

                    Review review = new Review();
                    review.setAuthor(author);
                    review.setContent(content);

                    reviews.add(review);
                }
                return reviews;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected List<Review> doInBackground(String... params) {

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
                        "http://api.themoviedb.org/3/movie/";
                final String EXTENTION = "reviews";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendPath(EXTENTION)
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIEDB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
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
            return getReviewsDataFromJson(forecastJsonStr);
        }


        @Override
        protected void onPostExecute(List<Review> reviews) {
            if (reviews != null) {
                mReviewAdapter.clear();
                for (Review review : reviews) {
                    mReviewAdapter.add(review);
                }
                mReviewAdapter.notifyDataSetChanged();
            }
        }
    }
}


