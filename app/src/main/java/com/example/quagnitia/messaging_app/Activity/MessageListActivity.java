package com.example.quagnitia.messaging_app.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quagnitia.messaging_app.Model.Data;
import com.example.quagnitia.messaging_app.Model.Text;
import com.example.quagnitia.messaging_app.Model.UserResponse;
import com.example.quagnitia.messaging_app.R;
import com.example.quagnitia.messaging_app.Storage.Preferences;
import com.example.quagnitia.messaging_app.adapter.MessageAdaptor;
import com.example.quagnitia.messaging_app.util.NetworkUtils;
import com.example.quagnitia.messaging_app.util.PaginationScrollListener;
import com.example.quagnitia.messaging_app.webservice.ApiServices;
import com.example.quagnitia.messaging_app.webservice.RetrofitClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageListActivity
        extends AppCompatActivity {
    TextView txtLogOut, txtname, txtschool, txttitle, txtfromshow, txttoshow, txtreset;
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
    private MediaPlayer mMediaPlayer;
    String fromdate = null, todate = null;
    Date from, to;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {//nikita
            mMediaPlayer.stop();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        preferences = new Preferences(MessageListActivity.this);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        txtreset = findViewById(R.id.txtreset);
        txtfromshow = findViewById(R.id.txtfromshow);
        txttoshow = findViewById(R.id.txttoshow);
        txtschool = findViewById(R.id.txtschool);
        txtLogOut = findViewById(R.id.txtLogOut);
        txtname = findViewById(R.id.txtname);
        rvlist = findViewById(R.id.rvlist);
        relLoad = findViewById(R.id.relLoad);
        relLoad.setVisibility(View.GONE);
        txtname.setText("" + preferences.getAgentName(this) + "");
        txtschool.setText("" + preferences.getSchool(this) + "");
        txttitle = findViewById(R.id.txttitle);
        txttitle.setText("Message List");

        try {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = df.format(c);

            txtfromshow.setText("01/01/2019");
            txttoshow.setText(formattedDate + "");

            String dateStr = "01/01/2019";

            SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
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

                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String formattedDate = df.format(c);

                        txtfromshow.setText("01/01/2019");
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
                DatePickerDialog dpd = new DatePickerDialog(MessageListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, dayOfMonth);
                        long selectedMilli = newDate.getTimeInMillis();

                        Date datePickerDate = new Date(selectedMilli);

                        String reportDate = new SimpleDateFormat("dd/MM/yyyy").format(datePickerDate);

                        if (to.before(datePickerDate)) {
                            Toast.makeText(MessageListActivity.this, "Select date less than to date.", Toast.LENGTH_SHORT).show();

                            txtfromshow.setText("  /  /    ");
                        } else {
                            txtfromshow.setText(reportDate);
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
                DatePickerDialog dpd = new DatePickerDialog(MessageListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, dayOfMonth);
                        long selectedMilli = newDate.getTimeInMillis();

                        Date datePickerDate = new Date(selectedMilli);

                        String reportDate = new SimpleDateFormat("dd/MM/yyyy").format(datePickerDate);

                        if (from.after(datePickerDate)) {
                            Toast.makeText(MessageListActivity.this, "Select date grater than from date.", Toast.LENGTH_SHORT).show();
                            txttoshow.setText("  /  /    ");
                        } else {
                            txttoshow.setText(reportDate);
                        }


                    }
                }, year, month, day);
                dpd.show();
            }
        });

        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(MessageListActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_alert);
                TextView txtYes = dialog.findViewById(R.id.txtYes);
                TextView txtNo = dialog.findViewById(R.id.txtNo);

                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new com.example.quagnitia.messaging_app.Storage.Preferences(MessageListActivity.this).clearPreferences();
                        Intent newIntent = new Intent(MessageListActivity.this, MainActivity.class);
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
        MessageAdaptor sd = new MessageAdaptor(MessageListActivity.this, activepicupList);
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

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().containsKey("FROM_NOTI")) {
            playSound();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayer != null) {//nikita
                        mMediaPlayer.stop();
                    }
                }
            }, 15000);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    finish();
                }
            }, 60000);

        }
    }

    private void playSound() {

        mMediaPlayer = new MediaPlayer();
        try {
//                mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) this
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mMediaPlayer.setDataSource(this, alarmTone);
                mMediaPlayer.prepare();
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();


            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
//        }
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
            call = apiService.getSchoolMessage(getIntent().getStringExtra("schoolId"), fromdate, todate);
        } else {
            relLoad.setVisibility(View.VISIBLE);
//
//            PagingItem pagingItem = new PagingItem();
//            pagingItem.setPage(LoadType);
//            pagingItem.setAgentId(new Preferences(getActivity()).getAgentId(getActivity()));
            call = apiService.getSchoolMessage(getIntent().getStringExtra("schoolId"), fromdate, todate);
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
                            Toast.makeText(MessageListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        relLoad.setVisibility(View.GONE);

                        pickups = response.body().getText();
                        if (pickups.getData() == null || pickups.getData().isEmpty()) {
                            isLastPage = true;
                        } else {
                            isLastPage = false;
                        }
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
                            if (activepicupList.size() > 11) {
                                rvlist.scrollToPosition(activepicupList.size() - (10));
                            }


                            if (currentPage != TOTAL_PAGES) {
//                                    notificationAdapter.addLoadingFooter();
                            } else isLastPage = true;

                        }


                    } else if (response.body() != null && response.body().getError().equals("1")) {
                        if (!response.body().getMessage().isEmpty()) {
                            if (!activepicupList.isEmpty()) {
                                setData();
                            }
                            Toast.makeText(MessageListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
            MessageAdaptor sd = new MessageAdaptor(this, activepicupList);
            rvlist.setAdapter(sd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

