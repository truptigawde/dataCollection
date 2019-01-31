package org.cabi.pdc.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.RoomDB_Session;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.Utility;

import java.util.List;

public class DraftsAllQueriesFragment extends Fragment {
    EditText etDraftsSearchAllForms;
    LinearLayout baseLayoutDraftsAll;
    Context mContext;

    public DraftsAllQueriesFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_drafts_all_queries, container, false);

        etDraftsSearchAllForms = rootView.findViewById(R.id.etDraftsSearchAllForms);
        String strSearchDrafts = "Search by clinic, farmer, crop or recommendation";
        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("QueriesSearchBy")) {
            strSearchDrafts = ApiData.getInstance().getMetadataTranslationsList().get("QueriesSearchBy").getValue();
        }
        etDraftsSearchAllForms.setHint(strSearchDrafts);

        baseLayoutDraftsAll = rootView.findViewById(R.id.baseLayoutDraftsAll);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadDrafts();
    }

    private void loadDrafts() {

        int dp5 = Utility.getDpFromPixels(mContext, 4);
        int dp10 = Utility.getDpFromPixels(mContext, 8);
        int dp20 = Utility.getDpFromPixels(mContext, 16);

        if (FormActivity.dcaFormDatabase != null) {
            List<RoomDb_Form> allForms = FormActivity.dcaFormDatabase.dcaFormDao().getAllForms();
            if (allForms != null & allForms.size() > 0) {

                if (baseLayoutDraftsAll.getChildCount() > 0) {
                    baseLayoutDraftsAll.removeAllViews();
                }
                for (RoomDb_Form formItem : allForms) {

                    if (formItem.getFormStatus().toUpperCase().equals("DRAFT")) {
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
                        tvSessionDate.setText(showDate);// + " - Session end time 14:55"
                        tvSessionDate.setTextColor(Color.BLACK);
                        tvSessionDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                        TableRow.LayoutParams sessionDateLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        sessionDateLayoutParams.leftMargin = dp20;
                        sessionDateLayoutParams.rightMargin = dp20;
                        sessionDateLayoutParams.topMargin = dp10;
                        tvSessionDate.setLayoutParams(sessionDateLayoutParams);
                        baseLayoutDraftsAll.addView(tvSessionDate);

                        TextView tvClinicName = new TextView(mContext);
                        tvClinicName.setText("Clinic " + formItem.getClinicCode().toUpperCase()); //ApiData.getInstance().getCurrentSession().getClinic()
                        tvClinicName.setTextColor(Color.BLACK);
                        tvClinicName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                        tvClinicName.setTypeface(Typeface.DEFAULT_BOLD);
                        tvClinicName.setGravity(Gravity.CENTER);
                        TableRow.LayoutParams sessionClinicLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        sessionClinicLayoutParams.leftMargin = dp20;
                        sessionClinicLayoutParams.rightMargin = dp20;
                        sessionClinicLayoutParams.topMargin = dp10;
                        tvClinicName.setLayoutParams(sessionClinicLayoutParams);
                        baseLayoutDraftsAll.addView(tvClinicName);

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
                        tvCHLayParam.setMargins(0, 0, 8, 8);
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

                        TextView tvEdit = new TextView(mContext);
                        TableRow.LayoutParams tvEditLayParam = new TableRow.LayoutParams();
                        tvEditLayParam.setMargins(0, 0, dp10, 0);
                        tvEdit.setLayoutParams(tvEditLayParam);
                        tvEdit.setPadding(dp10, dp5, dp10, dp5);
                        tvEdit.setCompoundDrawablePadding(dp10);
                        tvEdit.setText("Edit");
                        tvEdit.setAllCaps(false);
                        tvEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tvEdit.setTextColor(ContextCompat.getColor(mContext, R.color.ButtonGreen));
                        tvEdit.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_edit_black, 0, 0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            tvEdit.setCompoundDrawableTintList(ColorStateList.valueOf(Color.BLACK));
                        }
                        tvEdit.setTag(formItem);
                        tvEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onEditClick(view);
                            }
                        });
                        trFormheader.addView(tvEdit);

                        TextView tvDelete = new TextView(mContext);
                        TableRow.LayoutParams tvDeleteLayParam = new TableRow.LayoutParams();
                        tvDeleteLayParam.setMargins(0, 0, 10, 0);
                        tvDelete.setLayoutParams(tvDeleteLayParam);
                        tvDelete.setPadding(dp10, dp5, dp10, dp5);
                        tvDelete.setCompoundDrawablePadding(dp10);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            tvDelete.setCompoundDrawableTintList(ColorStateList.valueOf(Color.BLACK));
                        }
                        tvDelete.setText("Delete");
                        tvDelete.setAllCaps(false);
                        tvDelete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tvDelete.setTextColor(Color.RED);
                        tvDelete.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_delete_black, 0, 0);
                        tvDelete.setTag(formItem);
                        tvDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onDeleteClick(view);
                            }
                        });
                        trFormheader.addView(tvDelete);

                        tableForm.addView(trFormheader);
                        baseLayoutDraftsAll.addView(tableForm);
                    }
                }
            }
        }
    }

    private void onEditClick(View view) {
        RoomDb_Form form = (RoomDb_Form) view.getTag();
        if (form != null) {
            ApiData.getInstance().setCurrentForm(form);
            Intent intent = new Intent(getActivity(), FormActivity.class);
            intent.putExtra("EditDraft", true);
            startActivity(intent);
        }

        Toast.makeText(mContext, "Edit clicked", Toast.LENGTH_SHORT).show();
    }

    private void onDeleteClick(View view) {

        final RoomDb_Form form = (RoomDb_Form) view.getTag();
        if (form != null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dcaAlertDialog);
            builder.setTitle("Delete draft form")
                    .setMessage("This is a permanent action. Once you have deleted a draft it cannot be retrieved.")
                    .setCancelable(true)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int index) {

//                            Intent intent = new Intent(getActivity(), FormFinishActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);

                            List<RoomDB_Session> sessions = FormActivity.dcaFormDatabase.dcaSessionDao().getAllSessions();
                            if (sessions != null && sessions.size() > 0) {
                                for (RoomDB_Session sessionItem : sessions) {
                                    if (sessionItem.getSessionId().equals(form.getSessionId())) {
                                        int drafts = sessionItem.getDraft();
                                        if (drafts > 1) {
                                            sessionItem.setDraft(drafts - 1);
                                            FormActivity.dcaFormDatabase.dcaSessionDao().updateSession(sessionItem);
                                        } else if (drafts == 1) {
                                            FormActivity.dcaFormDatabase.dcaSessionDao().deleteSession(sessionItem);
                                        }
                                        break;
                                    }
                                }
                            }

                            FormActivity.dcaFormDatabase.dcaFormDao().deleteForm(form);
                            Toast.makeText(mContext, "Delete clicked", Toast.LENGTH_SHORT).show();
                            baseLayoutDraftsAll.removeAllViews();
                            loadDrafts();
                        }
                    })
                    .setNegativeButton("Cancel", null);
            AlertDialog deleteDraftAlert = builder.create();
            deleteDraftAlert.setInverseBackgroundForced(true);
            deleteDraftAlert.show();
            deleteDraftAlert.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.color.ButtonGray));
            deleteDraftAlert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
            deleteDraftAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        }
    }
}