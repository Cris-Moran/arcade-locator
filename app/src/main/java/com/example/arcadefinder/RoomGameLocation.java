package com.example.arcadefinder;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
@Entity(tableName = "location_table")
public class RoomGameLocation {

    @PrimaryKey
    @NonNull
    private String id;

    private Date createdAt;
    private Date updatedAt;
    private double longitude;
    private double latitude;
    private String locationName;
    private String address;
    private String title;
    private String description;
    private Bitmap image;
    private boolean verified;
    private String username;

    public RoomGameLocation(String id, Date createdAt, Date updatedAt, String locationName, String address, String title, String description, Bitmap image, boolean verified, String username, double longitude, double latitude) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.locationName = locationName;
        this.address = address;
        this.title = title;
        this.description = description;
        this.image = image;
        this.verified = verified;
        this.username = username;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public RoomGameLocation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

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

    public Bitmap getImage() {
        return image;
    }

    public boolean isVerified() {
        return verified;
    }

    public String getUsername() {
        return username;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
