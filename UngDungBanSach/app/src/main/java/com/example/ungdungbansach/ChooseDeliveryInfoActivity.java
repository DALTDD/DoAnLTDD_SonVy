package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import java.util.List;

import Adapter.DiaChiAdapter;
import Model.APIService;
import Model.DataService;
import Model.DiaChi;
import Model.OnCallBackDiaChi;
import Model.OnCallBackPTTT;
import Model.StringRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseDeliveryInfoActivity extends AppCompatActivity {
    RecyclerView rcChooseDC;
    ProgressBar pgBarChooseDC;
    View includeState;
    StateProgressBar stateProgressBar;
    ArrayList<DiaChi> diaChiArrayList;
    DiaChiAdapter diaChiAdapter;
    TextView txtThemDiaChi;
    Button btnXacNhanDiaChi;
    String maKH = "", maDiaChi = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_delivery_info);
        //
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thông tin giao hàng");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //
        if (loadPreferences("MaKH") == null) {
            Intent intent = new Intent(ChooseDeliveryInfoActivity.this, LoginActivity.class);
            intent.putExtra("Mode", 0);
            startActivity(intent);
            finish();
        } else {
            maKH = loadPreferences("MaKH");
        }
        //
        linkWidget();
        //
        String[] descriptionData = {"Địa chỉ", "Thanh toán","Xác nhận"};
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        //
        diaChiArrayList = new ArrayList<>();
        //
        diaChiAdapter = new DiaChiAdapter(ChooseDeliveryInfoActivity.this, diaChiArrayList, new OnCallBackDiaChi() {
            @Override
            public void onRadioButtonClick(int position) {
                DiaChi diaChi = diaChiArrayList.get(position);
                if(diaChi == null){
                    return;
                }
                maDiaChi = diaChi.getMaDiaChi();
                String diaChiMerger = "";
                String[] thanhPho = diaChi.getThanhPho().split(";");
                String[] quan = diaChi.getQuan().split(";");
                diaChiMerger = diaChi.getDiaChiNha() + ", " + diaChi.getPhuong() + ", ";
                if(quan.length >= 2){
                    diaChiMerger += quan[1] + ", ";
                }
                if(thanhPho.length >= 2){
                    diaChiMerger += thanhPho[1];
                }
                Toast.makeText(ChooseDeliveryInfoActivity.this, diaChiMerger, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDelete(int position) {
                xoaDiaChi(diaChiArrayList.get(position).getMaDiaChi());
            }

            @Override
            public void onUpdate(int position) {
                if(!maKH.isEmpty()){
                    Intent intent = new Intent(ChooseDeliveryInfoActivity.this,EditAddressActivity.class);
                    intent.putExtra("MaDiaChi",diaChiArrayList.get(position).getMaDiaChi());
                    intent.putExtra("MaKH",maKH);
                    intent.putExtra("Mode",0);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
                }
            }
        });
        rcChooseDC.setAdapter(diaChiAdapter);
        rcChooseDC.setLayoutManager(new LinearLayoutManager(ChooseDeliveryInfoActivity.this,RecyclerView.VERTICAL,false));
        rcChooseDC.setHasFixedSize(true);
        //
        loadDiaChi(maKH);
        //
        txtThemDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!maKH.isEmpty()){
                    Intent intent = new Intent(ChooseDeliveryInfoActivity.this,DeliveryInfoActivity.class);
                    intent.putExtra("MaKH",maKH);
                    intent.putExtra("Mode",0);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
                }
            }
        });
        //
        btnXacNhanDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maDiaChi.isEmpty()){
                    Log.d("SV","ChooseDeliveryAc - Chưa nhận đc mã địa chỉ");
                    return;
                }
                Log.d("SV","ChooseDeliveryAc - Put Intent maDiaChi: " + maDiaChi);
                Intent intent = new Intent(ChooseDeliveryInfoActivity.this, PaymentActivity.class);
                intent.putExtra("maDiaChi",maDiaChi);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
            }
        });
    }

    public void linkWidget() {
        includeState = findViewById(R.id.includeState);
        stateProgressBar = includeState.findViewById(R.id.stateProgressbar);
        rcChooseDC = findViewById(R.id.rcChooseDC);
        pgBarChooseDC = findViewById(R.id.pgBarChooseDC);
        txtThemDiaChi = findViewById(R.id.txtThemDiaChi);
        btnXacNhanDiaChi = findViewById(R.id.btnXacNhanDiaChi);
    }

    public void loadDiaChi(String maKH){
        pgBarChooseDC.setVisibility(View.VISIBLE);
        DataService dataService = APIService.getService();
        Call<List<DiaChi>> callBack = dataService.getAddressByMaKH(maKH);
        callBack.enqueue(new Callback<List<DiaChi>>() {
            @Override
            public void onResponse(Call<List<DiaChi>> call, Response<List<DiaChi>> response) {
                if(response.isSuccessful()){
                    ArrayList<DiaChi> arrayList = (ArrayList<DiaChi>) response.body();
                    diaChiArrayList.clear();
                    diaChiArrayList.addAll(arrayList);
                    diaChiAdapter.notifyDataSetChanged();
                    pgBarChooseDC.setVisibility(View.GONE);
                }
                else{
                    Log.d("SV","ChooseDelivetyInfoActivity - Load địa chỉ Not Success");
                }
            }

            @Override
            public void onFailure(Call<List<DiaChi>> call, Throwable t) {
                Log.d("SV","ChooseDeliveyyInfoActivity - Load địa chỉ onFailure: " + t.getMessage());
            }
        });
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
                            Log.d("SV", "ChooseDeliveyyInfoActivity - Xoá địa chỉ thành công");
                        }
                    }
                    else{
                        Log.d("SV", "ChooseDeliveyyInfoActivity - Xoá địa chỉ không thành công");
                    }
                }
            }

            @Override
            public void onFailure(Call<StringRequest> call, Throwable t) {
                Log.d("SV", "ChooseDeliveyyInfoActivity - Xoá địa chỉ onFailure: " + t.getMessage());
                if(t.getMessage().equals("timeout")){
                    xoaDiaChi(maDiaChi);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
    public String loadPreferences(String key) {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getString(key, null);
    }
}