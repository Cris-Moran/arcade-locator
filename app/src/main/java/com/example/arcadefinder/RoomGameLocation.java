package com.example.arcadefinder;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

@Entity(tableName = "location_table")
public class RoomGameLocation {

    @PrimaryKey
    private int id;
//    private Date createdAt;
//    private Date updatedAt;
//    private LatLng coordinates;
    private String locationName;
    private String address;
    private String title;
    private String description;
//    private Bitmap image;
    private boolean verified;
//    private ParseUser author;

    public RoomGameLocation(String locationName, String address, String title, String description, boolean verified) {
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//        this.coordinates = coordinates;
        this.locationName = locationName;
        this.address = address;
        this.title = title;
        this.description = description;
//        this.image = image;
        this.verified = verified;
//        this.author = author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public Date getUpdatedAt() {
//        return updatedAt;
//    }

//    public LatLng getCoordinates() {
//        return coordinates;
//    }

    public String getLocationName() {
        return locationName;
    }

    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

//    public Bitmap getImage() {
//        return image;
//    }

    public boolean isVerified() {
        return verified;
    }

//    public ParseUser getAuthor() {
//        return author;
//    }
}
