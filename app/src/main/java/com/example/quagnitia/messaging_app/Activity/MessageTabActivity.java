package com.example.quagnitia.messaging_app.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quagnitia.messaging_app.R;
import com.example.quagnitia.messaging_app.Storage.Preferences;


public class MessageTabActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    ImageView imgDrawer1;
    Context context = this;
    TextView txtPickupHeader;
    ImageView imgBack;
    public static boolean isLoad = false;
    BroadcastReceiver br;
    int load = 0;
    TextView txttitle, txtLogOut, txtname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        initUi();
        initListener();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoad) {
            isLoad = false;
            loadData();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void loadData() {
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        TabLayout tabLayout;
        tabLayout = findViewById(R.id.tabs);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //Custom code for adding and changing pages
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
//        if (tabLayout.getTabCount() < 3) {
//            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        } else {
//            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//
//        }
        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
/*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        if (getIntent().hasExtra("IS_OTHER") && getIntent().getBooleanExtra("IS_OTHER", false)) {
            if (load == 0) {
                load = 1;

                mViewPager.setCurrentItem(1, true);


            }
        }
    }

    private void initListener() {
        imgBack.setOnClickListener(this);
    }

    private void initUi() {
        imgBack = findViewById(R.id.imgBack);
        txtLogOut = findViewById(R.id.txtLogOut);
        txtname = findViewById(R.id.txtname);

        txttitle = findViewById(R.id.txttitle);
        txttitle.setText("Message List");
        txtname.setText("" + new Preferences(this).getAgentName(this) + "");

        if (new Preferences(this).getString("UT").equalsIgnoreCase("admin")) {
            imgBack.setVisibility(View.VISIBLE);
        } else {
            imgBack.setVisibility(View.GONE);
        }

        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MessageTabActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_alert);
                TextView txtYes = dialog.findViewById(R.id.txtYes);
                TextView txtNo = dialog.findViewById(R.id.txtNo);

                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new com.example.quagnitia.messaging_app.Storage.Preferences(MessageTabActivity.this).clearPreferences();
                        Intent newIntent = new Intent(MessageTabActivity.this, MainActivity.class);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(newIntent);
//                        Toast.makeText(WelcomeActivity.this, "Sign Out...", Toast.LENGTH_SHORT).show();
                        MessageTabActivity.this.finish();
                    }
                });
                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentAQI();
                case 1:
                    return new FragmentOthers();
                default:
                    return null;
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
