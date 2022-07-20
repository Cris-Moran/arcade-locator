package com.example.arcadefinder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GameLocationDao {

    @Insert
    void insert(RoomGameLocation roomGameLocation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RoomGameLocation... roomGameLocations);

    @Update
    void update(RoomGameLocation roomGameLocation);

    @Query("DELETE FROM location_table WHERE id = :objectId")
    void delete(String objectId);

    @Query("SELECT * FROM location_table ORDER BY createdAt DESC")
    List<RoomGameLocation> getUnverifiedLocations();

}
