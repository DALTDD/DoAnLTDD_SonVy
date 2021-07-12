package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter.DonHangAdapter;
import Model.APIService;
import Model.DataService;
import Model.DonHang;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    ListView lvOrderHistory;
    ArrayList<DonHang> arrLstDonHang;
    DonHangAdapter donHangAdapter;
    String maKH = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lịch sử mua hàng");
        }
        //
        if (loadPreferences("MaKH") == null) {
            Intent intent = new Intent(OrderHistoryActivity.this, LoginActivity.class);
            intent.putExtra("Mode", 0);
            startActivity(intent);
            finish();
        } else {
            maKH = loadPreferences("MaKH");
        }
        //
        lvOrderHistory = findViewById(R.id.lvOrderHistory);
        //
        arrLstDonHang = new ArrayList<>();
        donHangAdapter = new DonHangAdapter(arrLstDonHang,OrderHistoryActivity.this);
        lvOrderHistory.setAdapter(donHangAdapter);
        //
        loadDataDonHang();
        //
        lvOrderHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OrderHistoryActivity.this, DetailOrderActivity.class);
                intent.putExtra("MaDonHang",arrLstDonHang.get(position).getMaDonHang());
                intent.putExtra("Mode",1);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
            }
        });

    }
    public void loadDataDonHang(){
        //
        ProgressDialog progressDialog = new ProgressDialog(OrderHistoryActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_load_data);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //
        DataService dataService = APIService.getService();
        Call<List<DonHang>> callBack = dataService.getAllOrderByMaKH(maKH);
        callBack.enqueue(new Callback<List<DonHang>>() {
            @Override
            public void onResponse(Call<List<DonHang>> call, Response<List<DonHang>> response) {
                if(response.isSuccessful()){
                    ArrayList<DonHang> arrayList = (ArrayList<DonHang>) response.body();
                    if(arrayList != null){
                        arrLstDonHang.clear();
                        arrLstDonHang.addAll(arrayList);
                        donHangAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<DonHang>> call, Throwable t) {
                Log.d("SV", "DetailActivity - Call API loadDataDonHang onFailure: " + t.getMessage());
                if(t.getMessage().equals("timeout")){
                    loadDataDonHang();
                }
            }
        });
    }
    public String loadPreferences(String key) {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getString(key, null);
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

}