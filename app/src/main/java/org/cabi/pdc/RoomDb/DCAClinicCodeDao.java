package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCAClinicCodeDao {

    @Insert
    public void addClinicCode(RoomDB_ClinicCode clinicCode);

    @Query("select * from DCAClinicCodes")
    public List<RoomDB_ClinicCode> getAllClinicCodes();

    @Delete
    public void deleteClinicCode(RoomDB_ClinicCode clinicCode);

    @Update
    public void updateClinicCode(RoomDB_ClinicCode clinicCode);
}
