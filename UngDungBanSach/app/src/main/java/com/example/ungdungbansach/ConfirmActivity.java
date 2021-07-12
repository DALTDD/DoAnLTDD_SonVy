package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import Adapter.XacNhanRecyclerView;
import Model.APIService;
import Model.Cart;
import Model.CartItem;
import Model.CustomCallbackKhachHang;
import Model.DataService;
import Model.DetailOrder;
import Model.KhachHang;
import Model.Login;
import Model.Order;
import Model.StringRequest;
import Model.TTGiaoHang;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmActivity extends AppCompatActivity {
    View includeStateConfirm;
    StateProgressBar stateProgressBar;
    TextView txtThanhTienXacNhan, txtPhiVCXacNhan, txtTongTienXacNhan;
    RecyclerView rcXacNhan;
    Button btnXacNhan;
    Cart cart;
    XacNhanRecyclerView xacNhanRecyclerView;
    double tongTien = 0, phiVC = 0, thanhTien = 0;
    String maDiaChi = "", maPTVC = "", maPTTT = "", maKH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Xác nhận đơn hàng");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //
        Bundle bundle = getIntent().getBundleExtra("TTThanhToan");
        if (bundle == null) {
            //Dialog loi
            Log.d("SV", "ConfirmActivity - Nhận Bundle thất bại");
            showDialogError("Đã có lỗi xảy ra");
            Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        } else {
            maDiaChi = bundle.getString("maDiaChi");
            maPTVC = bundle.getString("maPTVC");
            maPTTT = bundle.getString("maPTTT");
            phiVC = bundle.getDouble("phiGH", 0);
            Log.d("SV", "ConfirmActivity - Nhận Bundle thành công: maDiaChi: " + maDiaChi + " - maPTVC: " + maPTVC + " - maPTTT: " + maPTTT + " - phiGH: " + phiVC);
        }
        //
        if (loadPreferences("MaKH") == null) {
            Intent intent = new Intent(ConfirmActivity.this, LoginActivity.class);
            intent.putExtra("Mode", 0);
            startActivity(intent);
            finish();
        } else {
            maKH = loadPreferences("MaKH");
        }

        //
        linkWidget();
        //
        String[] descriptionData = {"Địa chỉ", "Thanh toán", "Xác nhận"};
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        //Tinh gia tri don hang
        if (cart.getInstanceCart().size() > 0) {
            for (CartItem cartItem : cart.getInstanceCart()) {
                thanhTien += cartItem.getThanhTien();
            }
            Locale locale = new Locale("vi", "VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            txtThanhTienXacNhan.setText(numberFormat.format(thanhTien));
            tongTien = thanhTien + phiVC;
            txtPhiVCXacNhan.setText(numberFormat.format(phiVC));
            txtTongTienXacNhan.setText(numberFormat.format(tongTien));
        } else {
            showDialogError("Không có sản phẩm nào trong giỏ hàng của bạn");
            Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        }
        //
        xacNhanRecyclerView = new XacNhanRecyclerView(ConfirmActivity.this, cart.getInstanceCart());
        rcXacNhan.setAdapter(xacNhanRecyclerView);
        //
        rcXacNhan.setLayoutManager(new LinearLayoutManager(ConfirmActivity.this, RecyclerView.VERTICAL, false));
        rcXacNhan.setHasFixedSize(true);
        //
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maPTVC.isEmpty() || maDiaChi.isEmpty() || maPTTT.isEmpty()) {
                    //Dialog thong bao loi
                    showDialogError("Hệ thống chưa ghi nhận đầy đủ thông tin");
                    Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                //
                ProgressDialog progressDialog = new ProgressDialog(ConfirmActivity.this);
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.progress_load_data);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //
                if (cart.getInstanceCart().size() > 0) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    ArrayList<DetailOrder> detailOrderArrayList = new ArrayList<>();
                    for (CartItem cartItem : cart.getInstanceCart()) {
                        DetailOrder detailOrder = new DetailOrder(cartItem.getMaSach(), cartItem.getSoLuong(), cartItem.getGiaKhuyenMai());
                        detailOrderArrayList.add(detailOrder);
                    }
                    DataService dataService = APIService.getService();
                    Call<TTGiaoHang> callBack = dataService.getTTGHByMaDiaChi(maDiaChi);
                    callBack.enqueue(new Callback<TTGiaoHang>() {
                        @Override
                        public void onResponse(Call<TTGiaoHang> call, Response<TTGiaoHang> response) {
                            if (response.isSuccessful()) {
                                if (response.body() == null) {
                                    //
                                } else {
                                    TTGiaoHang ttGiaoHang = response.body();
                                    String hoTen = "", sdt = "", diaChi = "";
                                    hoTen = ttGiaoHang.getHoTen();
                                    sdt = ttGiaoHang.getSdt();
                                    String[] thanhPho = ttGiaoHang.getThanhPho().split(";");
                                    String[] quan = ttGiaoHang.getQuan().split(";");
                                    diaChi = ttGiaoHang.getDiaChiNha() + ", " + ttGiaoHang.getPhuong() + ", ";
                                    if (quan.length >= 2) {
                                        diaChi += quan[1] + ", ";
                                    }
                                    if (thanhPho.length >= 2) {
                                        diaChi += thanhPho[1];
                                    }
                                    Order order = new Order(maKH, dateFormat.format(Calendar.getInstance().getTime()), tongTien, maPTTT, maPTVC, hoTen, sdt, diaChi, detailOrderArrayList);
                                    Log.d("SV", "ConfirmActivity - Info order: " + maKH + " - " + dateFormat.format(Calendar.getInstance().getTime()) + " - " + tongTien + " - " + maPTTT + " - " + maPTVC + " - " + hoTen + " - " + sdt + " - " + diaChi + " - " + detailOrderArrayList.size());
                                    //
                                    DataService dataService = APIService.getService();
                                    Call<StringRequest> callBack = dataService.datHang(order);
                                    callBack.enqueue(new Callback<StringRequest>() {
                                        @Override
                                        public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                                            if (response.isSuccessful()) {
                                                StringRequest stringRequest = response.body();
                                                if (stringRequest.getStatus().equals("1")) {
                                                    progressDialog.dismiss();
                                                    cart.getInstanceCart().clear();
                                                    Intent intent = new Intent(ConfirmActivity.this, OrderSuccessActivity.class);
                                                    intent.putExtra("MaDonHang", stringRequest.getResultCode());
                                                    startActivity(intent);
                                                    finish();
                                                    Log.d("SV", "ConfirmActivity - Call API đặt hàng thành công, status: " + stringRequest.getStatus() + " - Mã đơn hàng: " + stringRequest.getResultCode());
                                                } else {
                                                    Toast.makeText(ConfirmActivity.this, "Đặt hàng không thành công. Vui lòng xem lại.", Toast.LENGTH_SHORT).show();
                                                    Log.d("SV", "ConfirmActivity - Call API đặt hàng không thành công, status: " + stringRequest.getStatus());
                                                }
                                            } else {
                                                Toast.makeText(ConfirmActivity.this, "Đặt hàng không thành công. Vui lòng xem lại.", Toast.LENGTH_SHORT).show();
                                                Log.d("SV", "ConfirmActivity - Call API đặt hàng fail");
                                            }
                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<StringRequest> call, Throwable t) {
                                            Log.d("SV", "ConfirmActivity - Call API đặt hàng onFailure : " + t.getMessage());
                                            progressDialog.dismiss();
                                        }
                                    });
                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<TTGiaoHang> call, Throwable t) {

                        }
                    });
                } else {
                    //Dialog thong bao  gio hang trong
                    showDialogError("Không có sản phẩm nào trong giỏ hàng của bạn");
                    Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void linkWidget() {
        includeStateConfirm = findViewById(R.id.includeStateConfirm);
        stateProgressBar = includeStateConfirm.findViewById(R.id.stateProgressbar);
        rcXacNhan = findViewById(R.id.rcXacNhan);
        txtThanhTienXacNhan = findViewById(R.id.txtThanhTienXacNhan);
        txtTongTienXacNhan = findViewById(R.id.txtTongTienXacNhan);
        txtPhiVCXacNhan = findViewById(R.id.txtPhiVCXacNhan);
        btnXacNhan = findViewById(R.id.btnXacNhan);
    }


    public void datHang(Order order) {
        DataService dataService = APIService.getService();
        Call<StringRequest> callBack = dataService.datHang(order);
        callBack.enqueue(new Callback<StringRequest>() {
            @Override
            public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                if (response.isSuccessful()) {
                    StringRequest stringRequest = response.body();
                    if (stringRequest.getStatus().equals("1")) {
                        cart.getInstanceCart().clear();
                        Intent intent = new Intent(ConfirmActivity.this, OrderSuccessActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
                        finish();
                        Log.d("SV", "ConfirmActivity - Call API đặt hàng thành công, status: " + stringRequest.getStatus() + " - Mã đơn hàng: " + stringRequest.getResultCode());
                    } else {
                        Toast.makeText(ConfirmActivity.this, "Đặt hàng không thành công. Vui lòng xem lại.", Toast.LENGTH_SHORT).show();
                        Log.d("SV", "ConfirmActivity - Call API đặt hàng không thành công, status: " + stringRequest.getStatus());
                    }
                } else {
                    Toast.makeText(ConfirmActivity.this, "Đặt hàng không thành công. Vui lòng xem lại.", Toast.LENGTH_SHORT).show();
                    Log.d("SV", "ConfirmActivity - Call API đặt hàng fail");
                }

            }

            @Override
            public void onFailure(Call<StringRequest> call, Throwable t) {
                Log.d("SV", "ConfirmActivity - Call API đặt hàng onFailure : " + t.getMessage());
            }
        });
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialogError(String error) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(ConfirmActivity.this);
        aBuilder.setTitle("Thông báo");
        aBuilder.setMessage(error);
        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = aBuilder.create();
        dialog.show();
    }

}