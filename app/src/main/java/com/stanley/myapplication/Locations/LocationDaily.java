package com.stanley.myapplication.Locations;

/**
 * Created by OhIris on 3/4/16.
 */
public class LocationDaily {
    private double distance;
    private double range;
    private double duration;
    private double durationSpec;
    private int type;
    private int userId;
    private long date;

    private static final String TAG = "Location";

    public LocationDaily(){
        this.userId = 1;
        this.date = (long)0;
        this.distance = 0;
        this.range = 0;
        this.duration = 0;
        this.durationSpec = 0;
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

    public double getDurationSpec() {
        return durationSpec;
    }

    public int getUserId() {
        return userId;
    }

    public long getDate() {
        return date;
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

    public void setDurationSpec(double durationSpec) {
        this.durationSpec = durationSpec;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setType(int type) {
        this.type = type;
    }
}
