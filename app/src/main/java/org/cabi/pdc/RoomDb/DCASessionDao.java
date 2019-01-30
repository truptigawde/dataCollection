package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCASessionDao {

    @Insert
    public void addSession(RoomDB_Session session);

    @Query("select * from DCASessions")
    public List<RoomDB_Session> getAllSessions();

    @Delete
    public void deleteSession(RoomDB_Session session);

    @Update
    public void updateSession(RoomDB_Session session);
}