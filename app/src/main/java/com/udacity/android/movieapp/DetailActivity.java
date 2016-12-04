package com.udacity.android.movieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

//    private ShareActionProvider mShareActionProvider;
//    private String mShareString;

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main,menu);
//        MenuItem menuItem = menu.findItem(R.id.action_share);
//        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//        if (mShareString != null) {
//            mShareActionProvider.setShareIntent(createShareIntent());
//        }
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
//
//    private Intent createShareIntent() {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareString );
//        return shareIntent;
//    }
}
