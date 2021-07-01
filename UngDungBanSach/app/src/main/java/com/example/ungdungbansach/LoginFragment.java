package com.example.ungdungbansach;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    TextView txtDangKyTKLogin, txtQuenMKLogin;
    EditText edtTenDangNhapLogin, edtMatKhauLogin;
    CheckBox cbHienThiMK;
    Button btnDangNhapLogin;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        edtTenDangNhapLogin = view.findViewById(R.id.edtTenDangNhapLogin);
        edtMatKhauLogin = view.findViewById(R.id.edtMatKhauLogin);
        cbHienThiMK = view.findViewById(R.id.cbHienThiMK);
        txtDangKyTKLogin = view.findViewById(R.id.txtDangKyTKLogin);
        txtQuenMKLogin = view.findViewById(R.id.txtQuenMKLogin);
        btnDangNhapLogin = view.findViewById(R.id.btnDangNhapLogin);
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
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        //
        txtQuenMKLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        //
        btnDangNhapLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTenDangNhapLogin.getText().toString().trim().isEmpty()) {
                    edtTenDangNhapLogin.setError("Tên đăng nhập không được để trống");
                    edtTenDangNhapLogin.requestFocus();
                    return;
                }
                if (edtMatKhauLogin.getText().toString().trim().isEmpty()) {
                    edtMatKhauLogin.setError("Mật khẩu không được để trống");
                    edtMatKhauLogin.requestFocus();
                    return;
                }
                //

                ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                        if (response.isSuccessful()) {
                            Login login = response.body();
                            Log.d("KRT", "LoginFragment - Status login: " + login.getStatus());
                            if (login.getStatus().equals("1")) {
                                savePreferences("TaiKhoan", edtTenDangNhapLogin.getText().toString().trim());
                                savePreferences("MatKhau", edtMatKhauLogin.getText().toString().trim());
                                savePreferences("MaKH",login.getKhachHang().getMaKH());
                                saveLogin();
                                Fragment fragment = new AccountFragment();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frameLayoutMain,fragment);
                                fragmentTransaction.commit();
                            } else if (login.getStatus().equals("0")) {
                                progressDialog.dismiss();
                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                SpannableString title = new SpannableString("Thông báo");
                                title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                                alert.setTitle(title);
                                alert.setMessage("Sai tên đăng nhập hoặc mật khẩu");
                                alert.setNegativeButton("OK", null);
                                alert.show().getWindow().setLayout(700, 500);
                            } else {
                                Toast.makeText(getContext(), "LoginFragment - Lỗi File Connect", Toast.LENGTH_SHORT).show();
                                Log.d("KRT", "LoginFragment - Lỗi File Connect + " + login.getStatus() + login.getKhachHang().getEmail());
                            }
                        } else {
                            Log.d("KRT", "LoginFragment - fail!");
                        }
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        Log.d("KRT", "LoginFragment - onFailure : " + t.getMessage());
                    }
                });
            }
        });
    }
    public void saveLogin() {
        SharedPreferences p = getActivity().getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.putBoolean("Login", true);
        edCaches.commit();
    }
    public void savePreferences(String key, String value) {
        SharedPreferences p = getActivity().getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.putString(key, value);
        edCaches.commit();
    }
}