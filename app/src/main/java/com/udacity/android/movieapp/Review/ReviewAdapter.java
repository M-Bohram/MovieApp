package com.udacity.android.movieapp.Review;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.android.movieapp.R;

import java.util.List;

/**
 * Created by dr mahmoud on 11/25/2016.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {

    private Context context ;


    public ReviewAdapter(Context context, List<Review> reviews){
        super(context,0,reviews);
        this.context=context;
    }


    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review review = getItem(position);

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.review, parent ,false);
        }

        TextView authorView = (TextView) convertView.findViewById(R.id.author);
        authorView.setText(review.getAuthor());

        TextView contentView = (TextView) convertView.findViewById(R.id.content);
        contentView.setText(review.getContent());

        return convertView;
    }

}
