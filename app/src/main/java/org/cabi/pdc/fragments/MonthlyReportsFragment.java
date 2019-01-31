package org.cabi.pdc.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.cabi.pdc.R;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.models.MyMonthlyReport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MonthlyReportsFragment extends Fragment {

    ListView monthsList;
    ArrayList<String> listMonths = new ArrayList<>();
    LinearLayout expandedMonthsLayout;
    ScrollView svExpandedViewMonthlyReports;

    Context mContext;

    public MonthlyReportsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_monthly_reports, container, false);

        expandedMonthsLayout = rootView.findViewById(R.id.monthExpandedViewMonthlyReports);
        monthsList = rootView.findViewById(R.id.monthListMonthlyReports);
        svExpandedViewMonthlyReports = rootView.findViewById(R.id.svExpandedViewMonthlyReports);

        if (ApiData.getInstance().getMyMonthlyReport() == null) {
            return null;
        }

        final MyMonthlyReport myMonthlyReport = ApiData.getInstance().getMyMonthlyReport();

        if (myMonthlyReport.getReports() != null && myMonthlyReport.getReports().size() > 0) {
            for (int i = 0; i < myMonthlyReport.getReports().size(); i++) {
                Gson gson = new Gson();
                String json = gson.toJson(myMonthlyReport.getReports().get(i));
                try {
                    JSONObject jObj = new JSONObject(json);
                    listMonths.add(jObj.opt("MonthName").toString());
                } catch (JSONException je) {

                }
            }

            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.report_month_list_item, listMonths);
            monthsList.setAdapter(adapter);
            View headerMonthlyReports = inflater.inflate(R.layout.report_month_list_header, container, false);
            TextView htMonthlyReports = headerMonthlyReports.findViewById(R.id.headerText);
            htMonthlyReports.setText("Date");
            monthsList.addHeaderView(headerMonthlyReports);
        }

        monthsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    TextView monthTitle = expandedMonthsLayout.findViewById(R.id.tvMonthlyReportsDate);
                    monthTitle.setText("My data: " + listMonths.get(position - 1));

                    Gson gson = new Gson();

                    TextView tvThisMonthSessions = expandedMonthsLayout.findViewById(R.id.tvThisMonthSessions);
                    TextView tvThisMonthQueries = expandedMonthsLayout.findViewById(R.id.tvThisMonthQueries);
                    TextView tvThisMonthMen = expandedMonthsLayout.findViewById(R.id.tvThisMonthMen);
                    TextView tvThisMonthWomen = expandedMonthsLayout.findViewById(R.id.tvThisMonthWomen);

                    String jsonThisMonth = gson.toJson(myMonthlyReport.getReports().get(position - 1));
                    try {
                        JSONObject jObj = new JSONObject(jsonThisMonth);
                        JSONArray reports = jObj.getJSONArray("Report");
                        JSONObject sessionStats = new JSONObject(reports.get(0).toString()).getJSONObject("SessionStats");
                        tvThisMonthSessions.setText(sessionStats.get("Sessions").toString());
                        tvThisMonthQueries.setText(sessionStats.get("Queries").toString());
                        tvThisMonthMen.setText(sessionStats.get("Men").toString());
                        tvThisMonthWomen.setText(sessionStats.get("Women").toString());
                    } catch (JSONException je) {

                    }

                    TextView tvAllTimeSessions = expandedMonthsLayout.findViewById(R.id.tvAllTimeSessions);
                    TextView tvAllTimeQueries = expandedMonthsLayout.findViewById(R.id.tvAllTimeQueries);
                    TextView tvAllTimeMen = expandedMonthsLayout.findViewById(R.id.tvAllTimeMen);
                    TextView tvAllTimeWomen = expandedMonthsLayout.findViewById(R.id.tvAllTimeWomen);

                    String jsonAllSessionStats = gson.toJson(myMonthlyReport.getAllTimeSessionStats());
                    try {
                        JSONObject jObj = new JSONObject(jsonAllSessionStats);
                        tvAllTimeSessions.setText(jObj.get("Sessions").toString());
                        tvAllTimeQueries.setText(jObj.get("Queries").toString());
                        tvAllTimeMen.setText(jObj.get("Men").toString());
                        tvAllTimeWomen.setText(jObj.get("Women").toString());
                    } catch (JSONException je) {

                    }

                    TextView tvThisMonthMin = expandedMonthsLayout.findViewById(R.id.tvThisMonthMin);
                    TextView tvThisMonthMax = expandedMonthsLayout.findViewById(R.id.tvThisMonthMax);
                    TextView tvThisMonthAverage = expandedMonthsLayout.findViewById(R.id.tvThisMonthAverage);
                    try {
                        JSONObject jObj = new JSONObject(jsonThisMonth);
                        JSONArray reports = jObj.getJSONArray("Report");
                        JSONObject sessionStats = new JSONObject(reports.get(0).toString()).getJSONObject("ClinicStats");
                        tvThisMonthMin.setText(sessionStats.get("Min").toString());
                        tvThisMonthMax.setText(sessionStats.get("Max").toString());
                        tvThisMonthAverage.setText(sessionStats.get("Average").toString());
                    } catch (JSONException je) {

                    }

                    TextView tvAllTimeMin = expandedMonthsLayout.findViewById(R.id.tvAllTimeMin);
                    TextView tvAllTimeMax = expandedMonthsLayout.findViewById(R.id.tvAllTimeMax);
                    TextView tvAllTimeAverage = expandedMonthsLayout.findViewById(R.id.tvAllTimeAverage);
                    String jsonAllClinicStats = gson.toJson(myMonthlyReport.getAllTimeClinicStats());
                    try {
                        JSONObject jObj = new JSONObject(jsonAllClinicStats);
                        tvAllTimeMin.setText(jObj.get("Min").toString());
                        tvAllTimeMax.setText(jObj.get("Max").toString());
                        tvAllTimeAverage.setText(jObj.get("Average").toString());
                    } catch (JSONException je) {

                    }

                    LinearLayout secMonthlyTopCropsPests = expandedMonthsLayout.findViewById(R.id.secMonthlyTopCropsPests);
                    TableLayout tableMyTopCropsPests = expandedMonthsLayout.findViewById(R.id.tableMyTopCropsPests);

                    if (tableMyTopCropsPests.getChildCount() > 0) {
                        tableMyTopCropsPests.removeAllViews();
                    }

                    try {
                        JSONObject jObj = new JSONObject(jsonThisMonth);
                        JSONArray reports = jObj.getJSONArray("Report");
                        JSONArray crops = new JSONArray(new JSONObject(reports.get(0).toString()).get("Crops").toString());

                        if (mContext == null) {
                            mContext = getContext();
                        }

                        int dp5 = Utility.getDpFromPixels(mContext, 5);
                        int dp10 = Utility.getDpFromPixels(mContext, 10);
                        int dp20 = Utility.getDpFromPixels(mContext, 20);

                        if (crops != null && crops.length() > 0) {
                            for (int ctrCrops = 0; ctrCrops < crops.length(); ctrCrops++) {
                                JSONObject cropsItem = new JSONObject(crops.get(ctrCrops).toString());
                                TableRow trCropName = new TableRow(mContext);
                                TableRow.LayoutParams trCropLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                trCropName.setLayoutParams(trCropLayoutParams);

                                String cropName = cropsItem.getString("Name");
                                TextView tvCropName = new TextView(mContext);
                                TableRow.LayoutParams tvCropNameLayoutParams = new TableRow.LayoutParams();
                                tvCropNameLayoutParams.rightMargin = dp5;
                                tvCropNameLayoutParams.bottomMargin = dp5;
                                tvCropName.setLayoutParams(tvCropNameLayoutParams);
                                tvCropName.setPadding(dp10, dp10, 0, dp10);
                                tvCropName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                tvCropName.setTextColor(Color.BLACK);
                                tvCropName.setTextSize(16);
                                tvCropName.setTypeface(Typeface.DEFAULT_BOLD);
                                tvCropName.setBackgroundColor(Color.WHITE);
                                tvCropName.setText(cropName);
                                trCropName.addView(tvCropName);

                                String cropCount = cropsItem.getString("Count");
                                TextView tvCropCount = new TextView(mContext);
                                TableRow.LayoutParams tvCropCountLayoutParams = new TableRow.LayoutParams();
                                tvCropCountLayoutParams.bottomMargin = dp5;
                                tvCropCount.setLayoutParams(tvCropCountLayoutParams);
                                tvCropCount.setPadding(0, dp10, dp10, dp10);
                                tvCropCount.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                                tvCropCount.setTextColor(Color.BLACK);
                                tvCropCount.setTextSize(16);
                                tvCropCount.setTypeface(Typeface.DEFAULT_BOLD);
                                tvCropCount.setBackgroundColor(Color.WHITE);
                                tvCropCount.setText(cropCount);
                                trCropName.addView(tvCropCount);

                                tableMyTopCropsPests.addView(trCropName);

                                JSONArray problems = new JSONArray(cropsItem.getJSONArray("Problems").toString());
                                if (problems != null && problems.length() > 0) {
                                    for (int ctrProblems = 0; ctrProblems < problems.length(); ctrProblems++) {
                                        JSONObject problemItem = new JSONObject(problems.get(ctrProblems).toString());
                                        TableRow trProblemName = new TableRow(mContext);
                                        TableRow.LayoutParams trProblemLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                                        trProblemName.setLayoutParams(trProblemLayoutParams);

                                        String problemName = problemItem.getString("Name");
                                        TextView tvProblemName = new TextView(mContext);
                                        TableRow.LayoutParams tvProblemNameLayoutParams = new TableRow.LayoutParams();
                                        tvProblemNameLayoutParams.rightMargin = dp5;
                                        tvProblemNameLayoutParams.bottomMargin = dp5;
                                        tvProblemNameLayoutParams.leftMargin = dp20;
                                        tvProblemName.setLayoutParams(tvProblemNameLayoutParams);
                                        tvProblemName.setPadding(dp10, dp10, 0, dp10);
                                        tvProblemName.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                        tvProblemName.setTextColor(Color.BLACK);
                                        tvProblemName.setTextSize(16);
                                        tvProblemName.setBackgroundColor(Color.WHITE);
                                        tvProblemName.setText(problemName);
                                        tvProblemName.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                                        trProblemName.addView(tvProblemName);

                                        String problemCount = problemItem.getString("Count");
                                        TextView tvProblemCount = new TextView(mContext);
                                        TableRow.LayoutParams tvProblemCountLayoutParams = new TableRow.LayoutParams();
                                        tvProblemCountLayoutParams.bottomMargin = dp5;
                                        tvProblemCount.setLayoutParams(tvProblemCountLayoutParams);
                                        tvProblemCount.setPadding(0, dp10, dp10, dp10);
                                        tvProblemCount.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                                        tvProblemCount.setTextColor(Color.BLACK);
                                        tvProblemCount.setTextSize(16);
                                        tvProblemCount.setBackgroundColor(Color.WHITE);
                                        tvProblemCount.setText(problemCount);
                                        tvProblemCount.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                                        trProblemName.addView(tvProblemCount);

                                        tableMyTopCropsPests.addView(trProblemName);
                                    }
                                }
                            }
                            secMonthlyTopCropsPests.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException je) {
                        Toast.makeText(mContext, je.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    monthsList.setVisibility(View.INVISIBLE);
                    svExpandedViewMonthlyReports.setVisibility(View.VISIBLE);
                }
            }
        });
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (monthsList != null && svExpandedViewMonthlyReports != null) {
            monthsList.setVisibility(View.VISIBLE);
            svExpandedViewMonthlyReports.setVisibility(View.INVISIBLE);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void showMonthsListView() {
        if (monthsList != null && svExpandedViewMonthlyReports != null) {
            monthsList.setVisibility(View.VISIBLE);
            svExpandedViewMonthlyReports.setVisibility(View.INVISIBLE);
        }
    }
}