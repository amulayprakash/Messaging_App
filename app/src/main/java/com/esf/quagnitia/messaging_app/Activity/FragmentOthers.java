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
import com.esf.quagnitia.messaging_app.Model.UserResponse;
import com.esf.quagnitia.messaging_app.R;
import com.esf.quagnitia.messaging_app.Storage.Preferences;
import com.esf.quagnitia.messaging_app.adapter.MessageAdaptor;
import com.esf.quagnitia.messaging_app.adapter.OtherMessageAdaptor;
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

public class FragmentOthers extends Fragment {
    //    ArrayList<Active> pickupList;
    TextView   txtfromshow, txttoshow, txtreset;
    RecyclerView rvlist;
    Preferences preferences;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 2;
    private int currentPage = PAGE_START;
    private static final int PAGE_START = 0;
    LinearLayoutManager linearLayoutManager;
    MessageList pickups;
    ArrayList<Data> activepicupList = new ArrayList<>();
    RelativeLayout relLoad;
    int loadType = 0;
    private MediaPlayer mMediaPlayer;
    String fromdate = null, todate = null;
    Date from, to;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater lf, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = lf.inflate(R.layout.fragment_past, container, false);

        try {
            preferences = new Preferences(getActivity());
//            final Window win = getWindow();
//            win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
//                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
//                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

            txtreset = rootView.findViewById(R.id.txtreset);
            txtfromshow = rootView.findViewById(R.id.txtfromshow);
            txttoshow = rootView.findViewById(R.id.txttoshow);
            rvlist = rootView.findViewById(R.id.rvlist);
            relLoad = rootView.findViewById(R.id.relLoad);
            relLoad.setVisibility(View.GONE);

            try {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(c);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(c);
                calendar.add(Calendar.DAY_OF_YEAR, -15);
                Date newDate = calendar.getTime();
                String dateStr = df.format(newDate);

                txtfromshow.setText(dateStr + "");
                txttoshow.setText(formattedDate + "");

                fromdate = txtfromshow.getText().toString();
                todate = txttoshow.getText().toString();

                SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                Date dateObj = curFormater.parse(dateStr);
                Date dateObj2 = curFormater.parse(formattedDate);
                from = dateObj;
                to = dateObj2;

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            txtreset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txtreset.getText().toString().equalsIgnoreCase("clear")) {
                        try {
                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String formattedDate = df.format(c);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(c);
                            calendar.add(Calendar.DAY_OF_YEAR, -15);
                            Date newDate = calendar.getTime();
                            String dateStr = df.format(newDate);

                            txtfromshow.setText(dateStr + "");
                            txttoshow.setText(formattedDate + "");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        txtreset.setText("Search");
                        fromdate = null;
                        todate = null;
                    } else {
                        txtreset.setText("Clear");
                        fromdate = txtfromshow.getText().toString();
                        todate = txttoshow.getText().toString();
                    }
                    isLastPage = false;
                    isLoading = false;
                    loadType = 0;
                    activepicupList.clear();
                    loadFirstPage();
                }
            });

            txtfromshow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            try {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, month, dayOfMonth);
                                long selectedMilli = newDate.getTimeInMillis();

                                Date datePickerDate = new Date(selectedMilli);

                                String reportDate = new SimpleDateFormat("yyyy-MM-dd").format(datePickerDate);

                                if (txttoshow.getText().toString().equalsIgnoreCase(reportDate)) {
                                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateObj = curFormater.parse(reportDate);
                                    from = dateObj;
                                    txtfromshow.setText(reportDate);
                                    txtreset.setText("Search");
                                    searchbydate();
                                } else if (to.before(datePickerDate)) {
                                    Toast.makeText(getActivity(), "Select date less than to date.", Toast.LENGTH_SHORT).show();
                                    txtfromshow.setText("  -  -    ");
                                } else {
                                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateObj = curFormater.parse(reportDate);
                                    from = dateObj;
                                    txtfromshow.setText(reportDate);
                                    txtreset.setText("Search");
                                    searchbydate();
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, year, month, day);
                    dpd.show();
                }
            });

            txttoshow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            try {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, month, dayOfMonth);
                                long selectedMilli = newDate.getTimeInMillis();

                                Date datePickerDate = new Date(selectedMilli);

                                String reportDate = new SimpleDateFormat("yyyy-MM-dd").format(datePickerDate);

                                if (txtfromshow.getText().toString().equalsIgnoreCase(reportDate)) {
                                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateObj = curFormater.parse(reportDate);
                                    to = dateObj;
                                    txttoshow.setText(reportDate);
                                    txtreset.setText("Search");
                                    searchbydate();
                                } else if (from.after(datePickerDate)) {
                                    Toast.makeText(getActivity(), "Select date grater than from date.", Toast.LENGTH_SHORT).show();
                                    txttoshow.setText("  -  -    ");
                                } else {
                                    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateObj = curFormater.parse(reportDate);
                                    to = dateObj;
                                    txttoshow.setText(reportDate);
                                    searchbydate();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                    }, year, month, day);
                    dpd.show();
                }
            });


            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rvlist.setLayoutManager(linearLayoutManager);
            MessageAdaptor sd = new MessageAdaptor(getActivity(), activepicupList);
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

            if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().containsKey("FROM_NOTI")) {
//                playSound();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mMediaPlayer != null) {//nikita
//                            mMediaPlayer.stop();
//                        }
//                    }
//                }, 15000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        getActivity().finish();
                    }
                }, 60000);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;

    }

    private void searchbydate() {
        fromdate = txtfromshow.getText().toString();
        todate = txttoshow.getText().toString();
        isLastPage = false;
        isLoading = false;
        loadType = 0;
        activepicupList.clear();
        loadFirstPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLastPage = false;
        isLoading = false;
        loadType = 0;
    }


    private void loadFirstPage() {//nikita
        try {
            if (!NetworkUtils.checkNetworkConnection(getActivity())) {
                callPickupWS("start");
            } else {
                Toast.makeText(getActivity(), "No internet connetion!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadNextPage() {//nikita
        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
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
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("loading...");
            pd.setCanceledOnTouchOutside(false);

            ApiServices apiService = RetrofitClient.getClient().create(ApiServices.class);

            Call<UserResponse> call;//nikita
            if (LoadType.equalsIgnoreCase("start")) {
                relLoad.setVisibility(View.GONE);
                pd.show();
                call = apiService.getSchoolMessage(preferences.getAgentId(getActivity()), null, "0", fromdate, todate, new Preferences(getActivity()).getString("SI"), getToken());
            } else {
                relLoad.setVisibility(View.VISIBLE);
                call = apiService.getSchoolMessage(preferences.getAgentId(getActivity()), LoadType, "0", fromdate, todate, new Preferences(getActivity()).getString("SI"), getToken());
            }

            Log.i("@nikita", "Url:" + call.request().url().toString());
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    try {
                        Log.i("@nikita", "Resp" + response);
                        pd.dismiss();
                        if (response.code() == 200) {// success
                            if (response.body() != null  && response.body().getIsSessionValid().equalsIgnoreCase("true")) {//&& response.body().getError().equals("0")
//                                if (!response.body().getMessage().isEmpty()) {
//                                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
                                relLoad.setVisibility(View.GONE);

                                pickups = response.body().getMessageList();
                                if (pickups.getData() == null || pickups.getData().isEmpty()) {
                                    isLastPage = true;
                                } else {
                                    isLastPage = false;
                                }
                                int postomove = activepicupList.size();
                                activepicupList.addAll(pickups.getData());
                                setData();

                                //nikita
                                if (pickups.getLast_page() != 0) {
                                    TOTAL_PAGES = pickups.getLast_page();
                                }
                                isLoading = false;
                                if (LoadType.equalsIgnoreCase("start")) {

                                    if (currentPage <= TOTAL_PAGES) {
//                                    notificationAdapter.addLoadingFooter();
                                    } else isLastPage = true;

                                } else {
                                    if (activepicupList.size() > 21) {
                                        rvlist.scrollToPosition(postomove - 1);
                                    }


                                    if (currentPage != TOTAL_PAGES) {
//                                    notificationAdapter.addLoadingFooter();
                                    } else isLastPage = true;

                                }


                            } else if (response.body() != null ) {//&& response.body().getError().equals("1")
                                if(response.body().getIsSessionValid().equalsIgnoreCase("false")){
//                                    if (!response.body().getMessage().isEmpty()) {
                                    Toast.makeText(getActivity(), "Invalid session...", Toast.LENGTH_SHORT).show();
//                                    }
                                    new com.esf.quagnitia.messaging_app.Storage.Preferences(getActivity()).clearPreferences();
                                    Intent newIntent = new Intent(getActivity(), MainActivity.class);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(newIntent);
                                    getActivity().finish();
                                }else {
//                                    if (!response.body().getMessage().isEmpty()) {
//                                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
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
//                                    Toast.makeText(getActivity(), jos.optString("message"), Toast.LENGTH_SHORT).show();

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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setData() {
        try {
            OtherMessageAdaptor sd = new OtherMessageAdaptor(getActivity(), activepicupList);
            rvlist.setAdapter(sd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
