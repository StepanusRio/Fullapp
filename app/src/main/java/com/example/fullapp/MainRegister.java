package com.example.fullapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.fullapp.API.RegisterAPI;
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

public class MainRegister extends AppCompatActivity {
    EditText etNama, etEmail, etPassword;
    Button btnProcessReg;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        etEmail=(EditText) findViewById(R.id.etEmail);
        etNama= (EditText)findViewById(R.id.etNama);
        etPassword= (EditText)findViewById(R.id.etPassword);
        btnProcessReg = (Button) findViewById(R.id.btnProcessReg);

        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainRegister.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnProcessReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processReg(etEmail.getText().toString(),etNama.getText().toString(),etPassword.getText().toString());
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

    void processReg(String vemail,String vnama, String vpassword) {
        ServerAPI urlapi = new ServerAPI();
        String URL = urlapi.BASE_URL;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        if (!isEmailValid(etEmail.getText().toString())) {
            AlertDialog.Builder msg = new AlertDialog.Builder(MainRegister.this);

            msg.setMessage("Email tidak Valid").setNegativeButton("Retry",null)
                    .create().show();
            return;
        }
        api.register(vemail,vnama,vpassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getString("status").toString().equals("1")){
                        if (json.getString("result").toString().equals("1")){
                            AlertDialog.Builder msg = new AlertDialog.Builder(MainRegister.this);

                            msg.setMessage("Register berhasil")
                                    .setPositiveButton("Ok",null).create().show();
                            Intent intent = new Intent(MainRegister.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            AlertDialog.Builder msg = new AlertDialog.Builder(MainRegister.this);

                            msg.setMessage("Simpan Gagal").setNegativeButton("Retry",null)
                                    .create().show();
                            etNama.setText("");
                            etPassword.setText("");
                            etEmail.setText("");
                            etNama.setFocusable(true);
                        }
                    }else{
                        AlertDialog.Builder msg = new AlertDialog.Builder(MainRegister.this);

                        msg.setMessage("User sudah terdaftar").setNegativeButton("Retry",null)
                                .create().show();
                    }
                }catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("Info Register", "onFailure: Register Gagal"+t.toString());
            }
        });
    }

}