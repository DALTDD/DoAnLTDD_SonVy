  package com.example.ungdungbansach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.SachAdapterRecyclerView;
import Adapter.SachAdapterRecyclerViewHorizontal;
import Model.APIService;
import Model.CustomCallbackQC;
import Model.DataService;
import Model.OnCallBack;
import Model.OnCallBackHome;
import Model.QuangCao;
import Model.Sach;
import Model.SachLite;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    CarouselView carouselQuangCao;
    ArrayList<QuangCao> arrLstQuangCao;
    NestedScrollView nestedScrollView;
    RecyclerView rcSachAll,rcSachKM, rcSachMoi, rcSachBanChay;
    SachAdapterRecyclerView sachAdapterRecyclerView;
    ArrayList<SachLite> arrLstSachAll, arrLstSachMoi, arrLstSachKM, arrLstSachBanChay;
    RelativeLayout relativeLayout;
    EditText searchView,edtSearchView2;
    TextView txtXemThemKM, txtXemThemSM,txtXemThemBC, txtXemThemTC;
    SachAdapterRecyclerViewHorizontal sachMoiAdapter, sachKMAdapter, sachBanChayAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        arrLstSachAll = new ArrayList<>();
        arrLstSachKM = new ArrayList<>();
        arrLstSachMoi = new ArrayList<>();
        arrLstSachBanChay = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        //Link widget
        carouselQuangCao = view.findViewById(R.id.carouselQuangCao);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        relativeLayout = view.findViewById(R.id.relative);
        rcSachAll = view.findViewById(R.id.recyclerViewSachHome);
        txtXemThemKM = view.findViewById(R.id.txtXemThemKM);
        txtXemThemSM = view.findViewById(R.id.txtXemThemSM);
        txtXemThemBC = view.findViewById(R.id.txtXemThemBC);
        txtXemThemTC = view.findViewById(R.id.txtXemThemTC);
        rcSachKM = view.findViewById(R.id.rcSachKM);
        rcSachMoi = view.findViewById(R.id.rcSachMoi);
        rcSachBanChay = view.findViewById(R.id.rcSachBanChay);
        //search
        searchView = view.findViewById(R.id.searchView);
        edtSearchView2 = view.findViewById(R.id.edtSearchView2);
        //
        arrLstQuangCao = new ArrayList<>();
        //
        sachKMAdapter = new SachAdapterRecyclerViewHorizontal(getContext(), arrLstSachKM, new OnCallBack() {
            @Override
            public void onItemRecyclerViewClick(int position) {
                Log.d("SV","HomeFragment - Sách khuyến mãi,  " + position + " - " + arrLstSachKM.get(position).getTenSach() + " Ma sach : " + arrLstSachKM.get(position).getMaSach());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("maSach", arrLstSachKM.get(position).getMaSach());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
        //
        sachMoiAdapter = new SachAdapterRecyclerViewHorizontal(getContext(), arrLstSachMoi, new OnCallBack() {
            @Override
            public void onItemRecyclerViewClick(int position) {
                Log.d("SV","HomeFragment - Sách mới,  " + position + " - " + arrLstSachMoi.get(position).getTenSach() + " Ma sach : " + arrLstSachMoi.get(position).getMaSach());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("maSach", arrLstSachMoi.get(position).getMaSach());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
        //
        sachBanChayAdapter = new SachAdapterRecyclerViewHorizontal(getContext(), arrLstSachBanChay, new OnCallBack() {
            @Override
            public void onItemRecyclerViewClick(int position) {
                Log.d("SV","HomeFragment - Sách bán chạy,  " + position + " - " + arrLstSachBanChay.get(position).getTenSach() + " Ma sach : " + arrLstSachBanChay.get(position).getMaSach());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("maSach", arrLstSachBanChay.get(position).getMaSach());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
        //
        sachAdapterRecyclerView = new SachAdapterRecyclerView(getContext(), arrLstSachAll, new OnCallBack() {
            @Override
            public void onItemRecyclerViewClick(int position) {
                Log.d("SV","HomeFragment - Tất cả sách,  " + position + " - " + arrLstSachAll.get(position).getTenSach() + " Ma sach : " + arrLstSachAll.get(position).getMaSach());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("maSach", arrLstSachAll.get(position).getMaSach());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
        //
        rcSachKM.setAdapter(sachKMAdapter);
        rcSachMoi.setAdapter(sachMoiAdapter);
        rcSachBanChay.setAdapter(sachBanChayAdapter);
        rcSachAll.setAdapter(sachAdapterRecyclerView);
        //
        rcSachKM.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rcSachKM.setHasFixedSize(true);
        rcSachMoi.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rcSachMoi.setHasFixedSize(true);
        rcSachBanChay.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rcSachBanChay.setHasFixedSize(true);
        rcSachAll.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_load_data);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final int[] check = {0};
        loadDataSachHorizontal(new OnCallBackHome() {
            @Override
            public void onCallBackLoaded() {
                check[0]++;
                if(check[0] >= 3){
                    progressDialog.dismiss();
                }
            }
        });

        getAllQuangCao(new CustomCallbackQC() {
            @Override
            public void onSucess(ArrayList<QuangCao> arrayList) {
                arrLstQuangCao = arrayList;
                if (arrLstQuangCao.size() > 0) {
                    carouselQuangCao.setViewListener(new ViewListener() {
                        @Override
                        public View setViewForPosition(int position) {
                            View customView = getLayoutInflater().inflate(R.layout.item_carousel_quangcao, null);
                            //set view attributes here
                            ImageView imgBannerQCBlur = customView.findViewById(R.id.imgBannerQCBlur);
                            ImageView imgBannerQC = customView.findViewById(R.id.imgBannerQC);
                            Picasso.with(getContext())
                                    .load(arrLstQuangCao.get(position).getAnhQC())
                                    .placeholder(R.mipmap.ic_launcher)
                                    .into(imgBannerQC);
                            Picasso.with(getContext())
                                    .load(arrLstQuangCao.get(position).getAnhQC())
                                    .transform(new BlurTransformation(getContext(), 180, 1))
                                    .into(imgBannerQCBlur);
                            return customView;
                        }
                    });
                    carouselQuangCao.setPageCount(arrLstQuangCao.size());
                }
            }

            @Override
            public void onFailure(String t) {

            }
        });
        //
        //sự kiện click edt search
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        //
        edtSearchView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        //

        //recyclerViewSachHome.setHasFixedSize(true);
        //
        //getAllSach();
        //
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= 150) {
                    relativeLayout.setVisibility(View.VISIBLE);
                } else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });
        //
        txtXemThemKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoadMoreActivity.class);
                intent.putExtra("Mode",1);//San pham khuyen mai
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
        //
        txtXemThemSM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoadMoreActivity.class);
                intent.putExtra("Mode",2);//San pham moi
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
        //
        txtXemThemBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoadMoreActivity.class);
                intent.putExtra("Mode",3);//San pham ban chay
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
        //
        txtXemThemTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoadMoreActivity.class);
                intent.putExtra("Mode",4);//Tat ca san pham
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
    }


    public void getAllQuangCao(CustomCallbackQC customCallbackQC) {
        DataService dataService = APIService.getService();
        Call<List<QuangCao>> callback = dataService.getAllQuangCao();
        callback.enqueue(new Callback<List<QuangCao>>() {
            @Override
            public void onResponse(Call<List<QuangCao>> call, Response<List<QuangCao>> response) {
                if (response.isSuccessful()) {
                    ArrayList<QuangCao> arrayList = (ArrayList<QuangCao>) response.body();
                    customCallbackQC.onSucess(arrayList);
                }
            }

            @Override
            public void onFailure(Call<List<QuangCao>> call, Throwable t) {
                customCallbackQC.onFailure(t.getMessage());
                Log.d("SV", "HomeFragment Load ảnh quảng cáo  onFailure: " + t.getMessage());
            }
        });
    }

    public void  getAllSach(){
        DataService dataService = APIService.getService();
        Call<List<SachLite>> callback = dataService.getAllSach();
        callback.enqueue(new Callback<List<SachLite>>() {
            @Override
            public void onResponse(Call<List<SachLite>> call, Response<List<SachLite>> response) {
                if(response.isSuccessful()){
                    ArrayList<SachLite> arrayList = (ArrayList<SachLite>) response.body();
                    arrLstSachAll.clear();
                    arrLstSachAll.addAll(arrayList);
                    sachAdapterRecyclerView.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<SachLite>> call, Throwable t) {
            }
        });
    }

    public void loadDataSachHorizontal(OnCallBackHome onCallBackHome){
        //
        DataService dataService = APIService.getService();
        Call<List<SachLite>> callBackSachKM = dataService.getSachKM();
        callBackSachKM.enqueue(new Callback<List<SachLite>>() {
            @Override
            public void onResponse(Call<List<SachLite>> call, Response<List<SachLite>> response) {
                if(response.isSuccessful()){
                    ArrayList<SachLite> arrayListSachKM = (ArrayList<SachLite>) response.body();
                    if(arrayListSachKM != null){
                        if(arrayListSachKM.size() > 0){
                            arrLstSachKM.clear();
                            arrLstSachKM.addAll(arrayListSachKM);
                            sachKMAdapter.notifyDataSetChanged();
                        }
                    }
                }
                onCallBackHome.onCallBackLoaded();
            }

            @Override
            public void onFailure(Call<List<SachLite>> call, Throwable t) {

            }
        });
        Call<List<SachLite>> callBackSachMoi = dataService.getSachMoi();
        callBackSachMoi.enqueue(new Callback<List<SachLite>>() {
            @Override
            public void onResponse(Call<List<SachLite>> call, Response<List<SachLite>> response) {
                if(response.isSuccessful()){
                    ArrayList<SachLite> arrayListSachMoi = (ArrayList<SachLite>) response.body();
                    if(arrayListSachMoi != null){
                        if(arrayListSachMoi.size() > 0){
                            arrLstSachMoi.clear();
                            arrLstSachMoi.addAll(arrayListSachMoi);
                            sachMoiAdapter.notifyDataSetChanged();
                        }
                    }
                }
                onCallBackHome.onCallBackLoaded();
            }

            @Override
            public void onFailure(Call<List<SachLite>> call, Throwable t) {

            }
        });
        Call<List<SachLite>> callBackSachBanChay = dataService.getSachBanChay();
        callBackSachBanChay.enqueue(new Callback<List<SachLite>>() {
            @Override
            public void onResponse(Call<List<SachLite>> call, Response<List<SachLite>> response) {
                if(response.isSuccessful()){
                    ArrayList<SachLite> arrayListSachBanChay= (ArrayList<SachLite>) response.body();
                    if(arrayListSachBanChay != null){
                        if(arrayListSachBanChay.size() > 0){
                            arrLstSachBanChay.clear();
                            arrLstSachBanChay.addAll(arrayListSachBanChay);
                            sachBanChayAdapter.notifyDataSetChanged();
                        }
                    }
                }
                onCallBackHome.onCallBackLoaded();
            }

            @Override
            public void onFailure(Call<List<SachLite>> call, Throwable t) {

            }
        });
        //
        Call<List<SachLite>> callBackSachAll = dataService.getSachAll();
        callBackSachAll.enqueue(new Callback<List<SachLite>>() {
            @Override
            public void onResponse(Call<List<SachLite>> call, Response<List<SachLite>> response) {
                if(response.isSuccessful()){
                    ArrayList<SachLite> arrayListSachAll= (ArrayList<SachLite>) response.body();
                    if(arrayListSachAll != null){
                        if(arrayListSachAll.size() > 0){
                            arrLstSachAll.clear();
                            arrLstSachAll.addAll(arrayListSachAll);
                            sachAdapterRecyclerView.notifyDataSetChanged();
                        }
                    }
                }
                onCallBackHome.onCallBackLoaded();
            }

            @Override
            public void onFailure(Call<List<SachLite>> call, Throwable t) {

            }
        });
    }
}