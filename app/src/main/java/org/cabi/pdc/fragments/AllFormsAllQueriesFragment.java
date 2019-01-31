package org.cabi.pdc.fragments;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
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
import org.cabi.pdc.RoomDb.DCAFormDatabase;
import org.cabi.pdc.RoomDb.RoomDb_Form;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.Utility;

import java.util.List;

public class AllFormsAllQueriesFragment extends Fragment {
    EditText etSearchAllForms;
    LinearLayout baseLayoutAllForms;
    Context mContext;

    public AllFormsAllQueriesFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_all_forms_all_queries, container, false);
        etSearchAllForms = rootView.findViewById(R.id.etAllFormsAllQueries);
        String strSearchAllForms = "Search by clinic, farmer, crop or recommendation";
        if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("QueriesSearchBy")) {
            strSearchAllForms = ApiData.getInstance().getMetadataTranslationsList().get("QueriesSearchBy").getValue();
        }
        etSearchAllForms.setHint(strSearchAllForms);
        baseLayoutAllForms = rootView.findViewById(R.id.baseLayoutAllForms);

//        loadAllForms();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadAllForms();
    }

    private void loadAllForms() {

        int dp5 = Utility.getDpFromPixels(mContext, 5);
        int dp10 = Utility.getDpFromPixels(mContext, 10);
        int dp20 = Utility.getDpFromPixels(mContext, 20);

        if (FormActivity.dcaFormDatabase == null) {
            FormActivity.dcaFormDatabase = Room.databaseBuilder(getActivity(), DCAFormDatabase.class, "DCAFormsDB").allowMainThreadQueries().build();
        }
        List<RoomDb_Form> allForms = FormActivity.dcaFormDatabase.dcaFormDao().getAllForms();
        if (allForms != null & allForms.size() > 0) {
            {
                if (baseLayoutAllForms.getChildCount() > 0) {
                    baseLayoutAllForms.removeAllViews();
                }
                for (RoomDb_Form formItem : allForms) {

                    TextView tvSessionDate = new TextView(mContext);
//                    String[] dateSplit = formItem.getFormStartDateTime().split(" ");
                    String sessionEndTime = "- Session end time";
                    if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("QueriesSessionEndTime")) {
                        sessionEndTime = "- " + ApiData.getInstance().getMetadataTranslationsList().get("QueriesSessionEndTime").getValue() + " ";
                    }
//                    dateSplit[dateSplit.length - 1] = sessionEndTime + dateSplit[dateSplit.length - 1];
//                    String showDate = "";
//                    for (int i = 0; i < dateSplit.length; i++) {
//                        showDate = showDate + " " + dateSplit[i];
//                    }
                    tvSessionDate.setText(formItem.getFormStartDateTime().replace("-", sessionEndTime));
                    //tvSessionDate.setText(showDate);// + " - Session end time 14:55"
                    tvSessionDate.setTextColor(Color.BLACK);
                    tvSessionDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    TableRow.LayoutParams sessionDateLayoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    sessionDateLayoutParams.leftMargin = dp20;
                    sessionDateLayoutParams.rightMargin = dp20;
                    sessionDateLayoutParams.topMargin = dp10;
                    tvSessionDate.setLayoutParams(sessionDateLayoutParams);
                    baseLayoutAllForms.addView(tvSessionDate);

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
                    baseLayoutAllForms.addView(tvClinicName);

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

                    if (formItem.getFormStatus().toUpperCase().equals("DRAFT")) {
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
                        tvEdit.setCompoundDrawableTintList(ColorStateList.valueOf(Color.BLACK));
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
                        tvDeleteLayParam.setMargins(0, 0, dp10, 0);
                        tvDelete.setLayoutParams(tvDeleteLayParam);
                        tvDelete.setPadding(dp10, dp5, dp10, dp5);
                        tvDelete.setCompoundDrawablePadding(dp10);
                        tvDelete.setCompoundDrawableTintList(ColorStateList.valueOf(Color.BLACK));
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
                    } else if (formItem.getFormStatus().toUpperCase().equals("NOTSENT")) {
                        TextView tvNotSent = new TextView(mContext);
                        TableRow.LayoutParams tvNotSentLayParam = new TableRow.LayoutParams();
                        tvNotSentLayParam.setMargins(0, 0, dp10, 0);
                        tvNotSent.setLayoutParams(tvNotSentLayParam);
                        tvNotSent.setPadding(dp10, dp5, dp10, dp5);
                        tvNotSent.setCompoundDrawablePadding(dp10);
                        tvNotSent.setCompoundDrawableTintList(ColorStateList.valueOf(Color.BLACK));
                        tvNotSent.setText("Not Sent");
                        tvNotSent.setAllCaps(false);
                        tvNotSent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tvNotSent.setTextColor(Color.BLACK);
//                        tvNotSent.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notsent_black, 0, 0);
                        tvNotSent.setTag(formItem);
                        tvNotSent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onSubmittedClick(view);
                            }
                        });
                        trFormheader.addView(tvNotSent);
                    } else {
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
                                onSubmittedClick(view);
                            }
                        });
                        trFormheader.addView(tvSent);
                    }
                    tableForm.addView(trFormheader);
                    baseLayoutAllForms.addView(tableForm);
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
    }

    private void onSubmittedClick(View view) {
        RoomDb_Form form = (RoomDb_Form) view.getTag();
        if (form != null) {
            ApiData.getInstance().setCurrentForm(form);
            Intent intent = new Intent(getActivity(), FormActivity.class);
            intent.putExtra("ViewSubmitted", true);
            startActivity(intent);
        }
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

                            FormActivity.dcaFormDatabase.dcaFormDao().deleteForm(form);
                            Toast.makeText(mContext, "Delete clicked", Toast.LENGTH_SHORT).show();
                            baseLayoutAllForms.removeAllViews();
                            loadAllForms();
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