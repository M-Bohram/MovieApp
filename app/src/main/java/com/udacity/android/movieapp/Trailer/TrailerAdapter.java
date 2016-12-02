package com.udacity.android.movieapp.Trailer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.android.movieapp.R;

import java.util.List;

/**
 * Created by dr mahmoud on 11/25/2016.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    private Context context ;


    public TrailerAdapter(Context context, List<Trailer> trailers){
        super(context,0,trailers);
        this.context=context;
    }


    @Nullable
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Trailer trailer = getItem(position);

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.trailer, parent ,false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.trailer_name);
        textView.setText(trailer.getName());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.play_button);
        Picasso.with(context).load(R.drawable.play_button).resize(100,100).into(imageView);

        return convertView;
    }

}
