package org.cabi.pdc.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.cabi.pdc.R;
import org.cabi.pdc.adapters.ViewPagerAdapter;
import org.cabi.pdc.fragments.LogbookFragment;
import org.cabi.pdc.fragments.MonthlyReportsFragment;
import org.cabi.pdc.fragments.NationalReportsFragment;

public class ReportsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private LogbookFragment logbookFragment;
    private MonthlyReportsFragment monthlyReportsFragment;
    private NationalReportsFragment nationalReportsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logbookFragment = new LogbookFragment();
        monthlyReportsFragment = new MonthlyReportsFragment();
        nationalReportsFragment = new NationalReportsFragment();

        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getText() != null) {
                    switch (tab.getText().toString().toUpperCase()) {
                        case "LOGBOOK":
                            logbookFragment.showMonthsListView();
                            break;
                        case "MONTHLY REPORTS":
                            monthlyReportsFragment.showMonthsListView();
                            break;
                        case "NATIONAL REPORTS":
                            nationalReportsFragment.showMonthsListView();
                            break;
                    }
                }
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        if (logbookFragment == null || monthlyReportsFragment == null || nationalReportsFragment == null) {
            logbookFragment = new LogbookFragment();
            monthlyReportsFragment = new MonthlyReportsFragment();
            nationalReportsFragment = new NationalReportsFragment();
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(logbookFragment, "Logbook");
        adapter.addFragment(monthlyReportsFragment, "Monthly reports");
        adapter.addFragment(nationalReportsFragment, "National reports");
        viewPager.setAdapter(adapter);
    }

    public void Click(View view) {
        if (view.getId() == R.id.btnHome) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
//            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}