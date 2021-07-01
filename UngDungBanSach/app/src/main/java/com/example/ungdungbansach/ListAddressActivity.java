package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadPoolExecutor;

import Adapter.ListDCAdapter;
import Model.APIService;
import Model.DataService;
import Model.DiaChi;
import Model.Login;
import Model.OnCallBackListDC;
import Model.StringRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAddressActivity extends AppCompatActivity {
    RecyclerView rcListDC;
    ProgressBar pgBarListDC;
    Button btnThemListDC;
    ArrayList<DiaChi> arrLstDiaChi;
    ListDCAdapter listDCAdapter;
    String maKH = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_address);
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sổ địa chỉ");
        }
        //
        if(getIntent().getStringExtra("MaKH") == null){
            finish();
        }
        else{
            maKH = getIntent().getStringExtra("MaKH");
        }
        //
        linkWidget();
        //
        arrLstDiaChi = new ArrayList<>();
        //
        btnThemListDC.setEnabled(false);
        //
        loadDiaChi(maKH);
        //

        listDCAdapter = new ListDCAdapter(ListAddressActivity.this, arrLstDiaChi, new OnCallBackListDC() {
            @Override
            public void onMenuClick(int position) {
                Toast.makeText(ListAddressActivity.this, position + "", Toast.LENGTH_SHORT).show();
                DiaChi diaChi = arrLstDiaChi.get(position);
                if(diaChi == null){
                    return;
                }
                Dialog dialog = new Dialog(ListAddressActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_list_address);
                //
                TextView txtDatDCMacDinh, txtChinhSua, txtXoaDC, txtHuyDC;
                txtDatDCMacDinh = dialog.findViewById(R.id.txtDatDCMacDinh);
                txtChinhSua = dialog.findViewById(R.id.txtChinhSua);
                txtXoaDC = dialog.findViewById(R.id.txtXoaDC);
                txtHuyDC = dialog.findViewById(R.id.txtHuyDC);
                //
                if(diaChi.getDiaChiMacDinh().equals("1")){
                    txtDatDCMacDinh.setVisibility(View.GONE);
                    txtXoaDC.setVisibility(View.GONE);
                }
                txtChinhSua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(!maKH.isEmpty()){
                            Intent intent = new Intent(ListAddressActivity.this,EditAddressActivity.class);
                            intent.putExtra("MaDiaChi",diaChi.getMaDiaChi());
                            intent.putExtra("MaKH",maKH);
                            intent.putExtra("Mode",1);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
                        }
                    }
                });
                txtHuyDC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                txtXoaDC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        xoaDiaChi(diaChi.getMaDiaChi());
                    }
                });
                txtDatDCMacDinh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        datDiaChiMacDinh(diaChi.getMaKH(),diaChi.getMaDiaChi());
                    }
                });
                //
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });
        rcListDC.setAdapter(listDCAdapter);
        //
        rcListDC.setLayoutManager(new LinearLayoutManager(ListAddressActivity.this,RecyclerView.VERTICAL,false));
        rcListDC.setHasFixedSize(true);
        //
        btnThemListDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!maKH.isEmpty()){
                    Intent intent = new Intent(ListAddressActivity.this,DeliveryInfoActivity.class);
                    intent.putExtra("MaKH",maKH);
                    intent.putExtra("Mode",1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
                }
            }
        });
    }
    public void linkWidget(){
        rcListDC = findViewById(R.id.rcListDC);
        pgBarListDC = findViewById(R.id.pgBarListDC);
        btnThemListDC = findViewById(R.id.btnThemListDC);
    }
    public void load(){
        if (loadPreferences("TaiKhoan") != null && loadPreferences("MatKhau") != null) {
            String taiKhoan = loadPreferences("TaiKhoan");
            String matKhau = loadPreferences("MatKhau");
            //Kiem tra
            DataService dataService = APIService.getService();
            Call<Login> callBack = dataService.loginAccount(taiKhoan,matKhau);
            callBack.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if(response.isSuccessful()){
                        Login login = response.body();
                        Log.d("KRT", "ListAddressAc - Login Preferences Status: "+login.getStatus());
                        if(login.getStatus().equals("1")){
                            maKH = login.getKhachHang().getMaKH();
                            loadDiaChi(login.getKhachHang().getMaKH());
                        }
                        else if(login.getStatus().equals("0")){
                            clearPreferences();
                            Intent intent = new Intent(ListAddressActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Log.d("KRT", "ListAddressAc - Login Preferences Status: "+login.getStatus() + " Loi file connect");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.d("KRT", "ListAddressAc - Login Preferences onFailure: " + t.getMessage());
                }
            });
        }
        else{
            clearPreferences();
            pgBarListDC.setVisibility(View.GONE);
            Intent intent = new Intent(ListAddressActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void loadDiaChi(String maKH){
        pgBarListDC.setVisibility(View.VISIBLE);
        btnThemListDC.setEnabled(false);
        DataService dataService = APIService.getService();
        Call<List<DiaChi>> callBackDiaChi = dataService.getAddressByMaKH(maKH);
        callBackDiaChi.enqueue(new Callback<List<DiaChi>>() {
            @Override
            public void onResponse(Call<List<DiaChi>> call, Response<List<DiaChi>> response) {
                if(response.isSuccessful()){
                    ArrayList<DiaChi> arrayList = (ArrayList<DiaChi>) response.body();
                    arrLstDiaChi.clear();
                    arrLstDiaChi.addAll(arrayList);
                    listDCAdapter.notifyDataSetChanged();
                    pgBarListDC.setVisibility(View.GONE);
                    btnThemListDC.setEnabled(true);
                }
                else{
                    Log.d("KRT", "ListAddressActivity - LoadDiaChi Not Success");
                }
            }

            @Override
            public void onFailure(Call<List<DiaChi>> call, Throwable t) {
                Log.d("KRT", "ListAddressActivity - LoadDiaChi onFailure: " + t.getMessage());
            }
        });
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
    public void xoaDiaChi(String maDiaChi){
        DataService dataService = APIService.getService();
        Call<StringRequest> callBack = dataService.deleteDiaChi(maDiaChi);
        callBack.enqueue(new Callback<StringRequest>() {
            @Override
            public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                if(response.isSuccessful()){
                    StringRequest stringRequest = response.body();
                    if(stringRequest.getStatus().equals("1")){
                        if(!maKH.isEmpty()) {
                            loadDiaChi(maKH);
                            Log.d("KRT", "ListAddressActivity - Xoá địa chỉ thành công");
                        }
                    }
                    else{
                        Log.d("KRT", "ListAddressActivity - Xoá địa chỉ không thành công");
                    }
                }
            }

            @Override
            public void onFailure(Call<StringRequest> call, Throwable t) {
                Log.d("KRT", "ListAddressActivity - Xoá địa chỉ onFailure: " + t.getMessage());
            }
        });
    }
    public void datDiaChiMacDinh(String maKH, String maDiaChi){
        DataService dataService = APIService.getService();
        Call<StringRequest> callBack = dataService.datDiaChiMacDinh(maDiaChi,maKH);
        callBack.enqueue(new Callback<StringRequest>() {
            @Override
            public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                if(response.isSuccessful()){
                    StringRequest stringRequest = response.body();
                    if(stringRequest.getStatus().equals("1")){
                        if(!maKH.isEmpty()){
                            loadDiaChi(maKH);
                            Log.d("KRT", "ListAddressActivity - Đặt địa chỉ mặc định thành công");
                        }
                    }
                    else{
                        Log.d("KRT", "ListAddressActivity - Đặt địa chỉ mặc định không thành công");
                    }
                }
            }

            @Override
            public void onFailure(Call<StringRequest> call, Throwable t) {
                Log.d("KRT", "ListAddressActivity - Đặt địa chỉ mặc định thành công onFailure: " + t.getMessage());
            }
        });
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

}