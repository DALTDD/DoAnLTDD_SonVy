package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Adapter.SachAdapterRecyclerView;
import Adapter.SachAdapterRecyclerViewHorizontal;
import Model.APIService;
import Model.Cart;
import Model.CartItem;
import Model.DataService;
import Model.OnCallBack;
import Model.Sach;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgSachCTSP;
    TextView txtTenSachCTSP, txtGiaKMCTSP, txtGiaGocCTSP, txtPTGiamGiaCTSP, txtTacGiaCTSP, txtNhaXuatBanCTSP, txtTheLoaiCTSP, txtMoTaCTSP, txtDanhMucCTSP;
    RecyclerView recyclerViewSachCTSP;
    SachAdapterRecyclerViewHorizontal sachAdapterRecyclerView;
    ArrayList<Sach> arrLstSach;
    Button btnThemGioHang;
    String maSach;
    View includeBtnNumber;
    TextView txtNumbers;
    ImageView imgMinus, imgPlus;
    int number = 1;
    Cart cart;
    CartItem cartItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //
        linkWidget();
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //
        arrLstSach = new ArrayList<>();
        sachAdapterRecyclerView = new SachAdapterRecyclerViewHorizontal(DetailActivity.this, arrLstSach, new OnCallBack() {
            @Override
            public void onItemRecyclerViewClick(int position) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("maSach", arrLstSach.get(position).getMaSach());
                startActivity(intent);
                finish();
            }
        });
        recyclerViewSachCTSP.setAdapter(sachAdapterRecyclerView);
        recyclerViewSachCTSP.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerViewSachCTSP.setHasFixedSize(true);
        //
        Intent intent = getIntent();
        if (intent != null) {
            maSach = intent.getStringExtra("maSach");
            //
            Log.d("KRT", "Chuyen tu man hinh home sang chi tiet ma sach : " + maSach);
            //
            //loadDetailSach(maSach);
            //AsyncTask
            new loadData().execute(maSach);
        }
        //
        btnThemGioHang.setOnClickListener(this::onClick);
        imgPlus.setOnClickListener(this::onClick);
        imgMinus.setOnClickListener(this::onClick);
    }

    public void linkWidget() {
        imgSachCTSP = findViewById(R.id.imgSachCTSP);
        txtTenSachCTSP = findViewById(R.id.txtTenSachCTSP);
        txtGiaKMCTSP = findViewById(R.id.txtGiaKMCTSP);
        txtGiaGocCTSP = findViewById(R.id.txtGiaGocCTSP);
        txtGiaGocCTSP.setPaintFlags(txtGiaGocCTSP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtPTGiamGiaCTSP = findViewById(R.id.txtPTGiamGiaCTSP);
        txtTacGiaCTSP = findViewById(R.id.txtTacGiaCTSP);
        txtNhaXuatBanCTSP = findViewById(R.id.txtNhaXuatBanCTSP);
        txtTheLoaiCTSP = findViewById(R.id.txtTheLoaiCTSP);
        txtMoTaCTSP = findViewById(R.id.txtMoTaCTSP);
        txtDanhMucCTSP = findViewById(R.id.txtDanhMucCTSP);
        recyclerViewSachCTSP = findViewById(R.id.recyclerViewCTSP);
        btnThemGioHang = findViewById(R.id.btnThemGioHang);
        includeBtnNumber = findViewById(R.id.includeBtnNumber);
        imgMinus = includeBtnNumber.findViewById(R.id.imgMinus);
        imgPlus = includeBtnNumber.findViewById(R.id.imgPlus);
        txtNumbers = includeBtnNumber.findViewById(R.id.txtNumbers);
    }

    private void loadDetailSach(String maSach) {
        DataService dataService = APIService.getService();
        Call<Sach> callback = dataService.getSachByMaSach(maSach);
        callback.enqueue(new Callback<Sach>() {
            @Override
            public void onResponse(Call<Sach> call, Response<Sach> response) {
                if (response.isSuccessful()) {
                    Sach sach = response.body();
                    if (sach != null) {
                        loadSachByTheLoaiRandom(sach.getMaLoai(), sach.getMaSach());
                        Picasso.with(DetailActivity.this)
                                .load(sach.getAnh())
                                .placeholder(R.mipmap.ic_launcher)
                                .into(imgSachCTSP);
                        txtTenSachCTSP.setText(sach.getTenSach());
                        double price = Double.parseDouble(sach.getGiaGoc());
                        double priceDiscount = Double.parseDouble(sach.getGiaKhuyenMai());
                        Locale locale = new Locale("vi", "VN");
                        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                        txtGiaGocCTSP.setText(numberFormat.format(price));
                        txtGiaKMCTSP.setText(numberFormat.format(priceDiscount));
                        txtMoTaCTSP.setText(sach.getMoTa().toString().replaceAll("_", System.getProperty("line.separator")));
                        txtTacGiaCTSP.setText(sach.getTacGia());
                        txtNhaXuatBanCTSP.setText(sach.getTenNXB());
                        txtTheLoaiCTSP.setText(sach.getTenTheLoai());
                        txtDanhMucCTSP.setText(sach.getTenDanhMuc());
                        if (sach.getGiaGoc().equals(sach.getGiaKhuyenMai())) {
                            txtPTGiamGiaCTSP.setVisibility(View.INVISIBLE);
                            txtGiaGocCTSP.setVisibility(View.INVISIBLE);
                        } else {
                            double priceSub = price - priceDiscount;
                            double percent = priceSub / price;
                            int percentInt = (int) (percent * 100);
                            String ptGiamGia = "-" + String.valueOf(percentInt) + "%";
                            txtPTGiamGiaCTSP.setText(ptGiamGia);
                        }
                        //
                        cartItem = new CartItem(sach.getMaSach(),sach.getTenSach(),sach.getAnh(),Double.parseDouble(sach.getGiaGoc()),Double.parseDouble(sach.getGiaKhuyenMai()));
                    }
                } else {
                    Log.d("KRT", "Call API chi tiet san pham : Null");
                }
            }

            @Override
            public void onFailure(Call<Sach> call, Throwable t) {

            }
        });
    }

    public void loadSachByTheLoaiRandom(String maLoai, String maSach) {
        DataService dataService = APIService.getService();
        Call<List<Sach>> callback = dataService.getSachByTheLoaiRandom(maLoai, maSach);
        callback.enqueue(new Callback<List<Sach>>() {
            @Override
            public void onResponse(Call<List<Sach>> call, Response<List<Sach>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Sach> arrayList = (ArrayList<Sach>) response.body();
                    Log.d("KRT", "Lay sach cung the loai random CTSP size = " + arrayList.size());
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

    @Override
    public void onClick(View v) {
        if (v.equals(btnThemGioHang)) {
            int soLuong = Integer.parseInt(txtNumbers.getText().toString());
            if(cart.getInstanceCart().size() > 0){
                Log.d("KRT","Gio > 0");
                boolean check = true;
                for(int i = 0; i < cart.getInstanceCart().size(); i++){
                    if(cart.getInstanceCart().get(i).getMaSach().equals(maSach)){
                        int soLuongHt = cart.getInstanceCart().get(i).getSoLuong();
                        cart.getInstanceCart().get(i).setSoLuong(soLuongHt + soLuong);
                        check = false;
                        break;
                    }
                }
                if(check){
                    cartItem.setSoLuong(soLuong);
                    cart.getInstanceCart().add(cartItem);
                }
            }
            else{
                cartItem.setSoLuong(soLuong);
                cart.getInstanceCart().add(cartItem);
            }
        }
        if (v.equals(imgMinus)) {
            if(number > 1){
                number--;
                txtNumbers.setText(number+"");
                if(number == 1){
                    imgMinus.setEnabled(false);
                }
            }
        }
        if (v.equals(imgPlus)) {
            number++;
            if(number > 1){
                imgMinus.setEnabled(true);
            }
            txtNumbers.setText(number+"");
        }
    }

    private class loadData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            loadDetailSach(strings[0]);
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
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