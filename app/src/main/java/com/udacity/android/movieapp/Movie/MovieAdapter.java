package com.udacity.android.movieapp.Movie;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.android.movieapp.R;

import java.util.List;

/**
 * Created by Highfreq on 10/30/2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context context ;

    
    public MovieAdapter(Context context, List<Movie> movies){
        super(context,0,movies);
        this.context=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if(convertView == null){
           convertView = layoutInflater.inflate(R.layout.fragment_layout, parent ,false);
        }

        SquareImageView imageView = (SquareImageView) convertView.findViewById(R.id.squaredimageView);

        // To get the URL of the poster image.
        String preURL = "http://image.tmdb.org/t/p/w185/";
        String imageURL = movie.getPoster_path();
        String fullURL = preURL + imageURL ;

        //use Picasso to retain the image specified by the url to imageview.

            Picasso.with(context)
                    .load(fullURL)
                    .into(imageView);

        return convertView;
    }

    /*
    * An inner class used to create a custom view to display a movie's poster on MainActivity
     */

    public static class SquareImageView extends ImageView {

        public SquareImageView(Context context) {
            super(context);
        }

        public SquareImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
        }
    }

}
