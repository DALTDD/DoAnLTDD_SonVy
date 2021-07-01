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

import java.util.ArrayList;
import java.util.List;

import Model.OnCallBack;
import Model.Sach;

public class RecyclerViewLoadMore extends RecyclerView.Adapter<RecyclerViewLoadMore.ViewHolder> {
    Context context;
    ArrayList<Sach> arrLstSach;
    OnCallBack itemRecyclerViewListener;
    public RecyclerViewLoadMore(Context context, ArrayList<Sach> arrLstSach, OnCallBack itemRecyclerViewListener) {
        this.context = context;
        this.arrLstSach = arrLstSach;
        this.itemRecyclerViewListener = itemRecyclerViewListener;
    }
    @NonNull
    @Override
    public RecyclerViewLoadMore.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(this.context).inflate(R.layout.item_recyclerview_home, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewLoadMore.ViewHolder holder, int position) {
        Sach sach = arrLstSach.get(position);
        holder.txtTenSachHome.setText(sach.getTenSach());
    }

    @Override
    public int getItemCount() {
        if(arrLstSach != null){
            return  arrLstSach.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSachHome, txtGiaGocHome, txtGiaKMHome, txtPTGiamGia;
        ImageView imgItemSachHome, imgTagDiscount;
        public ViewHolder(@NonNull View itemView) {
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
