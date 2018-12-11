package com.example.quagnitia.messaging_app.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quagnitia.messaging_app.R;

public class WelcomeActivity extends AppCompatActivity {
ImageView imgBack;
TextView txtWelcome,txtLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        imgBack=findViewById(R.id.imgBack);
        txtWelcome=findViewById(R.id.txtWelcome);
        txtLogOut=findViewById(R.id.txtLogOut);


        Intent i=getIntent();
        String name=i.getExtras().getString("name");

        //Bundle bundle = getIntent().getExtras();
      //  String name = bundle.getString("name");
        txtWelcome.setText("Welcome \n\n"+name);

      /*  imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });*/


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
                        Intent newIntent = new Intent(WelcomeActivity.this,MainActivity.class);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(newIntent);
                        Toast.makeText(WelcomeActivity.this, "Sign Out...", Toast.LENGTH_SHORT).show();



                        //   finish();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {}


        return false;
        // Disable back button..............
    }
}
