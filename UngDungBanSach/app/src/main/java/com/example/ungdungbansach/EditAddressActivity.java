package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;

import Model.APIService;
import Model.Citys;
import Model.DataService;
import Model.DiaChi;
import Model.Dictricts;
import Model.OnCallBack;
import Model.OnCallBackResult;
import Model.ResultCity;
import Model.ResultDictrict;
import Model.ResultWard;
import Model.StringRequest;
import Model.Wards;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddressActivity extends AppCompatActivity {
    TextView txtEditThanhPho, txtEditQuan, txtEditPhuong;
    EditText edtHoTenEditGiaoHang, edtSDTEditGiaoHang, edtDiaChiNhaEditGH;
    Button btnXacNhanEditDiaChi;
    Dialog dialog;
    Citys citys;
    Dictricts dictricts;
    Wards wards;
    String maKH = "", maThanhPho = "", maQuan = "", maDiaChi = "";
    ProgressDialog progressDialog;
    int mode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chỉnh sửa địa chỉ giao hàng");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //
        if (getIntent().getStringExtra("MaDiaChi") == null || (getIntent().getIntExtra("Mode", -1) == -1) || getIntent().getStringExtra("MaKH") == null) {
            finish();
        } else {
            maDiaChi = getIntent().getStringExtra("MaDiaChi");
            mode = getIntent().getIntExtra("Mode", -1);
            maKH = getIntent().getStringExtra("MaKH");
        }
        //
        citys = new Citys();
        dictricts = new Dictricts();
        wards = new Wards();
        //
        linkWidget();
        //
        txtEditThanhPho.setEnabled(false);
        txtEditQuan.setEnabled(false);
        txtEditPhuong.setEnabled(false);
        //
        loadDataCitys();
        //
        firstLoad(maDiaChi);
        //
        txtEditThanhPho.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    txtEditQuan.setEnabled(false);
                    txtEditQuan.setEnabled(false);
                } else {
                    txtEditQuan.setText("");
                    txtEditPhuong.setText("");
                    txtEditQuan.setEnabled(true);
                    txtEditPhuong.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        txtEditQuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    txtEditPhuong.setEnabled(false);
                } else {
                    txtEditPhuong.setText("");
                    txtEditPhuong.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        txtEditThanhPho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(EditAddressActivity.this);
                dialog.setContentView(R.layout.dialog_search_address);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //dialog.getWindow().setLayout(950,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView txtTitle = dialog.findViewById(R.id.txtTittle);
                EditText edtSearchAddress = dialog.findViewById(R.id.edtSearchAddress);
                ListView listView = dialog.findViewById(R.id.lstAddress);
                TextView txtHuy = dialog.findViewById(R.id.txtHuy);
                txtTitle.setText("Chọn Tỉnh / Thành phố");

                if (citys.getResults() != null) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(EditAddressActivity.this, android.R.layout.simple_list_item_1, citys.getResults());
                    listView.setAdapter(arrayAdapter);

                    //
                    edtSearchAddress.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            arrayAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ResultCity resultCity = (ResultCity) arrayAdapter.getItem(position);
                            txtEditThanhPho.setText(resultCity.getName());
                            maThanhPho = resultCity.getCode();
                            Log.d("KRT", "EditAddressAc - Ma thanh pho selected: " + maThanhPho);
                            loadDataDictricts(maThanhPho);
                            dialog.dismiss();
                        }
                    });
                    txtHuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        //
        txtEditQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(EditAddressActivity.this);
                dialog.setContentView(R.layout.dialog_search_address);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //dialog.getWindow().setLayout(950,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView txtTitle = dialog.findViewById(R.id.txtTittle);
                EditText edtSearchAddress = dialog.findViewById(R.id.edtSearchAddress);
                ListView listView = dialog.findViewById(R.id.lstAddress);
                TextView txtHuy = dialog.findViewById(R.id.txtHuy);
                txtTitle.setText("Chọn Quận / Huyện");
                if (dictricts.getResults() != null) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(EditAddressActivity.this, android.R.layout.simple_list_item_1, dictricts.getResults());
                    listView.setAdapter(arrayAdapter);
                    //
                    edtSearchAddress.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            arrayAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ResultDictrict resultDictrict = (ResultDictrict) arrayAdapter.getItem(position);
                            txtEditQuan.setText(resultDictrict.getName());
                            maQuan = resultDictrict.getCode();
                            Log.d("KRT", "EditAddressAc - Ma quan selected: " + maQuan);
                            loadDataWards(maQuan);
                            dialog.dismiss();
                        }
                    });
                    txtHuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        //
        txtEditPhuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(EditAddressActivity.this);
                dialog.setContentView(R.layout.dialog_search_address);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                //dialog.getWindow().setLayout(950,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView txtTitle = dialog.findViewById(R.id.txtTittle);
                EditText edtSearchAddress = dialog.findViewById(R.id.edtSearchAddress);
                ListView listView = dialog.findViewById(R.id.lstAddress);
                TextView txtHuy = dialog.findViewById(R.id.txtHuy);
                txtTitle.setText("Chọn Phường / Xã");
                if (wards.getResults() != null) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(EditAddressActivity.this, android.R.layout.simple_list_item_1, wards.getResults());
                    listView.setAdapter(arrayAdapter);
                    //
                    edtSearchAddress.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            arrayAdapter.getFilter().filter(s);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ResultWard resultWard = (ResultWard) arrayAdapter.getItem(position);
                            txtEditPhuong.setText(resultWard.getName());
                            String ward = resultWard.getCode();
                            Log.d("KRT", "EditAddressAc - Ma Phuong selected: " + ward);
                            dialog.dismiss();
                        }
                    });
                    txtHuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        //
        btnXacNhanEditDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hoTen = edtHoTenEditGiaoHang.getText().toString();
                String sdt = edtSDTEditGiaoHang.getText().toString();
                String thanhPho = txtEditThanhPho.getText().toString();
                String quan = txtEditQuan.getText().toString();
                String phuong = txtEditPhuong.getText().toString();
                String soNha = edtDiaChiNhaEditGH.getText().toString();
                if (hoTen.equals("") || hoTen.isEmpty()) {
                    edtHoTenEditGiaoHang.setError("Vui lòng nhập họ tên!");
                    edtHoTenEditGiaoHang.requestFocus();
                    return;
                }
                if (sdt.equals("") || sdt.isEmpty()) {
                    edtSDTEditGiaoHang.setError("Vui lòng nhập SDT!");
                    edtSDTEditGiaoHang.requestFocus();
                    return;
                }
                if (thanhPho.equals("")) {
                    showDialogError("Vui lòng chọn Tỉnh / Thành phố");
                    return;
                }
                if (quan.equals("")) {
                    showDialogError("Vui lòng chọn Quận / Huyện");
                    return;
                }
                if (phuong.equals("")) {
                    showDialogError("Vui lòng chọn Phường / Xã");
                    return;
                }
                if (soNha.equals("") || soNha.isEmpty()) {
                    edtDiaChiNhaEditGH.setError("Vui lòng nhập số nhà, đường!");
                    edtDiaChiNhaEditGH.requestFocus();
                    return;
                }
                if (maThanhPho.isEmpty() || maQuan.isEmpty()) {
                    showDialogError("Hệ thống chưa ghi nhận được địa chỉ");
                    return;
                }
                //
                String diaChi = soNha + ", " + phuong + ", " + quan + ", " + thanhPho;
                String iThanhPho = maThanhPho.trim() + ";" + thanhPho.trim();
                String iQuan = maQuan.trim() + ";" + quan;
                Dialog dialog = new Dialog(EditAddressActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_address);
                if (dialog.getWindow() == null) {
                    return;
                }
                //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                //
                TextView txtXacNhanDiaChi = dialog.findViewById(R.id.txtXacNhanDiaChi);
                Button btnChonLaiDC, btnXacNhanDC;
                btnChonLaiDC = dialog.findViewById(R.id.btnChonLaiDC);
                btnXacNhanDC = dialog.findViewById(R.id.btnXacNhanDC);
                btnChonLaiDC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnXacNhanDC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //
                        progressDialog = new ProgressDialog(EditAddressActivity.this);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setContentView(R.layout.progress_load_data);
                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        //
                        DataService dataService = APIService.getService();
                        Call<StringRequest> callBack = dataService.updateDiaChi(maDiaChi, hoTen, sdt, iThanhPho, iQuan, phuong, soNha);
                        callBack.enqueue(new Callback<StringRequest>() {
                            @Override
                            public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                                if (response.isSuccessful()) {
                                    StringRequest stringRequest = response.body();
                                    if (stringRequest.getStatus().equals("1")) {
                                        Log.d("KRT", "EditAddressAc - Sửa thành công");
                                    }
                                    else {
                                        Log.d("KRT", "EditAddressAc - Sửa không thành công");
                                    }
                                }
                                else {
                                    Log.d("KRT", "EditAddressAc - Sửa lỗi Not Successful");
                                }
                                progressDialog.dismiss();
                                moveScreen();
                            }
                            @Override
                            public void onFailure(Call<StringRequest> call, Throwable t) {
                                Log.d("KRT", "EditAddressAc - Sửa onFailure: " + t.getMessage());
                                progressDialog.dismiss();
                                moveScreen();
                            }
                        });
                    }
                });
                txtXacNhanDiaChi.setText(diaChi);
                dialog.show();
                //
            }
        });
    }

    public void linkWidget() {
        txtEditThanhPho = findViewById(R.id.txtEditThanhPho);
        txtEditQuan = findViewById(R.id.txtEditQuan);
        txtEditPhuong = findViewById(R.id.txtEditPhuong);
        edtHoTenEditGiaoHang = findViewById(R.id.edtHoTenEditGiaoHang);
        edtSDTEditGiaoHang = findViewById(R.id.edtSDTEditGiaoHang);
        edtDiaChiNhaEditGH = findViewById(R.id.edtDiaChiNhaEditGH);
        btnXacNhanEditDiaChi = findViewById(R.id.btnXacNhanEditDiaChi);
    }

    public void firstLoad(String maDiaChi) {
        progressDialog = new ProgressDialog(EditAddressActivity.this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_load_data);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DataService dataService = APIService.getService();
        Call<DiaChi> callBack = dataService.getDiaChiByMaDiaChi(maDiaChi);
        callBack.enqueue(new Callback<DiaChi>() {
            @Override
            public void onResponse(Call<DiaChi> call, Response<DiaChi> response) {
                if (response.isSuccessful()) {
                    DiaChi diaChi = response.body();
                    if (diaChi != null) {
                        edtSDTEditGiaoHang.setText(diaChi.getSdt());
                        edtHoTenEditGiaoHang.setText(diaChi.getHoTen());
                        edtDiaChiNhaEditGH.setText(diaChi.getDiaChiNha());
                        String[] thanhPho = diaChi.getThanhPho().split(";");
                        String[] quan = diaChi.getQuan().split(";");
                        if (thanhPho.length >= 2) {
                            txtEditThanhPho.setText(thanhPho[1]);
                            loadDataDictricts(thanhPho[0]);
                            maThanhPho = thanhPho[0];
                        }
                        if (quan.length >= 2) {
                            txtEditQuan.setText(quan[1]);
                            loadDataWards(quan[0]);
                            maQuan = quan[0];
                        }
                        txtEditPhuong.setText(diaChi.getPhuong());
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DiaChi> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void loadDataCitys() {
        DataService dataService = APIService.getServiceAddress();
        Call<Citys> callback = dataService.getDataCitys();
        callback.enqueue(new Callback<Citys>() {
            @Override
            public void onResponse(Call<Citys> call, Response<Citys> response) {
                if (response.isSuccessful()) {
                    Citys citysResponse = response.body();
                    citys = citysResponse;
                    txtEditThanhPho.setEnabled(true);
                    Log.d("KRT", "EditAddressActivity - Call API Citys size: " + citys.getResults().size());
                } else {
                    Log.d("KRT", "EditAddressActivity - Call API Citys Not Success");
                }

            }

            @Override
            public void onFailure(Call<Citys> call, Throwable t) {
                Log.d("KRT", "EditAddressActivity - Call API Citys onFailure: " + t.getMessage());
            }
        });
    }


    public void loadDataDictricts(String province) {
        DataService dataService = APIService.getServiceAddress();
        Call<Dictricts> callback = dataService.getDataDictricts(province);
        callback.enqueue(new Callback<Dictricts>() {
            @Override
            public void onResponse(Call<Dictricts> call, Response<Dictricts> response) {
                if (response.isSuccessful()) {
                    Dictricts dictrictsResponse = response.body();
                    dictricts = dictrictsResponse;
                    Log.d("KRT", "EditAddressActivity - Call API Dictricts size: " + dictricts.getResults().size());
                } else {
                    Log.d("KRT", "EditAddressActivity - Call API Dictricts fail");
                }
            }

            @Override
            public void onFailure(Call<Dictricts> call, Throwable t) {
                Log.d("KRT", "EditAddressActivity - Call API Dictricts onFailure: " + t.getMessage());
            }
        });
    }


    public void loadDataWards(String district) {
        DataService dataService = APIService.getServiceAddress();
        Call<Wards> callback = dataService.getDataWards(district);
        callback.enqueue(new Callback<Wards>() {
            @Override
            public void onResponse(Call<Wards> call, Response<Wards> response) {
                if (response.isSuccessful()) {
                    Wards wardsResponse = response.body();
                    wards = wardsResponse;
                    Log.d("KRT", "EditAddressActivity - Call API Wards size: " + wards.getResults().size());
                } else {
                    Log.d("KRT", "EditAddressActivity - Call API Wards Not Success");
                }
            }

            @Override
            public void onFailure(Call<Wards> call, Throwable t) {
                Log.d("KRT", "EditAddressActivity - Call API Wards onFailure: " + t.getMessage());
            }
        });
    }


    public void showDialogError(String error) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(EditAddressActivity.this);
        aBuilder.setTitle("Thông báo");
        aBuilder.setMessage(error);
        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = aBuilder.create();
        dialog.show();
    }

    //    private class updateDiaChiAsync extends AsyncTask<String,Void,Integer>{
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Integer doInBackground(String... strings) {
//            final int[] kq = {0};
//            DataService dataService = APIService.getService();
//            Call<StringRequest> callBack = dataService.updateDiaChi(strings[0],strings[1],strings[2],strings[3],strings[4],strings[5],strings[6]);
//            callBack.enqueue(new Callback<StringRequest>() {
//                @Override
//                public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
//                    if(response.isSuccessful()){
//                        StringRequest stringRequest = response.body();
//                        if(stringRequest.getStatus().equals("1")){
//                            Log.d("KRT","EditAddressAc - Sửa thành công");
//                            kq[0] = 1;
//                        }
//                        else{
//                            Log.d("KRT","EditAddressAc - Sửa không thành công");
//                        }
//                    }
//                    else{
//                        Log.d("KRT","EditAddressAc - Sửa lỗi Not Successful");
//                    }
//                }
//                @Override
//                public void onFailure(Call<StringRequest> call, Throwable t) {
//                    Log.d("KRT","EditAddressAc - Sửa onFailure: " + t.getMessage());
//                }
//            });
//            return kq[0];
//        }
//
//        @Override
//        protected void onPostExecute(Integer integer) {
//            Log.d("KRT","Onpost: " + integer);
//            if(integer == 1){
//                Intent intent = new Intent();
//                intent.putExtra("Result",1);
//                setResult(101,intent);
//                finish();
//                overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
//            }else{
//                Intent intent = new Intent();
//                intent.putExtra("Result",0);
//                setResult(101,intent);
//                finish();
//                overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
//            }
//        }
//    }
    public void moveScreen() {
        if (mode == 0) {
            Intent intent = new Intent(EditAddressActivity.this, ChooseDeliveryInfoActivity.class);
            intent.putExtra("MaKH", maKH);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (mode == 1) {
            Intent intent = new Intent(EditAddressActivity.this, ListAddressActivity.class);
            intent.putExtra("MaKH", maKH);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
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