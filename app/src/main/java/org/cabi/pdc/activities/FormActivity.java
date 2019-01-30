package org.cabi.pdc.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.DCAFormDatabase;
import org.cabi.pdc.RoomDb.RoomDB_Session;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.adapters.DrawerListAdapter;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.fragments.AboutTheFarmerFragment;
import org.cabi.pdc.fragments.AreaAffectedFragment;
import org.cabi.pdc.fragments.ClinicDateFragment;
import org.cabi.pdc.fragments.ClinicDetailsFragment;
import org.cabi.pdc.fragments.CountyFragment;
import org.cabi.pdc.fragments.CropSampleInformationFragment;
import org.cabi.pdc.fragments.CurrentControlFragment;
import org.cabi.pdc.fragments.DevelopmentStageFragment;
import org.cabi.pdc.fragments.DiagnosisFragment;
import org.cabi.pdc.fragments.DiagnosisProblemTypeFragment;
import org.cabi.pdc.fragments.FarmerDetailsFragment;
import org.cabi.pdc.fragments.FindRecommendationFragment;
import org.cabi.pdc.fragments.FormSummaryFragment;
import org.cabi.pdc.fragments.GenericFormSectionFragment;
import org.cabi.pdc.fragments.MajorSymptomsFragment;
import org.cabi.pdc.fragments.PartsAffectedFragment;
import org.cabi.pdc.fragments.PlannedActivitiesFragment;
import org.cabi.pdc.fragments.PlantDoctorFragment;
import org.cabi.pdc.fragments.RecommendationFragment;
import org.cabi.pdc.fragments.RecommendationTypeFragment;
import org.cabi.pdc.fragments.SampleBroughtFragment;
import org.cabi.pdc.fragments.SubCountyFragment;
import org.cabi.pdc.fragments.SymptomsDescribeProblemFragment;
import org.cabi.pdc.fragments.SymptomsDistributionFragment;
import org.cabi.pdc.fragments.VarietySampleInformationFragment;
import org.cabi.pdc.models.Section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class FormActivity extends AppCompatActivity {

    private static String TAG = FormActivity.class.getSimpleName();

    private ListView mDrawerList;
    private RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private ArrayList<String> mNavItems;
    private ArrayList<Section> listSections;

    int fragmentId = 0;

    public static DCAFormDatabase dcaFormDatabase;
    DCAApplication dcaApplication;

    LocationManager locationManager;
    LocationListener dcalocationListener;
    boolean locationPermissionGranted = false;
    static final int REQUEST_COARSE_ACCESS = 123;

    public boolean editDraft, viewSubmitted;
    ImageView ivBurgerMenu, ivPWLogoLeft, ivPWLogoRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        dcalocationListener = new DCALocationListener();
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_ACCESS);
//            return;
//        } else {
//            locationPermissionGranted = true;
//        }
//        if (locationPermissionGranted) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, dcalocationListener);
//        }

        dcaApplication = (DCAApplication) getApplicationContext();

        ivBurgerMenu = findViewById(R.id.ivBurgerMenu);
        ivPWLogoLeft = findViewById(R.id.ivPWLogoStart);
        ivPWLogoRight = findViewById(R.id.ivPWLogoEnd);

        listSections = ApiData.getInstance().getFormDefinition().getSections();
        dcaApplication.setSectionList(ApiData.getInstance().getFormDefinition().getSections());
        if (listSections != null && listSections.size() > 0) {
            mNavItems = new ArrayList<>();
            if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavOverview") &&
                    !TextUtils.isEmpty(ApiData.getInstance().getMetadataTranslationsList().get("InitialNavOverview").getValue())) {
                mNavItems.add(ApiData.getInstance().getMetadataTranslationsList().get("InitialNavOverview").getValue());
            } else {
                mNavItems.add("Overview");
            }
            Collections.sort(listSections, new Comparator<Section>() {
                @Override
                public int compare(Section section, Section t1) {
                    int order1 = section.getOrder();
                    int order2 = t1.getOrder();
                    if (order1 < order2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            for (int i = 0; i < listSections.size(); i++) {
                if (listSections.get(i).isActive()) {
                    mNavItems.add(ApiData.getInstance().getMetadataTranslationsList().get(listSections.get(i).getTranslationId()).getValue());
                }
            }
            if (ApiData.getInstance().getMetadataTranslationsList().containsKey("SectionSummary") &&
                    !TextUtils.isEmpty(ApiData.getInstance().getMetadataTranslationsList().get("SectionSummary").getValue())) {
                mNavItems.add(ApiData.getInstance().getMetadataTranslationsList().get("SectionSummary").getValue());
            } else {
                mNavItems.add("Summary");
            }

            if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavClose") &&
                    !TextUtils.isEmpty(ApiData.getInstance().getMetadataTranslationsList().get("InitialNavClose").getValue())) {
                TextView tvCloseMenu = findViewById(R.id.btnCloseMenu);
                tvCloseMenu.setText(ApiData.getInstance().getMetadataTranslationsList().get("InitialNavClose").getValue());
            }

            if (dcaFormDatabase == null) {
                dcaFormDatabase = Room.databaseBuilder(getApplicationContext(), DCAFormDatabase.class, "DCAFormsDB").allowMainThreadQueries().build();
            }
        }

//        mNavItems.add("Overview");
//        mNavItems.add("Plant doctor");
//        mNavItems.add("Clinic details");
//        mNavItems.add("Farmer");
//        mNavItems.add("About the farmer");
//        mNavItems.add("Sample Information - Crop");
//        mNavItems.add("Sample Information - Variety");
//        mNavItems.add("Sample Information - Sample");
//        mNavItems.add("Development Stage");
//        mNavItems.add("Parts affected");
//        mNavItems.add("Area affected");
//        mNavItems.add("Symptoms");
//        mNavItems.add("Symptoms - Distribution");
//        mNavItems.add("Symptoms - Describe problem");
//        mNavItems.add("Diagnosis - Type of problem");
//        mNavItems.add("Diagnosis");
//        mNavItems.add("Current control");
//        mNavItems.add("Recommendation - Type");
//        mNavItems.add("Recommendation");
//        mNavItems.add("Planned activities");
//        mNavItems.add("Summary");
//        mNavItems.add("Find recommendation");
//        mNavItems.add("Clinic Session Date");

        mDrawerLayout = findViewById(R.id.drawerLayout);

        mDrawerPane = findViewById(R.id.drawerPane);
        mDrawerList = findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setPageTitle(mNavItems.get(position).toString());
//                TextView sectionTitle = findViewById(R.id.tvSectionTitle);
//                sectionTitle.setText(mNavItems.get(position).toString());
                selectItemFromDrawer(position, mNavItems.get(position).toString(), false);
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (mNavItems != null && mNavItems.size() > 2) {
            if (bundle != null && (bundle.containsKey("EditDraft") || bundle.containsKey("ViewSubmitted")) && (bundle.getBoolean("EditDraft") == true || bundle.getBoolean("ViewSubmitted") == true)) {
                editDraft = bundle.getBoolean("EditDraft");
                viewSubmitted = bundle.getBoolean("ViewSubmitted");
                if (editDraft || viewSubmitted) {
                    selectItemFromDrawer(mNavItems.size() - 1, mNavItems.get(mNavItems.size() - 1), false);
                }
            } else if (ApiData.getInstance().getCurrentForm() != null && !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFormStatus()) && ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase().equals("CONTINUE")) {
                selectItemFromDrawer(mNavItems.size() - 1, mNavItems.get(mNavItems.size() - 1).toString(), false);
            } else {
                selectItemFromDrawer(1, mNavItems.get(1).toString(), false);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_ACCESS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                } else {
                    locationPermissionGranted = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_ACCESS);
//            return;
//        } else {
//            locationPermissionGranted = true;
//        }
//        if (locationPermissionGranted) {
//            locationManager.removeUpdates(dcalocationListener);
//        }
    }

    private void selectItemFromDrawer(int position, String sectionTitle, boolean fromSummary) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        if (sectionTitle.contains("/")) {
            sectionTitle = sectionTitle.split("/")[1].trim();
        }

        switch (sectionTitle.toUpperCase()) {
            case "OVERVIEW": {
                if (ApiData.getInstance().getCurrentForm() != null) {
                    ApiData.getInstance().getCurrentForm().setFormStatus("CONTINUE");
                }
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return;
            }
            case "PLANT DOCTOR":
                fragment = new PlantDoctorFragment();
                break;
            case "CLINIC DETAILS":
                fragment = new ClinicDetailsFragment();
                break;
            case "FARMER":
                fragment = new FarmerDetailsFragment();
                break;
            case "ABOUT THE FARMER":
                fragment = new AboutTheFarmerFragment();
                break;
            case "SAMPLE INFORMATION - CROP":
                fragment = new CropSampleInformationFragment();
                break;
            case "SAMPLE INFORMATION - VARIETY":
                fragment = new VarietySampleInformationFragment();
                break;
            case "SAMPLE INFORMATION - SAMPLE":
                fragment = new SampleBroughtFragment();
                break;
            case "DEVELOPMENT STAGE":
                fragment = new DevelopmentStageFragment();
                break;
            case "PARTS AFFECTED":
                fragment = new PartsAffectedFragment();
                break;
            case "AREA AFFECTED":
                fragment = new AreaAffectedFragment();
                break;
            case "SYMPTOMS":
                fragment = new MajorSymptomsFragment();
                break;
            case "SYMPTOMS - DISTRIBUTION":
                fragment = new SymptomsDistributionFragment();
                break;
            case "SYMPTOMS - DESCRIBE PROBLEM":
                fragment = new SymptomsDescribeProblemFragment();
                break;
            case "DIAGNOSIS - TYPE OF PROBLEM":
                fragment = new DiagnosisProblemTypeFragment();
                break;
            case "DIAGNOSIS":
                fragment = new DiagnosisFragment();
                break;
            case "CURRENT CONTROL":
                fragment = new CurrentControlFragment();
                break;
            case "RECOMMENDATION - TYPE":
                fragment = new RecommendationTypeFragment();
                break;
            case "RECOMMENDATION":
                fragment = new RecommendationFragment();
                break;
            case "PLANNED ACTIVITIES":
                fragment = new PlannedActivitiesFragment();
                break;
            case "SUMMARY":
                fragment = new FormSummaryFragment();
                break;
            case "CLINIC DATE":
                fragment = new ClinicDateFragment();
                break;
            default: {
                Toast.makeText(this, "Newly configured section", Toast.LENGTH_SHORT).show();
            }
            return;
//                fragment = new GenericFormSectionFragment();
//                break;
        }

        if (sectionTitle.toUpperCase().equals("SUMMARY")) {
            hideBurgerMenu();
        } else {
            showBurgerMenu();
        }

        setPageTitle(sectionTitle);

        if (listSections != null && position != 0 && position != (listSections.size() + 1)) {
            bundle.putSerializable("Section", listSections.get(position - 1));
            bundle.putInt("SectionPosition", position);
            bundle.putBoolean("FromSummary", fromSummary);
            fragment.setArguments(bundle);
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentId == 0) {
            transaction.replace(R.id.mainContent, fragment);
        } else {
            transaction.replace(R.id.mainContent, fragment);
            transaction.addToBackStack(mNavItems.get(position));
        }

        View filled = findViewById(R.id.viewProgressFilled);
        View unfilled = findViewById(R.id.viewProgressUnfilled);

        filled.setLayoutParams(new LinearLayout.LayoutParams(0, Utility.getDpFromPixels(this, 20), position + 1));
        unfilled.setLayoutParams(new LinearLayout.LayoutParams(0, Utility.getDpFromPixels(this, 20), mNavItems.size() - (position + 1)));

        if (ApiData.getInstance().getCurrentSession() == null) {
            ApiData.getInstance().setCurrentSession(new RoomDB_Session());
        }
        if (TextUtils.isEmpty(ApiData.getInstance().getCurrentSession().getSessionStartDateTime())) {
            ApiData.getInstance().getCurrentSession().setSessionStartDateTime(Utility.getUTCDateTime(new Date()));
            // new SimpleDateFormat("dd MMMM yyyy HH:mm").format(new Date())
        }
        if (TextUtils.isEmpty(ApiData.getInstance().getCurrentSession().getSessionId())) {
            ApiData.getInstance().getCurrentSession().setSessionId(Utility.getGuid());
        }

        if (ApiData.getInstance().getCurrentForm() == null) {
            ApiData.getInstance().setCurrentForm(new RoomDb_Form());
        } else {
            if (ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase().equals("SENT") ||
                    ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase().equals("NOTSENT")) {
                ApiData.getInstance().setCurrentForm(new RoomDb_Form());
            }
        }
        if (TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFormStartDateTime())) {
            ApiData.getInstance().getCurrentForm().setFormStartDateTime(Utility.getUTCDateTime(new Date()));
//            new SimpleDateFormat("dd MMMM yyyy HH:mm").format(new Date())
        }
        if (TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFormStatus())) {
            ApiData.getInstance().getCurrentForm().setFormStatus("NEW");
        }
        if (TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFormDefinitonId())) {
            ApiData.getInstance().getCurrentForm().setFormDefinitonId(ApiData.getInstance().getFormDefinition().getFormDefinitionId());
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

        mDrawerList.setItemChecked(position, true);
        if (position == 0) {
            setTitle("Overview");
        } else if (position > 0 && position <= listSections.size()) {
            setTitle(listSections.get(position - 1).getTitle());
        } else if (position == listSections.size() + 1) {
            setTitle("Summary");
        } else {
            Toast.makeText(this, "Invalid section position: " + position, Toast.LENGTH_SHORT).show();
        }

        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    public void setPageTitle(String sectionTitle) {
        TextView pageTitle = findViewById(R.id.tvSectionTitle);
        pageTitle.setText(sectionTitle);
    }

//    public void relaunchFragment(Section section, int fragmentId) {
//        FragmentManager fragmentManager = getFragmentManager();
//        TextView sectionTitle = findViewById(R.id.tvSectionTitle);
//        sectionTitle.setText(section.getTitle());
//        fragmentManager.beginTransaction().replace(fragmentId, new GenericFormSectionFragment()).commit();
//    }

    public void Click(View view) {
        switch (view.getId()) {
            case R.id.ivBurgerMenu:
                if (mDrawerLayout != null && mDrawerPane != null) {
                    mDrawerLayout.openDrawer(mDrawerPane);
                }
                break;
            case R.id.btnCloseMenu:
            case R.id.btnImgCloseMenu:
                if (mDrawerLayout != null && mDrawerPane != null) {
                    mDrawerLayout.closeDrawer(mDrawerPane);
                }
                break;
            case R.id.btnBackArrow: {
                String title;
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    int pos = mNavItems.indexOf(getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName());
                    if (pos > 0) {
                        if (getFragmentManager().getBackStackEntryCount() > 1) {
                            title = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2).getName();
                        } else {
                            title = mNavItems.get(pos - 1);
                        }

                        findViewById(R.id.viewProgressFilled).setLayoutParams(new LinearLayout.LayoutParams(0, Utility.getDpFromPixels(this, 20), pos + 1));
                        findViewById(R.id.viewProgressUnfilled).setLayoutParams(new LinearLayout.LayoutParams(0, Utility.getDpFromPixels(this, 20), mNavItems.size() - (pos + 1)));

                    } else {
                        switch (getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName().toUpperCase()) {
                            case "LOCATION1":
                                title = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2).getName();
                                break;
                            case "LOCATION2":
                                title = "Find county";
                                break;
                            case "FINDRECOMMENDATION":
                                title = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2).getName();
                                break;
                            default:
                                title = mNavItems.get(1);
                                break;
                        }
                    }
                } else {
                    title = mNavItems.get(1);
                }

                //((TextView) findViewById(R.id.tvSectionTitle)).setText(title);
                setPageTitle(title);
                super.onBackPressed();
            }
        }
    }

    public void loadNextSection(int position, int fragId, boolean fromSummary) {
        this.fragmentId = fragId;
//        TextView sectionTitle = findViewById(R.id.tvSectionTitle);
//        sectionTitle.setText(mNavItems.get(position).toString());

        selectItemFromDrawer(position, mNavItems.get(position), fromSummary);
    }

    public void loadNextSection(String sectionHeading, int fragId) {
        this.fragmentId = fragId;
//        TextView sectionTitle = findViewById(R.id.tvSectionTitle);
//        sectionTitle.setText(sectionHeading);
        setPageTitle(sectionHeading);

        Fragment fragment = null;

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        String backStackLabel = null;
        if (sectionHeading.toUpperCase().contains("SUB-COUNTY")) {
            fragment = new SubCountyFragment();
            backStackLabel = "Location2";

        } else if (sectionHeading.toUpperCase().contains("COUNTY")) {
            fragment = new CountyFragment();
            backStackLabel = "Location1";
        } else if (sectionHeading.toUpperCase().contains("FIND RECOMMENDATION")) {
            fragment = new FindRecommendationFragment();
            backStackLabel = "FindRecommendation";
        }

        if (fragmentId == 0 && fragment != null) {
            transaction.replace(R.id.mainContent, fragment);
        } else {
            transaction.replace(R.id.mainContent, fragment);
            transaction.addToBackStack(backStackLabel);
        }

        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public void showBurgerMenu() {
        ivBurgerMenu.setVisibility(View.VISIBLE);
        ivPWLogoLeft.setVisibility(View.INVISIBLE);
        ivPWLogoRight.setVisibility(View.VISIBLE);
    }

    public void hideBurgerMenu() {
        ivBurgerMenu.setVisibility(View.INVISIBLE);
        ivPWLogoLeft.setVisibility(View.VISIBLE);
        ivPWLogoRight.setVisibility(View.INVISIBLE);
    }

    public void goBack() {
        super.onBackPressed();
    }

    class DCALocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Toast.makeText(getBaseContext(), "Location is - " + location.getLatitude() + " , " + location.getLongitude(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Location is NULL", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}