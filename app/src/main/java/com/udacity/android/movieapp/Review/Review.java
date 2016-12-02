package com.udacity.android.movieapp.Review;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dr mahmoud on 11/25/2016.
 */

public class Review implements Parcelable {
    private String author;
    private String content;


    public Review (){
    }

    public Review (String author, String content) {
        this.author = author;
        this.content = content;
    }

    private Review (Parcel in){
        this.author = in.readString();
        this.content = in.readString();
    }

    //Setters :

    public void setAuthor (String author) { this.author=author; }
    public void setContent (String content){
        this.content=content;
    }


    //Getters :

    public String getAuthor () { return author; }
    public String getContent (){
        return content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(content);
    }

    public final static Parcelable.Creator <Review> CREATOR = new Parcelable.Creator<Review>(){

        public Review createFromParcel (Parcel in){
            return new Review(in);
        }

        public Review[] newArray (int size){
            return new Review[size];
        }


    };

}
