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

    @Update
    void update(RoomGameLocation roomGameLocation);

    @Delete
    void delete(RoomGameLocation roomGameLocation);
//
//    @Query("SELECT * FROM location_table WHERE verified ORDER BY createdAt DESC")
//    LiveData<List<RoomGameLocation>> getUnverifiedLocations();
}
