package org.cabi.pdc.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.RoomDB_Session;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.UrlFactory;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.helpers.CustomJSONObjectRequest;
import org.cabi.pdc.helpers.VolleyController;
import org.cabi.pdc.interfaces.VolleyCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormFinishActivity extends AppCompatActivity {
    HashMap<String, String> headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_finish);
        TextView todayDate = findViewById(R.id.FormEnd_Txt_TodayDate);
        if (TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getFormStartDateTime())) {
            Toast.makeText(this, "Form Date is Empty", Toast.LENGTH_SHORT).show();
        } else {
            String date = Utility.getDateFromDateTime(ApiData.getInstance().getCurrentForm().getFormStartDateTime());
            todayDate.setText(date);
        }
    }

    public void Click(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.FormEnd_Btn_SameFarmerNewProblem: {
                intent = new Intent(this, FormActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                RoomDb_Form form = new RoomDb_Form();
                form.setPlantDoctor(ApiData.getInstance().getCurrentForm().getPlantDoctor());
                form.setClinicCode(ApiData.getInstance().getCurrentForm().getClinicCode());
                form.setFarmerName(ApiData.getInstance().getCurrentForm().getFarmerName());
                form.setFarmerPhoneNumber(ApiData.getInstance().getCurrentForm().getFarmerPhoneNumber());
                form.setFarmerGender(ApiData.getInstance().getCurrentForm().getFarmerGender());
                form.setFarmerLocation1(ApiData.getInstance().getCurrentForm().getFarmerLocation1());
                form.setFarmerLocation2(ApiData.getInstance().getCurrentForm().getFarmerLocation2());
                form.setFarmerLocation3(ApiData.getInstance().getCurrentForm().getFarmerLocation3());

                ApiData.getInstance().setCurrentForm(form);

                startActivity(intent);
                break;
            }
            case R.id.FormEnd_Btn_NewForm: {
                intent = new Intent(this, FormActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ApiData.getInstance().setCurrentForm(null);
                startActivity(intent);
                break;
            }
            case R.id.FormEnd_Btn_FinishSession: {
                ApiData.getInstance().getCurrentSession().setSessionEndDateTime(Utility.getUTCDateTime(new Date()));

                if (FormActivity.dcaFormDatabase != null) {
                    List<RoomDB_Session> allSessions = FormActivity.dcaFormDatabase.dcaSessionDao().getAllSessions();
                    RoomDB_Session foundSession = null;
                    if (allSessions != null && allSessions.size() > 0) {
                        for (RoomDB_Session sessionItem : allSessions) {
                            if (Utility.getDateFromDateTime(sessionItem.getSessionStartDateTime()).equals(Utility.getDateFromDateTime(ApiData.getInstance().getCurrentSession().getSessionStartDateTime())) && sessionItem.getClinic().toUpperCase().equals(ApiData.getInstance().getCurrentSession().getClinic().toUpperCase())) {
                                foundSession = sessionItem;
                                break;
                            }
                        }

                    }
                    if (foundSession != null) {
                        foundSession.setSessionEndDateTime(ApiData.getInstance().getCurrentSession().getSessionEndDateTime());
                        if (ApiData.getInstance().getCurrentSession().getDraft() > 0) {
                            foundSession.setDraft(foundSession.getDraft() + ApiData.getInstance().getCurrentSession().getDraft());
                        }
                        if (ApiData.getInstance().getCurrentSession().getSubmitted() > 0) {
                            foundSession.setSubmitted(foundSession.getSubmitted() + ApiData.getInstance().getCurrentSession().getSubmitted());
                        }
                        if (ApiData.getInstance().getCurrentSession().getMen() > 0) {
                            foundSession.setMen(foundSession.getMen() + ApiData.getInstance().getCurrentSession().getMen());
                        }
                        if (ApiData.getInstance().getCurrentSession().getWomen() > 0) {
                            foundSession.setWomen(foundSession.getWomen() + ApiData.getInstance().getCurrentSession().getWomen());
                        }
                        FormActivity.dcaFormDatabase.dcaSessionDao().updateSession(foundSession);
                    } else {
                        FormActivity.dcaFormDatabase.dcaSessionDao().addSession(ApiData.getInstance().getCurrentSession());
                    }
                }

                ApiData.getInstance().setCurrentForm(null);
                submitSession();
                intent = new Intent(this, ReportsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void submitSession() {

        String postSessionUrl = UrlFactory.getPostSessions;
        if (headers == null) {
            headers = new HashMap<>();
            headers.put("jwt", ApiData.getInstance().getJwtToken());
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("SessionId", ApiData.getInstance().getCurrentSession().getSessionId());
        params.put("ProjectCode", ApiData.getInstance().getFormDefinition().getProjectCode());
        params.put("UserId", null);
        params.put("StartDateTime", ApiData.getInstance().getCurrentSession().getSessionStartDateTime());
        params.put("EndDateTime", ApiData.getInstance().getCurrentSession().getSessionEndDateTime());
        params.put("ClinicCode", "Training");

        makePOSTRequest(postSessionUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {

            }

            @Override
            public void onError(String result) throws Exception {

            }
        }, headers, params, "POST SESSION");
    }

    private void makePOSTRequest(final String url, final VolleyCallback callback, final HashMap<String, String> headers, final HashMap<String, String> params, String tag) {
        CustomJSONObjectRequest req = new CustomJSONObjectRequest(Request.Method.POST, url, params,
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
                return params;
            }
        };

        VolleyController.getInstance(this).addToRequestQueue(req, tag);
    }
}