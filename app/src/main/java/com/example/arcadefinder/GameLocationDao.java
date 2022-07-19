package com.example.arcadefinder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameLocationDao {

    @Insert
    void insert(RoomGameLocation roomGameLocation);

    @Insert
    void insertAll(RoomGameLocation... roomGameLocations);

    @Update
    void update(RoomGameLocation roomGameLocation);

    @Delete
    void delete(RoomGameLocation roomGameLocation);

    @Query("SELECT * FROM location_table WHERE verified = 0 ORDER BY createdAt DESC")
    LiveData<List<RoomGameLocation>> getUnverifiedLocations();
}
