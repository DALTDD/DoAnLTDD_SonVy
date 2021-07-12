package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter.SachSearchAdapter;
import Model.APIService;
import Model.DataService;
import Model.Sach;
import me.gujun.android.taggroup.TagGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
 TagGroup tag_group;
 ListView lstSearch;
 SearchView searchView;
 ArrayList<Sach> sachArrayList;
 SachSearchAdapter sachSearchAdapter;
 TextView txtKetQuaSearch;
 CardView cardViewSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        linkWidget();
        //back ve home
        //ArrayList<String> strings = new ArrayList<>();
        tag_group.setTags(new String[]{"Đừng nhạt nữa","Hoàng tử bé","Kinh doanh","Bình Hoa"});
        //tag_group.setTags(String.valueOf(strings.add("Light novel")));
        tag_group.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                searchView.setQuery(tag,false);
            }
        });
        //
        sachArrayList = new ArrayList<>();
        sachSearchAdapter = new SachSearchAdapter(this,sachArrayList);
        lstSearch.setAdapter(sachSearchAdapter);
        //atri Search
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getSachBySearch(newText);
                return false;
            }
        });
        //lstView click
        lstSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("maSach",sachArrayList.get(position).getMaSach());
                startActivity(intent);
                finish();
            }
        });
    }
    private void getSachBySearch(String search){
        DataService dataService = APIService.getService();
        Call<List<Sach>> callback = dataService.getSachBySearch(search);
        callback.enqueue(new Callback<List<Sach>>() {
            @Override
            public void onResponse(Call<List<Sach>> call, Response<List<Sach>> response) {
                if(response.isSuccessful()){
                    ArrayList<Sach> arrayList = (ArrayList<Sach>) response.body();
                    if(arrayList.size() > 0){
                        sachArrayList.clear();
                        sachArrayList.addAll(arrayList);
                        sachSearchAdapter.notifyDataSetChanged();
                        lstSearch.setVisibility(View.VISIBLE);
                        txtKetQuaSearch.setVisibility(View.GONE);
                        cardViewSearch.setVisibility(View.GONE);
                    }
                    else {
                        lstSearch.setVisibility(View.GONE);
                        txtKetQuaSearch.setVisibility(View.VISIBLE);
                        cardViewSearch.setVisibility(View.VISIBLE);
                    }
                    if(search.isEmpty()){
                        lstSearch.setVisibility(View.GONE);
                        cardViewSearch.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    Log.d("SV","Get sach by search that bai");
                }
            }
            @Override
            public void onFailure(Call<List<Sach>> call, Throwable t) {
                Log.d("SV","Get sach by search that bai"+t.getMessage());
            }
        });
    }
    private void linkWidget(){
        tag_group = findViewById(R.id.tag_group);
        lstSearch = findViewById(R.id.lstSearch);
        searchView = findViewById(R.id.searchView);
        txtKetQuaSearch = findViewById(R.id.txtKetQuaSearch);
        cardViewSearch = findViewById(R.id.cardViewSearch);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}