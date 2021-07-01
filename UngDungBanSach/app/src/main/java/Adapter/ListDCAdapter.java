package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungbansach.R;

import java.util.ArrayList;

import Model.DiaChi;
import Model.OnCallBackListDC;

public class ListDCAdapter extends RecyclerView.Adapter<ListDCAdapter.ListDCViewHolder>{
    Context context;
    ArrayList<DiaChi> diaChiArrayList;
    OnCallBackListDC onCallBackListDC;

    public ListDCAdapter(Context context, ArrayList<DiaChi> diaChiArrayList, OnCallBackListDC onCallBackListDC) {
        this.context = context;
        this.diaChiArrayList = diaChiArrayList;
        this.onCallBackListDC = onCallBackListDC;
    }

    @NonNull
    @Override
    public ListDCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.row_item_list_address, parent,false);
        return new ListDCViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListDCViewHolder holder, int position) {
        DiaChi diaChi = diaChiArrayList.get(position);
        if(diaChi == null){
            return;
        }
        holder.txtTenListDC.setText(diaChi.getHoTen());
        holder.txtSDTListDC.setText(diaChi.getSdt());
        String[] thanhPho = diaChi.getThanhPho().split(";");
        String[] quan = diaChi.getQuan().split(";");
        String diaChiMerger = "";
        diaChiMerger = diaChi.getDiaChiNha() + ", " + diaChi.getPhuong() + ", ";
        if(quan.length >= 2){
            diaChiMerger += quan[1] + ", ";
        }
        if(thanhPho.length >= 2){
            diaChiMerger += thanhPho[1];
        }
        holder.txtDCListDC.setText(diaChiMerger);
        if(diaChi.getDiaChiMacDinh().equals("1")){
            holder.txtMacDinhListDC.setText("Địa chỉ mặc định");
            holder.txtMacDinhListDC.setTextColor(Color.parseColor("#00BCD4"));
        }
        else{
            holder.txtMacDinhListDC.setText("Địa chỉ khác");
            holder.txtMacDinhListDC.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        if(diaChiArrayList != null){
            return diaChiArrayList.size();
        }
        return 0;
    }

    public class ListDCViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenListDC, txtSDTListDC, txtDCListDC, txtMacDinhListDC;
        ImageView imgInfoListDC;
        public ListDCViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            txtTenListDC = itemView.findViewById(R.id.txtTenListDC);
            txtSDTListDC = itemView.findViewById(R.id.txtSDTListDC);
            txtDCListDC = itemView.findViewById(R.id.txtDCListDC);
            txtMacDinhListDC = itemView.findViewById(R.id.txtMacDinhListDC);
            imgInfoListDC = itemView.findViewById(R.id.imgInfoListDC);
            //
            imgInfoListDC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCallBackListDC.onMenuClick(getAdapterPosition());
                }
            });
        }
    }
}
