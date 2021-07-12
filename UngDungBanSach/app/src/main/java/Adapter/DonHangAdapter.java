package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ungdungbansach.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Model.DonHang;

public class DonHangAdapter extends BaseAdapter {
    ArrayList<DonHang> arrayList;
    Context context;

    public DonHangAdapter(ArrayList<DonHang> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(arrayList != null){
            return  arrayList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_row_orderhistory, parent, false);
        }
        //
        TextView txtMaDonHang, txtNgayDat ,txtTongTien ,txtTinhTrang;
        txtMaDonHang = convertView.findViewById(R.id.txtMaDonHang);
        txtNgayDat = convertView.findViewById(R.id.txtNgayDat);
        txtTongTien = convertView.findViewById(R.id.txtTongTien);
        txtTinhTrang = convertView.findViewById(R.id.txtTinhTrang);
        DonHang donHang = arrayList.get(position);
        if(donHang != null){
            txtMaDonHang.setText("#" + donHang.getMaDonHang());
            txtNgayDat.setText(donHang.getNgayDat());
            if(donHang.getTinhTrang().equals("-1")){
                txtTinhTrang.setText("Bị huỷ");
                txtTinhTrang.setBackgroundResource(R.drawable.background_order_cancel);
                //
            }else if( donHang.getTinhTrang().equals("0")){
                txtTinhTrang.setText("Đang xử lý");
                txtTinhTrang.setBackgroundResource(R.drawable.background_order_success);
                //
            }else if( donHang.getTinhTrang().equals("1")){
                txtTinhTrang.setText("Đang giao");
                txtTinhTrang.setBackgroundResource(R.drawable.background_order_success);
                //
            }
            else if( donHang.getTinhTrang().equals("2")){
                txtTinhTrang.setText("Đã nhận");
                txtTinhTrang.setBackgroundResource(R.drawable.background_order_success);
                //
            }
            Locale locale = new Locale("vi","VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            txtTongTien.setText(numberFormat.format(Double.parseDouble(donHang.getThanhTien())));
        }
        //
        return convertView;
    }
}
