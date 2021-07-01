package Adapter;

import android.content.Context;
import android.graphics.Paint;
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

import Model.OnCallBack;
import Model.Sach;
import Model.SachLite;

public class SachAdapterRecyclerView extends RecyclerView.Adapter<SachAdapterRecyclerView.SachViewHolder> {
    Context context;
    ArrayList<SachLite> arrLstSach;
    OnCallBack itemRecyclerViewListener;

    public SachAdapterRecyclerView(Context context, ArrayList<SachLite> arrLstSach, OnCallBack itemRecyclerViewListener) {
        this.context = context;
        this.arrLstSach = arrLstSach;
        this.itemRecyclerViewListener = itemRecyclerViewListener;
    }

    @NonNull
    @Override
    public SachAdapterRecyclerView.SachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(this.context).inflate(R.layout.item_recyclerview_home, parent, false);
        return new SachViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull SachAdapterRecyclerView.SachViewHolder holder, int position) {
        SachLite sach = arrLstSach.get(position);
        holder.txtTenSachHome.setText(sach.getTenSach());
        //
        double price = Double.parseDouble(sach.getGiaGoc());
        double priceDiscount = Double.parseDouble(sach.getGiaKhuyenMai());
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        holder.txtGiaGocHome.setText(numberFormat.format(price));
        holder.txtGiaKMHome.setText(numberFormat.format(priceDiscount));
        if (sach.getGiaGoc().equals(sach.getGiaKhuyenMai())) {
            holder.imgTagDiscount.setVisibility(View.GONE);
            holder.txtPTGiamGia.setVisibility(View.GONE);
            holder.txtGiaGocHome.setVisibility(View.INVISIBLE);
        } else {
            double priceSub = price - priceDiscount;
            double percent = priceSub / price ;
            int percentInt = (int) (percent*100);
            String ptGiaGia = "-" + String.valueOf(percentInt) + "%";
            holder.txtPTGiamGia.setText(ptGiaGia);
        }
        Picasso.with(this.context)
                .load(sach.getAnh())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgItemSachHome);
    }

    @Override
    public int getItemCount() {
        if(arrLstSach != null) {
            return arrLstSach.size();
        }
        return  0;
    }

    public class SachViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSachHome, txtGiaGocHome, txtGiaKMHome, txtPTGiamGia;
        ImageView imgItemSachHome, imgTagDiscount;

        public SachViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSachHome = itemView.findViewById(R.id.txtTenSachHome);
            txtGiaGocHome = itemView.findViewById(R.id.txtGiaGocHome);
            txtGiaKMHome = itemView.findViewById(R.id.txtGIaKMHome);
            txtPTGiamGia = itemView.findViewById(R.id.txtPTGiamGia);
            imgItemSachHome = itemView.findViewById(R.id.imgItemSachHome);
            imgTagDiscount = itemView.findViewById(R.id.imgTagDiscount);
            //
            txtGiaGocHome.setPaintFlags(txtGiaGocHome.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemRecyclerViewListener.onItemRecyclerViewClick(getPosition());
                }
            });
        }
    }
}
