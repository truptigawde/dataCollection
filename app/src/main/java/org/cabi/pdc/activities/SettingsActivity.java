package org.cabi.pdc.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import org.cabi.pdc.R;
import org.cabi.pdc.common.ApiData;

public class SettingsActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TextView FormName, UserName;

        FormName = findViewById(R.id.txt_Settings_FormName);
        UserName = findViewById(R.id.txt_Settings_Username);
        if (ApiData.getInstance().getFormDefinition() != null) {

            if (!TextUtils.isEmpty(ApiData.getInstance().getFormDefinition().getFormName())) {
                FormName.setText(ApiData.getInstance().getFormDefinition().getFormName());
            }
            if (!TextUtils.isEmpty(ApiData.getInstance().getFormDefinition().getUsername())) {
                UserName.setText("\"" + ApiData.getInstance().getFormDefinition().getUsername() + "\"");
            }
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void Click(View view) {
        if (view.getId() == R.id.btnBackArrow) {
            super.onBackPressed();
        } else if (view.getId() == R.id.btnLogOut) {

            String logOutAppWarning = ApiData.getInstance().getMetadataTranslationsList().get("ErrorLogOutFromApp").getValue();
            if (TextUtils.isEmpty(logOutAppWarning)) {
                logOutAppWarning = "Warning: You are signing out of the app - You will need your Google sign in details to continue using the app.";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dcaAlertDialog);
            builder.setTitle("Confirm")
                    .setMessage(logOutAppWarning)
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String logoutSuccessful = ApiData.getInstance().getMetadataTranslationsList().get("ErrorLogoutSuccessful").getValue();
                            if (TextUtils.isEmpty(logoutSuccessful)) {
                                logoutSuccessful = "You have successfully signed out of the app - You will need your Google sign in details to continue using the app.";
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.dcaAlertDialog);
                            builder.setTitle("Confirm")
                                    .setMessage(logoutSuccessful)
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            signOut();
                                        }
                                    });
                            AlertDialog logoutWarningSuccess = builder.create();
                            logoutWarningSuccess.show();
                            logoutWarningSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ButtonGray)));
                            logoutWarningSuccess.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
                        }
                    })
                    .setNegativeButton("Cancel", null);

            AlertDialog logoutWarningAlert = builder.create();
            logoutWarningAlert.show();
            logoutWarningAlert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.ButtonGray)));
            logoutWarningAlert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.CYAN);
            logoutWarningAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.CYAN);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginInfos", MODE_PRIVATE);
                        sharedPreferences.edit().remove("IsSignedIn").commit();
                        sharedPreferences.edit().remove("Passcode").commit();
                        sharedPreferences.edit().remove("jwt").commit();
                        sharedPreferences.edit().remove("userEmail").commit();
                        SettingsActivity.this.onBackPressed();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SettingsActivity.this, "Sign Out failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void onBackPressed() {
        super.onBackPressed();
    }
}