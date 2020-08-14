package com.W9221214.AppLet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterParams implements Parcelable {
    private String minNumOfRooms = String.valueOf(Integer.MIN_VALUE);
    private String maxNumOfRooms = String.valueOf(Integer.MAX_VALUE);
    private String minNumOfBedRooms = String.valueOf(Integer.MIN_VALUE);
    private String maxNumOfBedRooms = String.valueOf(Integer.MAX_VALUE);
    private String minArea = String.valueOf(Integer.MIN_VALUE);
    private String maxArea = String.valueOf(Integer.MAX_VALUE);


    public FilterParams() {
    }

    private FilterParams(Parcel in) {
        minNumOfRooms = in.readString();
        maxNumOfRooms = in.readString();
        minNumOfBedRooms = in.readString();
        maxNumOfBedRooms = in.readString();
        minArea = in.readString();
        maxArea = in.readString();

    }

    public static final Creator<FilterParams> CREATOR = new Creator<FilterParams>() {
        @Override
        public FilterParams createFromParcel(Parcel in) {
            return new FilterParams(in);
        }

        @Override
        public FilterParams[] newArray(int size) {
            return new FilterParams[size];
        }
    };

    public String getMinNumOfRooms() {
        return minNumOfRooms;
    }

    public void setMinNumOfRooms(String minNumOfRooms) {
        this.minNumOfRooms = minNumOfRooms;
    }

    public String getMaxNumOfRooms() {
        return maxNumOfRooms;
    }

    public void setMaxNumOfRooms(String maxNumOfRooms) {
        this.maxNumOfRooms = maxNumOfRooms;
    }

    public String getMinNumOfBedRooms() {
        return minNumOfBedRooms;
    }

    public void setMinNumOfBedRooms(String minNumOfBedRooms) {
        this.minNumOfBedRooms = minNumOfBedRooms;
    }

    public String getMaxNumOfBedRooms() {
        return maxNumOfBedRooms;
    }

    public void setMaxNumOfBedRooms(String maxNumOfBedRooms) {
        this.maxNumOfBedRooms = maxNumOfBedRooms;
    }

    public String getMinArea() {
        return minArea;
    }

    public void setMinArea(String minArea) {
        this.minArea = minArea;
    }

    public String getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(String maxArea) {
        this.maxArea = maxArea;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(minNumOfRooms);
        dest.writeString(maxNumOfRooms);
        dest.writeString(minNumOfBedRooms);
        dest.writeString(maxNumOfBedRooms);
        dest.writeString(minArea);
        dest.writeString(maxArea);
    }
}