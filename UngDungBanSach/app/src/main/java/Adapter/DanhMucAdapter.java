package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ungdungbansach.R;

import java.util.List;

import Model.DanhMuc;

public class DanhMucAdapter extends ArrayAdapter<DanhMuc> {
    public DanhMucAdapter(@NonNull Context context, @NonNull List<DanhMuc> objects) {
        super(context, 0, objects);
    }
    private int selectedId =-1;
    public void setSelectedId(int pos){
        selectedId = pos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DanhMuc danhMuc = getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(R.layout.dong_danhmuc,parent,false);
        //anh xa
        TextView txtTenDanhMuc = convertView.findViewById(R.id.txtTenDanhMuc);
        TextView txtMau = convertView.findViewById(R.id.txtMau);
        txtTenDanhMuc.setText(danhMuc.getTenDanhMuc());
        if(selectedId == position){
            txtTenDanhMuc.setTextColor(Color.parseColor("#00BCD4"));
            txtMau.setBackgroundColor(Color.parseColor("#00BCD4"));
        }else{
            txtTenDanhMuc.setTextColor(Color.parseColor("#808080"));
            txtMau.setBackgroundColor(Color.parseColor("#65E5E5E5"));
        }
        return convertView;
    }
}
