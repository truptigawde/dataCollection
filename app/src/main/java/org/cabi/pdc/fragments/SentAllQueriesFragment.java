package org.cabi.pdc.fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.DCAFormDatabase;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.Utility;

import java.util.List;

public class SentAllQueriesFragment extends Fragment {
    EditText etSearchSentForms;
    LinearLayout baseLayoutSentForms;
    Context mContext;

    public SentAllQueriesFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContext = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_sent_all_queries, container, false);

        etSearchSentForms = rootView.findViewById(R.id.etSentSearchAllForms);
        String strSearchAllForms = "Search by clinic, farmer, crop or recommendation";
        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("QueriesSearchBy")) {
            strSearchAllForms = ApiData.getInstance().getMetadataTranslationsList().get("QueriesSearchBy").getValue();
        }
        etSearchSentForms.setHint(strSearchAllForms);
        baseLayoutSentForms = rootView.findViewById(R.id.baseLayoutSentForms);

        loadAllSent();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadAllSent();
    }

    private void loadAllSent() {
        int dp5 = Utility.getDpFromPixels(mContext, 5);
        int dp10 = Utility.getDpFromPixels(mContext, 10);
        int dp20 = Utility.getDpFromPixels(mContext, 20);

        if (FormActivity.dcaFormDatabase == null) {
            FormActivity.dcaFormDatabase = Room.databaseBuilder(getActivity(), DCAFormDatabase.class, "DCAFormsDB").allowMainThreadQueries().build();
        }
        List<RoomDb_Form> allForms = FormActivity.dcaFormDatabase.dcaFormDao().getAllForms();
        if (allForms != null & allForms.size() > 0) {
            if (baseLayoutSentForms.getChildCount() > 0) {
                baseLayoutSentForms.removeAllViews();
            }
            for (RoomDb_Form formItem : allForms) {
                if (formItem.getFormStatus().toUpperCase().equals("SENT")) {
                    TextView tvSessionDate = new TextView(mContext);
                    String[] dateSplit = formItem.getFormStartDateTime().split(" ");
                    String sessionEndTime = " - Session end time";
                    if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("QueriesSessionEndTime")) {
                        sessionEndTime = " - " + ApiData.getInstance().getMetadataTranslationsList().get("QueriesSessionEndTime").getValue() + " ";
                    }
                    dateSplit[dateSplit.length - 1] = sessionEndTime + dateSplit[dateSplit.length - 1];
                    String showDate = "";
                    for (int i = 0; i < dateSplit.length; i++) {
                        showDate = showDate + " " + dateSplit[i];
                    }
                    tvSessionDate.setText(showDate);
                    tvSessionDate.setTextColor(Color.BLACK);
                    tvSessionDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    TableRow.LayoutParams sessionDateLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    sessionDateLayoutParams.leftMargin = dp20;
                    sessionDateLayoutParams.rightMargin = dp20;
                    sessionDateLayoutParams.topMargin = dp10;
                    tvSessionDate.setLayoutParams(sessionDateLayoutParams);
                    baseLayoutSentForms.addView(tvSessionDate);

                    TextView tvClinicName = new TextView(mContext);
                    tvClinicName.setText("Clinic " + formItem.getClinicCode().toUpperCase());
                    tvClinicName.setTextColor(Color.BLACK);
                    tvClinicName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    tvClinicName.setTypeface(Typeface.DEFAULT_BOLD);
                    tvClinicName.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams sessionClinicLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    sessionClinicLayoutParams.leftMargin = dp20;
                    sessionClinicLayoutParams.rightMargin = dp20;
                    sessionClinicLayoutParams.topMargin = dp10;
                    tvClinicName.setLayoutParams(sessionClinicLayoutParams);
                    baseLayoutSentForms.addView(tvClinicName);

                    TableLayout tableForm = new TableLayout(mContext);
                    TableLayout.LayoutParams formLayParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    formLayParams.setMargins(dp20, dp10, dp20, 0);
                    tableForm.setLayoutParams(formLayParams);

                    TableRow trFormheader = new TableRow(mContext);
                    trFormheader.setBackgroundColor(Color.WHITE);
                    TableRow.LayoutParams trLayParam = new TableRow.LayoutParams();
                    trLayParam.bottomMargin = dp10;
                    trFormheader.setLayoutParams(trLayParam);

                    TextView tvClinicHeader = new TextView(mContext);
                    TableRow.LayoutParams tvCHLayParam = new TableRow.LayoutParams();
                    tvCHLayParam.width = 0;
                    tvCHLayParam.gravity = Gravity.LEFT;
                    tvCHLayParam.setMargins(0, 0, 5, 5);
                    tvCHLayParam.weight = 1;
                    tvClinicHeader.setLayoutParams(tvCHLayParam);
                    tvClinicHeader.setTypeface(Typeface.create("sans-serif-light", Typeface.BOLD));
                    tvClinicHeader.setPadding(dp10, dp5, dp10, dp5);
                    String strFarmerCrop;
                    if (!TextUtils.isEmpty(formItem.getFarmerName())) {
                        strFarmerCrop = formItem.getFarmerName();
                    } else {
                        strFarmerCrop = " ";
                    }
                    if (!TextUtils.isEmpty(formItem.getCropName())) {
                        strFarmerCrop = strFarmerCrop + "\n" + formItem.getCropName();
                    } else {
                        strFarmerCrop = strFarmerCrop + "\n" + " ";
                    }
                    tvClinicHeader.setText(strFarmerCrop);
                    tvClinicHeader.setAllCaps(false);
                    tvClinicHeader.setTextColor(Color.BLACK);
                    tvClinicHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    trFormheader.addView(tvClinicHeader);


                    TextView tvSent = new TextView(mContext);
                    TableRow.LayoutParams tvSentLayParam = new TableRow.LayoutParams();
                    tvSentLayParam.setMargins(0, 0, dp10, 0);
                    tvSent.setLayoutParams(tvSentLayParam);
                    tvSent.setPadding(dp10, dp5, dp10, dp5);
                    tvSent.setCompoundDrawablePadding(dp10);
                    tvSent.setCompoundDrawableTintList(ColorStateList.valueOf(Color.BLACK));
                    tvSent.setText("Sent");
                    tvSent.setAllCaps(false);
                    tvSent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    tvSent.setTextColor(Color.BLACK);
                    tvSent.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_check_black, 0, 0);
                    tvSent.setTag(formItem);
                    tvSent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onSentClick(view);
                        }
                    });
                    trFormheader.addView(tvSent);

                    tableForm.addView(trFormheader);
                    baseLayoutSentForms.addView(tableForm);
                }
            }
        }
    }

    private void onSentClick(View view) {
        RoomDb_Form form = (RoomDb_Form) view.getTag();
        if (form != null) {
            ApiData.getInstance().setCurrentForm(form);
            Intent intent = new Intent(getActivity(), FormActivity.class);
            intent.putExtra("ViewSubmitted", true);
            startActivity(intent);
        }
    }
}