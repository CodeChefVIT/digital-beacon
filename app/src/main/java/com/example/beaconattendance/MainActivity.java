package com.example.beaconattendance;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private Button btnsignup;
    private EditText edtEmail, edtPassword, edtName, edtReg;
    private TextView tvlogin;
    private String macAddress;
    private WifiManager wifiManager;
    private int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtpassword);
        edtName = findViewById(R.id.edtname);
        edtReg = findViewById(R.id.edtReg);
        tvlogin = findViewById(R.id.tvlogin);
        btnsignup = findViewById(R.id.btnsignup);

        btnsignup.setOnClickListener(this);
        tvlogin.setOnClickListener(this);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {

            count=1;

        }else {

            count=0;
        }

        macAddress= "192.168.43.117";

        wifiManager.setTdlsEnabledWithMacAddress(macAddress,true);



        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = "D-Link_DIR-600L";
        wc.preSharedKey = "milapnagar";
        wc.status = WifiConfiguration.Status.ENABLED;
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        // connect to and enable the connection
        int netId = wifiManager.addNetwork(wc);
        wifiManager.enableNetwork(netId, true);
        wifiManager.setWifiEnabled(true);
    }



    private void userSignUp() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String reg_no = edtReg.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError("Email is Required");
            edtEmail.requestFocus();
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter a valid Email");
            edtEmail.requestFocus();
        }

        if (password.isEmpty()) {
            edtPassword.setError("Password required");
            edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Password should be atleast 6 character long");
            edtPassword.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            edtName.setError("Name required");
            edtName.requestFocus();
            return;
        }

        if (reg_no.isEmpty()) {
            edtReg.setError("Registration Number required");
            edtReg.requestFocus();
        }

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .employees(email, password, name, reg_no);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              try{
                  String s=response.body().string();
                  Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();

              }catch (IOException e){
                  e.printStackTrace();
              }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnsignup:
                userSignUp();
                break;
            case R.id.tvlogin:
                userSignIn();
                break;
            default:
                    //Do nothing
        }
    }

    public void userSignIn() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
