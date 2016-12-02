package com.udacity.android.movieapp.Trailer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dr mahmoud on 11/25/2016.
 */

public class Trailer implements Parcelable {
    private String name;
    private String key;


    public Trailer (){
    }

    public Trailer (String name, String key) {
        this.name = name;
        this.key = key;
    }

    private Trailer (Parcel in){
        this.name = in.readString();
        this.key = in.readString();
    }

    //Setters :

    public void setName (String name) { this.name=name; }
    public void setKey (String key){
        this.key=key;
    }


    //Getters :

    public String getName () { return name; }
    public String getKey (){
        return key;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(key);
    }

    public final static Parcelable.Creator <Trailer> CREATOR = new Parcelable.Creator<Trailer>(){

        public Trailer createFromParcel (Parcel in){
            return new Trailer(in);
        }

        public Trailer[] newArray (int size){
            return new Trailer[size];
        }


    };

}
