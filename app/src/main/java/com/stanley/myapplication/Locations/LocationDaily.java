package com.stanley.myapplication.Locations;

/**
 * Created by OhIris on 3/4/16.
 */
public class LocationDaily {
    private double distance;
    private double range;
    private double duration;
    private int type;

    private static final String TAG = "Location";

    public LocationDaily(){

    }

    public double getDistance() {
        return distance;
    }

    public double getDuration() {
        return duration;
    }

    public double getRange() {
        return range;
    }

    public int getType() {
        return type;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setType(int type) {
        this.type = type;
    }
}
