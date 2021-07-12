package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import Model.APIService;
import Model.DataService;
import Model.KhachHang;
import Model.StringRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoUserActivity extends AppCompatActivity {
    EditText edtHoVaTenInfo, edtEmailInfo, edtSDTInfo;
    TextView txtNgaySinhInfo;
    RadioButton rdBtnNamInfo, rdBtnNuInfo;
    Button btnLuuThongTinInfo;
    String maKH = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        //
        linkControl();
        //
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thông tin tài khoản");
        }
        if (loadPreferences("MaKH") == null) {
            Intent intent = new Intent(InfoUserActivity.this, LoginActivity.class);
            intent.putExtra("Mode", 0);
            startActivity(intent);
            finish();
        } else {
            maKH = loadPreferences("MaKH");
        }
        getTTKHByMaKH(maKH);
        Log.d("SV", "InfoUserActivity - MaKH:" + loadPreferences("MaKH"));
        //
        txtNgaySinhInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonNgaySinh();
                Toast.makeText(InfoUserActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
        //click button update TT
        btnLuuThongTinInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoTen = edtHoVaTenInfo.getText().toString().trim();
                String SDT = edtSDTInfo.getText().toString().trim();
                String ngaySinh = txtNgaySinhInfo.getText().toString().trim();
                String email = edtEmailInfo.getText().toString().trim();
                String gioiTinh = "";

                if (hoTen.isEmpty()) {
                    edtHoVaTenInfo.setError("Họ và tên không được để trống");
                    return;
                }
                if (SDT.isEmpty()) {
                    edtSDTInfo.setError("Số điện thoại không được để trống");
                    return;
                }
                //
                if (ngaySinh.isEmpty()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InfoUserActivity.this);
                    alert.setTitle("Thông báo");
                    alert.setMessage("Vui lòng chọn ngày sinh");
                    alert.setNegativeButton("Đồng ý", null);
                    return;
                }
                //
                if (email.isEmpty()) {
                    edtEmailInfo.setError("Email không được để trống");
                    return;
                }
                if (!isValid(email)) {
                    edtEmailInfo.setError("Sai định dạng Email");
                    edtEmailInfo.requestFocus();
                    return;
                }
                if (!rdBtnNamInfo.isChecked() && !rdBtnNuInfo.isChecked()) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InfoUserActivity.this);
                    alert.setTitle("Thông báo");
                    alert.setMessage("Vui lòng chọn giới tính");
                    alert.setNegativeButton("Đồng ý", null);
                    return;
                }
                if (rdBtnNamInfo.isChecked()) {
                    gioiTinh = "Nam";
                } else {
                    gioiTinh = "Nữ";
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = formatter.parse(ngaySinh);
                    ngaySinh = dateFormat.format(date);
                } catch (ParseException e) {
                    ngaySinh = dateFormat.format(Calendar.getInstance().getTime());
                }
                Log.d("SV", "InfoUserActivity - Info: " + maKH + " - " + hoTen + " - " + SDT + " - " + ngaySinh + " - " + ngaySinh + " - " + gioiTinh);
                //
                ProgressDialog progressDialog = new ProgressDialog(InfoUserActivity.this);
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setContentView(R.layout.progress_load_data);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //
                DataService dataService = APIService.getService();
                Call<StringRequest> callback = dataService.updateAccountInfo(maKH, hoTen, SDT, ngaySinh, gioiTinh);
                callback.enqueue(new Callback<StringRequest>() {
                    @Override
                    public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                        if (response.isSuccessful()) {
                            StringRequest request = response.body();
                            if (request.getStatus().equals("1")) {
                                progressDialog.dismiss();
                                Log.d("SV", "InfoUserActivity- Sửa thành công");
                            } else {
                                Log.d("SV", "InfoUserActivity- Sửa thất bại");
                            }
                        } else {
                            Log.d("SV", "InfoUserActivity- Connect not Success");
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<StringRequest> call, Throwable t) {
                        Log.d("SV", "InfoUserActivity- Sửa TTKH onFailure " + t.getMessage());
                    }
                });
            }
        });

    }

    public void getTTKHByMaKH(String MaKH) {
        //
        ProgressDialog progressDialog = new ProgressDialog(InfoUserActivity.this);
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_load_data);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DataService dataService = APIService.getService();
        //
        Call<KhachHang> callback = dataService.getTTKHByMaKH(MaKH);
        callback.enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                if (response.isSuccessful()) {
                    KhachHang khachHang = response.body();
                    //maKH = khachHang.getMaKH();
                    edtHoVaTenInfo.setText(khachHang.getHoTen());
                    edtEmailInfo.setText(khachHang.getEmail());
                    edtSDTInfo.setText(khachHang.getSdt());
                    txtNgaySinhInfo.setText(khachHang.getNgaySinh());
                    if (khachHang.getGioiTinh() == null) {
                        rdBtnNamInfo.setChecked(false);
                        rdBtnNuInfo.setChecked(false);
                    } else {
                        if (khachHang.getGioiTinh().equals("Nữ")) {
                            rdBtnNuInfo.setChecked(true);
                            rdBtnNamInfo.setChecked(false);
                        } else if (khachHang.getGioiTinh().equals("Nam")) {
                            rdBtnNamInfo.setChecked(true);
                            rdBtnNuInfo.setChecked(false);
                        }
                    }
                    progressDialog.dismiss();
                    //Log.d("KRT","InfoUserActivity -getTTKHByMaKH- MaKH:"+maKH);
                    Log.d("SV", "InfoUserActivity -getTTKHByMaKH- MaKH:" + khachHang.getMaKH());
                } else {
                    Log.d("SV", "InfoUserActivity - getTTKHByMaKH Not Success");
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<KhachHang> call, Throwable t) {
                Log.d("SV", "InfoUserActivity - getTTKHByMaKH onFailure " + t.getMessage());
                if(t.getMessage().equals("timeout")){
                    getTTKHByMaKH(maKH);
                }
            }
        });
    }

    private void linkControl() {
        edtHoVaTenInfo = findViewById(R.id.edtHoVaTenInfo);
        edtEmailInfo = findViewById(R.id.edtEmailInfo);
        edtSDTInfo = findViewById(R.id.edtSDTInfo);
        txtNgaySinhInfo = findViewById(R.id.txtNgaySinhInfo);
        rdBtnNamInfo = findViewById(R.id.rdBtnNamInfo);
        rdBtnNuInfo = findViewById(R.id.rdBtnNuInfo);
        btnLuuThongTinInfo = findViewById(R.id.btnLuuThongTinInfo);
    }

    private void chonNgaySinh() {
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(InfoUserActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                txtNgaySinhInfo.setText(simpleDateFormat.format(calendar.getTime()));
                Log.d("SV","GetDate: " + calendar.getTime().toString());
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }

    public String loadPreferences(String key) {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getString(key, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    //Kiểm tra email
    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pattern.matcher(email).matches();
    }

}