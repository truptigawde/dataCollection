package org.cabi.pdc.RoomDb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RoomDb_Form.class, RoomDB_PlantDoctor.class, RoomDB_ClinicCode.class, RoomDB_Crop.class, RoomDB_CropVariety.class, RoomDB_Diagnosis.class, RoomDB_FarmerDetails.class, RoomDB_Recommendations.class, RoomDB_Location1.class, RoomDB_Location2.class, RoomDB_Session.class}, version = 1)
public abstract class DCAFormDatabase extends RoomDatabase {

    public abstract DCAFormDao dcaFormDao();

    public abstract DCAPlantDoctorDao dcaPlantDoctorDao();

    public abstract DCAClinicCodeDao dcaClinicCodeDao();

    public abstract DCACropDao dcaCropDao();

    public abstract DCACropVarietyDao dcaCropVarietyDao();

    public abstract DCADiagnosisDao dcaDiagnosisDao();

    public abstract DCAFarmerDao dcaFarmerDao();

    public abstract DCARecommendationDao dcaRecommendationDao();

    public abstract DCALocation1Dao dcaLocation1Dao();

    public abstract DCALocation2Dao dcaLocation2Dao();

    public abstract DCASessionDao dcaSessionDao();
}

//Local tables - AppData', 'Crop', 'Variety', 'Diagnosis', 'Forms', 'Sessions', 'AppData', 'ClinicCode', 'County', 'farmerDetail', 'Location', 'plantDoctor', 'Recommendation', 'Updates