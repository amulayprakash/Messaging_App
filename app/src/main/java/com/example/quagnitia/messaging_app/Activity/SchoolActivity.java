package com.example.quagnitia.messaging_app.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quagnitia.messaging_app.Model.Data;
import com.example.quagnitia.messaging_app.Model.Text;
import com.example.quagnitia.messaging_app.Model.UserResponse;
import com.example.quagnitia.messaging_app.R;
import com.example.quagnitia.messaging_app.Storage.Preferences;
import com.example.quagnitia.messaging_app.adapter.ActiveAdaptor;
import com.example.quagnitia.messaging_app.util.NetworkUtils;
import com.example.quagnitia.messaging_app.util.PaginationScrollListener;
import com.example.quagnitia.messaging_app.webservice.ApiServices;
import com.example.quagnitia.messaging_app.webservice.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchoolActivity extends AppCompatActivity {
    TextView txtLogOut, txtname,txttitle;
    RecyclerView rvlist;
    Preferences preferences;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 2;
    private int currentPage = PAGE_START;
    private static final int PAGE_START = 0;
    LinearLayoutManager linearLayoutManager;
    Text pickups;
    ArrayList<Data> activepicupList = new ArrayList<>();
    RelativeLayout relLoad;
    int loadType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);

        preferences = new Preferences(SchoolActivity.this);

        txtLogOut = findViewById(R.id.txtLogOut);
        txtname = findViewById(R.id.txtname);
        rvlist = findViewById(R.id.rvlist);
        relLoad = findViewById(R.id.relLoad);
        relLoad.setVisibility(View.GONE);
        txtname.setText("" + preferences.getAgentName(this) + "");
        txttitle = findViewById(R.id.txttitle);
        txttitle.setText("School List");

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

                        new com.example.quagnitia.messaging_app.Storage.Preferences(SchoolActivity.this).clearPreferences();
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

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvlist.setLayoutManager(linearLayoutManager);
        ActiveAdaptor sd = new ActiveAdaptor(SchoolActivity.this, activepicupList);
        rvlist.setAdapter(sd);
        rvlist.setItemAnimator(new DefaultItemAnimator());

        rvlist.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 500);
            }

            @Override
            public int getTotalPageCount() {
                if (loadType == 0) {
                    loadType = 1;
                    return TOTAL_PAGES;
                } else {
                    return 1;
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        loadFirstPage();
    }

    private void loadFirstPage() {//nikita

        if (!NetworkUtils.checkNetworkConnection(this)) {
            callPickupWS("start");
        } else {
            Toast.makeText(this, "No internet connetion!", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadNextPage() {//nikita

        if (!NetworkUtils.checkNetworkConnection(this)) {
            if (pickups.getNext_page_url() != null && !pickups.getNext_page_url().equals("") && !pickups.getNext_page_url().equalsIgnoreCase("null")) {
                String[] arr = pickups.getNext_page_url().split("=");
                String page = "";
                if (arr != null) {
                    if (arr.length == 2) {
                        page = arr[1];
                    }
                    if (!page.isEmpty()) {
                        callPickupWS(page);
                    } else {
//                        Toast.makeText(this, "No more data available.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(this, "No internet connetion!", Toast.LENGTH_SHORT).show();
        }
    }

    private void callPickupWS(final String LoadType) {//nikita
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("loading...");
        pd.setCanceledOnTouchOutside(false);


        ApiServices apiService = RetrofitClient.getClient().create(ApiServices.class);

        Call<UserResponse> call;//nikita
        if (LoadType.equalsIgnoreCase("start")) {
            relLoad.setVisibility(View.GONE);
            pd.show();
            call = apiService.getSchhollist(preferences.getAgentId(SchoolActivity.this),"1");
        } else {
            relLoad.setVisibility(View.VISIBLE);
//
//            PagingItem pagingItem = new PagingItem();
//            pagingItem.setPage(LoadType);
//            pagingItem.setAgentId(new Preferences(getActivity()).getAgentId(getActivity()));
            call = apiService.getSchhollist(preferences.getAgentId(SchoolActivity.this),LoadType);
        }
        Log.i("@nikita", "Url:" + call.request().url().toString());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                try {
                    Log.i("@nikita", "Resp" + response);
                    pd.dismiss();
                    if (response.body() != null && response.body().getError().equals("0")) {
                        if (!response.body().getMessage().isEmpty()) {
                            Toast.makeText(SchoolActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        relLoad.setVisibility(View.GONE);

                        pickups = response.body().getText();
                        if (pickups.getData() == null || pickups.getData().isEmpty()) {
                            isLastPage = true;
                        }else {
                            isLastPage = false;
                        }
                        activepicupList.addAll(pickups.getData());
                        setData();

                        //nikita
                        if (pickups.getLast_page()!=0) {
                            TOTAL_PAGES = pickups.getLast_page();
                        }
                        isLoading = false;
                        if (LoadType.equalsIgnoreCase("start")) {

                            if (currentPage <= TOTAL_PAGES) {
//                                    notificationAdapter.addLoadingFooter();
                            }
//                            else isLastPage = true;

                        } else {
                            if (activepicupList.size() > 11 ) {
                                rvlist.scrollToPosition(activepicupList.size() - (10 ));
                            }



                            if (currentPage != TOTAL_PAGES) {
                            }

                        }
                    } else if (response.body() != null && response.body().getError().equals("1")) {
                        if (!response.body().getMessage().isEmpty()) {
                            if (!activepicupList.isEmpty()) {
                                setData();
                            }
                            Toast.makeText(SchoolActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception ex) {
                    if (!activepicupList.isEmpty()) {
                        setData();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.i("@nikita", "Error" + t);
                if (!activepicupList.isEmpty()) {
                    setData();
                }
            }
        });


    }

    private void setData() {
        try {
            ActiveAdaptor sd = new ActiveAdaptor(this, activepicupList);
            rvlist.setAdapter(sd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
