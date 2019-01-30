package org.cabi.pdc.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.DCAFormDatabase;
import org.cabi.pdc.RoomDb.RoomDB_Session;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.GifImageView;
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

public class MainActivity extends AppCompatActivity {
    private final int RC_SIGN_IN = 1000;
    private static final int REQUEST_CODE_FOR_PASSCODE = 1001;
    private GoogleSignInClient mGoogleSignInClient;
    private boolean isSignedIn = false;
    DCAApplication dcaApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
//            Thread.sleep(2000);
        } catch (Exception e) {
        }
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        super.onCreate(savedInstanceState);

//        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        dcaApplication = (DCAApplication) getApplicationContext();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        SharedPreferences sP = getSharedPreferences("LoginInfos", MODE_PRIVATE);
        if (sP.contains("IsSignedIn") && sP.contains("jwt")) {
            String signIn = sP.getString("IsSignedIn", "");
            if (!TextUtils.isEmpty(signIn)) {
                isSignedIn = Boolean.parseBoolean(signIn);
            }
            if (TextUtils.isEmpty(ApiData.getInstance().getJwtToken())) {
                String jwt = sP.getString("jwt", "");
                if (!TextUtils.isEmpty(jwt)) {
                    ApiData.getInstance().setJwtToken(jwt);
                }
            }
        } else {
            isSignedIn = false;
        }

        if (sP.contains("userEmail")) {
            ApiData.getInstance().setUserEmail(sP.getString("userEmail", ""));
        }

        if (isSignedIn) {
            setContentView(R.layout.activity_main);

            SharedPreferences spForms = getSharedPreferences("FormData", MODE_PRIVATE);
            if (spForms.contains("Reports") && (ApiData.getInstance().getMyMonthlyReport() == null || ApiData.getInstance().getNationalReport() == null)) {
                if (!TextUtils.isEmpty(spForms.getString("Reports", ""))) {
                    try {
                        JSONObject result = new JSONObject(spForms.getString("Reports", ""));
                        if (result != null) {
                            Gson gson = new Gson();
                            ApiData.getInstance().setMyMonthlyReport(gson.fromJson(result.getJSONObject("MyMonthlyReport").toString(), MyMonthlyReport.class));
                            ApiData.getInstance().getMyMonthlyReport().setAllTimeClinicStats(gson.fromJson(result.getJSONObject("MyMonthlyReport").getJSONObject("AllTimeClinicStats").toString(), AllTimeClinicStats.class));
                            ApiData.getInstance().getMyMonthlyReport().setAllTimeSessionStats(gson.fromJson(result.getJSONObject("MyMonthlyReport").getJSONObject("AllTimeSessionStats").toString(), AllTimeSessionStats.class));
                            ApiData.getInstance().setNationalReport(gson.fromJson(result.getJSONObject("NationalReport").toString(), NationalReport.class));
                            ApiData.getInstance().getNationalReport().setAllTimeClinicStats(gson.fromJson(result.getJSONObject("NationalReport").getJSONObject("AllTimeClinicStats").toString(), AllTimeClinicStats.class));
                            ApiData.getInstance().getNationalReport().setAllTimeSessionStats(gson.fromJson(result.getJSONObject("NationalReport").getJSONObject("AllTimeSessionStats").toString(), AllTimeSessionStats.class));
                        }
                    } catch (JSONException je) {

                    }
                }
            }

            if (spForms.contains("FormDefs") && ApiData.getInstance().getFormDefinition() == null) {
                if (!TextUtils.isEmpty(spForms.getString("FormDefs", ""))) {
                    try {
                        JSONObject result = new JSONObject(spForms.getString("FormDefs", ""));
                        if (result != null) {
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
                                    }
                                }
                                ApiData.getInstance().getFormDefinition().setSections(listSections);
                            }
                        }
                    } catch (JSONException je) {

                    }
                }
            }

            if (spForms.contains("MetaDataComplete") && ApiData.getInstance().getMetadataList() == null) {
                if (!TextUtils.isEmpty(spForms.getString("MetaDataComplete", ""))) {
                    try {
                        JSONObject result = new JSONObject(spForms.getString("MetaDataComplete", ""));
                        if (result != null) {
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
                            }
                        }
                    } catch (JSONException je) {

                    }
                }
            }

            if (spForms.contains("MetadataTranslation") && ApiData.getInstance().getMetadataTranslationsList() == null) {
                if (!TextUtils.isEmpty(spForms.getString("MetadataTranslation", ""))) {
                    try {
                        JSONObject result = new JSONObject(spForms.getString("MetadataTranslation", ""));
                        if (result != null) {
                            JSONArray jArrTranslations = result.getJSONArray("Translations");
                            if (jArrTranslations != null && jArrTranslations.length() > 0) {
                                HashMap<String, MetadataTranslation> listMetadataTranslationset = new HashMap<>();
                                Gson gson = new Gson();
                                for (int i = 0; i < jArrTranslations.length(); i++) {
                                    listMetadataTranslationset.put(new JSONObject(jArrTranslations.get(i).toString()).getString("Id"), gson.fromJson(jArrTranslations.get(i).toString(), MetadataTranslation.class));
                                }
                                ApiData.getInstance().setMetadataTranslationsList(listMetadataTranslationset);
                            }
                        }
                    } catch (JSONException je) {

                    }
                }
            }

            if (!ApiData.getInstance().isPasscodeChecked()) { //goToPasscode
                startActivityForResult(new Intent(MainActivity.this, PasscodeActivity.class), REQUEST_CODE_FOR_PASSCODE);
            } else {

                Button btnNewForm = findViewById(R.id.btnNewForm);
                if (ApiData.getInstance().getCurrentForm() != null && ApiData.getInstance().getCurrentForm().getFormStatus().toUpperCase().equals("CONTINUE")) {
                    String continueForm = "Continue";
                    if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavContinue")) {
                        continueForm = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavContinue").getValue();
                    }
                    btnNewForm.setText(continueForm);
                } else {
                    String newForm = "New form";
                    if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavNewForm")) {
                        newForm = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavNewForm").getValue();
                    }
                    btnNewForm.setText(newForm);
                }

                TextView tvDraft = findViewById(R.id.tvDrafts);
                String strDraft = "Draft";
                Button btnDraft = findViewById(R.id.btnEditDrafts);
                String strEditDraft = "Edit";
                TextView tvNotSent = findViewById(R.id.tvNotSent);
                String strNotSent = "Not Sent";
                Button btnNotSent = findViewById(R.id.btnNotSent);
                String strBtnNotSent = "Send";
                TextView tvSent = findViewById(R.id.tvSent);
                String strSent = "Sent";
                Button btnSent = findViewById(R.id.btnSent);
                String strBtnSent = "View";
                TextView tvTotal = findViewById(R.id.tvTotal);
                String strTotal = "Total";
                Button btnTotal = findViewById(R.id.btnTotal);
                String strBtnTotal = "View";
                TextView tvMyReports = findViewById(R.id.tvHomeMyReports);
                String strMyReports = "My reports";

                if (ApiData.getInstance().getMetadataTranslationsList() != null) {
                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavDraft")) {
                        strDraft = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavDraft").getValue();
                    }
                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavEdit")) {
                        strEditDraft = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavEdit").getValue();
                    }
                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavNotSent")) {
                        strNotSent = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavNotSent").getValue();
                    }
                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavSend")) {
                        strBtnNotSent = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavSend").getValue();
                    }
                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavSent")) {
                        strSent = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavSent").getValue();
                    }
                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavView")) {
                        strBtnSent = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavView").getValue();
                    }
                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavTotal")) {
                        strTotal = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavTotal").getValue();
                    }
                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavView")) {
                        strBtnTotal = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavView").getValue();
                    }
                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("InitialNavMyReports")) {
                        strMyReports = ApiData.getInstance().getMetadataTranslationsList().get("InitialNavMyReports").getValue();
                    }
                }

                tvDraft.setText(strDraft);
                btnDraft.setText(strEditDraft);
                tvNotSent.setText(strNotSent);
                btnNotSent.setText(strBtnNotSent);
//                btnNotSent.setVisibility(View.INVISIBLE);
                tvSent.setText(strSent);
                btnSent.setText(strBtnSent);
                tvTotal.setText(strTotal);
                btnTotal.setText(strBtnTotal);
                tvMyReports.setText(strMyReports);

                if (FormActivity.dcaFormDatabase == null) {
                    FormActivity.dcaFormDatabase = Room.databaseBuilder(getApplicationContext(), DCAFormDatabase.class, "DCAFormsDB").allowMainThreadQueries().build();
                }
                List<RoomDB_Session> allSessions = FormActivity.dcaFormDatabase.dcaSessionDao().getAllSessions();
                List<RoomDb_Form> allForms = FormActivity.dcaFormDatabase.dcaFormDao().getAllForms();
                int draft = 0, sent = 0, notSent = 0, total;
//                if (allSessions != null & allSessions.size() > 0) {
//                    for (RoomDB_Session sessItem : allSessions) {
//                        draft = draft + sessItem.getDraft();
//                        sent = sent + sessItem.getSubmitted();
//                    }
//                }
//                if (ApiData.getInstance().getCurrentSession() != null) {
//                    draft = draft + ApiData.getInstance().getCurrentSession().getDraft();
//                    sent = sent + ApiData.getInstance().getCurrentSession().getSubmitted();
//                }

                if (allForms != null && allForms.size() > 0) {
                    for (RoomDb_Form formItem : allForms) {
                        switch (formItem.getFormStatus().toUpperCase()) {
                            case "DRAFT":
                                draft = draft + 1;
                                break;
                            case "SENT":
                                sent = sent + 1;
                                break;
                            case "NOTSENT":
                                notSent = notSent + 1;
                                break;
                        }
                    }
                }
                total = draft + sent + notSent;

                TextView tvDraftCount, tvSentCount, tvNotsentCount, tvTotalCount;
                tvDraftCount = findViewById(R.id.tvDraftsCount);
                tvDraftCount.setText(String.valueOf(draft));
                if (draft > 0) {
                    btnDraft.setVisibility(View.VISIBLE);
                } else {
                    btnDraft.setVisibility(View.INVISIBLE);
                }
                tvNotsentCount = findViewById(R.id.tvNotSentCount);
                tvNotsentCount.setText(String.valueOf(notSent));
                if (notSent > 0) {
                    btnNotSent.setVisibility(View.VISIBLE);
                } else {
                    btnNotSent.setVisibility(View.INVISIBLE);
                }
                tvSentCount = findViewById(R.id.tvSentCount);
                tvSentCount.setText(String.format("%d", sent));
                if (sent > 0) {
                    btnSent.setVisibility(View.VISIBLE);
                } else {
                    btnSent.setVisibility(View.INVISIBLE);
                }
                tvTotalCount = findViewById(R.id.tvTotalCount);
                tvTotalCount.setText(Integer.toString(total));
                if (total > 0) {
                    btnTotal.setVisibility(View.VISIBLE);
                } else {
                    btnTotal.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            if (account == null) {
                setContentView(R.layout.activity_google_sign_in);
                GifImageView gifImageView = (GifImageView) findViewById(R.id.gifImage);
                gifImageView.setGifImageResource(R.drawable.leaf_loader);
            } else {
                updateUI(account);
            }
        }
    }

    public void makeRequest(final String url, final VolleyCallback callback, final HashMap<String, String> headers, String tag) {
        CustomJSONObjectRequest req = new CustomJSONObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("Response", response.toString());
                        try {
//                            String resp = response.getString("Countries");
                            if (response != null) {
                                callback.onSuccess(response);
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

        VolleyController.getInstance(getApplicationContext()).addToRequestQueue(req, tag);
    }

    public void btnApiActivityClick(View view) {
        Intent i = new Intent(this, ApiActivity.class);
        startActivity(i);
    }

    public void Click(View view) {
        final Intent i;
        switch (view.getId()) {
            case R.id.btnNewForm: {
                i = new Intent(this, FormActivity.class);
                startActivity(i);
                break;
            }
            case R.id.btnSettings: {
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.btnMyReports: {
                i = new Intent(this, ReportsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.btnGoogleSignIn: {

//                final RelativeLayout defaultProgress = findViewById(R.id.defaultProgress);
//                final LinearLayout googleSignIn = findViewById(R.id.layoutSignIn);
//                defaultProgress.setVisibility(View.VISIBLE);
//                googleSignIn.setVisibility(View.GONE);

                googleSignIn();

//                String url = UrlFactory.verifyUserByEmail;
//                url = String.format(url, "s.gupta@pwdoctor.org"); //pwkbindia@pwdoctor.org

//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                if (response.contains("\"")) {
//                                    ApiData.getInstance().setJwtToken(response.replace("\"", ""));
//                                } else {
//                                    ApiData.getInstance().setJwtToken(response);
//                                }
//
//                                if (TextUtils.isEmpty(ApiData.getInstance().getUserEmail())) {
//                                    ApiData.getInstance().setUserEmail("s.gupta@pwdoctor.org");
//                                }
//
//                                SharedPreferences sp = getSharedPreferences("LoginInfos", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sp.edit();
//                                editor.putString("IsSignedIn", "true");
//                                editor.putString("jwt", ApiData.getInstance().getJwtToken());
//                                editor.putString("userEmail", ApiData.getInstance().getUserEmail());
//                                editor.commit();
//
//                                defaultProgress.setVisibility(View.INVISIBLE);
//                                startActivityForResult(new Intent(MainActivity.this, PasscodeActivity.class), REQUEST_CODE_FOR_PASSCODE);
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(MainActivity.this, "Authenticate user by email api error: " + error, Toast.LENGTH_LONG).show();
//                                defaultProgress.setVisibility(View.INVISIBLE);
//                            }
//                        });
//
//                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                requestQueue.add(stringRequest);
            }
            break;
            case R.id.btnEditDrafts: {
                i = new Intent(this, AllQueriesActivity.class);
                i.putExtra("LoadSection", "DRAFTS");
                startActivity(i);
            }
            break;
            case R.id.btnNotSent: {
                Button notSent = findViewById(R.id.btnNotSent);
                notSent.setVisibility(View.INVISIBLE);
//                GifImageView gifSending = (GifImageView) findViewById(R.id.gifSending);
//                gifSending.setGifImageResource(R.drawable.sending);
//                gifSending.setVisibility(View.VISIBLE);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Sending forms failed", Toast.LENGTH_SHORT).show();
                notSent.setVisibility(View.VISIBLE);
//                gifSending.setVisibility(View.INVISIBLE);
            }
            break;
            case R.id.btnSent: {
                i = new Intent(this, AllQueriesActivity.class);
                i.putExtra("LoadSection", "SENT");
                startActivity(i);
            }
            break;
            case R.id.btnTotal: {
                i = new Intent(this, AllQueriesActivity.class);
                i.putExtra("LoadSection", "ALL");
                startActivity(i);
            }
            break;
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            return;
        }

//        if (!TextUtils.isEmpty(ApiData.getInstance().getJwtToken()) && !TextUtils.isEmpty(ApiData.getInstance().getUserEmail())) {
//            return;
//        }

        try {
            final String userEmail;

            if (TextUtils.isEmpty(account.getEmail())) {
                userEmail = "pwkbindia@pwdoctor.org";
            } else {
                userEmail = account.getEmail();
            }

            String url = UrlFactory.verifyUserByEmail;
            url = String.format(url, userEmail);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("User")) {
                                return;
                            } else {
                                if (response.contains("\"")) {
                                    ApiData.getInstance().setJwtToken(response.replace("\"", ""));
                                } else {
                                    ApiData.getInstance().setJwtToken(response);
                                }

                                if (TextUtils.isEmpty(ApiData.getInstance().getUserEmail())) {
                                    ApiData.getInstance().setUserEmail(userEmail);
                                }

                                SharedPreferences sp = getSharedPreferences("LoginInfos", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("IsSignedIn", "true");
                                editor.putString("jwt", ApiData.getInstance().getJwtToken());
                                editor.putString("userEmail", ApiData.getInstance().getUserEmail());
                                editor.commit();

                                findViewById(R.id.defaultProgress).setVisibility(View.INVISIBLE);
                                startActivityForResult(new Intent(MainActivity.this, PasscodeActivity.class), REQUEST_CODE_FOR_PASSCODE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            findViewById(R.id.defaultProgress).setVisibility(View.INVISIBLE);
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        } catch (Exception e) {

        }
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        try {
            Log.d("startActivityForResult","startActivityForResult");
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        catch (Exception e){
            Log.d("error","error");
            e.printStackTrace();
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            List<String> err = new ArrayList<>();
            err.add("Google sign in failed for this user");
            err.add("Server error");
            err.add("Google Play authentication is required");
            err.add("DEVELOPER_ERROR This application is misconfigured");

//            int t = Integer.valueOf(String.valueOf(Math.floor(Math.random() * 10) % 4).substring(0, 1));

//            Toast.makeText(this, "DEVELOPER_ERROR This application is misconfigured", Toast.LENGTH_SHORT).show();
            updateUI(account); //account
        } catch (ApiException e) {
            Log.d("error","error");
            e.printStackTrace();
            Log.w("TAG_GOOGLE_SIGN_IN", "GOOGLE SIGN IN EXCEPTION:: " + e.getMessage());
            Toast.makeText(this, "Google Sign In Failed. Try Again", Toast.LENGTH_SHORT).show();
            updateUI(null);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
//        else if (requestCode == REQUEST_CODE_FOR_PASSCODE) {
//            if (resultCode == RESULT_OK) {
//                if (data != null) {
//
//                    if (!TextUtils.isEmpty(data.getStringExtra("FROM_PASSCODE"))) {
//                        String from = data.getStringExtra("FROM_PASSCODE");
//                        if (!TextUtils.isEmpty(from) && from.toUpperCase().equals("TRUE")) {
//                            goToPasscode = false;
//                        } else {
//                            goToPasscode = true;
//                        }
//                    }
//                }
//            }
//        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

}