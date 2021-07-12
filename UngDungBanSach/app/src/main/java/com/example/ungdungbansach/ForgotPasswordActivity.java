package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import Model.APIService;
import Model.DataService;
import Model.Login;
import Model.MailAPI;
import Model.QuenMK;
import Model.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edtQuenMK;
    Button btnQuenMK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quên mật khẩu");
        }
        //
        linkWidget();;
        //
        btnQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenDN = edtQuenMK.getText().toString().trim();
                if(tenDN.isEmpty()){
                    edtQuenMK.setError("Vui lòng nhật tên đăng nhập");
                    edtQuenMK.requestFocus();
                    return;
                }
                //
                ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setContentView(R.layout.progress_load_data);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //
                DataService dataService = APIService.getService();
                Call<QuenMK> callBack = dataService.forgotPassword(tenDN);
                callBack.enqueue(new Callback<QuenMK>() {
                    @Override
                    public void onResponse(Call<QuenMK> call, Response<QuenMK> response) {
                        if(response.isSuccessful()){
                            QuenMK quenMK = response.body();
                            if(quenMK.getStatus().equals("1")){
                                progressDialog.dismiss();
                                String hoTen = " ";
                                if(quenMK.getHoTen() != null){
                                    hoTen = quenMK.getHoTen();
                                }
                                Log.d("SV","ForgotPWActivity - Call API Status: " + quenMK.getStatus() + " - " + hoTen + " - " + quenMK.getEmail() + " - " + quenMK.getMatKhau());
                                new MailAPI(quenMK.getEmail(), Utils.tittle,Utils.createMessage(hoTen,quenMK.getMatKhau())).execute();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                SpannableString title = new SpannableString("Thông báo");
                                title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
                                builder.setTitle(title);
                                builder.setMessage("Hãy kiểm tra hộp thư Email. Mật khẩu đã được gửi đến địa chỉ Email của bạn!");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        dialog.dismiss();
                                        //Intent intent = new Intent(ForgotPasswordActivity.this, Login.class);
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //startActivity(intent);
                                        finish();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.setCancelable(false);
                                dialog.show();
                            }
                            else if(quenMK.getStatus().equals("0")){
                                showDialog("Tên đăng nhập không đúng. Vui lòng kiểm tra lại!");
                                Log.d("SV","ForgotPWActivity - Call API Status: " + quenMK.getStatus());
                            }
                            else{
                                showDialog("Lỗi kết nối");
                                Log.d("SV","ForgotPWActivity - Call API Status: " + quenMK.getStatus() + " - loi connect");
                            }
                        }else{
                            showDialog("Lỗi kết nối");
                            Log.d("SV","ForgotPWActivity - Call API fail");
                        }
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<QuenMK> call, Throwable t) {
                        Log.d("SV","ForgotPWActivity - Call API onFailure: " + t.getMessage());
                    }
                });

            }
        });
    }
    public void linkWidget(){
        edtQuenMK = findViewById(R.id.edtQuenMK);
        btnQuenMK = findViewById(R.id.btnQuenMK);
    }


    public void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
        SpannableString title = new SpannableString("Thông báo");
        title.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length(), 0);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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