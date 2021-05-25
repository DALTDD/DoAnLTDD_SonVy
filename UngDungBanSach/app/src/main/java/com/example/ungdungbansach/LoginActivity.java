package com.example.ungdungbansach;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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

import Model.APIService;
import Model.DataService;
import Model.StringRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView txtDangKyTKLogin,txtQuenMKLogin;
    EditText edtTenDangNhapLogin, edtMatKhauLogin;
    CheckBox cbHienThiMK;
    ConstraintLayout constraintLayout;
    ImageView img;
    Animation top, bottom;
    Button btnDangNhapLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        linkWidget();
        //
        cbHienThiMK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edtMatKhauLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
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
        top = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top);
        //
        img.setAnimation(top);
        constraintLayout.setAnimation(bottom);
        //
        btnDangNhapLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtTenDangNhapLogin.getText().toString().trim().isEmpty()) {
                    edtTenDangNhapLogin.setError("Tên đăng nhập không được để trống");
                    return;
                }
                if(edtMatKhauLogin.getText().toString().trim().isEmpty()) {
                    edtMatKhauLogin.setError("Mật khẩu không được để trống");
                    return;
                }

                DataService dataService = APIService.getService();
                Call<StringRequest> callback = dataService.login(edtTenDangNhapLogin.getText().toString().trim(),edtMatKhauLogin.getText().toString().trim());
                callback.enqueue(new Callback<StringRequest>() {
                    @Override
                    public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                        if(response.isSuccessful())
                        {
                            StringRequest stringRequest = response.body();
                            if(stringRequest.getStatus().equals("ok"))
                            {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
//                                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
//                                SpannableString title = new SpannableString("Thông báo");
//                                title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,title.length(),0);
//                                alert.setTitle(title);
//                                alert.setMessage("Sai tên đăng nhập hoặc mật khẩu");
//                                alert.setNegativeButton("OK", null);
//                                alert.show().getWindow().setLayout(700,500);
                            }
                        }else{
                            Log.d("KRT",""+response.body().getStatus() + response.body().getResultCode());
                        }
                    }

                    @Override
                    public void onFailure(Call<StringRequest> call, Throwable t) {

                    }
                });
            }
        });
    }
    private void linkWidget(){
        constraintLayout = findViewById(R.id.formDN);
        img = findViewById(R.id.imgHinhLogin);
        edtTenDangNhapLogin = findViewById(R.id.edtTenDangNhapLogin);
        edtMatKhauLogin = findViewById(R.id.edtMatKhauLogin);
        cbHienThiMK = findViewById(R.id.cbHienThiMK);
        txtDangKyTKLogin = findViewById(R.id.txtDangKyTKLogin);
        txtQuenMKLogin = findViewById(R.id.txtQuenMKLogin);
        btnDangNhapLogin = findViewById(R.id.btnDangNhapLogin);
    }
}