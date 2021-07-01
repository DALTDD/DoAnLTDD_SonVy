package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungbansach.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Model.CartItem;

public class XacNhanRecyclerView extends RecyclerView.Adapter<XacNhanRecyclerView.XacNhanViewHolder> {
    Context context;
    ArrayList<CartItem> arrLstCartItem;
    public XacNhanRecyclerView(Context context, ArrayList<CartItem> arrLstCartItem){
        this.context = context;
        this.arrLstCartItem = arrLstCartItem;
    }
    @NonNull
    @Override
    public XacNhanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(this.context).inflate(R.layout.row_item_xacnhan, parent, false);
        return new XacNhanViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull XacNhanViewHolder holder, int position) {
        CartItem cartItem = arrLstCartItem.get(position);
        //
        holder.txtTenSachXacNhan.setText(cartItem.getTenSach());
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        holder.txtGiaKMXacNhan.setText(numberFormat.format(cartItem.getGiaKhuyenMai()));
        holder.txtSoLuongXacNhan.setText("x" + cartItem.getSoLuong());
        Picasso.with(this.context)
                .load(cartItem.getAnh())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgSachXacNhan);
    }

    @Override
    public int getItemCount() {
        if(arrLstCartItem != null){
            return arrLstCartItem.size();
        }
        return 0;
    }

    public class XacNhanViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSachXacNhan, txtGiaKMXacNhan, txtSoLuongXacNhan;
        ImageView imgSachXacNhan;
        public XacNhanViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            txtTenSachXacNhan = itemView.findViewById(R.id.txtTenSachXacNhan);
            txtGiaKMXacNhan = itemView.findViewById(R.id.txtGiaKMXacNhan);
            imgSachXacNhan = itemView.findViewById(R.id.imgSachXacNhan);
            txtSoLuongXacNhan= itemView.findViewById(R.id.txtSoLuongXacNhan);
            //

        }
    }
}
