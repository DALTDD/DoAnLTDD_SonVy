package com.example.ungdungbansach;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Model.APIService;
import Model.DataService;
import Model.Login;
import Model.StringRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView txtDangKyTKLogin, txtQuenMKLogin;
    EditText edtTenDangNhapLogin, edtMatKhauLogin;
    CheckBox cbHienThiMK;
    ConstraintLayout constraintLayout;
    ImageView img;
    Animation top, bottom;
    Button btnDangNhapLogin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        linkWidget();
        //
        if(getIntent().getStringExtra("TenDN") != null){
            edtTenDangNhapLogin.setText(getIntent().getStringExtra("TenDN"));
        }
        if(getIntent().getStringExtra("MatKhau") != null){
            edtMatKhauLogin.setText(getIntent().getStringExtra("MatKhau"));
        }
        int mode = getIntent().getIntExtra("Mode",-1);
        //
        cbHienThiMK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtMatKhauLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edtMatKhauLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        //
        txtDangKyTKLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //
        txtQuenMKLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        //
        top = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        //
        img.setAnimation(top);
        constraintLayout.setAnimation(bottom);
        //
        btnDangNhapLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTenDangNhapLogin.getText().toString().trim().isEmpty()) {
                    edtTenDangNhapLogin.setError("Tên đăng nhập không được để trống");
                    return;
                }
                if (edtMatKhauLogin.getText().toString().trim().isEmpty()) {
                    edtMatKhauLogin.setError("Mật khẩu không được để trống");
                    return;
                }
                //

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setContentView(R.layout.progress_load_data);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //
                DataService dataService = APIService.getService();
                Call<Login> callBack = dataService.loginAccount(edtTenDangNhapLogin.getText().toString().trim(), edtMatKhauLogin.getText().toString().trim());
                callBack.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            Login login = response.body();
                            Log.d("SV", "LoginActivity - Status login: " + login.getStatus());
                            if (login.getStatus().equals("1")) {
                                savePreferences("TaiKhoan", edtTenDangNhapLogin.getText().toString().trim());
                                savePreferences("MatKhau", edtMatKhauLogin.getText().toString().trim());
                                savePreferences("MaKH",login.getKhachHang().getMaKH());
                                saveLogin();
                                if(mode == 0) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else {
                                    finish();
                                }
                            } else if (login.getStatus().equals("0")) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                                SpannableString title = new SpannableString("Thông báo");
                                title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                                alert.setTitle(title);
                                alert.setMessage("Sai tên đăng nhập hoặc mật khẩu");
                                alert.setNegativeButton("OK", null);
                                alert.show().getWindow().setLayout(700, 500);
                            } else {
                                Toast.makeText(LoginActivity.this, "Lỗi File Connect", Toast.LENGTH_SHORT).show();
                                Log.d("SV", "LoginActivity - Lỗi File Connect + " + login.getStatus() + login.getKhachHang().getEmail());
                            }
                        } else {
                            Log.d("SV", "LoginActivity - fail!");
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        Log.d("SV", "LoginActivity - onFailure : " + t.getMessage());
                    }
                });
            }
        });
    }


    private void linkWidget() {
        constraintLayout = findViewById(R.id.formDN);
        img = findViewById(R.id.imgHinhLogin);
        edtTenDangNhapLogin = findViewById(R.id.edtTenDangNhapLogin);
        edtMatKhauLogin = findViewById(R.id.edtMatKhauLogin);
        cbHienThiMK = findViewById(R.id.cbHienThiMK);
        txtDangKyTKLogin = findViewById(R.id.txtDangKyTKLogin);
        txtQuenMKLogin = findViewById(R.id.txtQuenMKLogin);
        btnDangNhapLogin = findViewById(R.id.btnDangNhapLogin);
    }


    public void savePreferences(String key, String value) {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.putString(key, value);
        edCaches.commit();
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
}