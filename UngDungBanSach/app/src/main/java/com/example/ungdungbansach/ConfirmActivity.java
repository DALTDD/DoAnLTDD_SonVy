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
    String hoTen = "", sdt = "", diaChi = "", maPTTT = "";

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
            Log.d("KRT", "ConfirmActivity - Nhận Bundle thất bại");
            showDialogError("Đã có lỗi xảy ra");
            Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        } else {
            hoTen = bundle.getString("hoTen");
            sdt = bundle.getString("sdt");
            diaChi = bundle.getString("diaChi");
            maPTTT = bundle.getString("maPTTT");
            Log.d("KRT", "ConfirmActivity - Nhận Bundle thành công: " + hoTen + " - " + sdt + " - " + diaChi + " - " + maPTTT);
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
            if (thanhTien >= 250000) {
                phiVC = 0;
            } else {
                phiVC = 25000;
            }
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
                if (hoTen.isEmpty() || sdt.isEmpty() || diaChi.isEmpty() || maPTTT.isEmpty()) {
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
                    KhachHang infoKH = new KhachHang();
                    getInfoKhachHang(new CustomCallbackKhachHang() {
                        @Override
                        public void onSucess(String maKH) {
                            Order order = new Order(maKH, dateFormat.format(Calendar.getInstance().getTime()), tongTien, maPTTT, hoTen, sdt, diaChi, detailOrderArrayList);
                            Log.d("KRT","ConfirmActivity - Info order: " + maKH + " - " + dateFormat.format(Calendar.getInstance().getTime()) + " - " + tongTien + " - " + maPTTT + " - " + hoTen + " - " + sdt + " - " + diaChi + " - " + detailOrderArrayList.size());
                            //datHang(order);
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
                                            intent.putExtra("MaHoaDon",stringRequest.getResultCode());
                                            startActivity(intent);
                                            finish();
                                            progressDialog.dismiss();
                                            Log.d("KRT", "ConfirmActivity - Call API đặt hàng thành công, status: " + stringRequest.getStatus() + " - Mã đơn hàng: " + stringRequest.getResultCode());
                                        } else {
                                            Toast.makeText(ConfirmActivity.this, "Đặt hàng không thành công. Vui lòng xem lại.", Toast.LENGTH_SHORT).show();
                                            Log.d("KRT", "ConfirmActivity - Call API đặt hàng không thành công, status: " + stringRequest.getStatus());
                                        }
                                    } else {
                                        Toast.makeText(ConfirmActivity.this, "Đặt hàng không thành công. Vui lòng xem lại.", Toast.LENGTH_SHORT).show();
                                        Log.d("KRT", "ConfirmActivity - Call API đặt hàng fail");
                                    }
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<StringRequest> call, Throwable t) {
                                    Log.d("KRT", "ConfirmActivity - Call API đặt hàng onFailure : " + t.getMessage());
                                    progressDialog.dismiss();
                                }
                            });
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
                if(progressDialog.isShowing()){
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

    public void getInfoKhachHang(CustomCallbackKhachHang customCallbackKhachHang) {
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
                        if (login.getStatus().equals("1")) {
                            customCallbackKhachHang.onSucess(login.getKhachHang().getMaKH());
                            Log.d("KRT", "ConfirmActivity - Login Preferences đã đăng nhập, Status: " + login.getStatus());
                        } else if (login.getStatus().equals("0")) {
                            clearPreferences();
                            Intent intent = new Intent(ConfirmActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            Log.d("KRT", "ConfirmActivity- Login Preferences sai tk, mk, Status: " + login.getStatus());
                        } else {
                            Log.d("KRT", "ConfirmActivity - Login Preferences Status: " + login.getStatus() + " Loi file connect");
                        }
                    }
                }
                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.d("KRT", "ConfirmActivity - Login Preferences onFailure: " + t.getMessage());
                }
            });
        } else {
            Intent intent = new Intent(ConfirmActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
                        finish();
                        Log.d("KRT", "ConfirmActivity - Call API đặt hàng thành công, status: " + stringRequest.getStatus() + " - Mã đơn hàng: " + stringRequest.getResultCode());
                    } else {
                        Toast.makeText(ConfirmActivity.this, "Đặt hàng không thành công. Vui lòng xem lại.", Toast.LENGTH_SHORT).show();
                        Log.d("KRT", "ConfirmActivity - Call API đặt hàng không thành công, status: " + stringRequest.getStatus());
                    }
                } else {
                    Toast.makeText(ConfirmActivity.this, "Đặt hàng không thành công. Vui lòng xem lại.", Toast.LENGTH_SHORT).show();
                    Log.d("KRT", "ConfirmActivity - Call API đặt hàng fail");
                }

            }

            @Override
            public void onFailure(Call<StringRequest> call, Throwable t) {
                Log.d("KRT", "ConfirmActivity - Call API đặt hàng onFailure : " + t.getMessage());
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