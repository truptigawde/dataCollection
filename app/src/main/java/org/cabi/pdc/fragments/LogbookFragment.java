package org.cabi.pdc.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.DCAFormDatabase;
import org.cabi.pdc.RoomDb.RoomDB_Session;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.activities.MainActivity;
import org.cabi.pdc.common.Utility;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class LogbookFragment extends Fragment implements View.OnClickListener {

    ListView sessionList;
    ArrayList<String> listSessions = new ArrayList<>();
    LinearLayout llExpandedViewLogbook;
    EditText etLogbookPhoneNumber, etLogbookSMSMessage;
    Button btnLogbookFindContact, btnLogbookNoThanks, btnLogbookSendReport;
    TextView txtNoData;
    TableLayout tableLogbook;

    public LogbookFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_logbook, container, false);

        sessionList = rootView.findViewById(R.id.sessionListLogbook);
        llExpandedViewLogbook = rootView.findViewById(R.id.llExpandedViewLogbook);
        txtNoData = rootView.findViewById(R.id.txtNoDataLogbook);
        tableLogbook = rootView.findViewById(R.id.tableLogbook);
        etLogbookPhoneNumber = rootView.findViewById(R.id.etLogbookPhoneNumber);
        etLogbookSMSMessage = rootView.findViewById(R.id.etLogbookSMSMessage);
        btnLogbookFindContact = rootView.findViewById(R.id.btnLogbookFindContact);
        btnLogbookFindContact.setOnClickListener(this);
        btnLogbookNoThanks = rootView.findViewById(R.id.btnLogbookNoThanks);
        btnLogbookNoThanks.setOnClickListener(this);
        btnLogbookSendReport = rootView.findViewById(R.id.btnLogbookSendReport);
        btnLogbookSendReport.setOnClickListener(this);

        if (FormActivity.dcaFormDatabase == null) {
            FormActivity.dcaFormDatabase = Room.databaseBuilder(getActivity(), DCAFormDatabase.class, "DCAFormsDB").allowMainThreadQueries().build();
        }
        final List<RoomDB_Session> allSessions = FormActivity.dcaFormDatabase.dcaSessionDao().getAllSessions();

        if (allSessions != null && allSessions.size() > 0) {
            for (RoomDB_Session session : allSessions) {
                if (!listSessions.contains(Utility.getDateFromDateTime(session.getSessionStartDateTime()))) {
                    listSessions.add(Utility.getDateFromDateTime(session.getSessionStartDateTime()));
                }
            }

            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.report_month_list_item, listSessions);
            sessionList.setAdapter(adapter);
            View headerLogbook = inflater.inflate(R.layout.report_month_list_header, container, false);
            TextView htLogbook = headerLogbook.findViewById(R.id.headerText);
            htLogbook.setText("Date");
            sessionList.addHeaderView(headerLogbook);

            sessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if (position > 0) {

                        TextView sessionDate, clinicName, queries, draft, submitted, men, women;
                        int queryCount = allSessions.get(position - 1).getDraft() + allSessions.get(position - 1).getSubmitted();

                        sessionDate = rootView.findViewById(R.id.val_Logbook_SessionDate);
                        sessionDate.setText(Utility.getDateFromDateTime(allSessions.get(position - 1).getSessionStartDateTime()));

                        clinicName = rootView.findViewById(R.id.val_Logbook_Clinic);
                        clinicName.setText(allSessions.get(position - 1).getClinic());

                        queries = rootView.findViewById(R.id.val_Logbook_Queries);
                        queries.setText(String.valueOf(queryCount));

                        draft = rootView.findViewById(R.id.val_Logbook_Draft);
                        draft.setText(String.valueOf(allSessions.get(position - 1).getDraft()));

                        submitted = rootView.findViewById(R.id.val_Logbook_Submitted);
                        submitted.setText(String.valueOf(allSessions.get(position - 1).getSubmitted()));

                        men = rootView.findViewById(R.id.val_Logbook_Men);
                        men.setText(String.valueOf(allSessions.get(position - 1).getMen()));

                        women = rootView.findViewById(R.id.val_Logbook_Women);
                        women.setText(String.valueOf(allSessions.get(position - 1).getWomen()));

                        etLogbookSMSMessage.setText("Clinic logbook " + allSessions.get(position - 1).getSessionStartDateTime() + ":" + allSessions.get(position - 1).getClinic() + ", " + allSessions.get(position - 1).getWomen() + " Women and " + allSessions.get(position - 1).getMen() + " Men, " + queryCount + " Queries total.");

                        sessionList.setVisibility(View.INVISIBLE);
                        llExpandedViewLogbook.setVisibility(View.VISIBLE);
                    }
                }
            });
            txtNoData.setVisibility(View.INVISIBLE);
            sessionList.setVisibility(View.VISIBLE);
            llExpandedViewLogbook.setVisibility(View.INVISIBLE);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            sessionList.setVisibility(View.INVISIBLE);
            llExpandedViewLogbook.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    final int PICK_CONTACT = 9001;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogbookFindContact: {
//                showContacts();
                if (getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
                break;
            }
            case R.id.btnLogbookNoThanks: {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            }
            case R.id.btnLogbookSendReport: {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                if (TextUtils.isEmpty(etLogbookPhoneNumber.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dcaAlertDialog);
                    builder.setTitle("Error")
                            .setMessage("Please enter the phone number you want to send the message to")
                            .setCancelable(true)
                            .setPositiveButton("OK", null);
                    AlertDialog sendSMSAlert = builder.create();
                    sendSMSAlert.show();
                    sendSMSAlert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ButtonGray)));
                    sendSMSAlert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
                } else {
                    smsIntent.setData(Uri.parse("smsto: " + etLogbookPhoneNumber.getText().toString()));
                    if (!TextUtils.isEmpty(etLogbookSMSMessage.getText().toString())) {
                        smsIntent.putExtra("sms_body", etLogbookSMSMessage.getText().toString());
                        if (smsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivity(smsIntent);
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dcaAlertDialog);
                        builder.setTitle("Error")
                                .setMessage("SMS cannot be sent as the message is empty, please add a recommendation and try again.")
                                .setCancelable(true)
                                .setPositiveButton("OK", null);
                        AlertDialog sendSMSAlert = builder.create();
                        sendSMSAlert.show();
                        sendSMSAlert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ButtonGray)));
                        sendSMSAlert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
//                showContacts();
            } else {
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                String number = "";
                Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                if (hasPhone.equals("1")) {
                    Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");
                    }
                    phones.close();
                    //Do something with number
                    etLogbookPhoneNumber.setText(number);
                } else {
                    Toast.makeText(getActivity(), "This contact has no phone number", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }
    }

    public void showMonthsListView() {
        if (sessionList != null && llExpandedViewLogbook != null) {
            sessionList.setVisibility(View.VISIBLE);
            llExpandedViewLogbook.setVisibility(View.INVISIBLE);
        }
    }
}