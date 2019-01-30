package org.cabi.pdc.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.cabi.pdc.R;
import org.cabi.pdc.adapters.ViewPagerAdapter;
import org.cabi.pdc.fragments.AllFormsAllQueriesFragment;
import org.cabi.pdc.fragments.DraftsAllQueriesFragment;
import org.cabi.pdc.fragments.SentAllQueriesFragment;

public class AllQueriesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_queries);

        viewPager = findViewById(R.id.viewPagerAllQueries);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabAllQueries);
        tabLayout.setupWithViewPager(viewPager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("LoadSection")) {
            switch (bundle.get("LoadSection").toString().toUpperCase()) {
                case "DRAFTS":
                    viewPager.setCurrentItem(1);
                    break;
                case "SENT":
                    viewPager.setCurrentItem(2);
                    break;
                case "ALL":
                default:
                    viewPager.setCurrentItem(0);
                    break;
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllFormsAllQueriesFragment(), "All");
        adapter.addFragment(new DraftsAllQueriesFragment(), "Draft");
        adapter.addFragment(new SentAllQueriesFragment(), "Sent");
        viewPager.setAdapter(adapter);
    }

    public void Click(View view) {
        if (view.getId() == R.id.btnHomeAllQueries) {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}