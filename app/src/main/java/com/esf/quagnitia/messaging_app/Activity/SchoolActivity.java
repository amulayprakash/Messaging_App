package com.esf.quagnitia.messaging_app.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esf.quagnitia.messaging_app.Model.Data;
import com.esf.quagnitia.messaging_app.Model.Text;
import com.esf.quagnitia.messaging_app.Model.UserResponse;
import com.esf.quagnitia.messaging_app.R;
import com.esf.quagnitia.messaging_app.Storage.Preferences;
import com.esf.quagnitia.messaging_app.adapter.ActiveAdaptor;
import com.esf.quagnitia.messaging_app.util.NetworkUtils;
import com.esf.quagnitia.messaging_app.util.PaginationScrollListener;
import com.esf.quagnitia.messaging_app.webservice.ApiServices;
import com.esf.quagnitia.messaging_app.webservice.RetrofitClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolActivity extends AppCompatActivity {
    TextView txtLogOut, txtname, txttitle;
    Preferences preferences;
    ImageView imgBack,img_settings;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);

        preferences = new Preferences(SchoolActivity.this);

        imgBack = findViewById(R.id.imgBack);
        img_settings = findViewById(R.id.img_settings);
        img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SchoolActivity.this, SettingsActivity.class);
                startActivity(in);
            }
        });
        txtLogOut = findViewById(R.id.txtLogOut);
        txtname = findViewById(R.id.txtname);

        txtname.setText("" + preferences.getAgentName(this) + "");
        txttitle = findViewById(R.id.txttitle);
        txttitle.setText("School List");



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(SchoolActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_alert);
                TextView txtYes = dialog.findViewById(R.id.txtYes);
                TextView txtNo = dialog.findViewById(R.id.txtNo);

                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new com.esf.quagnitia.messaging_app.Storage.Preferences(SchoolActivity.this).clearPreferences();
                        Intent newIntent = new Intent(SchoolActivity.this, MainActivity.class);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(newIntent);
//                        Toast.makeText(WelcomeActivity.this, "Sign Out...", Toast.LENGTH_SHORT).show();
                        finish();
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
        loadData();

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
                    return new FragmentSchool();
                case 1:
                    return new FragmentMap();
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

    @Override
    protected void onResume() {
        super.onResume();

    }



}
