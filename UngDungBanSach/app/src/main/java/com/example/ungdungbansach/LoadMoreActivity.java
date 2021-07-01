package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter.RecyclerViewLoadMore;
import Adapter.SachAdapterRecyclerView;
import Model.APIService;
import Model.DataService;
import Model.OnCallBack;
import Model.Sach;
import Model.SachLite;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadMoreActivity extends AppCompatActivity {
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<SachLite> arrayList = new ArrayList<>();
    SachAdapterRecyclerView sachAdapterRecyclerView;
    int page = 0, limit = 6;
    boolean checkLast = false, checkLoad = false;
    int mode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_more);
        //
        if(getIntent().getIntExtra("Mode",0) == 0){
            finish();
        }
        else{
            mode = getIntent().getIntExtra("Mode",0);
        }
        //
        linkWidget();
        //
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            switch (mode){
                case 1:{
                    getSupportActionBar().setTitle("Sách khuyến mãi nhiều");
                    break;
                }
                case 2:{
                    getSupportActionBar().setTitle("Sách mới");
                    break;
                }
                case 3:{
                    getSupportActionBar().setTitle("Top sách bán chạy");
                    break;
                }
                case 4:{
                    getSupportActionBar().setTitle("Tất cả sách");
                    break;
                }
            }

        }
        //
        sachAdapterRecyclerView =  new SachAdapterRecyclerView(LoadMoreActivity.this, arrayList, new OnCallBack() {
            @Override
            public void onItemRecyclerViewClick(int position) {
                Log.d("KRT","LoadMoreActivity - " + position + " - " + arrayList.get(position).getTenSach() + " Ma sach : " + arrayList.get(position).getMaSach());
                Intent intent = new Intent(LoadMoreActivity.this, DetailActivity.class);
                intent.putExtra("maSach", arrayList.get(position).getMaSach());
                startActivity(intent);
                overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(sachAdapterRecyclerView);
        //
        switch (mode){
            case 1:{
                loadSachKhuyenMai();
                break;
            }
            case 2:{
                loadSachMoi();
                break;
            }
            case 3:{
                loadSachBanChay();
                break;
            }
            case  4:{
                loadTatCaSach();
                break;
            }
        }
        //
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    if(checkLast == false && checkLoad == true){
                        progressBar.setVisibility(View.VISIBLE);
                        switch (mode){
                            case 1:{
                                loadSachKhuyenMai();
                                break;
                            }
                            case 2:{
                                loadSachMoi();
                                break;
                            }
                            case 3:{
                                loadSachBanChay();
                                break;
                            }
                            case 4:{
                                loadTatCaSach();
                                break;
                            }
                        }
                    }
                }
            }
        });
    }


    public void loadSachKhuyenMai(){
        checkLoad = false;
        DataService dataService = APIService.getService();
        Call<List<SachLite>> callBack = dataService.getSachKMPaging(page,limit);
        callBack.enqueue(new Callback<List<SachLite>>() {
            @Override
            public void onResponse(Call<List<SachLite>> call, Response<List<SachLite>> response) {
                if(response.isSuccessful()){
                    ArrayList<SachLite> sachArrayList = (ArrayList<SachLite>) response.body();
                    if(sachArrayList != null){
                        Log.d("KRT","LoadMoreActivity - Sách KM, Page: " + page + ", Size: " + sachArrayList.size());
                        if(sachArrayList.size() == 0){
                            checkLast = true;
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            arrayList.addAll(sachArrayList);
                            sachAdapterRecyclerView.notifyDataSetChanged();
                            page++;
                        }
                    }
                }
                else{
                    Log.d("KRT","LoadMoreActivity - Sách KM, Not Success");
                }
                checkLoad = true;
            }

            @Override
            public void onFailure(Call<List<SachLite>> call, Throwable t) {
                Log.d("KRT","LoadMoreActivity - Sách KM, onFailure: " + t.getMessage());
            }
        });
    }
    public void loadSachMoi(){
        checkLoad = false;
        DataService dataService = APIService.getService();
        Call<List<SachLite>> callBack = dataService.getSachMoiPaging(page,limit);
        callBack.enqueue(new Callback<List<SachLite>>() {
            @Override
            public void onResponse(Call<List<SachLite>> call, Response<List<SachLite>> response) {
                if(response.isSuccessful()){
                    ArrayList<SachLite> sachArrayList = (ArrayList<SachLite>) response.body();
                    if(sachArrayList != null){
                        Log.d("KRT","LoadMoreActivity - Sách mới, Page: " + page + ", Size: " + sachArrayList.size());
                        if(sachArrayList.size() == 0){
                            checkLast = true;
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            arrayList.addAll(sachArrayList);
                            sachAdapterRecyclerView.notifyDataSetChanged();
                            page++;
                        }
                    }
                }
                else{
                    Log.d("KRT","LoadMoreActivity - Sách mới, Not Success");
                }
                checkLoad = true;
            }

            @Override
            public void onFailure(Call<List<SachLite>> call, Throwable t) {
                Log.d("KRT","LoadMoreActivity - Sách mới, onFailure: " + t.getMessage());
            }
        });
    }
    public void loadSachBanChay(){
        checkLoad = false;
        DataService dataService = APIService.getService();
        Call<List<SachLite>> callBack = dataService.getSachBanChayPaging(page,limit);
        callBack.enqueue(new Callback<List<SachLite>>() {
            @Override
            public void onResponse(Call<List<SachLite>> call, Response<List<SachLite>> response) {
                if(response.isSuccessful()){
                    ArrayList<SachLite> sachArrayList = (ArrayList<SachLite>) response.body();
                    if(sachArrayList != null){
                        Log.d("KRT","LoadMoreActivity - Sách bán chạy, Page: " + page + ", Size: " + sachArrayList.size());
                        if(sachArrayList.size() == 0){
                            checkLast = true;
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            arrayList.addAll(sachArrayList);
                            sachAdapterRecyclerView.notifyDataSetChanged();
                            page++;
                        }
                    }
                }
                else{
                    Log.d("KRT","LoadMoreActivity - Sách bán chạy, Not Success");
                }
                checkLoad = true;
            }

            @Override
            public void onFailure(Call<List<SachLite>> call, Throwable t) {
                Log.d("KRT","LoadMoreActivity - Sách bán chạy, onFailure: " + t.getMessage());
            }
        });
    }
    public void loadTatCaSach(){
        checkLoad = false;
        DataService dataService = APIService.getService();
        Call<List<SachLite>> callBack = dataService.getSachAllPaging(page,limit);
        callBack.enqueue(new Callback<List<SachLite>>() {
            @Override
            public void onResponse(Call<List<SachLite>> call, Response<List<SachLite>> response) {
                if(response.isSuccessful()){
                    ArrayList<SachLite> sachArrayList = (ArrayList<SachLite>) response.body();
                    if(sachArrayList != null){
                        Log.d("KRT","LoadMoreActivity - Tất cả sách, Page: " + page + ", Size: " + sachArrayList.size());
                        if(sachArrayList.size() == 0){
                            checkLast = true;
                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            arrayList.addAll(sachArrayList);
                            sachAdapterRecyclerView.notifyDataSetChanged();
                            page++;
                        }
                    }
                }
                else{
                    Log.d("KRT","LoadMoreActivity - Tất cả sách, Not Success");
                }
                checkLoad = true;
            }

            @Override
            public void onFailure(Call<List<SachLite>> call, Throwable t) {
                Log.d("KRT","LoadMoreActivity - Tất cả sách, onFailure: " + t.getMessage());
            }
        });
    }
    public void linkWidget(){
        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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