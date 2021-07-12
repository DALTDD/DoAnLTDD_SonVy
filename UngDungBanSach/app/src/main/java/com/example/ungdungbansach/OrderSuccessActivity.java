package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderSuccessActivity extends AppCompatActivity {
    Button btnTiepTucMH, btnXemCTDH;
    TextView txtMaDHSuccsess;
    String maDonHang = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        //
        linkWidget();
        //
        if(getIntent().getStringExtra("MaDonHang") == null){
            finish();
        }
        else{
            maDonHang = getIntent().getStringExtra("MaDonHang");
            txtMaDHSuccsess.setText("Mã đơn hàng: #"+maDonHang);
        }

        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Đặt hàng");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //
        btnTiepTucMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        //
        btnXemCTDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSuccessActivity.this, DetailOrderActivity.class);
                intent.putExtra("MaDonHang",maDonHang);
                intent.putExtra("Mode",0);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
                finish();
            }
        });
    }
    public void linkWidget(){
        btnTiepTucMH = findViewById(R.id.btnTiepTucMH);
        btnXemCTDH = findViewById(R.id.btnXemCTDH);
        txtMaDHSuccsess = findViewById(R.id.txtMaDHSuccsess);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                Intent intent = new Intent(OrderSuccessActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}