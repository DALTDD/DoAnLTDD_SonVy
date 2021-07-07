package com.example.ungdungbansach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import Model.APIService;
import Model.DataService;
import Model.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //clearPreferences();
        //
        if (loadPreferences("TaiKhoan") != null && loadPreferences("MatKhau") != null) {
            String taiKhoan = loadPreferences("TaiKhoan");
            String matKhau = loadPreferences("MatKhau");
            //Kiem tra
            DataService dataService = APIService.getService();
            Call<Login> callBack = dataService.loginAccount(taiKhoan, matKhau);
            callBack.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.isSuccessful()) {
                        Login login = response.body();
                        Log.d("KRT", "WelcomeActivity - Login Preferences Status: " + login.getStatus() + " ,MaKH: " + login.getKhachHang().getMaKH());
                        if (login.getStatus().equals("1")) {
                            saveLogin();
                            savePreferences("MaKH", login.getKhachHang().getMaKH());
                        } else if (login.getStatus().equals("0")) {
                            clearPreferences();
                        } else {
                            Log.d("KRT", "WelcomeActivity - Login Preferences Status: " + login.getStatus() + " Loi file connect");
                        }
                    }
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.d("KRT", "WelcomeActivity - Login Preferences onFailure: " + t.getMessage());
                }
            });
        } else {
            clearPreferences();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }, 2000);
    }

    public String loadPreferences(String key) {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getString(key, null);
    }

    public void clearPreferences() {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.clear();
        edCaches.commit();
    }

    public void saveLogin() {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.putBoolean("Login", true);
        edCaches.commit();
    }

    public Boolean checkLogin() {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getBoolean("Login", false);
    }

    public void savePreferences(String key, String value) {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.putString(key, value);
        edCaches.commit();
    }
}