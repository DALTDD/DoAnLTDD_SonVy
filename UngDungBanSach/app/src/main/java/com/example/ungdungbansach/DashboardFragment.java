package com.example.ungdungbansach;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ungdungbansach.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.DanhMucAdapter;
import Adapter.TheLoaiAdapter;
import Model.APIService;
import Model.DanhMuc;
import Model.DataService;
import Model.TheLoai;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment {

    ListView lvDanhMuc,lvTheLoai;
    DanhMucAdapter danhMucAdapter;
    TheLoaiAdapter theLoaiAdapter;

    ArrayList<TheLoai> theLoaiArrayList;
    ArrayList<DanhMuc> danhMucArrayList;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        lvDanhMuc = view.findViewById(R.id.lvDanhMuc);
        lvTheLoai = view.findViewById(R.id.lvTheLoai);
        getDataDanhMuc();

        //Danh muc
        danhMucArrayList = new ArrayList<>();
        danhMucAdapter = new DanhMucAdapter(getContext(),danhMucArrayList);
        lvDanhMuc.setAdapter(danhMucAdapter);
//       //The loai
        theLoaiArrayList = new ArrayList<>();
        theLoaiAdapter = new TheLoaiAdapter(getContext(),theLoaiArrayList);
        lvTheLoai.setAdapter(theLoaiAdapter);


        lvDanhMuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext(), ""+danhMucArrayList.get(position).getMaDanhMuc(), Toast.LENGTH_SHORT).show();
                getTLByIdDanhMuc(danhMucArrayList.get(position).getMaDanhMuc());
                //

                //
                danhMucAdapter.setSelectedId(position);
                danhMucAdapter.notifyDataSetChanged();//
            }
        });
        lvTheLoai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayoutMain,CategoriesFragment.newInstance(theLoaiArrayList.get(position).getMaLoai(), theLoaiArrayList.get(position).getTenTheLoai()),"Categories");
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Log.d("KRT","DashboardFragment - ItemClickListview the loai " + theLoaiArrayList.get(position).getMaLoai());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    private void getDataDanhMuc(){
        DataService dataService = APIService.getService();
        Call<List<DanhMuc>> callback = dataService.getAllDanhMuc();
        callback.enqueue(new Callback<List<DanhMuc>>() {
            @Override
            public void onResponse(Call<List<DanhMuc>> call, Response<List<DanhMuc>> response) {
                if(response.isSuccessful()){
                    ArrayList<DanhMuc> arrayList = (ArrayList<DanhMuc>) response.body();
                    // danhMucArrayList = arrayList;
                    danhMucArrayList.clear();
                    danhMucArrayList.addAll(arrayList);
                    danhMucAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<DanhMuc>> call, Throwable t) {
                Log.d("KRT","DashboardFragment - GetData Danh mục onFailure: " + t.getMessage());
            }
        });
    }
    private void getTLByIdDanhMuc(String maDanhMuc){
        DataService dataService = APIService.getService();
        Call<List<TheLoai>> callback = dataService.getTheLoaiByDanhMuc(maDanhMuc);
        callback.enqueue(new Callback<List<TheLoai>>() {
            @Override
            public void onResponse(Call<List<TheLoai>> call, Response<List<TheLoai>> response) {
                if(response.isSuccessful()){
                    ArrayList<TheLoai> arrayList = (ArrayList<TheLoai>) response.body();
                    //theLoaiArrayList = arrayList;
                    theLoaiArrayList.clear();
                    TheLoai tlAllSach = new TheLoai(String.valueOf(-1),String.valueOf(-1),"Tất cả sách");
                    theLoaiArrayList.add(0,tlAllSach);
                    theLoaiArrayList.addAll(arrayList);
                    theLoaiAdapter.notifyDataSetChanged();
                }
                else {
                    Log.d("KRT","DashboardFragment - GetTheLoaiByDanhMuc Not Success");
                }
            }

            @Override
            public void onFailure(Call<List<TheLoai>> call, Throwable t) {
                Log.d("KRT","DashboardFragment - GetTheLoaiByDanhMuc onFailure: " + t.getMessage());
            }
        });
    }

}