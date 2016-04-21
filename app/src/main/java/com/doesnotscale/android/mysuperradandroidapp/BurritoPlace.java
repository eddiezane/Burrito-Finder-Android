package com.doesnotscale.android.mysuperradandroidapp;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eddiezane on 4/21/16.
 */

public class BurritoPlace implements Parcelable {
    private String id;
    private String name;
    private String address;
    private Location location;

    public BurritoPlace() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    protected BurritoPlace(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        location = (Location) in.readValue(Location.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeValue(location);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BurritoPlace> CREATOR = new Parcelable.Creator<BurritoPlace>() {
        @Override
        public BurritoPlace createFromParcel(Parcel in) {
            return new BurritoPlace(in);
        }

        @Override
        public BurritoPlace[] newArray(int size) {
            return new BurritoPlace[size];
        }
    };
}
