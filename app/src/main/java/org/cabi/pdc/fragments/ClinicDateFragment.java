package org.cabi.pdc.fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.common.DCAApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ClinicDateFragment extends Fragment {

    TextView tvClinicDate;
    Calendar myCalendar = Calendar.getInstance();
    Context mContext;
    DCAApplication dcaApplication;


    public ClinicDateFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();
        dcaApplication = (DCAApplication) getActivity().getApplication();

        View rootView = inflater.inflate(R.layout.fragment_clinic_date, container, false);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        tvClinicDate = rootView.findViewById(R.id.textClinicDate);
        rootView.findViewById(R.id.btnNextClinicDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return rootView;
    }

    public void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tvClinicDate.setText(sdf.format(myCalendar.getTime()));
    }
}