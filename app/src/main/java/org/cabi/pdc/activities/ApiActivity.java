package org.cabi.pdc.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.cabi.pdc.R;
import org.cabi.pdc.common.UrlFactory;
import org.cabi.pdc.helpers.CustomJSONObjectRequest;
import org.cabi.pdc.helpers.VolleyController;
import org.cabi.pdc.interfaces.VolleyCallback;
import org.cabi.pdc.models.AllTimeClinicStats;
import org.cabi.pdc.models.AllTimeSessionStats;
import org.cabi.pdc.models.FormDefinition;
import org.cabi.pdc.models.FormDefinitions;
import org.cabi.pdc.models.NationalReport;
import org.cabi.pdc.models.Project;
import org.cabi.pdc.models.Reports;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ApiActivity extends AppCompatActivity {

    VolleyCallback callback;
    View clickedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        final TextView txtResponse = findViewById(R.id.txtResponse);

        callback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                try {
                    Gson gson = new Gson();
                    if (clickedView.getId() == R.id.btnProjectApi) {
                        Project[] projects = gson.fromJson(result.getJSONArray("Projects").toString(), Project[].class);
                        for (Project proj : projects) {
                            String projectDetails = proj.getProjectId() + proj.getName() + proj.getCode();
                        }
                    } else if (clickedView.getId() == R.id.btnFormsDefsApi) {
                        FormDefinitions[] fdefs = gson.fromJson(result.getJSONArray("FormDefinitions").toString(), FormDefinitions[].class);
                        for (FormDefinitions fdef : fdefs) {
                            List<FormDefinition> formDef = fdef.getFormDefinitions();
                            String formDefinitonId = "sjkdv";
                        }

                    } else if (clickedView.getId() == R.id.btnReportsApi) {
                        Reports reports = new Reports();
                        reports.setNationalReport(gson.fromJson(result.getJSONObject("NationalReport").toString(), NationalReport.class));
                        reports.getNationalReport().setAllTimeClinicStats(gson.fromJson(result.getJSONObject("NationalReport").getJSONObject("AllTimeClinicStats").toString(), AllTimeClinicStats.class));
                        reports.getNationalReport().setAllTimeSessionStats(gson.fromJson(result.getJSONObject("NationalReport").getJSONObject("AllTimeSessionStats").toString(), AllTimeSessionStats.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                txtResponse.setText(result.toString());
                txtResponse.setTextColor(getResources().getColor(R.color.ButtonGreen)); //getColor(R.color.ButtonGreen));
            }

            @Override
            public void onError(String result) throws Exception {
                txtResponse.setText(result);
                txtResponse.setTextColor(Color.RED);
            }
        };
    }

    public void Click(View view) {
        String url = "";
        final HashMap<String, String> headers = new HashMap<>();

        clickedView = view;
        if (view.getId() == R.id.btnProjectApi) {
            url = UrlFactory.getProject;
            headers.put("jwt", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiJjNjk1YmExNi0xNmU0LTQ2MjYtOTJjZC05YTFjNWZkYWEyN2MiLCJQaW4iOjEyMzQsIkV4cGlyZXMiOiIyMDE4LTA1LTEwVDEwOjQwOjIzLjc1MTk1MDZaIiwiUmVhdXRoZW50aWNhdGlvblRpbWUiOiIyMDE4LTA1LTEwVDE2OjQwOjIzLjc1MTk1MDZaIn0=.CeH459RVlX91Aztia2M4Ny9t+dbpvQZ7Lahe8pg/1F0=");
        } else if (view.getId() == R.id.btnFormsDefsApi) {
            url = UrlFactory.getFormdefs;
            headers.put("jwt", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiJjNjk1YmExNi0xNmU0LTQ2MjYtOTJjZC05YTFjNWZkYWEyN2MiLCJQaW4iOjEyMzQsIkV4cGlyZXMiOiIyMDE4LTA1LTEwVDEwOjQwOjIzLjc1MTk1MDZaIiwiUmVhdXRoZW50aWNhdGlvblRpbWUiOiIyMDE4LTA1LTEwVDE2OjQwOjIzLjc1MTk1MDZaIn0=.CeH459RVlX91Aztia2M4Ny9t+dbpvQZ7Lahe8pg/1F0=");
        } else if (view.getId() == R.id.btnVerifyUserByEmailApi) {
            url = UrlFactory.verifyUserByEmail;
        } else if (view.getId() == R.id.btnReportsApi) {
            headers.put("jwt", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiJmMTU0NGJhYi1jMGIzLTRiYTQtOTE5Yi02NDM2Yzc4YjBmY2MiLCJQaW4iOjEyMzQsIkV4cGlyZXMiOiIyMDE4LTA1LTE1VDExOjQ0OjUyLjc5NTQ3MTRaIiwiUmVhdXRoZW50aWNhdGlvblRpbWUiOiIyMDE4LTA1LTE1VDE3OjQ0OjUyLjc5NTQ3MTRaIn0=.ojUFOb5/IpIyPkxGt46maFa94RqGfp+RSanob3Vynrk=");
            url = UrlFactory.getReport;
        } else if (view.getId() == R.id.btnUTCDateTime) {
            Date time = Calendar.getInstance().getTime();
            SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
            ((TextView) findViewById(R.id.txtUTCDateTime)).setText(outputFmt.format(time));
            return;
        }

        CustomJSONObjectRequest customJSONObjectRequest = new CustomJSONObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.onSuccess(response);
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    callback.onError(error.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return headers;
            }
        };

        VolleyController.getInstance(getApplicationContext()).addToRequestQueue(customJSONObjectRequest);
    }
}