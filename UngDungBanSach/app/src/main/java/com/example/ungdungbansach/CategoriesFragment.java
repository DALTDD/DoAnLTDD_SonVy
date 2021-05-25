package com.example.ungdungbansach;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
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

    public CategoriesFragment() {
        // Required empty public constructor
    }


    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        //
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtTenTheLoaiCate = view.findViewById(R.id.txtTenTheLoaiCategories);
        rcCategories = view.findViewById(R.id.rcCategories);
        //Sach By TheLoai
        Bundle bundle = getArguments();
        if(bundle != null){
            txtTenTheLoaiCate.setText(bundle.getString("name"));
        }
        sachArrayList = getSachByTheLoai(bundle.getString("id"));
        //Log.d("Vy",""+sachArrayList.size());
        sachAdapterRecyclerView = new SachAdapterRecyclerView(getContext(), sachArrayList, new OnCallBack() {
            @Override
            public void onItemRecyclerViewClick(int position) {
                Toast.makeText(getContext(), ""+sachArrayList.get(position).getTenSach(), Toast.LENGTH_SHORT).show();
            }
        });
        rcCategories.setLayoutManager(new GridLayoutManager(getContext(),2));
        rcCategories.setAdapter(sachAdapterRecyclerView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }


    private ArrayList<Sach> getSachByTheLoai(String maTheLoai){
        sachArrayList = new ArrayList<>();
        DataService dataService = APIService.getService();
        Call<List<Sach>> callback = dataService.getSachByTheLoai(maTheLoai);
        callback.enqueue(new Callback<List<Sach>>() {
            @Override
            public void onResponse(Call<List<Sach>> call, Response<List<Sach>> response) {
                if(response.isSuccessful()){
                    ArrayList<Sach> arrayList = (ArrayList<Sach>) response.body();
                    //sachArrayList = arrayList;
                    //arrayList.clear();
                    sachArrayList.addAll(arrayList);
                    sachAdapterRecyclerView.notifyDataSetChanged();
                }else{
                    Log.d("Vy","Goi sach by the loai that bai");
                }
            }
            @Override
            public void onFailure(Call<List<Sach>> call, Throwable t) {
                Log.d("Vy","Goi sach by the loai that bai"+t.getMessage());
            }
        });
        return sachArrayList;
    }
}