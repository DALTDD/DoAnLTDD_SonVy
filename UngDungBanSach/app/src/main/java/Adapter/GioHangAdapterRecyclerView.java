package Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungbansach.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Model.CartItem;
import Model.OnCartCallBack;

public class GioHangAdapterRecyclerView extends RecyclerView.Adapter<GioHangAdapterRecyclerView.CartViewHolder> {
    Context context;
    ArrayList<CartItem> arrLstCartItem;
    OnCartCallBack onCartCallBack;

    public GioHangAdapterRecyclerView(Context context, ArrayList<CartItem> arrLstCartItem, OnCartCallBack onCartCallBack) {
        this.context = context;
        this.arrLstCartItem = arrLstCartItem;
        this.onCartCallBack = onCartCallBack;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(this.context).inflate(R.layout.row_cart_item_rc, parent, false);
        return new CartViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = arrLstCartItem.get(position);
        //
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        holder.txtTenSachGioHang.setText(cartItem.getTenSach());
        holder.txtGiaKMGioHang.setText(numberFormat.format(cartItem.getGiaKhuyenMai()));
        holder.txtSLGioHang.setText(String.valueOf(cartItem.getSoLuong()));
        if(cartItem.getSoLuong() == 1){
            holder.imgGiamSLGioHang.setEnabled(false);
        }
        if (cartItem.getGiaGoc() == cartItem.getGiaKhuyenMai()) {
            holder.txtGiaGocGioHang.setText("");
            holder.txtPTGGGioHang.setText("");
        } else {
            double priceSub = cartItem.getGiaGoc() - cartItem.getGiaKhuyenMai();
            double percent = priceSub / cartItem.getGiaGoc();
            int percentInt = (int) (percent * 100);
            String ptGiaGia = "-" + String.valueOf(percentInt) + "%";
            holder.txtGiaGocGioHang.setText(numberFormat.format(cartItem.getGiaGoc()));
            holder.txtPTGGGioHang.setText(ptGiaGia);
        }
        Picasso.with(this.context)
                .load(cartItem.getAnh())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgSachGioHang);
        //
        holder.imgGiamSLGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCartCallBack.OnButtonCartCallBack();
                int soLuong = arrLstCartItem.get(holder.getAdapterPosition()).getSoLuong();
                if (soLuong > 1) {
                    soLuong--;
                    holder.txtSLGioHang.setText(String.valueOf(soLuong));
                    if (soLuong == 1) {
                        holder.imgGiamSLGioHang.setEnabled(false);
                    }
                    arrLstCartItem.get(holder.getAdapterPosition()).setSoLuong(soLuong);
                    //notifyItemChanged(holder.getAdapterPosition());
                    onCartCallBack.OnButtonCartCallBack();
                }
            }
        });
        //
        holder.imgTangSLGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soLuong = arrLstCartItem.get(holder.getAdapterPosition()).getSoLuong();//arrLstCartItem.get(position).getSoLuong();
                soLuong++;
                if (soLuong > 1) {
                    holder.imgGiamSLGioHang.setEnabled(true);
                }
                holder.txtSLGioHang.setText(String.valueOf(soLuong));
                //Cart.getInstanceCart().get(position).setSoLuong(soLuong);
                arrLstCartItem.get(holder.getAdapterPosition()).setSoLuong(soLuong);
                //notifyItemChanged(holder.getAdapterPosition());
                onCartCallBack.OnButtonCartCallBack();
            }
        });
        holder.imgXoaGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo!");
                builder.setMessage("Bạn có muốn xoá sản phẩm này ra khỏi giỏ hàng?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        arrLstCartItem.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        onCartCallBack.OnButtonDeleteCartCallBack();
                    }
                });
                builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                //arrLstCartItem.remove(holder.getAdapterPosition());
                //notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrLstCartItem != null) {
            return arrLstCartItem.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSachGioHang, txtGiaKMGioHang, txtGiaGocGioHang, txtPTGGGioHang, txtSLGioHang;
        ImageView imgSachGioHang, imgGiamSLGioHang, imgTangSLGioHang, imgXoaGioHang;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            txtTenSachGioHang = itemView.findViewById(R.id.txtTenSachXacNhan);
            txtGiaKMGioHang = itemView.findViewById(R.id.txtGiaKMXacNhan);
            txtGiaGocGioHang = itemView.findViewById(R.id.txtSoLuongXacNhan);
            txtPTGGGioHang = itemView.findViewById(R.id.txtPTGGXacNhan);
            txtSLGioHang = itemView.findViewById(R.id.txtSLGioHang);
            imgSachGioHang = itemView.findViewById(R.id.imgSachXacNhan);
            imgGiamSLGioHang = itemView.findViewById(R.id.imgGiamSLGioHang);
            imgTangSLGioHang = itemView.findViewById(R.id.imgTangSLGioHang);
            imgXoaGioHang = itemView.findViewById(R.id.imgXoaGioHang);
            //
            txtGiaGocGioHang.setPaintFlags(txtGiaGocGioHang.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //
//            imgGiamSLGioHang.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onCartCallBack.OnButtonCartCallBack();
//                    int soLuong = arrLstCartItem.get(getPosition()).getSoLuong();
//                    if(soLuong > 1){
//                        soLuong--;
//                        txtSLGioHang.setText(String.valueOf(soLuong));
//                        if(soLuong == 1){
//                            imgGiamSLGioHang.setEnabled(false);
//                        }
//                    }
//                }
//            });
//            //
//            imgTangSLGioHang.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onCartCallBack.OnButtonCartCallBack();
//                    int soLuong = arrLstCartItem.get(getPosition()).getSoLuong();
//                    soLuong++;
//                    if(soLuong > 1){
//                        imgGiamSLGioHang.setEnabled(true);
//                    }
//                    txtSLGioHang.setText(String.valueOf(soLuong));
//                }
//            });
        }
    }
}
