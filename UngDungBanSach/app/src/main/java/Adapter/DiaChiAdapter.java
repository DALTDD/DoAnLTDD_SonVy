package Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungbansach.R;

import java.util.ArrayList;

import Model.DiaChi;
import Model.OnCallBackDiaChi;
import Model.OnCallBackPTTT;

public class DiaChiAdapter extends RecyclerView.Adapter<DiaChiAdapter.DiaChiViewHolder> {
    Context context;
    ArrayList<DiaChi> arrayList;
    OnCallBackDiaChi onCallBackDiaChi;
    int lastSelectedPostion = -1;
    public DiaChiAdapter(Context context, ArrayList<DiaChi> arrayList, OnCallBackDiaChi onCallBackDiaChi) {
        this.context = context;
        this.arrayList = arrayList;
        this.onCallBackDiaChi = onCallBackDiaChi;
    }

    @NonNull
    @Override
    public DiaChiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.row_item_choose_address,parent,false);
        return new DiaChiViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaChiViewHolder holder, int position) {
        DiaChi diaChi = arrayList.get(position);
        if(diaChi == null){
            return;
        }
        if(diaChi.getDiaChiMacDinh().equals("1") && lastSelectedPostion == -1){
            holder.rdBtnDiaChi.setChecked(true);
            holder.txtMacDinh.setText("Địa chỉ mặc định");
            holder.imgDeleteDiaChi.setVisibility(View.GONE);
            holder.txtMacDinh.setTextColor(Color.parseColor("#00BCD4"));
            onCallBackDiaChi.onRadioButtonClick(position);
        }
        else if(lastSelectedPostion == position){
            holder.rdBtnDiaChi.setChecked(true);
            onCallBackDiaChi.onRadioButtonClick(position);
        }
        else{
            holder.rdBtnDiaChi.setChecked(false);
        }
        holder.txtTenSDT.setText(diaChi.getHoTen() + " - " + diaChi.getSdt());
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
        holder.txtDiaChiChoose.setText(diaChiMerger);
    }

    @Override
    public int getItemCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    public class DiaChiViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSDT, txtDiaChiChoose, txtMacDinh;
        ImageView imgEditDiaChi, imgDeleteDiaChi;
        RadioButton rdBtnDiaChi;
        public DiaChiViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            txtTenSDT = itemView.findViewById(R.id.txtTenSDT);
            txtDiaChiChoose = itemView.findViewById(R.id.txtDiaChiChoose);
            txtMacDinh = itemView.findViewById(R.id.txtMacDinh);
            rdBtnDiaChi = itemView.findViewById(R.id.rdBtnDiaChi);
            imgEditDiaChi = itemView.findViewById(R.id.imgEditDiaChi);
            imgDeleteDiaChi = itemView.findViewById(R.id.imgDeleteDiaChi);
            //
            rdBtnDiaChi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPostion = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
            //
            imgDeleteDiaChi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCallBackDiaChi.onDelete(getAdapterPosition());
                }
            });
            //
            imgEditDiaChi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCallBackDiaChi.onUpdate(getAdapterPosition());
                }
            });
        }
    }
}
