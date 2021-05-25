package com.example.ungdungbansach;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ungdungbansach.R;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.SachAdapterRecyclerView;
import Model.APIService;
import Model.CustomCallbackQC;
import Model.DataService;
import Model.OnCallBack;
import Model.QuangCao;
import Model.Sach;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    CarouselView carouselQuangCao;
    ArrayList<QuangCao> arrLstQuangCao;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerViewSachHome;
    SachAdapterRecyclerView sachAdapterRecyclerView;
    ArrayList<Sach> arrLstSach;
    RelativeLayout relativeLayout;

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
        arrLstSach = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        Toast.makeText(getContext(), "KRT Checked", Toast.LENGTH_SHORT).show();
        //Link widget
        carouselQuangCao = view.findViewById(R.id.carouselQuangCao);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        relativeLayout = view.findViewById(R.id.relative);
        recyclerViewSachHome = view.findViewById(R.id.recyclerViewSachHome);
        //
        arrLstQuangCao = new ArrayList<>();
        getAllQuangCao(new CustomCallbackQC() {
            @Override
            public void onSucess(ArrayList<QuangCao> arrayList) {
                arrLstQuangCao = arrayList;
                Log.d("KRT", "Lan 1 " + arrayList.size() + " " + arrLstQuangCao.size());
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

        //
        sachAdapterRecyclerView = new SachAdapterRecyclerView(getContext(), arrLstSach, new OnCallBack() {
            @Override
            public void onItemRecyclerViewClick(int position) {
                Log.d("KRT","Click sach " + position + arrLstSach.get(position).getTenSach() + " Ma sach : " + arrLstSach.get(position).getMaSach());
                //
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("maSach", arrLstSach.get(position).getMaSach());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
            }
        });
        //
        //

        recyclerViewSachHome.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewSachHome.setAdapter(sachAdapterRecyclerView);
        //recyclerViewSachHome.setHasFixedSize(true);
        //
        //getAllSach();
        new loadData().execute();
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
    }


    public void getAllQuangCao(CustomCallbackQC customCallbackQC) {
        DataService dataService = APIService.getService();
        Call<List<QuangCao>> callback = dataService.getAllQuangCao();
        callback.enqueue(new Callback<List<QuangCao>>() {
            @Override
            public void onResponse(Call<List<QuangCao>> call, Response<List<QuangCao>> response) {
                if (response.isSuccessful()) {
                    ArrayList<QuangCao> arrayList = (ArrayList<QuangCao>) response.body();
                    Log.d("KRT", arrayList.size() + "");
                    customCallbackQC.onSucess(arrayList);
                }
            }

            @Override
            public void onFailure(Call<List<QuangCao>> call, Throwable t) {
                customCallbackQC.onFailure(t.getMessage());
            }
        });
    }

    public void  getAllSach(){
        DataService dataService = APIService.getService();
        Call<List<Sach>> callback = dataService.getAllSach();
        callback.enqueue(new Callback<List<Sach>>() {
            @Override
            public void onResponse(Call<List<Sach>> call, Response<List<Sach>> response) {
                if(response.isSuccessful()){
                    ArrayList<Sach> arrayList = (ArrayList<Sach>) response.body();
                    Log.d("KRT", "Tat ca sach : "+ arrayList.size());
                    arrLstSach.clear();
                    arrLstSach.addAll(arrayList);
                    sachAdapterRecyclerView.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Sach>> call, Throwable t) {
            }
        });
    }

    public class loadData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            getAllSach();
            return null;
        }
    }
}