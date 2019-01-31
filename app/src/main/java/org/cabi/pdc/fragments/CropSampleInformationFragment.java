package org.cabi.pdc.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.cabi.pdc.R;
import org.cabi.pdc.RoomDb.RoomDB_Crop;
import org.cabi.pdc.activities.FormActivity;
import org.cabi.pdc.common.ApiData;
import org.cabi.pdc.common.DCAApplication;
import org.cabi.pdc.models.Metadata;
import org.cabi.pdc.models.Section;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CropSampleInformationFragment extends Fragment {

    private Context mContext;
    ListView mAllSampleCropNames;
    ArrayList<String> mAllSampleCropNamesList = new ArrayList<>();
    ListView mRecentSampleCropNames;
    ArrayList<String> mRecentSampleCropNamesList = new ArrayList<>();
    DCAApplication dcaApplication;
    EditText etSampleInfoCropName;

    public CropSampleInformationFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();
        dcaApplication = (DCAApplication) getActivity().getApplication();

        View rootView = inflater.inflate(R.layout.fragment_sample_info_crop, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            final int sectionPos = bundle.getInt("SectionPosition", -1);
            final int fragmentId = this.getId();
            final boolean fromSummary = bundle.getBoolean("FromSummary");
            Section section = (Section) bundle.getSerializable("Section");

            if (section.getFields() != null && section.getFields().size() > 0) {

                for (int i = 0; i < section.getFields().size(); i++) {
                    switch (section.getFields().get(i).getFieldType()) {
                        case 25: {
                            TextView tvBaseTitleSampleInfoCropName = rootView.findViewById(R.id.baseTitleSampleInfoCropName);
                            tvBaseTitleSampleInfoCropName.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            etSampleInfoCropName = rootView.findViewById(R.id.etSampleInfoCropName);
                            etSampleInfoCropName.setHint(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getHintTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null) {
                                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName())) {
                                    etSampleInfoCropName.setText(ApiData.getInstance().getCurrentForm().getCropName());
                                }
                            }
                        }
                        break;
                        case 10: {
                            TextView tvNextButton = rootView.findViewById(R.id.btnTextNextSampleCrop);
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

                            rootView.findViewById(R.id.btnNextSampleCrop).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sectionPos > -1) {
                                        if (ApiData.getInstance().getCurrentForm() != null) {

                                            String cropName = etSampleInfoCropName.getText().toString();
                                            ApiData.getInstance().getCurrentForm().setCropName(cropName);
                                            if (fromSummary) {
                                                ((FormActivity) getActivity()).loadNextSection(dcaApplication.getSectionList().size() + 1, fragmentId, true);

                                            } else {
                                                ((FormActivity) getActivity()).loadNextSection(sectionPos + 1, fragmentId, false);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        break;
                    }
                }
            }
        }

        SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
        final String strRecentCrops = sp.getString("Recent_Crop", "");
        if (!TextUtils.isEmpty(strRecentCrops)) {
            try {
                JSONArray jArrRecentCrop = new JSONArray(strRecentCrops);
                for (int i = 0; i < jArrRecentCrop.length(); i++) {
                    if (!mRecentSampleCropNamesList.contains(jArrRecentCrop.getString(i))) {
                        mRecentSampleCropNamesList.add(jArrRecentCrop.getString(i));
                    }
                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }

        mRecentSampleCropNames = rootView.findViewById(R.id.lvRecentCropNames);
        if (mRecentSampleCropNamesList.size() > 0) {
            final ArrayAdapter<String> recentCropAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mRecentSampleCropNamesList);
            mRecentSampleCropNames.setAdapter(recentCropAdapter);
            View headerViewRecentCrops = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextRecent = headerViewRecentCrops.findViewById(R.id.headerText);
            String recentCropHeader = "Recent crops";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("CropRecent")) {
                recentCropHeader = ApiData.getInstance().getMetadataTranslationsList().get("CropRecent").getValue();
            }
            headerTextRecent.setText(recentCropHeader);
            mRecentSampleCropNames.addHeaderView(headerViewRecentCrops);
            mRecentSampleCropNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });
        } else {
            mRecentSampleCropNames.setVisibility(View.GONE);
        }


        if (dcaApplication.getCropMetadata() != null && dcaApplication.getCropMetadata().size() > 0) {
            for (Metadata item : dcaApplication.getCropMetadata()) {
                if (!mAllSampleCropNamesList.contains(item.getName())) {
                    mAllSampleCropNamesList.add(item.getName());
                }
            }
        }

        List<RoomDB_Crop> croplist = ((FormActivity) getActivity()).dcaFormDatabase.dcaCropDao().getAllCrops();
        if (croplist != null && croplist.size() > 0) {
            for (RoomDB_Crop crop : croplist) {
                if (!mAllSampleCropNamesList.contains(crop.getCropName())) {
                    mAllSampleCropNamesList.add(crop.getCropName());
                }
            }
        }

        mAllSampleCropNames = rootView.findViewById(R.id.lvAllSampleCropNames);
        if (mAllSampleCropNamesList.size() > 0) {
            final ArrayAdapter<String> cropAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mAllSampleCropNamesList);
            mAllSampleCropNames.setAdapter(cropAdapter);

            View headerAllSampleCrops = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextAllSampleCrops = headerAllSampleCrops.findViewById(R.id.headerText);
            String allCropHeader = "All crops:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("CropAll")) {
                allCropHeader = ApiData.getInstance().getMetadataTranslationsList().get("CropAll").getValue();
            }
            headerTextAllSampleCrops.setText(allCropHeader);
            mAllSampleCropNames.addHeaderView(headerAllSampleCrops);

            mAllSampleCropNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });

            etSampleInfoCropName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        mRecentSampleCropNames.setVisibility(View.GONE);
                    } else {
                        mRecentSampleCropNames.setVisibility(View.VISIBLE);
                    }
                    cropAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            mAllSampleCropNames.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    private void OnListItemClick(AdapterView<?> parent, View view, int position, long id) {
//        EditText etSampleInfoCropName = getView().findViewById(R.id.etSampleInfoCropName);
        if (position < 1)
            return;
        if (TextUtils.isEmpty(((TextView) view.findViewById(R.id.title)).getText())) {
            return;
        }
        etSampleInfoCropName.setText(((TextView) view.findViewById(R.id.title)).getText());
    }
}