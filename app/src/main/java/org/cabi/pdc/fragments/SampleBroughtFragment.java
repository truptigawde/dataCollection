package org.cabi.pdc.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.cabi.pdc.R;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.common.Utility;
import org.cabi.pdc.models.Section;

import java.io.FileDescriptor;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static org.cabi.pdc.common.Utility.getBitmapFromString;
import static org.cabi.pdc.common.Utility.getStringFromBitmap;

public class SampleBroughtFragment extends Fragment {

    Context mContext;

    private static final int MY_CAMERA_PERMISSION_CODE = 400;
    private static final int CAMERA_REQUEST = 1800;
    private static final int GALLERY_REQUEST = 1810;
    private ImageView imgSampleBrought;
    DCAApplication dcaApplication;
    RelativeLayout btnTakePhoto;

    public SampleBroughtFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContext = getActivity();
        dcaApplication = (DCAApplication) getActivity().getApplication();

        final View rootView = inflater.inflate(R.layout.fragment_sample_brought, container, false);

        btnTakePhoto = rootView.findViewById(R.id.btnTakePhoto);

        final Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");
            Section section = (Section) bundle.getSerializable("Section");

            if (section != null && section.getFields() != null && section.getFields().size() > 0) {

                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {

                        case 2: {
                            TextView baseTitle = rootView.findViewById(R.id.baseTitleSampleBrought);
                            baseTitle.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            final RadioGroup radioGroupSampleBrought = rootView.findViewById(R.id.rbgrpSampleBrought);
                            if (section.getFields().get(i).getOptions() != null && section.getFields().get(i).getOptions().size() > 0) {
                                for (int j = 0; j < section.getFields().get(i).getOptions().size(); j++) {

                                    RadioButton radioButtonSampleBrought = (RadioButton) inflater.inflate(R.layout.generic_radio_button, null);
                                    RadioGroup.LayoutParams layoutParamsRB = new RadioGroup.LayoutParams(Utility.getDpFromPixels(mContext, 340), ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (j != 0) {
                                        layoutParamsRB.topMargin = Utility.getDpFromPixels(mContext, 10);
                                    }
                                    radioButtonSampleBrought.setLayoutParams(layoutParamsRB);
                                    radioButtonSampleBrought.setId(j);
                                    radioButtonSampleBrought.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getOptions().get(j).getTranslationId()).getValue());
                                    radioButtonSampleBrought.setTag(section.getFields().get(i).getOptions().get(j).getValue());
                                    radioButtonSampleBrought.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (view.getTag().toString().toUpperCase().equals("YES")) {
                                                btnTakePhoto.setVisibility(View.VISIBLE);
                                            } else {
                                                btnTakePhoto.setVisibility(View.GONE);
                                                rootView.findViewById(R.id.imgSampleBrought).setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                    radioGroupSampleBrought.addView(radioButtonSampleBrought);
                                }


                                if (ApiData.getInstance().getCurrentForm() != null) {
                                    String sampleBrought = ApiData.getInstance().getCurrentForm().getCropSampleBrought();
                                    if (!TextUtils.isEmpty(sampleBrought)) {
                                        for (int k = 0; k < radioGroupSampleBrought.getChildCount(); k++) {
                                            RadioButton rbutton = (RadioButton) radioGroupSampleBrought.getChildAt(k);
                                            if (rbutton.getTag().toString().toUpperCase().equals(sampleBrought.toUpperCase())) {
                                                rbutton.setChecked(true);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                        case 4: {
                            if (!TextUtils.isEmpty(section.getFields().get(i).getLinkedToField().toString())) {
                                imgSampleBrought = rootView.findViewById(R.id.imgSampleBrought);
                                if (ApiData.getInstance().getCurrentForm() != null &&
                                        ApiData.getInstance().getCurrentForm().getCropSampleBrought() != null &&
                                        ApiData.getInstance().getCurrentForm().getCropSampleBrought().toUpperCase().equals("YES") &&
                                        ApiData.getInstance().getCurrentForm().getCropPhotoSample() != null) {
                                    Bitmap img = getBitmapFromString(ApiData.getInstance().getCurrentForm().getCropPhotoSample());
                                    imgSampleBrought.setImageBitmap(img);
                                    imgSampleBrought.setVisibility(View.VISIBLE);
                                }

                                TextView tvTakePhoto = rootView.findViewById(R.id.tvTakePhoto);
                                tvTakePhoto.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());
                                btnTakePhoto.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder photoBuilder = new AlertDialog.Builder(mContext);
                                        photoBuilder.setTitle("Choose Image Source");
                                        photoBuilder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                switch (which) {
                                                    case 0:
                                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                                        intent.setType("image/*");
                                                        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
                                                        startActivityForResult(chooser, GALLERY_REQUEST);
                                                        break;
                                                    case 1:
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                                                        } else {
                                                            Intent myCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                            startActivityForResult(myCameraIntent, CAMERA_REQUEST);
                                                        }
                                                        break;
                                                }
                                            }
                                        });
                                        photoBuilder.show();
                                    }
                                });
                            }
                        }
                        break;
                        case 10: {
                            LinearLayout baseLayout = rootView.findViewById(R.id.baseLayoutSampleBrought);
                            LinearLayout btnNextUpdate = (LinearLayout) inflater.inflate(R.layout.generic_section_next_button, null);
                            TextView tvNextButton = btnNextUpdate.findViewById(R.id.tvButtonNextUpdate);
                            String strNextBtn = "Update";
                            if (ApiData.getInstance().getMetadataTranslationsList() != null) {
                                if (fromSummary) {
                                    if (ApiData.getInstance().getMetadataTranslationsList().containsKey("SelectUpdate")) {
                                        strNextBtn = ApiData.getInstance().getMetadataTranslationsList().get("SelectUpdate").getValue();
                                    }
                                } else {
                                    strNextBtn = ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue();
                                }
                            }
                            tvNextButton.setText(strNextBtn);
                            RelativeLayout btnNextSection = btnNextUpdate.findViewById(R.id.relLayoutButtonNext);
                            btnNextSection.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sectionPos > -1) {
                                        RadioGroup sampleBrought = (RadioGroup) getView().findViewById(R.id.rbgrpSampleBrought);
                                        int selectedId = sampleBrought.getCheckedRadioButtonId();
                                        RadioButton selectedSampleBrought = (RadioButton) getView().findViewById(selectedId);

                                        if (ApiData.getInstance().getCurrentForm() != null) {
                                            if (selectedSampleBrought != null) {
                                                String broughtSample = selectedSampleBrought.getTag().toString();
                                                ApiData.getInstance().getCurrentForm().setCropSampleBrought(broughtSample);
                                            }
                                            if (fromSummary) {
                                                ((FormActivity) getActivity()).loadNextSection(dcaApplication.getSectionList().size() + 1, fragmentId, true);

                                            } else {
                                                ((FormActivity) getActivity()).loadNextSection(sectionPos + 1, fragmentId, false);
                                            }
                                        }
                                    }
                                }
                            });
                            baseLayout.addView(btnNextUpdate);
                        }
                        break;
                    }
                }
            }
        }
        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                Intent myCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(myCameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(mContext, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST: {
                    if (imgSampleBrought != null && data != null) {
                        Uri selectedImage = data.getData();
                        Bitmap imgFromGallery = null;
                        try {
                            imgFromGallery = getBitmapFromUri(selectedImage);
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                        imgSampleBrought.setImageBitmap(imgFromGallery);
                        if (ApiData.getInstance().getCurrentForm() != null) {
                            ApiData.getInstance().getCurrentForm().setCropPhotoSample(getStringFromBitmap(imgFromGallery));
                        }
                        imgSampleBrought.setVisibility(View.VISIBLE);
                    }
                }
                break;
                case CAMERA_REQUEST: {
                    if (imgSampleBrought != null) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        imgSampleBrought.setImageBitmap(photo);
                        if (ApiData.getInstance().getCurrentForm() != null) {
                            ApiData.getInstance().getCurrentForm().setCropPhotoSample(getStringFromBitmap(photo));
                        }
                        imgSampleBrought.setVisibility(View.VISIBLE);
                    }
                }
                break;
            }
        } else {
            Toast.makeText(mContext, "Failed to obtain image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}