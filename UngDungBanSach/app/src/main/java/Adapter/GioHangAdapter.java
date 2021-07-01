package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ungdungbansach.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import Model.CartItem;


public class GioHangAdapter extends ArrayAdapter<CartItem> {
    public GioHangAdapter(@NonNull Context context, @NonNull List<CartItem> objects) {
        super(context,0, objects);
    }
    public class ViewHolder{
        public TextView txtTenSachGioHang, txtGiaKMGioHang, txtGiaGocGioHang, txtPTGGGioHang, txtSLGioHang;
        public ImageView imgSachGioHang, imgGiamSLGioHang, imgTangSLGioHang, imgXoaGioHang;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            //
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.row_cart_item,parent,false);
            //
            viewHolder.txtTenSachGioHang = convertView.findViewById(R.id.txtTenSachXacNhan);
            viewHolder.txtGiaKMGioHang = convertView.findViewById(R.id.txtGiaKMXacNhan);
            viewHolder.txtGiaGocGioHang = convertView.findViewById(R.id.txtSoLuongXacNhan);
            viewHolder.txtPTGGGioHang = convertView.findViewById(R.id.txtPTGGXacNhan);
            viewHolder.txtSLGioHang = convertView.findViewById(R.id.txtSLGioHang);
            viewHolder.imgSachGioHang = convertView.findViewById(R.id.imgSachXacNhan);
            viewHolder.imgGiamSLGioHang = convertView.findViewById(R.id.imgGiamSLGioHang);
            viewHolder.imgTangSLGioHang = convertView.findViewById(R.id.imgTangSLGioHang);
            viewHolder.imgXoaGioHang = convertView.findViewById(R.id.imgXoaGioHang);
            //
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CartItem cartItem = getItem(position);
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        viewHolder.txtTenSachGioHang.setText(cartItem.getTenSach());
        viewHolder.txtGiaKMGioHang.setText(numberFormat.format(cartItem.getGiaKhuyenMai()));
        viewHolder.txtSLGioHang.setText(String.valueOf(cartItem.getSoLuong()));
        if(cartItem.getGiaGoc() == cartItem.getGiaKhuyenMai()){
            viewHolder.txtGiaGocGioHang.setText("");
            viewHolder.txtPTGGGioHang.setText("");
        }
        else{
            double priceSub = cartItem.getGiaGoc() - cartItem.getGiaKhuyenMai();
            double percent = priceSub / cartItem.getGiaGoc();
            int percentInt = (int) (percent*100);
            String ptGiaGia = "-" + String.valueOf(percentInt) + "%";
            viewHolder.txtGiaGocGioHang.setText(numberFormat.format(cartItem.getGiaGoc()));
            viewHolder.txtPTGGGioHang.setText(ptGiaGia);
        }
        Picasso.with(this.getContext())
                .load(cartItem.getAnh())
                .placeholder(R.mipmap.ic_launcher)
                .into(viewHolder.imgSachGioHang);

        return convertView;
    }
}
