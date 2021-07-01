package com.example.ungdungbansach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddAddressActivity extends AppCompatActivity {
//    edtHoTenAddGiaoHang
//            edtSDTAddGiaoHang
//    txtAddThanhPho
//            txtAddQuan
//    txtAddPhuong
//            edtDiaChiNhaAddGH
//    btnXacNhanAddDiaChi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thêm địa chỉ giao hàng");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}