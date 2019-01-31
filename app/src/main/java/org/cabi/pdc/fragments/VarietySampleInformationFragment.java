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
import org.cabi.pdc.RoomDb.RoomDB_CropVariety;
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

public class VarietySampleInformationFragment extends Fragment {

    private Context mContext;
    ListView mAllSampleVarietyNames;
    ArrayList<String> mAllSampleVarietyNamesList = new ArrayList<>();
    ListView mRecentVarietyNames;
    ArrayList<String> mRecentVarietyNamesList = new ArrayList<>();
    DCAApplication dcaApplication;
    EditText etSampleInfoVarietyName;

    public VarietySampleInformationFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mContext = getActivity();
        dcaApplication = (DCAApplication) getActivity().getApplication();

        View rootView = inflater.inflate(R.layout.fragment_sample_info_variety, container, false);

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
                            TextView tvBaseTitleSampleInfoVarietyName = rootView.findViewById(R.id.baseTitleSampleInfoVarietyName);
                            tvBaseTitleSampleInfoVarietyName.setText(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getTranslationId()).getValue());

                            etSampleInfoVarietyName = rootView.findViewById(R.id.etSampleInfoVarietyName);
                            etSampleInfoVarietyName.setHint(ApiData.getInstance().getMetadataTranslationsList().get(section.getFields().get(i).getHintTranslationId()).getValue());
                            if (ApiData.getInstance().getCurrentForm() != null) {
                                if (!TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropVariety())) {
                                    etSampleInfoVarietyName.setText(ApiData.getInstance().getCurrentForm().getCropVariety());
                                }
                            }
                        }
                        break;
                        case 10: {
                            TextView tvNextButton = rootView.findViewById(R.id.btnTextNextSampleVariety);
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
                            rootView.findViewById(R.id.btnNextSampleVariety).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (sectionPos > -1) {
                                        if (ApiData.getInstance().getCurrentForm() != null) {
                                            String varietyName = etSampleInfoVarietyName.getText().toString();
                                            ApiData.getInstance().getCurrentForm().setCropVariety(varietyName);
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

                SharedPreferences sp = getActivity().getSharedPreferences("FormData", MODE_PRIVATE);
                final String strRecentVariety = sp.getString("Recent_CropVariety", "");
                if (!TextUtils.isEmpty(strRecentVariety)) {
                    try {
                        JSONArray jArrRecentVariety = new JSONArray(strRecentVariety);
                        for (int i = 0; i < jArrRecentVariety.length(); i++) {
                            if (!mRecentVarietyNamesList.contains(jArrRecentVariety.getString(i))) {
                                mRecentVarietyNamesList.add(jArrRecentVariety.getString(i));
                            }
                        }
                    } catch (JSONException je) {
                        je.printStackTrace();
                    }
                }

                mRecentVarietyNames = rootView.findViewById(R.id.lvRecentVarietyNames);
                if (mRecentVarietyNamesList.size() > 0) {
                    final ArrayAdapter<String> recentVarietyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mRecentVarietyNamesList);
                    mRecentVarietyNames.setAdapter(recentVarietyAdapter);
                    View headerViewRecentVariety = inflater.inflate(R.layout.generic_listview_header, container, false);
                    TextView headerTextRecent = headerViewRecentVariety.findViewById(R.id.headerText);
                    String recentVarietyHeader = "Recent varieties:";
                    if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("VarietyRecent")) {
                        recentVarietyHeader = ApiData.getInstance().getMetadataTranslationsList().get("VarietyRecent").getValue();
                    }
                    headerTextRecent.setText(recentVarietyHeader);
                    mRecentVarietyNames.addHeaderView(headerViewRecentVariety);
                    mRecentVarietyNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            OnListItemClick(adapterView, view, i, l);
                        }
                    });
                } else {
                    mRecentVarietyNames.setVisibility(View.GONE);
                }

                if (dcaApplication.getCropVarietyMetadata() != null && dcaApplication.getCropVarietyMetadata().size() > 0 && !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName())) {
                    for (Metadata item : dcaApplication.getCropVarietyMetadata()) {
                        if (item.getParentName().toUpperCase().contains(ApiData.getInstance().getCurrentForm().getCropName().toUpperCase())) {
                            if (!mAllSampleVarietyNamesList.contains(item.getName())) {
                                mAllSampleVarietyNamesList.add(item.getName());
                            }
                        }
                    }
                }
                List<RoomDB_CropVariety> cropVarietylist = ((FormActivity) getActivity()).dcaFormDatabase.dcaCropVarietyDao().getAllCropVarities();
                if (cropVarietylist != null && cropVarietylist.size() > 0 && !TextUtils.isEmpty(ApiData.getInstance().getCurrentForm().getCropName())) {
                    for (RoomDB_CropVariety cropVariety : cropVarietylist) {
                        if (cropVariety.getCropName().toUpperCase().contains(ApiData.getInstance().getCurrentForm().getCropName().toUpperCase())) {
                            if (!mAllSampleVarietyNamesList.contains(cropVariety.getCropVarietyName())) {
                                mAllSampleVarietyNamesList.add(cropVariety.getCropVarietyName());
                            }
                        }
                    }
                }
            }
        }

        mAllSampleVarietyNames = rootView.findViewById(R.id.lvAllSampleVarietyNames);
        if (mAllSampleVarietyNamesList.size() > 0) {
            final ArrayAdapter<String> varietyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.generic_listview_item, R.id.title, mAllSampleVarietyNamesList);
            mAllSampleVarietyNames.setAdapter(varietyAdapter);

            View headerAllSampleVariety = inflater.inflate(R.layout.generic_listview_header, container, false);
            TextView headerTextAllSampleVariety = headerAllSampleVariety.findViewById(R.id.headerText);
            String allVarietyHeader = "All varieties:";
            if (ApiData.getInstance().getMetadataTranslationsList() != null && ApiData.getInstance().getMetadataTranslationsList().containsKey("VarietyAll")) {
                allVarietyHeader = ApiData.getInstance().getMetadataTranslationsList().get("VarietyAll").getValue();
            }
            headerTextAllSampleVariety.setText(allVarietyHeader);
            mAllSampleVarietyNames.addHeaderView(headerAllSampleVariety);

            mAllSampleVarietyNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    OnListItemClick(adapterView, view, i, l);
                }
            });

            if (mAllSampleVarietyNamesList.size() == 0) {
                mAllSampleVarietyNames.setVisibility(View.INVISIBLE);
            } else {
                mAllSampleVarietyNames.setVisibility(View.VISIBLE);
            }

            etSampleInfoVarietyName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        mRecentVarietyNames.setVisibility(View.GONE);
                    } else {
                        mRecentVarietyNames.setVisibility(View.VISIBLE);
                    }
                    varietyAdapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } else {
            mAllSampleVarietyNames.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    private void OnListItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1)
            return;
        if (TextUtils.isEmpty(((TextView) view.findViewById(R.id.title)).getText())) {
            return;
        }
        etSampleInfoVarietyName.setText(((TextView) view.findViewById(R.id.title)).getText());
    }
}