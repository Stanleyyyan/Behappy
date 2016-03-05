package com.stanley.myapplication.Locations;

import java.util.Date;

/**
 * Created by OhIris on 3/4/16.
 */
public class LocationBehappy {
    private double latitude;
    private double longitude;
    private int type;

    public LocationBehappy(){

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
}
