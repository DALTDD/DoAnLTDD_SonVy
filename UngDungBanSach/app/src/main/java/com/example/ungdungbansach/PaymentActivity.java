package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Adapter.PTTTAdapter;
import Model.APIService;
import Model.Cart;
import Model.CartItem;
import Model.DataService;
import Model.OnCallBackPTTT;
import Model.PhuongThucTT;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    View includeStatePayment;
    StateProgressBar stateProgressBar;
    ProgressBar pgBarPayment;
    RadioButton rdbtnPTGH;
    RecyclerView rcPTTT;
    TextView txtThanhTienThanhToan, txtPhiVCThanhToan, txtTongTienThanhToan;
    ArrayList<PhuongThucTT> arrLstPTTT;
    PTTTAdapter ptttAdapter;
    Cart cart;
    Button btnThanhToanGiaoHang;
    String hoTen = "", sdt = "", diaChi = "", maPTTT = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thông tin thanh toán");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //
        //
        Bundle bundle = getIntent().getBundleExtra("TTGiaoHang");
        if(bundle == null){
            showDialogError("Đã có lỗi xảy ra");
           //Dialog thong bao bundle null
           this.finish();
        }
        else {
            hoTen = bundle.getString("hoTen");
            sdt = bundle.getString("sdt");
            diaChi = bundle.getString("diaChi");
            Log.d("KRT","PaymentActivity - Nhận Bundle thành công: " + hoTen + " - " + sdt + " - " + diaChi);
        }
        //
        linkWidget();
        //
        btnThanhToanGiaoHang.setEnabled(false);
        //
        arrLstPTTT = new ArrayList<>();
        //

        //
        String[] descriptionData = {"Địa chỉ", "Thanh toán","Xác nhận"};
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        //
        rcPTTT.setLayoutManager(new LinearLayoutManager(PaymentActivity.this,RecyclerView.VERTICAL,false));
        rcPTTT.setHasFixedSize(true);
        ptttAdapter = new PTTTAdapter(PaymentActivity.this, arrLstPTTT, new OnCallBackPTTT() {
            @Override
            public void onRadioButtonClick(int position) {
                maPTTT = arrLstPTTT.get(position).getMaPTTT();
                Toast.makeText(PaymentActivity.this, arrLstPTTT.get(position).getTenPTTT(), Toast.LENGTH_SHORT).show();
            }
        });
        rcPTTT.setAdapter(ptttAdapter);
        loadDataPTTT();
        //
        loadGiaTriDonHang();
        //
        btnThanhToanGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hoTen.isEmpty() || sdt.isEmpty() || diaChi.isEmpty() || maPTTT.isEmpty()){
                    //Dialog thong bao loi
                    showDialogError("Hệ thống chưa ghi được thông tin");
                    return;
                }
                Bundle bundleTT = new Bundle();
                bundle.putString("hoTen",hoTen);
                bundle.putString("sdt",sdt);
                bundle.putString("diaChi",diaChi);
                bundle.putString("maPTTT",maPTTT);
                Intent intent = new Intent(PaymentActivity.this, ConfirmActivity.class);
                intent.putExtra("TTThanhToan",bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
    }
    public void linkWidget(){
        pgBarPayment = findViewById(R.id.pgBarPayment);
        includeStatePayment = findViewById(R.id.includeStateConfirm);
        stateProgressBar = includeStatePayment.findViewById(R.id.stateProgressbar);
        rdbtnPTGH = findViewById(R.id.rdbtnPTGH);
        rcPTTT = findViewById(R.id.rcPTTT);
        txtThanhTienThanhToan = findViewById(R.id.txtThanhTienThanhToan);
        txtPhiVCThanhToan = findViewById(R.id.txtPhiVCThanhToan);
        txtTongTienThanhToan = findViewById(R.id.txtTongTienThanhToan);
        btnThanhToanGiaoHang = findViewById(R.id.btnThanhToanGiaoHang);
    }

    public void loadDataPTTT(){
        pgBarPayment.setVisibility(View.VISIBLE);
        DataService dataService = APIService.getService();
        Call<List<PhuongThucTT>> callback = dataService.getDataPTTT();
        callback.enqueue(new Callback<List<PhuongThucTT>>() {
            @Override
            public void onResponse(Call<List<PhuongThucTT>> call, Response<List<PhuongThucTT>> response) {
                if(response.isSuccessful()){
                    ArrayList<PhuongThucTT> arrayListRespone = (ArrayList<PhuongThucTT>) response.body();
                    arrLstPTTT.clear();
                    arrLstPTTT.addAll(arrayListRespone);
                    ptttAdapter.notifyDataSetChanged();
                    Log.d("KRT","PaymentActivity - Call API PTT size: " + arrayListRespone.size());
                }
                else{
                    Log.d("KRT","PaymentActivity - Call API PTT fail!");
                }
                pgBarPayment.setVisibility(View.GONE);
                btnThanhToanGiaoHang.setEnabled(true);
            }

            @Override
            public void onFailure(Call<List<PhuongThucTT>> call, Throwable t) {
                Log.d("KRT","PaymentActivity - Call API PTT onFailure : " + t.getMessage());
            }
        });
    }
    public void loadGiaTriDonHang(){
        double tongTien = 0;
        double phiVC = 25000;
        for (CartItem cartItem : cart.getInstanceCart()){
            tongTien += cartItem.getThanhTien();
        }
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        txtThanhTienThanhToan.setText(numberFormat.format(tongTien));
        if(tongTien >= 250000){
            phiVC = 0;
            rdbtnPTGH.setText("Giao hàng tiêu chuẩn: 0đ");
        }
        txtPhiVCThanhToan.setText(numberFormat.format(phiVC));
        txtTongTienThanhToan.setText(numberFormat.format(tongTien+phiVC));
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
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(PaymentActivity.this);
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