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
import org.cabi.pdc.models.NationalReport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NationalReportsFragment extends Fragment {

    ListView monthsList;
    ArrayList<String> listMonths = new ArrayList<>();
    LinearLayout expandedMonthsLayoutNationalReports;
    ScrollView svExpandedViewNationalReports;

    Context mContext;

    public NationalReportsFragment() {

    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_national_reports, container, false);

        expandedMonthsLayoutNationalReports = rootView.findViewById(R.id.monthExpandedViewNationalReports);
        monthsList = rootView.findViewById(R.id.monthListNationalReports);
        svExpandedViewNationalReports = rootView.findViewById(R.id.svExpandedViewNationalReports);

        final NationalReport nationalReport = ApiData.getInstance().getNationalReport();

        if (nationalReport.getReports() != null && nationalReport.getReports().size() > 0) {
            for (int i = 0; i < nationalReport.getReports().size(); i++) {
                Gson gson = new Gson();
                String json = gson.toJson(nationalReport.getReports().get(i));
                try {
                    JSONObject jObj = new JSONObject(json);
                    listMonths.add(jObj.opt("MonthName").toString());
                } catch (JSONException je) {

                }
            }

            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.report_month_list_item, listMonths);
            monthsList.setAdapter(adapter);
            View headerNationalReport = inflater.inflate(R.layout.report_month_list_header, container, false);
            TextView htNationalReport = headerNationalReport.findViewById(R.id.headerText);
            htNationalReport.setText("Date");
            monthsList.addHeaderView(headerNationalReport);
        }

        monthsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    TextView monthTitle = expandedMonthsLayoutNationalReports.findViewById(R.id.tvNationalReportsDate);
                    monthTitle.setText("National data: " + listMonths.get(position - 1));

                    Gson gson = new Gson();

                    TextView tvThisMonthSessions = expandedMonthsLayoutNationalReports.findViewById(R.id.tvThisMonthSessionsNR);
                    TextView tvThisMonthQueries = expandedMonthsLayoutNationalReports.findViewById(R.id.tvThisMonthQueriesNR);
                    TextView tvThisMonthMen = expandedMonthsLayoutNationalReports.findViewById(R.id.tvThisMonthMenNR);
                    TextView tvThisMonthWomen = expandedMonthsLayoutNationalReports.findViewById(R.id.tvThisMonthWomenNR);

                    String jsonThisMonth = gson.toJson(nationalReport.getReports().get(position - 1));
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

                    TextView tvAllTimeSessions = expandedMonthsLayoutNationalReports.findViewById(R.id.tvAllTimeSessionsNR);
                    TextView tvAllTimeQueries = expandedMonthsLayoutNationalReports.findViewById(R.id.tvAllTimeQueriesNR);
                    TextView tvAllTimeMen = expandedMonthsLayoutNationalReports.findViewById(R.id.tvAllTimeMenNR);
                    TextView tvAllTimeWomen = expandedMonthsLayoutNationalReports.findViewById(R.id.tvAllTimeWomenNR);

                    String jsonAllSessionStats = gson.toJson(nationalReport.getAllTimeSessionStats());
                    try {
                        JSONObject jObj = new JSONObject(jsonAllSessionStats);
                        tvAllTimeSessions.setText(jObj.get("Sessions").toString());
                        tvAllTimeQueries.setText(jObj.get("Queries").toString());
                        tvAllTimeMen.setText(jObj.get("Men").toString());
                        tvAllTimeWomen.setText(jObj.get("Women").toString());
                    } catch (JSONException je) {

                    }

                    TextView tvThisMonthMin = expandedMonthsLayoutNationalReports.findViewById(R.id.tvThisMonthMinNR);
                    TextView tvThisMonthMax = expandedMonthsLayoutNationalReports.findViewById(R.id.tvThisMonthMaxNR);
                    TextView tvThisMonthAverage = expandedMonthsLayoutNationalReports.findViewById(R.id.tvThisMonthAverageNR);
                    try {
                        JSONObject jObj = new JSONObject(jsonThisMonth);
                        JSONArray reports = jObj.getJSONArray("Report");
                        JSONObject sessionStats = new JSONObject(reports.get(0).toString()).getJSONObject("ClinicStats");
                        tvThisMonthMin.setText(sessionStats.get("Min").toString());
                        tvThisMonthMax.setText(sessionStats.get("Max").toString());
                        tvThisMonthAverage.setText(sessionStats.get("Average").toString());
                    } catch (JSONException je) {

                    }

                    TextView tvAllTimeMin = expandedMonthsLayoutNationalReports.findViewById(R.id.tvAllTimeMinNR);
                    TextView tvAllTimeMax = expandedMonthsLayoutNationalReports.findViewById(R.id.tvAllTimeMaxNR);
                    TextView tvAllTimeAverage = expandedMonthsLayoutNationalReports.findViewById(R.id.tvAllTimeAverageNR);
                    String jsonAllClinicStats = gson.toJson(nationalReport.getAllTimeClinicStats());
                    try {
                        JSONObject jObj = new JSONObject(jsonAllClinicStats);
                        tvAllTimeMin.setText(jObj.get("Min").toString());
                        tvAllTimeMax.setText(jObj.get("Max").toString());
                        tvAllTimeAverage.setText(jObj.get("Average").toString());
                    } catch (JSONException je) {

                    }

                    LinearLayout secNationalTopCropsPests = expandedMonthsLayoutNationalReports.findViewById(R.id.secNationalTopCropsPests);
                    TableLayout tableNationalTopCropsPests = expandedMonthsLayoutNationalReports.findViewById(R.id.tableNationalTopCropsPests);

                    if (tableNationalTopCropsPests.getChildCount() > 0) {
                        tableNationalTopCropsPests.removeAllViews();
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

                                tableNationalTopCropsPests.addView(trCropName);

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
                                        tvProblemName.setMaxWidth(100);
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

                                        tableNationalTopCropsPests.addView(trProblemName);
                                    }
                                }
                            }
                            secNationalTopCropsPests.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException je) {
                        Toast.makeText(mContext, je.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    monthsList.setVisibility(View.INVISIBLE);
                    svExpandedViewNationalReports.setVisibility(View.VISIBLE);
                }
            }
        });
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (monthsList != null && svExpandedViewNationalReports != null) {
            monthsList.setVisibility(View.VISIBLE);
            svExpandedViewNationalReports.setVisibility(View.INVISIBLE);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void showMonthsListView() {
        if (monthsList != null && svExpandedViewNationalReports != null) {
            monthsList.setVisibility(View.VISIBLE);
            svExpandedViewNationalReports.setVisibility(View.INVISIBLE);
        }
    }
}