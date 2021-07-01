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
import Model.Dictricts;
import Model.OnCallBackListDC;
import Model.ResultCity;
import Model.ResultDictrict;
import Model.ResultWard;
import Model.StringRequest;
import Model.Wards;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryInfoActivity extends AppCompatActivity {
    TextView txtThanhPho, txtQuan, txtPhuong;
    EditText edtHoTenGiaoHang, edtSDTGiaoHang, edtDiaChiNhaGiaoHang;
    Button btnXacNhanDiaChi;
    Dialog dialog;
    Citys citys;
    Dictricts dictricts;
    Wards wards;
    String maKH = "", maThanhPho = "", maQuan = "", maPhuong = "";
    int mode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_info);
        //
        //Thay doi mau Actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thêm địa chỉ giao hàng");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //
        if (getIntent().getStringExtra("MaKH") == null || (getIntent().getIntExtra("Mode", -1) == -1)) {
            finish();
        } else {
            maKH = getIntent().getStringExtra("MaKH");
            mode = getIntent().getIntExtra("Mode", -1);
        }
        //
        citys = new Citys();
        dictricts = new Dictricts();
        wards = new Wards();
        //
        linkWidget();
        //
        txtQuan.setEnabled(false);
        txtPhuong.setEnabled(false);
        txtThanhPho.setEnabled(false);
        //
        loadDataCitys();
        //
        txtThanhPho.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    txtQuan.setEnabled(false);
                    txtPhuong.setEnabled(false);
                } else {
                    txtQuan.setText("");
                    txtPhuong.setText("");
                    txtQuan.setEnabled(true);
                    txtPhuong.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        txtQuan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    txtPhuong.setEnabled(false);
                } else {
                    txtPhuong.setText("");
                    txtPhuong.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //
        txtThanhPho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(DeliveryInfoActivity.this);
                dialog.setContentView(R.layout.dialog_search_address);
                //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setLayout(950,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView txtTitle = dialog.findViewById(R.id.txtTittle);
                EditText edtSearchAddress = dialog.findViewById(R.id.edtSearchAddress);
                ListView listView = dialog.findViewById(R.id.lstAddress);
                TextView txtHuy = dialog.findViewById(R.id.txtHuy);
                txtTitle.setText("Chọn Tỉnh / Thành phố");
                if (citys.getResults() != null) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(DeliveryInfoActivity.this, android.R.layout.simple_list_item_1, citys.getResults());
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
                            txtThanhPho.setText(resultCity.getName());
                            maThanhPho = resultCity.getCode();
                            Log.d("KRT", "DeliveryInfoActivity - Ma thanh pho selected: " + maThanhPho);
                            loadDataDictricts(maThanhPho);
                            //new loadDictrictsAsync().execute(maThanhPho);
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
        txtQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(DeliveryInfoActivity.this);
                dialog.setContentView(R.layout.dialog_search_address);
                //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setLayout(950,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView txtTitle = dialog.findViewById(R.id.txtTittle);
                EditText edtSearchAddress = dialog.findViewById(R.id.edtSearchAddress);
                ListView listView = dialog.findViewById(R.id.lstAddress);
                TextView txtHuy = dialog.findViewById(R.id.txtHuy);
                txtTitle.setText("Chọn Quận / Huyện");
                if (dictricts.getResults() != null) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(DeliveryInfoActivity.this, android.R.layout.simple_list_item_1, dictricts.getResults());
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
                            txtQuan.setText(resultDictrict.getName());
                            maQuan = resultDictrict.getCode();
                            Log.d("KRT", "DeliveryInfoActivity - Ma quan selected: " + maQuan);
                            loadDataWards(maQuan);
                            //new loadWardAsync().execute(maQuan);
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
        txtPhuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(DeliveryInfoActivity.this);
                dialog.setContentView(R.layout.dialog_search_address);
                //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setLayout(950,WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView txtTitle = dialog.findViewById(R.id.txtTittle);
                EditText edtSearchAddress = dialog.findViewById(R.id.edtSearchAddress);
                ListView listView = dialog.findViewById(R.id.lstAddress);
                TextView txtHuy = dialog.findViewById(R.id.txtHuy);
                txtTitle.setText("Chọn Phường / Xã");
                if (wards.getResults() != null) {
                    ArrayAdapter arrayAdapter = new ArrayAdapter(DeliveryInfoActivity.this, android.R.layout.simple_list_item_1, wards.getResults());
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
                            txtPhuong.setText(resultWard.getName());
                            String ward = resultWard.getCode();
                            Log.d("KRT", "DeliveryInfoActivity - Ma Phuong selected: " + ward);
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
        btnXacNhanDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hoTen = edtHoTenGiaoHang.getText().toString();
                String sdt = edtSDTGiaoHang.getText().toString();
                String thanhPho = txtThanhPho.getText().toString();
                String quan = txtQuan.getText().toString();
                String phuong = txtPhuong.getText().toString();
                String soNha = edtDiaChiNhaGiaoHang.getText().toString();
                if (hoTen.equals("") || hoTen.isEmpty()) {
                    edtHoTenGiaoHang.setError("Vui lòng nhập họ tên!");
                    edtHoTenGiaoHang.requestFocus();
                    return;
                }
                if (sdt.equals("") || sdt.isEmpty()) {
                    edtSDTGiaoHang.setError("Vui lòng nhập SDT!");
                    edtSDTGiaoHang.requestFocus();
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
                    edtDiaChiNhaGiaoHang.setError("Vui lòng nhập số nhà, đường!");
                    edtDiaChiNhaGiaoHang.requestFocus();
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
                Dialog dialog = new Dialog(DeliveryInfoActivity.this);
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
                        ProgressDialog progressDialog = new ProgressDialog(DeliveryInfoActivity.this);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setContentView(R.layout.progress_load_data);
                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        //
                        // String[] parameter = {maKH,hoTen,sdt,iThanhPho,iQuan,phuong,soNha};
                        //new insertAddress().execute(parameter);
                        DataService dataService = APIService.getService();
                        Call<StringRequest> callBack = dataService.insertDeliveryAddress(maKH, hoTen, sdt, iThanhPho, iQuan, phuong, soNha);
                        callBack.enqueue(new Callback<StringRequest>() {
                            @Override
                            public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
                                if (response.isSuccessful()) {
                                    StringRequest stringRequest = response.body();
                                    if (stringRequest.getStatus().equals("1")) {
                                        Log.d("KRT", "DeliveryInfoActivity - Insert địa chỉ thành công");
                                    } else {
                                        Log.d("KRT", "DeliveryInfoActivity - Insert địa chỉ không thành công");
                                    }
                                } else {
                                    Log.d("KRT", "DeliveryInfoActivity - Insert địa chỉ không thành công - Not Success");
                                }
                                progressDialog.dismiss();
                                moveScreen();
                            }

                            @Override
                            public void onFailure(Call<StringRequest> call, Throwable t) {
                                Log.d("KRT", "DeliveryInfoActivity - Insert địa chỉ onFailure: " + t.getMessage());
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


    private void linkWidget() {
        txtThanhPho = findViewById(R.id.txtThanhPho);
        txtQuan = findViewById(R.id.txtQuan);
        txtPhuong = findViewById(R.id.txtPhuong);
        edtHoTenGiaoHang = findViewById(R.id.edtHoTenGiaoHang);
        edtSDTGiaoHang = findViewById(R.id.edtSDTGiaoHang);
        edtDiaChiNhaGiaoHang = findViewById(R.id.edtDiaChiNhaGiaoHang);
        btnXacNhanDiaChi = findViewById(R.id.btnXacNhanDiaChi);
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
                    Log.d("KRT", "DeliveryInfoActivity - Call API Citys size: " + citys.getResults().size());
                } else {
                    Log.d("KRT", "DeliveryInfoActivity - Call API Citys fail");
                }
                txtThanhPho.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Citys> call, Throwable t) {
                Log.d("KRT", "DeliveryInfoActivity - Call API Citys onFailure: " + t.getMessage());
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
                    Log.d("KRT", "DeliveryInfoAc - Call API Dictricts size: " + dictricts.getResults().size());
                } else {
                    Log.d("KRT", "DeliveryInfoAc - Call API Dictricts fail");
                }
            }

            @Override
            public void onFailure(Call<Dictricts> call, Throwable t) {
                Log.d("KRT", "DeliveryInfoAc - Call API Dictricts onFailure: " + t.getMessage());
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
                    Log.d("KRT", "DeliveryInfoAc - Call API Wards size: " + wards.getResults().size());
                } else {
                    Log.d("KRT", "DeliveryInfoAc - Call API Wards fail");
                }
            }

            @Override
            public void onFailure(Call<Wards> call, Throwable t) {
                Log.d("KRT", "DeliveryInfoAc - Call API Wards onFailure: " + t.getMessage());
            }
        });
    }



//    private class insertAddress extends AsyncTask<String,Void,Void>{
//        ProgressDialog progressDialog;
//        @Override
//        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(DeliveryInfoActivity.this);
//            progressDialog.show();
//            progressDialog.setCancelable(false);
//            progressDialog.setContentView(R.layout.progress_load_data);
//            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        }
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            DataService dataService = APIService.getService();
//            Call<StringRequest> callBack = dataService.insertDeliveryAddress(strings[0],strings[1],strings[2],strings[3],strings[4],strings[5],strings[6]);
//           callBack.enqueue(new Callback<StringRequest>() {
//               @Override
//               public void onResponse(Call<StringRequest> call, Response<StringRequest> response) {
//                   if(response.isSuccessful()){
//                       StringRequest stringRequest = response.body();
//                       if(stringRequest.getStatus().equals("1")){
//                           Log.d("KRT", "DeliveryInfoAc - Insert địa chỉ thành công");
//                       }
//                       else{
//                           Log.d("KRT", "DeliveryInfoAc - Insert địa chỉ không thành công");
//                       }
//                   }
//                   else{
//                       Log.d("KRT", "DeliveryInfoAc - Insert địa chỉ không thành công - Not Success");
//                   }
//               }
//
//               @Override
//               public void onFailure(Call<StringRequest> call, Throwable t) {
//                   Log.d("KRT", "DeliveryInfoAc - Insert địa chỉ onFailure: " + t.getMessage());
//               }
//           });
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            Log.d("KRT", "DeliveryInfoAc - Insert địa chỉ thành công POST");
//            progressDialog.dismiss();
//            if (mode == 0) {
//                Intent intent = new Intent(DeliveryInfoActivity.this, ChooseDeliveryInfoActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            } else if (mode == 1) {
//                Intent intent = new Intent(DeliveryInfoActivity.this, ListAddressActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//        }
//    }


    public void showDialogError(String error) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(DeliveryInfoActivity.this);
        aBuilder.setTitle("Thông báo!");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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

    public void moveScreen() {
        if (mode == 0) {
            Intent intent = new Intent(DeliveryInfoActivity.this, ChooseDeliveryInfoActivity.class);
            intent.putExtra("MaKH",maKH);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (mode == 1) {
            Intent intent = new Intent(DeliveryInfoActivity.this, ListAddressActivity.class);
            intent.putExtra("MaKH",maKH);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}