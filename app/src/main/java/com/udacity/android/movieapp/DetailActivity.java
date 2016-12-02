package com.udacity.android.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle sentBundle = intent.getExtras();

        DetailFragment df = new DetailFragment();
        df.setArguments(sentBundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.movie_detail_container,df,"")
                .commit();
    }
}
