package com.udacity.android.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.android.movieapp.Movie.Movie;

public class FavouriteActivity extends AppCompatActivity implements FavouriteFragment.Callback{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

        } else {
            mTwoPane = false;
        }
    }

    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("Movie", movie);
            DetailFragment df = new DetailFragment();
            DetailFragment.isTwoPane= true;
            df.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, df,"")
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra("Movie", movie);
            startActivity(intent);
        }
    }
}
