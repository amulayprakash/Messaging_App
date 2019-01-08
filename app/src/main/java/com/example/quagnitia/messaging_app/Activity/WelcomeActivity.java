package com.example.quagnitia.messaging_app.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quagnitia.messaging_app.Model.Req;
import com.example.quagnitia.messaging_app.Model.Text;
import com.example.quagnitia.messaging_app.Model.MsgResponse;
import com.example.quagnitia.messaging_app.Model.User;
import com.example.quagnitia.messaging_app.Preferences.Preferences;
import com.example.quagnitia.messaging_app.R;
import com.example.quagnitia.messaging_app.webservice.ApiServices;
import com.example.quagnitia.messaging_app.webservice.RetrofitClient;
import com.google.firebase.iid.FirebaseInstanceId;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class WelcomeActivity extends AppCompatActivity {
    ImageView imgBack;
    TextView  txtmsg, txtmsgname, txtlevel, txtschool, txtLogOut, txtname, txtlevellast, txtmsglast, txttimelast, txttime;
    RelativeLayout head;
    WebView txtbody;
    LinearLayout lin2, lin3;
    com.example.quagnitia.messaging_app.Preferences.Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        preferences = new Preferences(WelcomeActivity.this);

        txtbody = findViewById(R.id.txtbody);
        imgBack=findViewById(R.id.imgBack);
//        txtmsg = findViewById(R.id.txtmsg);
        txtLogOut=findViewById(R.id.txtLogOut);
        txtname = findViewById(R.id.txtname);
//        txtmsgname = findViewById(R.id.txtmsgname);
//        txtlevel = findViewById(R.id.txtlevel);
        txtschool = findViewById(R.id.txtschool);
//        head = findViewById(R.id.head);
//        lin2 = findViewById(R.id.lin2);
//        txtlevellast = findViewById(R.id.txtlevellast);
//        txtmsglast = findViewById(R.id.txtmsglast);
//        lin3 = findViewById(R.id.lin3);
//        txttime = findViewById(R.id.txttime);
//        txttimelast = findViewById(R.id.txttimelast);

        txtname.setText("Welcome " + preferences.getAgentName(this) + " !");
//        txtschool.setText(preferences.getSchool(this));

        txtLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(WelcomeActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_alert);
                TextView txtYes = dialog.findViewById(R.id.txtYes);
                TextView txtNo = dialog.findViewById(R.id.txtNo);

                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new com.example.quagnitia.messaging_app.Preferences.Preferences(WelcomeActivity.this).clearPreferences();
                        Intent newIntent = new Intent(WelcomeActivity.this,MainActivity.class);
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

//        setdata();

        if (!NetworkUtils.checkNetworkConnection(this)) {
            callMessageWS();
        } else {
            Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
        }
    }

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(800);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }


    private void setdata() {
//
////        txtmsgname.setText("Name : " + preferences.getString("name"));
//        txtmsg.setText("" + preferences.getString("msg").toString().trim());
////        txtmsglast.setText("" + preferences.getString("msglast").toString().trim());
//        String ll = "";
//        if (!preferences.getString("level").toString().isEmpty()) {
//            int value = Integer.parseInt(preferences.getString("level"));
//
////            if (value >= 1 || value <= 3) {//low
////                ll = "low";
////                lin2.setBackgroundResource((R.drawable.bgcorner)); // color green
////            } else if (value >= 4 || value <= 6) {//moderate
////                ll = "moderate";
////                lin2.setBackgroundResource((R.drawable.bgcorner2)); // color orange
////            } else if (value >= 7 || value <= 9) {//high
////                lin2.setBackgroundResource((R.drawable.bgcorner3)); // color dark red
////                ll = "high";
////            } else if (value >= 10 || value <= 12) {//very high
////                ll = "very high";
////                lin2.setBackgroundResource((R.drawable.bgcorner4)); // color dark brown
////            } else {//serious
////                ll = "serious";
////                lin2.setBackgroundResource(R.drawable.bgcorner5); // color black
////            }
//
//            if (value == 1) {//low
//                ll = "Low";
//                lin2.setBackgroundResource((R.drawable.bgcorner));// color green
//            } else if (value == 2) {//moderate
//                ll = "Moderate";
//                lin2.setBackgroundResource((R.drawable.bgcorner2)); // color orange
//            } else if (value == 3) {//high
//                lin2.setBackgroundResource((R.drawable.bgcorner3));// color dark red
//                ll = "High";
//            } else {//serious
//                ll = "serious";
//                lin2.setBackgroundResource(R.drawable.bgcorner4);// color dark brown
//            }
//
//        }
//        txtlevel.setText("Current Level : " + ll);// + preferences.getString("level") + " (" + ll + ")");
//
//        String lllast = "";
//        if (!preferences.getString("levellast").toString().isEmpty()) {
//            int value = Integer.parseInt(preferences.getString("levellast"));
//            if (value == 1) {//low
//                lllast = "Low";
//                lin3.setBackgroundResource((R.drawable.bgcorner));
//            } else if (value == 2) {//moderate
//                lllast = "Moderate";
//                lin3.setBackgroundResource((R.drawable.bgcorner2));
//            } else if (value == 3) {//high
//                lin3.setBackgroundResource((R.drawable.bgcorner3));
//                lllast = "High";
//            } else {//serious
//                lllast = "serious";
//                lin3.setBackgroundResource(R.drawable.bgcorner4);
//            }
//
//        }
//        txtlevellast.setText("Last Level : " + lllast);// + preferences.getString("levellast") + " (" + lllast + ")");
//
//        try {
//            txttime.setText(parseDate(preferences.getString("date")));
//            txttimelast.setText(parseDate(preferences.getString("datelast")));
//        } catch (Exception ex) {
//            txttime.setVisibility(View.GONE);
//            txttimelast.setVisibility(View.GONE);
//            ex.printStackTrace();
//        }
//
//        lin2.setAnimation(inFromRightAnimation());
//        lin2.setVisibility(View.VISIBLE);
//        lin3.setAnimation(inFromRightAnimation());
//        lin3.setVisibility(View.VISIBLE);
        txtschool.setText(preferences.getString("subject"));
        String summary = (preferences.getString("body"));
        txtbody.loadData(summary, "text/html", "utf-8");
        txtbody.setAnimation(inFromRightAnimation());
        txtbody.setVisibility(View.VISIBLE);
    }

    private String parseDate(String givenDateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dates = format.parse(givenDateString);
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTimeInMillis(dates.getTime());
            String date = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", cal).toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date dateObj = sdf.parse(date);
            PrettyTime p = new PrettyTime();
            return p.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        //  Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
        String msg = "Instance ID Token: " + token;
//        Log.v("TOKENS",token);
        return token;
    }

    private void callMessageWS() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        ApiServices apiService = RetrofitClient.getClient().create(ApiServices.class);
        Req user = new Req();
        user.setFcmTokenId(getToken());
        user.setUserId(preferences.getAgentId(this));
        Call<MsgResponse> call = apiService.showMessage(user);

        Log.i("@nikita", "LoginUrl: " + call.request().url().toString());
        call.enqueue(new Callback<MsgResponse>() {

            @Override
            public void onResponse(Call<MsgResponse> call, retrofit2.Response<MsgResponse> response) {
                Log.i("@nikita", "LoginResp: " + response);
                MsgResponse userResponse = response.body();
                if (userResponse != null) {
                    if (userResponse.getError().equals("0")) {
                        // preferences.setLOGIN(true);
//                        Toast.makeText(WelcomeActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();

//                        Data userlog = userResponse.getData();
//                        if (userlog != null) {
//                            preferences.putString("level", userlog.getMessageLevel());
//                            preferences.putString("msg", userlog.getMessage());
//                            preferences.putString("name", userlog.getMessageName());
//                            preferences.putString("date", userlog.getAqiDateTime());
//                        }
//
//                        Data userlast = userResponse.getPrev();
//                        if (userlast != null) {
//                            preferences.putString("levellast", userlast.getMessageLevel());
//                            preferences.putString("msglast", userlast.getMessage());
//                            preferences.putString("namelast", userlast.getMessageName());
//                            preferences.putString("datelast", userlast.getAqiDateTime());
//                        }

                        Text msg = userResponse.getText();
                        if (msg != null) {
                            preferences.putString("subject", msg.getSubject());
                            preferences.putString("body", msg.getBody());
                        }

                        setdata();
                    } else if (userResponse.getError().equalsIgnoreCase("1")) {
//                        Toast.makeText(WelcomeActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    Toast.makeText(MainActivity.this, "Please register your account", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

//            @Override
//            public void onResponsejj(@NonNull Call<MsgResponse> call, @NonNull retrofit2.Text response) {
//
//            }

            @Override
            public void onFailure(@NonNull Call<MsgResponse> call, @NonNull Throwable t) {
                Log.i("@nikita", "Error: " + t.toString());
                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, getResources().getString(R.string.inProgress) + "", LENGTH_SHORT).show();

            }
        });
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode==KeyEvent.KEYCODE_BACK)
//        {}
//
//
//        return false;
//        // Disable back button..............
//    }
}
