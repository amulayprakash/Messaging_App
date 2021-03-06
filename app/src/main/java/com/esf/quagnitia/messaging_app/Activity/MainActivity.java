package com.esf.quagnitia.messaging_app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esf.quagnitia.messaging_app.Model.User;
import com.esf.quagnitia.messaging_app.Model.UserResponse;
import com.esf.quagnitia.messaging_app.R;
import com.esf.quagnitia.messaging_app.Storage.Preferences;
import com.esf.quagnitia.messaging_app.util.NetworkUtils;
import com.esf.quagnitia.messaging_app.webservice.ApiServices;
import com.esf.quagnitia.messaging_app.webservice.RetrofitClient;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout llLogin;
    EditText etEmail, etPass;
    TextView forget;
    TextInputLayout etEmailLayout;
    RelativeLayout rlMain;
    Preferences preferences;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        etPass.setTransformationMethod(new PasswordTransformationMethod());
        // etPass.setTransformationMethod(new DoNothingTransformation());
        initListner();

        etEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etEmail.setFocusable(true);
                return false;
            }
        });


    }

    private void initListner() {
        llLogin.setOnClickListener(this);
        rlMain.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    private void initUi() {
        llLogin = findViewById(R.id.llLogin);
        etEmail = findViewById(R.id.etEmail);
        rlMain = findViewById(R.id.rlMain);
        etPass = findViewById(R.id.etPass);
        forget = findViewById(R.id.forget);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llLogin:
                if (validation()) {
                    if (!NetworkUtils.checkNetworkConnection(this)) {
                        callLoginWS();
                    } else {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.rlMain:
                hideSoftKeyboard();
                break;
            case R.id.forget:
                Intent forgetIntent = new Intent(context, ForgetPassword.class);
                startActivity(forgetIntent);
                //  Toast.makeText(context, R.string.inProgress, Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void callLoginWS() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        ApiServices apiService = RetrofitClient.getClient().create(ApiServices.class);
        Call<UserResponse> call = apiService.loginUser(sendUserData());

        Log.i("@Rahul", "LoginUrl: " + call.request().url().toString());
        call.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                Log.i("@Rahul", "LoginResp: " + response);
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if (response.code() == 200) {// success
                        // preferences.setLOGIN(true);
                        Toast.makeText(MainActivity.this, "Login Successfull..!!!", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        com.esf.quagnitia.messaging_app.Storage.Preferences preferences1 = new com.esf.quagnitia.messaging_app.Storage.Preferences(MainActivity.this);

                        if (userResponse.getUserType() != null && !userResponse.getUserType().isEmpty()) {
                            preferences1.putString("UT", userResponse.getUserType());
                        }
                        preferences1.putString("SI", userResponse.getSessionId());//session id
                        User userlog = userResponse.getUser();
                        if (userlog != null) {
                            preferences1.putString("schoolId", userlog.getSchoolID());
                            preferences1.setLogin(true);
                            preferences1.saveAgentName(MainActivity.this, (userlog.getName()));
                            preferences1.saveAgentId(MainActivity.this, userlog.getUserID());
                            preferences1.saveSchool(MainActivity.this, userlog.getSchoolName());
                        }
                        String name = etEmail.getText().toString().trim();


                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("notifications_new_message", true);
                        editor.putBoolean("notifications_new_message_vibrate", true);
                        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        editor.putString("notifications_new_message_ringtone", notificationSoundUri.toString());
                        editor.apply();
//                        editor.commit();

                        boolean sound = prefs.getBoolean("notifications_new_message", false);
                        boolean vibr = prefs.getBoolean("notifications_new_message_vibrate", false);
                        String ring = prefs.getString("notifications_new_message_ringtone", "");

                        Intent i = new Intent(context, AutostartActivity.class);
                        i.putExtra("AUTOSTART", true);
                        i.putExtra("name", name);
//                        i.putExtra("schoolId",userlog.getSchoolID());
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                        clearData();
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Login Failed..!!!", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Login Failed..!!!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Log.i("@shradha", "Error: " + t.toString());
                Toast.makeText(MainActivity.this, "Error...", LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void clearData() {
        etEmail.setText("");
        etPass.setText("");
    }

    private User sendUserData() {
        User user = new User();
        user.setEmail(etEmail.getText().toString().trim());
        user.setPassword(etPass.getText().toString().trim());
        user.setLogin_type("0");
        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        user.setDeviceId(deviceId);
        user.setFcmTokenId(getToken());
        return user;
    }

    private String getToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        //  Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
        String msg = "Instance ID Token: " + token;
//        Log.v("TOKENS",token);
        return token;
    }

    private boolean validation() {
        boolean flag = false;
        if (etEmail.getText().toString().trim().equals("")) {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
            etEmail.setError("Enter email");
        } else if (!etEmail.getText().toString().trim().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
            etEmail.setError("Enter valid email");
//        } else if (etPass.getText().length() < 6) {
//            Toast.makeText(context, R.string.emailVali, Toast.LENGTH_SHORT).show();
//            etPass.setError("Enter min 6 character");
        } else
            flag = true;


        return flag;
    }


    public void hideSoftKeyboard() {
        InputMethodManager inm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

}
