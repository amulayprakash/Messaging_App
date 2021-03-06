package com.esf.quagnitia.messaging_app.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esf.quagnitia.messaging_app.Model.Data;
import com.esf.quagnitia.messaging_app.Model.MessageList;
import com.esf.quagnitia.messaging_app.Model.Text;
import com.esf.quagnitia.messaging_app.Model.UserResponse;
import com.esf.quagnitia.messaging_app.R;
import com.esf.quagnitia.messaging_app.Storage.Preferences;
import com.esf.quagnitia.messaging_app.adapter.ActiveAdaptor;
import com.esf.quagnitia.messaging_app.adapter.MessageAdaptor;
import com.esf.quagnitia.messaging_app.util.NetworkUtils;
import com.esf.quagnitia.messaging_app.util.PaginationScrollListener;
import com.esf.quagnitia.messaging_app.webservice.ApiServices;
import com.esf.quagnitia.messaging_app.webservice.RetrofitClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSchool extends Fragment {
    //    ArrayList<Active> pickupList;
    TextView txtother, txtbadge;
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
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater lf, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = lf.inflate(R.layout.fragment_school, container, false);

        try {
            preferences = new Preferences(getActivity());
//            final Window win = getWindow();
//            win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
//                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

            rvlist = rootView.findViewById(R.id.rvlist);
            relLoad = rootView.findViewById(R.id.relLoad);
            txtother = rootView.findViewById(R.id.txtother);
            relLoad.setVisibility(View.GONE);
            txtother.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getActivity(), OthersMessageListActivity.class);
                    getActivity().startActivity(in);
                }
            });

            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rvlist.setLayoutManager(linearLayoutManager);
            ActiveAdaptor sd = new ActiveAdaptor(getActivity(), activepicupList);
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
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        activepicupList.clear();
        setData();
        isLastPage = false;
        isLoading = false;
        loadType = 0;
        loadFirstPage();
    }


    private void loadFirstPage() {//nikita

        if (!NetworkUtils.checkNetworkConnection(getActivity())) {
            callPickupWS("start");
        } else {
            Toast.makeText(getActivity(), "No internet connetion!", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadNextPage() {//nikita

        if (!NetworkUtils.checkNetworkConnection(getActivity())) {
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
            Toast.makeText(getActivity(), "No internet connetion!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        //  Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
        String msg = "Instance ID Token: " + token;
//        Log.v("TOKENS",token);
        return token;
    }

    private void callPickupWS(final String LoadType) {//nikita
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("loading...");
        pd.setCanceledOnTouchOutside(false);


        ApiServices apiService = RetrofitClient.getClient().create(ApiServices.class);

        Call<UserResponse> call;//nikita
        if (LoadType.equalsIgnoreCase("start")) {
            relLoad.setVisibility(View.GONE);
            pd.show();
            call = apiService.getSchhollist(preferences.getAgentId(getActivity()), "1", new Preferences(getActivity()).getString("SI"), getToken());
        } else {
            relLoad.setVisibility(View.VISIBLE);
            call = apiService.getSchhollist(preferences.getAgentId(getActivity()), LoadType, new Preferences(getActivity()).getString("SI"), getToken());
        }
        Log.i("@nikita", "Url:" + call.request().url().toString());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                try {
                    Log.i("@nikita", "Resp" + response);
                    pd.dismiss();
                    if (response.code() == 200) {// success
                        if (response.body() != null && response.body().getIsSessionValid().equalsIgnoreCase("true")) {//&& response.body().getError().equals("0")
//                            if (!response.body().getMessage().isEmpty()) {
//                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
                            relLoad.setVisibility(View.GONE);

                            pickups = response.body().getText();
                            if (pickups.getData() == null || pickups.getData().isEmpty()) {
                                isLastPage = true;
                            } else {
                                isLastPage = false;
                            }
                            activepicupList.addAll(pickups.getData());
                            setData();

                            if (response.body().getOtherNotificationCount() > 0) {
                                txtbadge.setVisibility(View.VISIBLE);
                                txtbadge.setText(response.body().getOtherNotificationCount()+"");
                            } else {
                                txtbadge.setVisibility(View.GONE);
                            }

                            //nikita
                            if (pickups.getLast_page() != 0) {
                                TOTAL_PAGES = pickups.getLast_page();
                            }
                            isLoading = false;
                            if (LoadType.equalsIgnoreCase("start")) {

                                if (currentPage <= TOTAL_PAGES) {
//                                    notificationAdapter.addLoadingFooter();
                                }
//                            else isLastPage = true;

                            } else {
                                if (activepicupList.size() > 11) {
                                    rvlist.scrollToPosition(activepicupList.size() - (10));
                                }


                                if (currentPage != TOTAL_PAGES) {
                                }

                            }
                        } else if (response.body() != null) {//&& response.body().getError().equals("1")
                            if (response.body().getIsSessionValid().equalsIgnoreCase("false")) {
//                                if (!response.body().getMessage().isEmpty()) {
                                Toast.makeText(getActivity(), "Invalid session...", Toast.LENGTH_SHORT).show();
//                                }
                                new com.esf.quagnitia.messaging_app.Storage.Preferences(getActivity()).clearPreferences();
                                Intent newIntent = new Intent(getActivity(), MainActivity.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(newIntent);
                                getActivity().finish();
                            } else {
                                if (!response.body().getMessage().isEmpty()) {
                                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                if (!activepicupList.isEmpty()) {
                                    setData();
                                }
                            }
                        }
                    } else if (response.code() == 401) {// invalid
                        if (response.errorBody() != null) {
                            JSONObject jos = new JSONObject(response.errorBody().string().trim());

                            if (jos.optString("isSessionValid").equalsIgnoreCase("true")) {
                                if (!activepicupList.isEmpty()) {
                                    setData();
                                }
//                                Toast.makeText(getActivity(), jos.optString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Invalid session...", Toast.LENGTH_SHORT).show();
                                new com.esf.quagnitia.messaging_app.Storage.Preferences(getActivity()).clearPreferences();
                                Intent newIntent = new Intent(getActivity(), MainActivity.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(newIntent);
                                getActivity().finish();
                            }
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
            ActiveAdaptor sd = new ActiveAdaptor(getActivity(), activepicupList);
            rvlist.setAdapter(sd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
