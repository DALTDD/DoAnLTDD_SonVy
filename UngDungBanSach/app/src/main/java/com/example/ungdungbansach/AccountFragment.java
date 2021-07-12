package com.example.ungdungbansach;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.ungdungbansach.R;

import java.security.Signature;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Model.APIService;
import Model.DataService;
import Model.DetailOrder;
import Model.KhachHang;
import Model.Login;
import Model.Order;
import Model.StringRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {
    Button btnThongTinCNAccount, btnDiaChiGHAccount, btnQuanLyDHAccount, btnDangXuat, btnDoiMatKhauAccount;
    TextView txtTenDNAccount;
    String maKH = "";

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        if (loadPreferences("MaKH") == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("Mode", 0);
            startActivity(intent);
            getActivity().finish();
        } else {
            maKH = loadPreferences("MaKH");
        }
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        btnThongTinCNAccount = view.findViewById(R.id.btnThongTinCNAccount);
        btnDiaChiGHAccount = view.findViewById(R.id.btnDiaChiGHAccount);
        btnQuanLyDHAccount = view.findViewById(R.id.btnQuanLyDHAccount);
        btnDangXuat = view.findViewById(R.id.btnDangXuat);
        txtTenDNAccount = view.findViewById(R.id.txtTenDNAccount);
        btnDoiMatKhauAccount = view.findViewById(R.id.btnDoiMatKhauAccount);
        //
        getTTKHByMaKH(maKH);
        //Thống tin cá nhân
        btnThongTinCNAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InfoUserActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
            }
        });
        //
        //Đổi mk
        btnDoiMatKhauAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
            }
        });
        //
        btnDiaChiGHAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListAddressActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
            }
        });
        //
        btnQuanLyDHAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
            }
        });
        //
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPreferences();
                Fragment fragment = new LoginFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
                fragmentTransaction.commit();
            }
        });

    }

    public void clearPreferences() {
        SharedPreferences p = getActivity().getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.clear();
        edCaches.commit();
    }

    public void savePreferences(String key, String value) {
        SharedPreferences p = getActivity().getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.putString(key, value);
        edCaches.commit();
    }

    public String loadPreferences(String key) {
        SharedPreferences p = getActivity().getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getString(key, null);
    }


    public void getTTKHByMaKH(String MaKH) {
        //
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_load_data);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DataService dataService = APIService.getService();
        //
        Call<KhachHang> callback = dataService.getTTKHByMaKH(MaKH);
        callback.enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                if (response.isSuccessful()) {
                    KhachHang khachHang = response.body();
                    if (khachHang != null) {
                        if (khachHang.getHoTen() != null) {
                            txtTenDNAccount.setText(khachHang.getHoTen());
                        } else {
                            txtTenDNAccount.setText(khachHang.getTaiKhoan());
                        }
                    }
                    progressDialog.dismiss();
                    Log.d("SV", "AccountFragment - getTTKHByMaKH- MaKH:" + khachHang.getMaKH());
                } else {
                    Log.d("SV", "AccountFragment - getTTKHByMaKH Not Success");
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<KhachHang> call, Throwable t) {
                Log.d("SV", "AccountFragment - getTTKHByMaKH onFailure " + t.getMessage());
                if(t.getMessage().equals("timeout")){
                    getTTKHByMaKH(maKH);
                }
            }
        });
    }

//    public void loadInfo() {
//        if (loadPreferences("TaiKhoan") != null && loadPreferences("MatKhau") != null && checkLogin() == true) {
//            String taiKhoan = loadPreferences("TaiKhoan");
//            String matKhau = loadPreferences("MatKhau");
//            //Kiem tra
//            //
//            ProgressDialog progressDialog = new ProgressDialog(getContext());
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//            progressDialog.setContentView(R.layout.progress_load_data);
//            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            //
//            DataService dataService = APIService.getService();
//            Call<Login> callBack = dataService.loginAccount(taiKhoan, matKhau);
//            callBack.enqueue(new Callback<Login>() {
//                @Override
//                public void onResponse(Call<Login> call, Response<Login> response) {
//                    if (response.isSuccessful()) {
//                        Login login = response.body();
//                        Log.d("SV", "AccountFragment - Login Preferences Status: " + login.getStatus());
//                        if (login.getStatus().equals("1")) {//Da dang nhap
//                            if (login.getKhachHang().getHoTen() != null) {
//                                txtTenDNAccount.setText(login.getKhachHang().getHoTen());
//                            } else {
//                                txtTenDNAccount.setText(login.getKhachHang().getTaiKhoan());
//                            }
//                            maKH = login.getKhachHang().getMaKH();
//                            btnQuanLyDHAccount.setEnabled(true);
//                            btnDiaChiGHAccount.setEnabled(true);
//                            btnThongTinCNAccount.setEnabled(true);
//                            btnDoiMatKhauAccount.setEnabled(true);
//                        } else if (login.getStatus().equals("0")) {//Da dang nhap sai tk, mk
//                            clearPreferences();
//                            Intent intent = new Intent(getActivity(), LoginActivity.class);
//                            intent.putExtra("Mode", 0);
//                            startActivity(intent);
//                            getActivity().finish();
//                        } else {
//                            Log.d("SV", "AccountFrag - Login Preferences Status: " + login.getStatus() + " Loi file connect");
//                        }
//                    }
//                    progressDialog.dismiss();
//                }
//
//                @Override
//                public void onFailure(Call<Login> call, Throwable t) {
//                    progressDialog.dismiss();
//                    Log.d("SV", "AccountFrag - Login Preferences onFailure: " + t.getMessage());
//                }
//            });
//        } else {//Chua dang nhap
//            Log.d("KRT", "AccountFragment - Chưa đăng nhập");
//            clearPreferences();
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            intent.putExtra("Mode", 0);
//            startActivity(intent);
//            getActivity().finish();
//        }
//    }

    public Boolean checkLogin() {
        SharedPreferences p = getActivity().getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getBoolean("Login", false);
    }

}