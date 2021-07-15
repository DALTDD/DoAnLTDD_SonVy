package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import java.text.NumberFormat;
import java.util.Locale;

import Adapter.ChiTietDHAdapter;
import Model.APIService;
import Model.ChiTietDH;
import Model.ChiTietDonHang;
import Model.DataService;
import Model.ResultRequest;
import Model.ThongTinDH;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderActivity extends AppCompatActivity {
    TextView txtMaDH, txtTinhTrangDH, txtNgayMua, txtTongTienDH, txtHoTenNDDH, txtSDTNDDH, txtDiaChiNDDH, txtPTVCDH, txtPTTTDH, txtThanhTienDH, txtPhiVCDH
    ,txtTongTienDH_2;
    ChiTietDHAdapter chiTietDHAdapter;
    RecyclerView lvChiTietOrder;
    Button btnHuyDH;
    String maDH = "";
    int mode = 0;
    boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chi tiết đơn hàng");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //
        linkWidget();
        //
        if(getIntent().getStringExtra("MaDonHang") == null){
            finish();
        }
        else {
            maDH = getIntent().getStringExtra("MaDonHang");
        }
        mode = getIntent().getIntExtra("Mode",0);

        loadChiTietDH(maDH);
        //
        btnHuyDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailOrderActivity.this);
                builder.setTitle("Thông báo!");
                builder.setMessage("Bạn có muốn huỷ đơn hàng?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        dialog.cancel();
                        //
                        ProgressDialog progressDialog = new ProgressDialog(DetailOrderActivity.this);
                        progressDialog.show();
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setContentView(R.layout.progress_load_data);
                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        //
                        DataService dataService = APIService.getService();
                        Call<ResultRequest> callBack = dataService.cancelOrder(maDH);
                        callBack.enqueue(new Callback<ResultRequest>() {
                            @Override
                            public void onResponse(Call<ResultRequest> call, Response<ResultRequest> response) {
                                if(response.isSuccessful()){
                                    ResultRequest resultRequest = response.body();
                                    if(resultRequest != null){
                                        if(resultRequest.getStatus().equals("1")){
                                            check = true;
                                            btnHuyDH.setVisibility(View.GONE);
                                            loadChiTietDH(maDH);
                                            progressDialog.dismiss();
                                        }
                                    }
                                }
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultRequest> call, Throwable t) {
                                Log.d("SV", "DetailActivity - Call API huỷ đơn hàng onFailure: " + t.getMessage());
                            }
                        });
                    }
                });
                builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                //



            }
        });
    }
    public void linkWidget(){
        txtMaDH = findViewById(R.id.txtMaDH);
        txtTinhTrangDH = findViewById(R.id.txtTinhTrangDH);
        txtNgayMua = findViewById(R.id.txtNgayMua);
        txtTongTienDH = findViewById(R.id.txtTongTienDH);
        txtHoTenNDDH = findViewById(R.id.txtHoTenNDDH);
        txtSDTNDDH = findViewById(R.id.txtSDTNDDH);
        txtDiaChiNDDH = findViewById(R.id.txtDiaChiNDDH);
        txtPTVCDH = findViewById(R.id.txtPTVCDH);
        txtPTTTDH = findViewById(R.id.txtPTTTDH);
        txtThanhTienDH = findViewById(R.id.txtThanhTienDH);
        txtPhiVCDH = findViewById(R.id.txtPhiVCDH);
        txtTongTienDH_2 = findViewById(R.id.txtTongTienDH_2);
        lvChiTietOrder = findViewById(R.id.lvChiTietOrder);
        btnHuyDH = findViewById(R.id.btnHuyDH);
    }
    public void loadChiTietDH(String maDH){
        //
        ProgressDialog progressDialog = new ProgressDialog(DetailOrderActivity.this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_load_data);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //
        DataService dataService = APIService.getService();
        Call<ChiTietDonHang> callBack = dataService.getDetailOrderByMaDH(maDH);
        callBack.enqueue(new Callback<ChiTietDonHang>() {
            @Override
            public void onResponse(Call<ChiTietDonHang> call, Response<ChiTietDonHang> response) {
                if(response.isSuccessful()){
                    ChiTietDonHang chiTietDonHang = response.body();
                    if(chiTietDonHang != null){
                        if(chiTietDonHang.getChiTietDH() != null){
                            chiTietDHAdapter = new ChiTietDHAdapter(chiTietDonHang.getChiTietDH() ,DetailOrderActivity.this);
                            lvChiTietOrder.setAdapter(chiTietDHAdapter);
                            lvChiTietOrder.setLayoutManager(new LinearLayoutManager(DetailOrderActivity.this, RecyclerView.VERTICAL, false));
                            lvChiTietOrder.setHasFixedSize(true);
                        }
                        if(chiTietDonHang.getThongTinDH() != null){
                            ThongTinDH thongTinDH = chiTietDonHang.getThongTinDH();
                            Locale locale = new Locale("vi","VN");
                            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                            if(thongTinDH.getTinhTrang().equals("-1")){
                                txtTinhTrangDH.setText("Đơn hàng bị huỷ");
                                txtTinhTrangDH.setBackgroundResource(R.drawable.background_order_cancel);
                                txtTinhTrangDH.setTextColor(Color.parseColor("#A82424"));
                                //
                            }else if( thongTinDH.getTinhTrang().equals("0")){
                                txtTinhTrangDH.setText("Đơn hàng đang xử lý");
                                btnHuyDH.setVisibility(View.VISIBLE);
                                //
                            }else if( thongTinDH.getTinhTrang().equals("1")){
                                txtTinhTrangDH.setText("Đơn hàng đang giao");
                                //
                            }
                            else if( thongTinDH.getTinhTrang().equals("2")){
                                txtTinhTrangDH.setText("Đơn hàng đã nhận");
                                //
                            }
                            txtMaDH.setText("#" + thongTinDH.getMaDonHang());
                            txtNgayMua.setText(thongTinDH.getNgayDat());
                            txtHoTenNDDH.setText(thongTinDH.getHoTen());
                            txtSDTNDDH.setText(thongTinDH.getSdt());
                            txtDiaChiNDDH.setText(thongTinDH.getDiaChi());
                            txtPTVCDH.setText(thongTinDH.getTenPTVC());
                            txtPTTTDH.setText(thongTinDH.getTenPTTT());
                            txtThanhTienDH.setText(numberFormat.format(Double.parseDouble(thongTinDH.getTongTien())));
                            txtTongTienDH.setText(numberFormat.format(Double.parseDouble(thongTinDH.getThanhTien())));
                            txtTongTienDH_2.setText(numberFormat.format(Double.parseDouble(thongTinDH.getThanhTien())));
                            txtPhiVCDH.setText(numberFormat.format(Double.parseDouble(thongTinDH.getThanhTien()) - Double.parseDouble(thongTinDH.getTongTien())));
                        }
                    }
                    progressDialog.dismiss();
                }
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ChiTietDonHang> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("SV","DetailOrderActivity - Call API load chi tiết đơn hàng onFailure:" + t.getMessage());
                if(t.getMessage().equals("timeout")){
                    loadChiTietDH(maDH);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mode == 0){
            Intent intent = new Intent(DetailOrderActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else{
            if(check){
                Intent intent = new Intent(DetailOrderActivity.this, OrderHistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            this.finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                if(mode == 0){
                    Intent intent = new Intent(DetailOrderActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else{
                    if(check){
                        Intent intent = new Intent(DetailOrderActivity.this, OrderHistoryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    this.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}