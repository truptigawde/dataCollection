package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCADiagnosisDao {

        @Insert
        public void addDiagnosis(RoomDB_Diagnosis diagnosis);

        @Query("select * from DCADiagnosis")
        public List<RoomDB_Diagnosis> getAllDiagnosis();

        @Delete
        public void deleteDiagnosis(RoomDB_Diagnosis diagnosis);

        @Update
        public void updateDiagnosis(RoomDB_Diagnosis diagnosis);
}