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
import Adapter.PTVCAdapter;
import Model.APIService;
import Model.Cart;
import Model.CartItem;
import Model.DataService;
import Model.OnCallBackPTTT;
import Model.OnCallBackPTVC;
import Model.OnCallBackPhiVC;
import Model.PhiVanChuyen;
import Model.PhuongThucTT;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    View includeStatePayment;
    StateProgressBar stateProgressBar;
    ProgressBar pgBarPayment;
    RecyclerView rcPTTT,rcPTVC;
    TextView txtThanhTienThanhToan, txtPhiVCThanhToan, txtTongTienThanhToan;
    ArrayList<PhuongThucTT> arrLstPTTT;
    PTTTAdapter ptttAdapter;
    PhiVanChuyen phiVanChuyen;
    PTVCAdapter ptvcAdapter;
    Cart cart;
    Button btnThanhToanGiaoHang;
    String maDiaChi = "", maPTTT = "", maPTVC = "";
    double phiGH = 0;
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
        if(getIntent().getStringExtra("maDiaChi") == null){
           // showDialogError("Đã có lỗi xảy ra");
            Log.d("SV","PaymentActivity - Null, MaDiaChi: " + getIntent().getStringExtra(maDiaChi));
           //Dialog thong bao bundle null
           //this.finish();
            finish();
            return;
        }
        else {
            maDiaChi = getIntent().getStringExtra("maDiaChi");
            Log.d("SV","PaymentActivity - Nhận Intent thành công, MaDiaChi: " + maDiaChi);
        }
        //
        linkWidget();
        //
        btnThanhToanGiaoHang.setEnabled(false);
        //
        arrLstPTTT = new ArrayList<>();
        phiVanChuyen = new PhiVanChuyen();
        //

        //
        String[] descriptionData = {"Địa chỉ", "Thanh toán","Xác nhận"};
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        //
        ptttAdapter = new PTTTAdapter(PaymentActivity.this, arrLstPTTT, new OnCallBackPTTT() {
            @Override
            public void onRadioButtonClick(int position) {
                maPTTT = arrLstPTTT.get(position).getMaPTTT();
                Toast.makeText(PaymentActivity.this, arrLstPTTT.get(position).getTenPTTT(), Toast.LENGTH_SHORT).show();
            }
        });
        rcPTTT.setAdapter(ptttAdapter);
        rcPTTT.setLayoutManager(new LinearLayoutManager(PaymentActivity.this,RecyclerView.VERTICAL,false));
        rcPTTT.setHasFixedSize(true);
        loadDataPTTT();
        //
        loadPTVC(new OnCallBackPhiVC() {
            @Override
            public void onSuccess(PhiVanChuyen phiVanChuyen) {
                ptvcAdapter = new PTVCAdapter(phiVanChuyen, PaymentActivity.this, new OnCallBackPTVC() {
                    @Override
                    public void onRadioButtonClick(int position) {
                        maPTVC = phiVanChuyen.getPTVanChuyen().get(position).getMaPTVC();
                        loadGiaTriDonHang(Double.parseDouble(phiVanChuyen.getMienPhiVC()),Double.parseDouble(phiVanChuyen.getPTVanChuyen().get(position).getPhiVC()));
                        Toast.makeText(PaymentActivity.this, phiVanChuyen.getPTVanChuyen().get(position).getTenPTVC(), Toast.LENGTH_SHORT).show();
                    }
                }, tinhThanhTienCart());
                rcPTVC.setAdapter(ptvcAdapter);
            }
        });
        //
        rcPTVC.setLayoutManager(new LinearLayoutManager(PaymentActivity.this,RecyclerView.VERTICAL,false));
        rcPTVC.setHasFixedSize(true);
        //
        btnThanhToanGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maDiaChi.isEmpty() || maPTTT.isEmpty() || maPTVC.isEmpty()){
                    //Dialog thong bao loi
                    showDialogError("Hệ thống chưa ghi được thông tin");
                    return;
                }
                Bundle bundleTT = new Bundle();
                bundleTT.putString("maDiaChi",maDiaChi);
                bundleTT.putString("maPTTT",maPTTT);
                bundleTT.putString("maPTVC",maPTVC);
                bundleTT.putDouble("phiGH",phiGH);
                Intent intent = new Intent(PaymentActivity.this, ConfirmActivity.class);
                intent.putExtra("TTThanhToan",bundleTT);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
    }
    public void linkWidget(){
        pgBarPayment = findViewById(R.id.pgBarPayment);
        includeStatePayment = findViewById(R.id.includeStateConfirm);
        stateProgressBar = includeStatePayment.findViewById(R.id.stateProgressbar);
        rcPTVC = findViewById(R.id.rcPTVC);
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
                    Log.d("SV","PaymentActivity - Call API PTTT size: " + arrayListRespone.size());
                }
                else{
                    Log.d("SV","PaymentActivity - Call API PTTT Not Success");
                }
                pgBarPayment.setVisibility(View.GONE);
                btnThanhToanGiaoHang.setEnabled(true);
            }

            @Override
            public void onFailure(Call<List<PhuongThucTT>> call, Throwable t) {
                Log.d("SV","PaymentActivity - Call API PTTT onFailure : " + t.getMessage());
                if(t.getMessage().equals("timeout")){
                    loadDataPTTT();
                }
            }
        });
    }
    public void loadPTVC(OnCallBackPhiVC onCallBackPhiVC){
        //
        ProgressDialog progressDialog = new ProgressDialog(PaymentActivity.this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_load_data);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //
        DataService dataService = APIService.getService();
        Call<PhiVanChuyen> callBack = dataService.getPTVC();
        callBack.enqueue(new Callback<PhiVanChuyen>() {
            @Override
            public void onResponse(Call<PhiVanChuyen> call, Response<PhiVanChuyen> response) {
                if(response.isSuccessful()){
                    PhiVanChuyen phiVC = response.body();
                    onCallBackPhiVC.onSuccess(phiVC);
                    if(phiVC.getPTVanChuyen() != null){
                        Log.d("SV","PaymentActivity - Call API PTVC size: " + phiVC.getPTVanChuyen().size());
                    }
                    else {
                        Log.d("SV","PaymentActivity - Call API PTVC null");
                    }
                    progressDialog.dismiss();
                }
                else{
                    Log.d("SV","PaymentActivity - Call API PTVC Not Success");
                }
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PhiVanChuyen> call, Throwable t) {
                Log.d("SV","PaymentActivity - Call API PTVC onFailure : " + t.getMessage());
                if(t.getMessage().equals("timeout")){
                }
            }
        });
    }
    public void loadGiaTriDonHang(double phiMienPhi, double phiVC){
        double tongTien = tinhThanhTienCart();
        phiGH = phiVC;
        if(tongTien >= phiMienPhi)
        {
            phiGH = 0;
        }
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        txtThanhTienThanhToan.setText(numberFormat.format(tongTien));
        txtPhiVCThanhToan.setText(numberFormat.format(phiGH));
        txtTongTienThanhToan.setText(numberFormat.format(tongTien+phiGH));
    }
    public double tinhThanhTienCart(){
        double tongTien = 0;
        double phiVC = 25000;
        for (CartItem cartItem : cart.getInstanceCart()){
            tongTien += cartItem.getThanhTien();
        }
        return tongTien;
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