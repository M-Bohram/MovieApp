package com.udacity.android.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.android.movieapp.Movie.Movie;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private boolean mTwoPane;

    private String DETAILFRAGMENT_TAG = "DFTAG";

    private String sorted_by;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // If there is a detail container , then we are dealing with two-pane UI
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favourite) {
            Intent intent = new Intent(this, FavouriteActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.popularity) {
            sorted_by = "popularity.desc";
            MainFragment.updateMovieData(sorted_by);
            return true;
        }
        if (id == R.id.top_rated) {
            sorted_by = "vote_average.desc";
            MainFragment.updateMovieData(sorted_by);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    * To make the MainActivity notified if the user clicked on a movie, and handle if two-pane case
    * or smartphones.
    */
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("Movie", movie);
            DetailFragment df = new DetailFragment();
            DetailFragment.isTwoPane= true;
            df.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, df, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra("Movie", movie);
            startActivity(intent);
        }
    }
}