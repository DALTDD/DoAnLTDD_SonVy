package com.example.ungdungbansach;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import Adapter.GioHangAdapterRecyclerView;
import Model.APIService;
import Model.Cart;
import Model.CartItem;
import Model.DataService;
import Model.Login;
import Model.OnCartCallBack;
import Model.StringRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {
    TextView txtGioHangTrong, txtThanhTienGioHang;
    ImageView imgGioHangTrong;
    Button btnThanhToanGioHang;
    RecyclerView rcGioHang;
    //GioHangAdapter gioHangAdapter;
    GioHangAdapterRecyclerView gioHangAdapterRecyclerView;
    Cart cart;

    public CartFragment() {
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        txtGioHangTrong = view.findViewById(R.id.txtGioHangTrong);
        imgGioHangTrong = view.findViewById(R.id.imgGioHangTrong);
        rcGioHang = view.findViewById(R.id.rcGioHang);
        btnThanhToanGioHang = view.findViewById(R.id.btnThanhToanGioHang);
        txtThanhTienGioHang = view.findViewById(R.id.txtThanhTienGioHang);
        //
        if (cart.getInstanceCart().size() == 0) {
            imgGioHangTrong.setVisibility(View.VISIBLE);
            txtGioHangTrong.setVisibility(View.VISIBLE);
            rcGioHang.setVisibility(View.GONE);
            btnThanhToanGioHang.setEnabled(false);
        } else {
            gioHangAdapterRecyclerView = new GioHangAdapterRecyclerView(getContext(), cart.getInstanceCart(), new OnCartCallBack() {
                @Override
                public void OnButtonCartCallBack() {
                    tinhThanhTien();
                }

                @Override
                public void OnButtonDeleteCartCallBack() {
                    tinhThanhTien();
                    if (cart.getInstanceCart().size() == 0) {
                        imgGioHangTrong.setVisibility(View.VISIBLE);
                        txtGioHangTrong.setVisibility(View.VISIBLE);
                        rcGioHang.setVisibility(View.GONE);
                        btnThanhToanGioHang.setEnabled(false);
                        MainActivity.hideNumberIconNav();
                    } else {
                        MainActivity.showNumberIconNav(cart.getInstanceCart().size());
                    }
                }
            });
            rcGioHang.setAdapter(gioHangAdapterRecyclerView);
            rcGioHang.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            rcGioHang.setHasFixedSize(true);
            imgGioHangTrong.setVisibility(View.GONE);
            txtGioHangTrong.setVisibility(View.GONE);
            rcGioHang.setVisibility(View.VISIBLE);
            btnThanhToanGioHang.setEnabled(true);
        }
        tinhThanhTien();
        //
        btnThanhToanGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Kiem tra dang nhap
                if (loadPreferences("TaiKhoan") != null && loadPreferences("MatKhau") != null) {
                    String taiKhoan = loadPreferences("TaiKhoan");
                    String matKhau = loadPreferences("MatKhau");
                    //Kiem tra
                    //
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.progress_load_data);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    //
                    DataService dataService = APIService.getService();
                    Call<Login> callBack = dataService.loginAccount(taiKhoan, matKhau);
                    callBack.enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            if (response.isSuccessful()) {
                                Login login = response.body();
                                if (login.getStatus().equals("1")) {//Da dang nhap
                                    checkDiaChiGH(login.getKhachHang().getMaKH(), login.getKhachHang().getHoTen(), login.getKhachHang().getSdt());
                                    progressDialog.dismiss();
                                } else if (login.getStatus().equals("0")) {//Chua dang nhap
                                    clearPreferences();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    Log.d("KRT", "CartFragment - Login Preferences sai tk, mk, Status: " + login.getStatus());
                                } else {
                                    Log.d("KRT", "CartFragment - Login Preferences Status: " + login.getStatus() + " Loi file connect");
                                }
                            } else {
                                Log.d("KRT", "CartFragment - Login Preferences Not Success");
                            }
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            Log.d("KRT", "CartFragment - Login Preferences onFailure: " + t.getMessage());
                            progressDialog.dismiss();
                        }
                    });
                } else {//Chua dang nhap
                    clearPreferences();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void tinhThanhTien() {
        double tongTien = 0;
        for (CartItem cartItem : cart.getInstanceCart()) {
            tongTien += cartItem.getThanhTien();
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        txtThanhTienGioHang.setText(numberFormat.format(tongTien));
    }

    public String loadPreferences(String key) {
        SharedPreferences p = getActivity().getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getString(key, null);
    }

    public void checkDiaChiGH(String maKH, String hoTen, String sdt) {
        DataService dataService = APIService.getService();
        Call<StringRequest> callBack = dataService.checkDiaChiGH(maKH);
        callBack.enqueue(new Callback<StringRequest>() {
            @Override
            public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                if (response.isSuccessful()) {
                    StringRequest stringRequest = response.body();
                    if (stringRequest.getStatus().equals("1")) {//Da co dia chi giao hang
                        Log.d("KRT", "CartFragment - Kiem tra dia chi giao hang: MaKH: " + maKH + " .Da co dia chi giao hang, Status: " + stringRequest.getStatus());
                        Intent intent = new Intent(getActivity(), ChooseDeliveryInfoActivity.class);
                        intent.putExtra("MaKH", maKH);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
                        //
                    } else if (stringRequest.getStatus().equals("0")) {//Chua co dia chi giao hang
                        Log.d("KRT", "CartFragment - Kiem tra dia chi giao hang: MaKH: " + maKH + " .Chua co dia chi giao hang, Status: " + stringRequest.getStatus());
                        Intent intent = new Intent(getActivity(), DeliveryInfoActivity.class);
                        intent.putExtra("MaKH", maKH);
                        intent.putExtra("Mode", 0);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
                        //
                    } else {
                        Log.d("KRT", "CartFragment - Kiem tra dia chi giao hang: MaKH: " + maKH + " .Loi connect: " + stringRequest.getStatus());
                    }
                } else {
                    Log.d("KRT", "CartFragment - Kiem tra dia chi giao hang: MaKH: " + maKH + " .Loi ket noi");
                }
            }

            @Override
            public void onFailure(Call<StringRequest> call, Throwable t) {
                Log.d("KRT", "CartFragment - Kiem tra dia chi giao hang: MaKH: " + maKH + " .onFailure: " + t.getMessage());
            }
        });
    }

    public void clearPreferences() {
        SharedPreferences p = getActivity().getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.clear();
        edCaches.commit();
    }
}