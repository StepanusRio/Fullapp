package com.example.fullapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fullapp.API.LoginAPI;
import com.example.fullapp.API.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText etEmail,etPassword;
    Button btnRegister,btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLogin(etEmail.getText().toString(),etPassword.getText().toString());
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainRegister.class);
                startActivity(intent);
                finish();
            }
        });
    }
    void processLogin(String vmail,String vpassword){
        ServerAPI urlapi = new ServerAPI();
        String URL = urlapi.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginAPI api = retrofit.create(LoginAPI.class);
        if (!isEmailValid(etEmail.getText().toString())) {
            AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity.this);

            msg.setMessage("Email tidak Valid").setNegativeButton("Retry",null)
                    .create().show();
            return;
        }
        api.login(vmail,vpassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getString("status").equals("1")) {
                        AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity.this);

                        msg.setMessage("Login berhasil")
                                .setPositiveButton("Ok",null).create().show();
                        Intent intent = new Intent(MainActivity.this,MainHomeProfile.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity.this);
                        msg.setMessage("Login gagal")
                                .setNegativeButton("Retry",null).create().show();
                        etEmail.setText("");
                        etPassword.setText("");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Info Login", "onFailure: Login Gagal"+t.toString());
            }
        });
    }
    public boolean isEmailValid(String email){
        boolean isValid = false;

        String expression ="^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid=true;
        }
        return  isValid;
    }
}