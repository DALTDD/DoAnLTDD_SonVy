package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import Model.APIService;
import Model.DataService;
import Model.StringRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    Button btnXacNhanChangeMK;
    CheckBox chkHienThiMKChangePassword;
    EditText edtNhapLaiMKCP, edtMatKhauMoiCP, edtMatKhauCuCP;
    String maKH="", matKhau ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        //
        linkControl();
        //
        if (loadPreferences("MaKH") == null) {
            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
            intent.putExtra("Mode", 0);
            startActivity(intent);
            finish();
        } else {
            maKH = loadPreferences("MaKH");
        }
        //
        matKhau = loadPreferences("MatKhau");
        if(getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("?????i m???t kh???u");
        }

        btnXacNhanChangeMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matKhauEdit = edtMatKhauCuCP.getText().toString().trim();
                String matKhauMoiEdit = edtMatKhauMoiCP.getText().toString().trim();
                String nhapLaiMKEdit = edtNhapLaiMKCP.getText().toString().trim();
                //check
                if(matKhauEdit.isEmpty()){
                    edtMatKhauCuCP.setError("M???t kh???u kh??ng ???????c ????? tr???ng");
                    edtMatKhauCuCP.requestFocus();
                    return;
                }
                if(matKhauMoiEdit.isEmpty()){
                    edtMatKhauMoiCP.setError("M???t kh???u m???i kh??ng ???????c ????? tr???ng");
                    edtMatKhauMoiCP.requestFocus();
                    return;
                }
                if(nhapLaiMKEdit.isEmpty()){
                    edtNhapLaiMKCP.setError("Nh???p l???i m???t kh???u kh??ng ???????c ????? tr???ng");
                    edtNhapLaiMKCP.requestFocus();
                    return;
                }
                if(matKhauMoiEdit.length() < 6){
                    edtMatKhauMoiCP.setError("M???t kh???u t??? 6 k?? t??? tr??? l??n");
                    edtMatKhauMoiCP.requestFocus();
                    return;
                }
                if(!matKhau.equals(matKhauEdit)){
//                    edtMatKhauCuCP.setError("M???t kh???u kh??ng kh???p v???i th??ng tin l??u trong h??? th???ng");
//                    return;
                    //
                    AlertDialog.Builder alert = new AlertDialog.Builder(ChangePasswordActivity.this);
                    SpannableString title = new SpannableString("Th??ng b??o");
                    title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                    alert.setTitle(title);
                    alert.setMessage("M???t kh???u kh??ng kh???p v???i th??ng tin l??u trong h??? th???ng");
                    alert.setNegativeButton("?????ng ??",null);
                    alert.show();
                    return;

                }
                if(!nhapLaiMKEdit.equals(matKhauMoiEdit)){
//                    edtMatKhauMoiCP.setError("Nh???p l???i m???t kh???u ph???i tr??ng kh???p v???i m???t kh???u m???i");
//                    return;
                    AlertDialog.Builder alert = new AlertDialog.Builder(ChangePasswordActivity.this);
                    SpannableString title = new SpannableString("Th??ng b??o");
                    title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                    alert.setTitle(title);
                    alert.setMessage("Nh???p l???i m???t kh???u ph???i tr??ng kh???p v???i m???t kh???u m???i");
                    alert.setNegativeButton("?????ng ??",null);
                    alert.show();
                    return;
                }

                Log.d("KRT", "ChangePasswordActivity - ?????i m???t kh???u - MaKH:" + maKH);
                DataService dataService = APIService.getService();
                Call<StringRequest> callback = dataService.updateMatKhauKH(maKH,matKhauMoiEdit);
                callback.enqueue(new Callback<StringRequest>() {
                    @Override
                    public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                        if(response.isSuccessful()){
                            StringRequest stringRequest = response.body();
                            if(stringRequest.getStatus().equals("1")){
                                Log.d("SV", "ChangePasswordActivity-?????i m???t kh???u - Th??nh c??ng");
                                savePreferences("MatKhau",matKhauMoiEdit);
                                AlertDialog.Builder alert = new AlertDialog.Builder(ChangePasswordActivity.this);
                                alert.setTitle("Th??ng b??o");
                                alert.setMessage("?????i m???t kh???u th??nh c??ng!");
                                alert.setPositiveButton("?????ng ??", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //clearPreferences();
                                        //Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                        //startActivity(intent);
                                        finish();
                                    }
                                });
                                alert.show();
                            }
                            else {
                                Log.d("SV", "ChangePasswordActivity-?????i m???t kh???u - Th???t b???i");
                            }
                        }
                        else {
                            Log.d("SV", "ChangePasswordActivity-?????i m???t kh???u - Connect not Success");
                        }
                    }

                    @Override
                    public void onFailure(Call<StringRequest> call, Throwable t) {
                        Log.d("SV", "ChangePasswordActivity - ?????i m???t kh???u - onFailure"+t.getMessage());
                    }
                });
            }
        });
        chkHienThiMKChangePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chkHienThiMKChangePassword.isChecked()){
                    //edtMatKhauCuCP, edtNhapLaiMKCP, edtMatKhauMoiCP,
                    edtMatKhauCuCP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtNhapLaiMKCP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtMatKhauMoiCP.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    edtMatKhauCuCP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtNhapLaiMKCP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtMatKhauMoiCP.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void linkControl() {
        btnXacNhanChangeMK = findViewById(R.id.btnXacNhanChangeMK);
        chkHienThiMKChangePassword = findViewById(R.id.chkHienThiMKChangePassword);
        edtMatKhauCuCP = findViewById(R.id.edtMatKhauCuCP);
        edtNhapLaiMKCP = findViewById(R.id.edtNhapLaiMKCP);
        edtMatKhauMoiCP = findViewById(R.id.edtMatKhauMoiCP);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
    public String loadPreferences(String key) {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getString(key, null);
    }
    public void clearPreferences() {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.clear();
        edCaches.commit();
    }
    public void savePreferences(String key, String value) {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor edCaches = p.edit();
        edCaches.putString(key, value);
        edCaches.commit();
    }
}