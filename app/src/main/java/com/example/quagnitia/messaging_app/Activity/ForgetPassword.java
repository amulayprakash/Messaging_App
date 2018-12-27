package com.example.quagnitia.messaging_app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quagnitia.messaging_app.Model.UserResponse;
import com.example.quagnitia.messaging_app.R;
import com.example.quagnitia.messaging_app.webservice.ApiServices;
import com.example.quagnitia.messaging_app.webservice.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_SHORT;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {

    ImageView imgBack;
    RelativeLayout rlForgetMain;
    EditText etEmail;
    TextView txtResetPassword,txtCancel,txtLogin;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        init();
        initListner();

    }

    private void initListner() {
//        etEmail.setOnClickListener(this);
        txtResetPassword.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        rlForgetMain.setOnClickListener(this);

    }

    private void init() {
        imgBack=findViewById(R.id.imgBack);
        rlForgetMain=findViewById(R.id.rlForgetMain);
        etEmail=findViewById(R.id.etEmail);
        txtCancel=findViewById(R.id.txtCancel);
        txtResetPassword=findViewById(R.id.txtResetPassword);
        txtLogin=findViewById(R.id.txtLogin);

    }

    public void hideSoftKeyboard() {
        InputMethodManager inm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.rlForgetMain:
                hideSoftKeyboard();
                break;
            case R.id.etEmail:
                 validation();
                break;
            case R.id.txtCancel:
                 finish();
                break;
            case R.id.imgBack:
                finish();
                break;
            case R.id.txtLogin:
                finish();
                break;
            case R.id.txtResetPassword:
                if (validation()) {
                    if (!NetworkUtils.checkNetworkConnection(context)) {
                        //   Toast.makeText(context, "Validation for Registration successfull..!!", Toast.LENGTH_SHORT).show();
                        callForgetPass();
                    } else {
                        Toast.makeText(context, getResources().getString(R.string.nointernetconnection) , LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void callForgetPass() {


        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle(getResources().getString(R.string.plwait) + "");
        progressDialog.show();

        ApiServices apiService = RetrofitClient.getClient().create(ApiServices.class);
        Call<UserResponse> call = apiService.forgotPassword(etEmail.getText().toString().trim());


        Log.i("@Rahul", "Url:" + call.request().url().toString());

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                try {
                    Log.i("@Rahul", "Resp" + response);
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            clearData();
                        }
                    }
                    if (response.body() != null && response.body().getError().equals("0")) {

                        Toast.makeText(ForgetPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        // finish();
                    } else if (response.body().getError().equals("1")) {
                        Toast.makeText(ForgetPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Log.v("@RESPONSE", response.body().getError());
                    Log.v("@RESPONSE", response.body().getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(ForgetPassword.this, getResources().getString(R.string.severNotResponding) + "", LENGTH_SHORT).show();
                }
                //  progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                Log.i("@Error", "Error: " + t.toString());
                Toast.makeText(ForgetPassword.this, getResources().getString(R.string.severNotResponding) + "", LENGTH_SHORT).show();

            }
        });

    }

    private void clearData() {
        etEmail.setText("");
    }

    private boolean validation() {
        boolean flag = false;
        if (etEmail.getText().toString().trim().equals("")) {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
            etEmail.setError("Enter email");
        }
        else if (!etEmail.getText().toString().trim().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
            etEmail.setError("Enter valid email");
        }
        else
            flag = true;


        return flag;
    }
}
