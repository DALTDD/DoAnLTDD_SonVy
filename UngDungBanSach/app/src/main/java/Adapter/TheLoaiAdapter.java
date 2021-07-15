package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ungdungbansach.R;

import java.util.List;

import Model.TheLoai;

public class TheLoaiAdapter extends ArrayAdapter<TheLoai> {
    public TheLoaiAdapter(@NonNull Context context, @NonNull List<TheLoai> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TheLoai theLoai = getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        convertView = layoutInflater.inflate(R.layout.dong_theloai,parent,false);
        TextView txtTenTheLoai = convertView.findViewById(R.id.txtTenTheLoai);
        txtTenTheLoai.setText(theLoai.getTenTheLoai());
        return convertView;
    }

}
