package org.cabi.pdc.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.cabi.pdc.R;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.UrlFactory;
import org.cabi.pdc.helpers.CustomJSONObjectRequest;
import org.cabi.pdc.helpers.VolleyController;
import org.cabi.pdc.interfaces.VolleyCallback;
import org.cabi.pdc.models.AllTimeClinicStats;
import org.cabi.pdc.models.AllTimeSessionStats;
import org.cabi.pdc.models.Field;
import org.cabi.pdc.models.FormDefinition;
import org.cabi.pdc.models.Metadata;
import org.cabi.pdc.models.MetadataTranslation;
import org.cabi.pdc.models.MyMonthlyReport;
import org.cabi.pdc.models.NationalReport;
import org.cabi.pdc.models.Section;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasscodeActivity extends AppCompatActivity {
    private boolean isPasscodeSaved;
    private TextView passcodeLabel;
    private EditText et_First, et_Second, et_Third, et_Fourth;
    private String passcodeNewOrConfirm = "NEW";
    private HashMap<String, String> headers;
    private int inCorrectPin = 0;
    LinearLayout layoutPasscode;
    RelativeLayout layoutProgress;
    DCAApplication dcaApplication;
    Button btnOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        dcaApplication = (DCAApplication) getApplication();

        SharedPreferences spLogin = getSharedPreferences("LoginInfos", MODE_PRIVATE);
        if (spLogin.contains("Passcode")) {
            String passcode = spLogin.getString("Passcode", "");
            if (!TextUtils.isEmpty(passcode)) {
                isPasscodeSaved = true;
            }
            //getIntent().getExtras().getBoolean("IsSignedIn", false);
        }

        passcodeLabel = findViewById(R.id.txt_Passcode_Label);
        et_First = findViewById(R.id.et_Passcode_First);
        et_First.addTextChangedListener(new DCATextWatcher(et_First));
        et_Second = findViewById(R.id.et_Passcode_Second);
        et_Second.addTextChangedListener(new DCATextWatcher(et_Second));
        et_Third = findViewById(R.id.et_Passcode_Third);
        et_Third.addTextChangedListener(new DCATextWatcher(et_Third));
        et_Fourth = findViewById(R.id.et_Passcode_Fourth);
        et_Fourth.addTextChangedListener(new DCATextWatcher(et_Fourth));

        if (isPasscodeSaved) {
            passcodeLabel.setText("Enter your passcode");
            passcodeNewOrConfirm = "CONFIRM";
        } else {
            passcodeLabel.setText("Create a passcode");
            passcodeNewOrConfirm = "NEW";
        }

        if (headers == null) {
            headers = new HashMap<>();
            headers.put("jwt", ApiData.getInstance().getJwtToken());
        }

        layoutPasscode = findViewById(R.id.layoutPasscode);
        layoutProgress = findViewById(R.id.layoutProgress);

        if (ApiData.getInstance().getMyMonthlyReport() == null || ApiData.getInstance().getNationalReport() == null) {
            String urlReports = UrlFactory.getReport;
            makeRequest(urlReports, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) throws JSONException {

                    showPasscodeUI();
                    Gson gson = new Gson();
                    ApiData.getInstance().setMyMonthlyReport(gson.fromJson(result.getJSONObject("MyMonthlyReport").toString(), MyMonthlyReport.class));
                    ApiData.getInstance().getMyMonthlyReport().setAllTimeClinicStats(gson.fromJson(result.getJSONObject("MyMonthlyReport").getJSONObject("AllTimeClinicStats").toString(), AllTimeClinicStats.class));
                    ApiData.getInstance().getMyMonthlyReport().setAllTimeSessionStats(gson.fromJson(result.getJSONObject("MyMonthlyReport").getJSONObject("AllTimeSessionStats").toString(), AllTimeSessionStats.class));
                    ApiData.getInstance().setNationalReport(gson.fromJson(result.getJSONObject("NationalReport").toString(), NationalReport.class));
                    ApiData.getInstance().getNationalReport().setAllTimeClinicStats(gson.fromJson(result.getJSONObject("NationalReport").getJSONObject("AllTimeClinicStats").toString(), AllTimeClinicStats.class));
                    ApiData.getInstance().getNationalReport().setAllTimeSessionStats(gson.fromJson(result.getJSONObject("NationalReport").getJSONObject("AllTimeSessionStats").toString(), AllTimeSessionStats.class));

                    SharedPreferences spForms = getSharedPreferences("FormData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = spForms.edit();
                    editor.putString("Reports", result.toString());
                    editor.commit();
                }

                @Override
                public void onError(String err) throws Exception {
                    Toast.makeText(getApplicationContext(), "GET_REPORTS_Error: " + err, Toast.LENGTH_LONG).show();
                    showPasscodeUI();
                }
            }, headers, "getReports");
        }

        String urlFormDefsUpdate = UrlFactory.getFormdefsUpdates;
        makeRequest(urlFormDefsUpdate, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                showPasscodeUI();
                String formsLastUpdate = result.getString("FormsLastUpdate");
                if (!TextUtils.isEmpty(formsLastUpdate)) {

                }
                String metadataLastUpdate = result.getString("MetadataLastUpdate");
                String message = result.getString("Message");
            }

            @Override
            public void onError(String result) throws Exception {
                showPasscodeUI();
            }
        }, headers, "getFormDefUpdates");

        if (ApiData.getInstance().getFormDefinition() == null) {
            String urlFormDefinitions = UrlFactory.getFormdefs; //"http://www.mocky.io/v2/5b8ee5b532000069177b3b72";
            makeRequest(urlFormDefinitions, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) throws JSONException {
                    Gson gson = new Gson();
                    ApiData.getInstance().setFormDefinition(gson.fromJson(result.getJSONArray("FormDefinitions").get(0).toString(), FormDefinition.class));

                    ArrayList<Section> listSections;
                    ArrayList<Field> listFields;

                    JSONArray jArrSections = new JSONArray(new JSONObject(result.getJSONArray("FormDefinitions").get(0).toString()).get("Sections").toString());
                    if (jArrSections != null && jArrSections.length() > 0) {
                        listSections = ApiData.getInstance().getFormDefinition().getSections();
                        for (int i = 0; i < jArrSections.length(); i++) {
                            Section sectionItem = listSections.get(i);
                            if (!new JSONObject(jArrSections.get(i).toString()).get("Fields").equals(null)) {
                                JSONArray jArrFields = new JSONArray(new JSONObject(jArrSections.get(i).toString()).get("Fields").toString());
                                listFields = new ArrayList<Field>();
                                if (jArrFields != null && jArrFields.length() > 0) {
                                    for (int j = 0; j < jArrFields.length(); j++) {
                                        listFields.add(gson.fromJson(jArrFields.get(j).toString(), Field.class));
                                    }
                                    sectionItem.setFields(listFields);
                                }
//                            listSections.add(sectionItem);
                            }
                        }
                        ApiData.getInstance().getFormDefinition().setSections(listSections);

                        SharedPreferences spForms = getSharedPreferences("FormData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = spForms.edit();
                        editor.putString("FormDefs", result.toString());
                        editor.commit();
                    }
                    showPasscodeUI();
                }

                @Override
                public void onError(String result) throws Exception {
                    showPasscodeUI();
                    Toast.makeText(getApplicationContext(), "Error: " + result, Toast.LENGTH_LONG).show();
                }
            }, headers, "getFormDefs");
        }

        if (ApiData.getInstance().getMetadataList() == null) {
            String urlMetadataComplete = UrlFactory.getMetadataComplete;
            makeRequest(urlMetadataComplete, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) throws JSONException {
                    ApiData.getInstance().setMetaDataLastUpdate(result.getString("MetadataLastUpdate"));

                    JSONArray jArrMetadata = result.getJSONArray("MetaDatas");
                    if (jArrMetadata != null && jArrMetadata.length() > 0) {
                        ArrayList<Metadata> listMetadata = new ArrayList<Metadata>();
                        Gson gson = new Gson();
                        List<Metadata> plantDoctors = new ArrayList<>();
                        List<Metadata> clinics = new ArrayList<>();
                        List<Metadata> crops = new ArrayList<>();
                        List<Metadata> varieties = new ArrayList<>();
                        List<Metadata> diagnosis = new ArrayList<>();

                        for (int i = 0; i < jArrMetadata.length(); i++) {
                            Metadata item = gson.fromJson(jArrMetadata.get(i).toString(), Metadata.class);
                            listMetadata.add(item);
                            switch (item.getTemplate().toUpperCase()) {
                                case "PLANTDOCTOR":
                                    plantDoctors.add(item);
                                    break;
                                case "CLINICCODE":
                                    clinics.add(item);
                                    break;
                                case "CROP":
                                    crops.add(item);
                                    break;
                                case "VARIETY":
                                    varieties.add(item);
                                    break;
                                case "DIAGNOSIS":
                                    diagnosis.add(item);
                                    break;
                            }
                        }
                        dcaApplication.setPlantDoctorMetadata(plantDoctors);
                        dcaApplication.setClinicCodeMetadata(clinics);
                        dcaApplication.setCropMetadata(crops);
                        dcaApplication.setCropVarietyMetadata(varieties);
                        dcaApplication.setDiagnosisMetadata(diagnosis);
                        ApiData.getInstance().setMetadataList(listMetadata);

                        SharedPreferences spForms = getSharedPreferences("FormData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = spForms.edit();
                        editor.putString("MetaDataComplete", result.toString());
                        editor.commit();
                    }
                    showPasscodeUI();
                }

                @Override
                public void onError(String err) throws Exception {
                    Toast.makeText(PasscodeActivity.this, "Error: " + err, Toast.LENGTH_LONG).show();
                    showPasscodeUI();
                }
            }, headers, "metadata/complete");
        }

        if (ApiData.getInstance().getMetadataTranslationsList() == null) {
            String urlMetadataTranslation = UrlFactory.getMetadataTranslations;
            makeRequest(urlMetadataTranslation, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) throws JSONException {
                    JSONArray jArrTranslations = result.getJSONArray("Translations");
                    if (jArrTranslations != null && jArrTranslations.length() > 0) {
                        HashMap<String, MetadataTranslation> listMetadataTranslationset = new HashMap<>();
                        Gson gson = new Gson();
                        for (int i = 0; i < jArrTranslations.length(); i++) {
                            listMetadataTranslationset.put(new JSONObject(jArrTranslations.get(i).toString()).getString("Id"), gson.fromJson(jArrTranslations.get(i).toString(), MetadataTranslation.class));
                        }
                        ApiData.getInstance().setMetadataTranslationsList(listMetadataTranslationset);

                        SharedPreferences spForms = getSharedPreferences("FormData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = spForms.edit();
                        editor.putString("MetadataTranslation", result.toString());
                        editor.commit();
                    }
                    showPasscodeUI();
                }

                @Override
                public void onError(String err) throws Exception {
                    Toast.makeText(getApplicationContext(), "Error: " + err, Toast.LENGTH_LONG).show();
                    showPasscodeUI();
                }
            }, headers, "metadata/Translation");
        }

        locationStatusCheck();

        if (!dcaApplication.isAppUpdateDismissed()) {
            appVersionCheck();
        }

        btnOK = findViewById(R.id.btnOK);
    }

    private void showPasscodeUI() {
        layoutPasscode.setVisibility(View.VISIBLE);
        layoutProgress.setVisibility(View.GONE);
    }

    public void makeRequest(final String url, final VolleyCallback callback, final HashMap<String, String> headers, String tag) {
        CustomJSONObjectRequest req = new CustomJSONObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("Response", response.toString());
                        try {
                            if (response != null) {
                                callback.onSuccess(response);
                            } else {
                                callback.onError("Null response from server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Response", error.toString());
                        String err = null;
                        if (error instanceof NoConnectionError) {
                            err = "No Internet access";
                        }
                        try {
                            if (err != null) {
                                callback.onError(err);
                            } else {
                                callback.onError(error.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleyController.getInstance(this).addToRequestQueue(req, tag);
    }

    public void makePOSTRequest(final String url, final VolleyCallback callback, final HashMap<String, String> headers, String tag) {
        CustomJSONObjectRequest req = new CustomJSONObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null) {
                                callback.onSuccess(response);
                                Log.v("Response", response.toString());
                            } else if (response == null && url.contains("auth/savepasscode")) {
                                callback.onSuccess(response);
                            } else {
                                callback.onError("Null response from server");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("Response", error.toString());
                        String err = null;
                        if (error instanceof NoConnectionError) {
                            err = "No Internet access";
                        }
                        try {
                            if (err != null) {
                                callback.onError(err);
                            } else {
                                callback.onError(error.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleyController.getInstance(this).addToRequestQueue(req, tag);
    }

    public void Click(View v) {
        if (v.getId() == R.id.btnOK) {

            String errorPasscodeInvalid = "Your passcode was not recognised - Please try again. If the problem persists, contact your support person.";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("ErrorPasscodeInvalid")) {
                errorPasscodeInvalid = ApiData.getInstance().getMetadataTranslationsList().get("ErrorPasscodeInvalid").getValue();
            }

            if (inCorrectPin > 3) {
                Toast.makeText(this, errorPasscodeInvalid, Toast.LENGTH_SHORT).show();
                return;
            }

            String errorPasscodeIncomplete = "Too few digits. Please input all four digits of your passcode.";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("ErrorPasscodeIncomplete")) {
                errorPasscodeIncomplete = ApiData.getInstance().getMetadataTranslationsList().get("ErrorPasscodeIncomplete").getValue();
            }

            if (passcodeNewOrConfirm.toUpperCase().equals("CONFIRM")) {
                if (!TextUtils.isEmpty(et_First.getText().toString()) &&
                        !TextUtils.isEmpty(et_Second.getText().toString()) &&
                        !TextUtils.isEmpty(et_Third.getText().toString()) &&
                        !TextUtils.isEmpty(et_Fourth.getText().toString())) {

                    String passcode = et_First.getText().toString() + et_Second.getText().toString() + et_Third.getText().toString() + et_Fourth.getText().toString();

                    SharedPreferences spLogin = getSharedPreferences("LoginInfos", MODE_PRIVATE);
                    if (spLogin.contains("Passcode")) {
                        if (spLogin.getString("Passcode", "").equals(passcode)) {
                            et_First.setText("");
                            et_Second.setText("");
                            et_Third.setText("");
                            et_Fourth.setText("");

                            ApiData.getInstance().setPasscodeChecked(true);

                            Intent backIntent = new Intent(this, MainActivity.class);
//                            backIntent.putExtra("FROM_PASSCODE", "TRUE");
//                            setResult(RESULT_OK, backIntent);
                            finish();
                        } else {
                            Toast.makeText(this, errorPasscodeInvalid, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, errorPasscodeIncomplete, Toast.LENGTH_SHORT).show();
                }
            } else {

                if (!TextUtils.isEmpty(et_First.getText().toString()) &&
                        !TextUtils.isEmpty(et_Second.getText().toString()) &&
                        !TextUtils.isEmpty(et_Third.getText().toString()) &&
                        !TextUtils.isEmpty(et_Fourth.getText().toString())) {

                    String passcode = et_First.getText().toString() + et_Second.getText().toString() + et_Third.getText().toString() + et_Fourth.getText().toString();

                    SharedPreferences spLogin = getSharedPreferences("LoginInfos", MODE_PRIVATE);
                    SharedPreferences.Editor editor = spLogin.edit();
                    editor.putString("Passcode", passcode);
                    editor.commit();

                    et_First.setText("");
                    et_Second.setText("");
                    et_Third.setText("");
                    et_Fourth.setText("");

                    String urlSavePasscode = UrlFactory.postSavePasscode;
                    urlSavePasscode = String.format(urlSavePasscode, passcode);
                    if (headers == null) {
                        headers = new HashMap<>();
                    }
                    if (!headers.containsKey("jwt")) {
                        headers.put("jwt", ApiData.getInstance().getJwtToken());
                    }
                    makePOSTRequest(urlSavePasscode, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject result) throws JSONException {
                            String s = result.toString();
                            Toast.makeText(PasscodeActivity.this, "POST_SAVE_PASSCODE: Successfully saved in DB", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String error) throws Exception {
                            Toast.makeText(PasscodeActivity.this, "POST_SAVE_PASSCODE: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }, headers, "POST_SAVE_PASSCODE");

                    passcodeLabel.setText("Enter your passcode");
                    passcodeNewOrConfirm = "CONFIRM";
                    et_First.requestFocus();
                } else {
                    Toast.makeText(this, errorPasscodeIncomplete, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void appVersionCheck() {

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            final String currentVersion = pInfo.versionName;

            String appVersionUrl = "http://www.mocky.io/v2/5c24769e30000078007a5fc7";
            makeRequest(appVersionUrl, new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject result) throws JSONException {

                    if (result.has("DCA_APP_VERSION")) {
                        String latestVersion = result.getString("DCA_APP_VERSION");

                        if (!latestVersion.equals(currentVersion)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(PasscodeActivity.this, R.style.dcaAlertDialog);
                            builder.setTitle("Update the Plantwise Data Collection app");
                            builder.setMessage("There is a new version of this app available in the Google Play Store. Update the app to get new features and improvements");
                            builder.setCancelable(true);
                            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=org.cabi.pdc")));
                                    dialog.dismiss();
                                }

                                ;
                            });
                            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dcaApplication.setAppUpdateDismissed(true);
                                    dialog.cancel();
                                }
                            });

                            AlertDialog updateAppAlert = builder.create();
                            updateAppAlert.show();
                            updateAppAlert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ButtonGray)));
                            updateAppAlert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
                            updateAppAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Not getting updated version from api", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String result) throws Exception {
                    Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new HashMap<String, String>(), "APP_VERSION_CHECK");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void locationStatusCheck() {
        final LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("To continue, turn on device location, which uses Google's location service")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public class DCATextWatcher implements TextWatcher {
        private EditText eT;

        public DCATextWatcher(EditText e) {
            eT = e;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String s = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String s = charSequence.toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String s = editable.toString();
            switch (eT.getId()) {
                case R.id.et_Passcode_First:
                    if (s.length() > 0) {
                        et_Second.requestFocus();
                    }
                    break;
                case R.id.et_Passcode_Second:
                    if (s.length() > 0) {
                        et_Third.requestFocus();
                    }
                    break;
                case R.id.et_Passcode_Third:
                    if (s.length() > 0) {
                        et_Fourth.requestFocus();
                    }
                    break;
                case R.id.et_Passcode_Fourth: {
                    if (s.length() > 0) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(et_Fourth.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        btnOK.requestFocus();
                    }
                }
                break;
            }
        }
    }
}