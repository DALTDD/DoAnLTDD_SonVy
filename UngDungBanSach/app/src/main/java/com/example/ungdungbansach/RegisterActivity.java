package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

import Model.APIService;
import Model.DataService;
import Model.StringRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    TextView txtDangNhapRegister;
    EditText edtHoVaTenRegister, edtTenDangNhapRegister, edtMatKhauRegister, edtXacNhanMKRegister, edtEmailRegister;
    Button btnDangKyRegister;
    ImageView imgDangKy;
    ConstraintLayout constraintLayout;
    Animation top, bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Đăng ký tài khoản");
        }
        //
        linkWidget();
        //
        txtDangNhapRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("Mode",0);
                startActivity(intent);
            }
        });
        //Button dang nhap
        btnDangKyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoTen = edtHoVaTenRegister.getText().toString().trim();
                String email = edtEmailRegister.getText().toString().trim();
                String tenDangNhap = edtTenDangNhapRegister.getText().toString().trim();
                String matKhau = edtMatKhauRegister.getText().toString().trim();
                String xacNhanMK = edtXacNhanMKRegister.getText().toString().trim();
                if (hoTen.isEmpty()) {
                    edtHoVaTenRegister.setError("Họ và tên không được để trống");
                    edtHoVaTenRegister.requestFocus();
                    return;
                }
                if (tenDangNhap.isEmpty()) {
                    edtTenDangNhapRegister.setError("Tên đăng nhập không được để trống");
                    edtTenDangNhapRegister.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    edtEmailRegister.setError("Email không được để trống");
                    edtEmailRegister.requestFocus();
                    return;
                }
                if (!isValid(email)) {
                    edtEmailRegister.setError("Sai định dạng Email");
                    edtEmailRegister.requestFocus();
                    return;
                }
                if (matKhau.isEmpty()) {
                    edtMatKhauRegister.setError("Mật khẩu không được để trống");
                    edtMatKhauRegister.requestFocus();
                    return;
                }
                if (matKhau.length() < 6) {
                    edtMatKhauRegister.setError("Mật khẩu từ 6 ký tự trở lên");
                    edtMatKhauRegister.requestFocus();
                    return;
                }
                if (xacNhanMK.isEmpty()) {
                    edtXacNhanMKRegister.setError("Xác nhận mật khẩu không được để trống");
                    edtXacNhanMKRegister.requestFocus();
                    return;
                }
                if (matKhau.equals(xacNhanMK)) {
                    DataService dataService = APIService.getService();
                    Call<StringRequest> callback = dataService.register(hoTen, email, tenDangNhap, matKhau);
                    callback.enqueue(new Callback<StringRequest>() {
                        @Override
                        public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                            if (response.isSuccessful()) {
                                StringRequest stringRequest = response.body();
                                if (stringRequest.getResultCode().equals("-1")) {
                                    //Trùng tên đăng nhập
                                    //edtTenDangNhapRegister.setError("Tên đăng nhập đã tồn tại. Vui lòng nhập tên đăng nhập khác!");
                                    AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                                    SpannableString title = new SpannableString("Thông báo");
                                    title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                                    alert.setTitle(title);
                                    alert.setMessage("Tên đăng nhập đã tồn tại. Vui lòng nhập tên đăng nhập khác!");
                                    alert.setNegativeButton("OK", null);
                                    alert.show().getWindow().setLayout(750, 530);
                                } else if (stringRequest.getResultCode().equals("-2")) {
//                                    edtEmailRegister.setError("Email đã tồn tại. Vui lòng nhập địa chỉ Email khác!");
//                                    return;
                                    AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                                    SpannableString title = new SpannableString("Thông báo");
                                    title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                                    alert.setTitle(title);
                                    alert.setMessage("Email đã tồn tại. Vui lòng nhập địa chỉ Email khác!");
                                    alert.setNegativeButton("OK", null);
                                    alert.show().getWindow().setLayout(700, 500);
                                } else if (stringRequest.getStatus().equals("ok") && stringRequest.getResultCode().equals("0")) {
                                    //THanh cong
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra("TenDN",tenDangNhap);
                                    intent.putExtra("MatKhau",matKhau);
                                    intent.putExtra("Mode",0);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.d("KRT", "RegisterActivity " + stringRequest.getStatus() + " " + stringRequest.getResultCode());
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<StringRequest> call, Throwable t) {
                            Log.d("KRT", "RegisterActivity - Dang ky onFailure: " + t.getMessage());
                        }
                    });
                } else {
                    edtXacNhanMKRegister.setError("Mật khẩu và xác nhận mật khẩu không khớp.");
                    edtXacNhanMKRegister.setText("");
                    edtXacNhanMKRegister.requestFocus();
                    return;
                }
            }
        });

    }


    private void linkWidget() {
        txtDangNhapRegister = findViewById(R.id.txtDangNhapRegister);
        edtHoVaTenRegister = findViewById(R.id.edtHoVaTenRegister);
        edtTenDangNhapRegister = findViewById(R.id.edtTenDangNhapRegister);
        edtMatKhauRegister = findViewById(R.id.edtMatKhauRegister);
        edtXacNhanMKRegister = findViewById(R.id.edtXacNhanMKRegister);
        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        btnDangKyRegister = findViewById(R.id.btnDangKyRegister);
        imgDangKy = findViewById(R.id.imgDangKy);
        constraintLayout = findViewById(R.id.formDK);
    }
    //Kiểm tra email
    public static boolean isValid(String email){
        String emailRegex ="^[a-zA-Z0-9_+&*-]+(?:\\."   +
                "[a-zA-Z0-9_+&*-]+)*@"                  +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z"             +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if(email == null)
            return false;
        return pattern.matcher(email).matches();
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
}