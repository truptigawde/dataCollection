package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DCAFormDao {

    @Insert
    public void addForm(RoomDb_Form form);

    @Query("select * from DCAForms")
    public List<RoomDb_Form> getAllForms();

//    @Query("select count(1) from DCAForms where Form_Id = :formId")
//    public RoomDb_Form getFormByFormId(String formId);

    @Delete
    public void deleteForm(RoomDb_Form form);

    @Update
    public void updateform(RoomDb_Form form);
}