package com.udacity.android.movieapp.Movie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Highfreq on 11/4/2016.
 */
public class Movie implements Parcelable {

    private int id ;
    private String poster_path ;
    private String title;
    private String overview;
    private String back_drop;
    private double vote_average;
    private String release_date;

    public Movie (){
    }

    public Movie (int id, String poster_path, String title, String overview, String back_drop,
                  double vote_average, String release_date) {
        this.id = id;
        this.poster_path = poster_path;
        this.title = title;
        this.overview = overview;
        this.back_drop = back_drop;
        this.vote_average = vote_average;
        this.release_date = release_date;

    }

    private Movie (Parcel in){
        this.id = in.readInt();
        this.poster_path = in.readString();
        this.title = in.readString();
        this.overview = in.readString();
        this.back_drop = in.readString();
        this.vote_average = in.readDouble();
        this.release_date = in.readString();
    }

    //Setters :

    public void setId (int id) { this.id=id; }

    public void setPoster_path (String poster_path){
        this.poster_path=poster_path;
    }

    public void setTitle (String title){
        this.title=title;
    }

    public void setOverview (String overview){
        this.overview=overview;
    }

    public void setBack_drop (String back_drop){
        this.back_drop=back_drop;
    }

    public void setVote_average (double vote_average){
        this.vote_average=vote_average;
    }

    public void setRelease_date (String release_date){
        this.release_date=release_date;
    }

    //Getters :

    public int getId () { return id; }

    public String getPoster_path (){
        return poster_path;
    }

    public String getTitle (){
        return title;
    }

    public String getOverview (){
        return overview;
    }

    public String getBack_drop (){
        return back_drop;
    }

    public double getVote_average (){
        return vote_average;
    }

    public String getRelease_date (){
        return release_date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(poster_path);
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(back_drop);
        parcel.writeDouble(vote_average);
        parcel.writeString(release_date);

    }

    public final static Parcelable.Creator <Movie> CREATOR = new Parcelable.Creator<Movie>(){

        public Movie createFromParcel (Parcel in){
            return new Movie(in);
        }

        public Movie[] newArray (int size){
            return new Movie[size];
        }


    };
}
