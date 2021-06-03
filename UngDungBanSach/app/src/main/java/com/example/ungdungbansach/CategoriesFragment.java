package com.example.ungdungbansach;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter.SachAdapterRecyclerView;
import Model.APIService;
import Model.DataService;
import Model.OnCallBack;
import Model.Sach;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoriesFragment extends Fragment {

    TextView txtTenTheLoaiCate;
    RecyclerView rcCategories;
    SachAdapterRecyclerView sachAdapterRecyclerView;
    ArrayList<Sach> sachArrayList;
    String maLoai;
    Bundle bundle;
    AppCompatActivity appCompatActivity;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    public static CategoriesFragment newInstance(String maLoai,String tenLoai) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("maLoai", maLoai);
        bundle.putString("tenLoai",tenLoai);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        bundle = getArguments();
        if (bundle != null) {
            maLoai = bundle.getString("maLoai");
        }
        appCompatActivity = (AppCompatActivity) getActivity();
        if(appCompatActivity.getSupportActionBar() != null){
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setTitle(bundle.getString("tenLoai"));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        txtTenTheLoaiCate = view.findViewById(R.id.txtTenTheLoaiCategories);
        rcCategories = view.findViewById(R.id.rcCategories);
        txtTenTheLoaiCate.setText(bundle.getString("tenLoai"));
        //

        //Sach By TheLoai
        sachArrayList = new ArrayList<>();
        sachAdapterRecyclerView = new SachAdapterRecyclerView(getContext(), sachArrayList, new OnCallBack() {
            @Override
            public void onItemRecyclerViewClick(int position) {
                Intent intent = new Intent(getContext(),DetailActivity.class);
                intent.putExtra("maSach",sachArrayList.get(position).getMaSach());
                startActivity(intent);
            }
        });
        rcCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcCategories.setAdapter(sachAdapterRecyclerView);
        Log.d("Vy", "Ma loai get bundle caegories : " + maLoai);
        getSachByTheLoai(maLoai);
        if(String.valueOf(maLoai) == String.valueOf(-1)){
            getAllSachByTheLoai();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }


    private void getSachByTheLoai(String maLoai) {
        DataService dataService = APIService.getService();
        Call<List<Sach>> callback = dataService.getSachByTheLoai(maLoai);
        callback.enqueue(new Callback<List<Sach>>() {
            @Override
            public void onResponse(Call<List<Sach>> call, Response<List<Sach>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Sach> arrayList = (ArrayList<Sach>) response.body();
                    sachArrayList.clear();
                    sachArrayList.addAll(arrayList);
                    sachAdapterRecyclerView.notifyDataSetChanged();
                    Log.d("Vy", "Load sach theo the loai " + arrayList.size());
                } else {
                    Log.d("Vy", "Goi sach by the loai that bai");
                }
            }

            @Override
            public void onFailure(Call<List<Sach>> call, Throwable t) {
                Log.d("Vy", "Goi sach by the loai that bai" + t.getMessage());
            }
        });
    }
    private void getAllSachByTheLoai(){
        DataService dataService = APIService.getService();
        Call<List<Sach>> callback = dataService.getAllSach();
        callback.enqueue(new Callback<List<Sach>>() {
            @Override
            public void onResponse(Call<List<Sach>> call, Response<List<Sach>> response) {
                if(response.isSuccessful()){
                    ArrayList<Sach> arrayList = (ArrayList<Sach>) response.body();
                    sachArrayList.clear();
                    sachArrayList.addAll(arrayList);
                    sachAdapterRecyclerView.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Sach>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMain, new DashboardFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                appCompatActivity.getSupportActionBar().setHomeButtonEnabled(false);
                appCompatActivity.getSupportActionBar().setTitle("Danh mục");
                Toast.makeText(getContext(), "Click menu", Toast.LENGTH_SHORT).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

}