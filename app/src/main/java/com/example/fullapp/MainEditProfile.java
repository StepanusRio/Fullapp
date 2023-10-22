package com.example.fullapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fullapp.API.GetAPI;
import com.example.fullapp.API.ServerAPI;
import com.example.fullapp.API.UpdateAPI;
import com.example.fullapp.Model.DataPelanggan;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainEditProfile extends AppCompatActivity {
    EditText etedNama,etedAlamat,etedKota,etedProvinsi,etedTelp,etedKodepos;
    String email;
    Button btneditProcess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_edit_profile);

        etedNama = (EditText) findViewById(R.id.etedNama);
        etedAlamat = (EditText) findViewById(R.id.etedAddress);
        etedKota = (EditText) findViewById(R.id.etedKota);
        etedProvinsi = (EditText) findViewById(R.id.etedProvinsi);
        etedTelp = (EditText) findViewById(R.id.etedTelp);
        etedKodepos = (EditText) findViewById(R.id.etedKodepos);

        btneditProcess = (Button) findViewById(R.id.btneditProcess);

        email = getIntent().getStringExtra("email");
        getProfile(email);

        btneditProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataPelanggan data = new DataPelanggan();
                data.setAlamat(etedAlamat.getText().toString());
                data.setNama(etedNama.getText().toString());
                data.setKota(etedKota.getText().toString());
                data.setProvinsi(etedProvinsi.getText().toString());
                data.setTelp(etedTelp.getText().toString());
                data.setKodepos(etedKodepos.getText().toString());
                data.setEmail(email);
                updateProfile(data);
            }
        });
    }

    void updateProfile (DataPelanggan data){
        ServerAPI urlApi = new ServerAPI();
        String URL = urlApi.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UpdateAPI api = retrofit.create(UpdateAPI.class);
        Call<ResponseBody> call = api.updateProfile(
                data.getNama(),
                data.getAlamat(),
                data.getKota(),
                data.getProvinsi(),
                data.getTelp(),
                data.getKodepos(),
                data.getEmail()
                );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Toast.makeText(MainEditProfile.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                    getProfile(data.getEmail());
                }catch (JSONException |IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AlertDialog.Builder msg = new AlertDialog.Builder(MainEditProfile.this);
                msg.setMessage("Simpan gagal: " + t.toString())
                        .setNegativeButton("retry",null)
                        .create().show();
            }
        });
    }

    void getProfile (String vmail){
        ServerAPI urlApi = new ServerAPI();
        String URL = urlApi.BASE_URL;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetAPI api = retrofit.create(GetAPI.class);
        api.getProfile(vmail).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.getString("result").toString().equals("1")){
                        etedNama.setText(json.getJSONObject("data").getString("nama"));
                        etedAlamat.setText(json.getJSONObject("data").getString("alamat"));
                        etedKota.setText(json.getJSONObject("data").getString("kota"));
                        etedProvinsi.setText(json.getJSONObject("data").getString("provinsi"));
                        etedTelp.setText(json.getJSONObject("data").getString("telp"));
                        etedKodepos.setText(json.getJSONObject("data").getString("kodepos"));
                        Log.i("Info Profile", json.getJSONObject("data").getString("nama"));
                    }else{

                    }
                }catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}