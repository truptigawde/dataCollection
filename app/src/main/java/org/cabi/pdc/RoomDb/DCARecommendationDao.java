package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCARecommendationDao {

    @Insert
    public void addRecommedations(RoomDB_Recommendations recommendations);

    @Query("select * from DCARecommendations")
    public List<RoomDB_Recommendations> getAllRecommendations();

    @Delete
    public void deleteRecommedations(RoomDB_Recommendations recommendations);

    @Update
    public void updateRecommedations(RoomDB_Recommendations recommendations);
}