package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungbansach.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Model.OnCallBackPTVC;
import Model.PTVanChuyen;
import Model.PhiVanChuyen;
import Model.PhuongThucTT;

public class PTVCAdapter extends RecyclerView.Adapter<PTVCAdapter.PTVCViewHolder> {
    PhiVanChuyen phiVanChuyen;
    Context context;
    OnCallBackPTVC onCallBackPTVC;
    double tongTien;
    int lastSelectedPositon = -1;
    public PTVCAdapter(PhiVanChuyen phiVanChuyen, Context context, OnCallBackPTVC onCallBackPTVC, double tongTien) {
        this.phiVanChuyen = phiVanChuyen;
        this.context = context;
        this.onCallBackPTVC = onCallBackPTVC;
        this.tongTien = tongTien;
    }

    @NonNull
    @Override
    public PTVCAdapter.PTVCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.row_item_ptvc,parent,false);
        return new PTVCViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull PTVCAdapter.PTVCViewHolder holder, int position) {
        PTVanChuyen ptVanChuyen = phiVanChuyen.getPTVanChuyen().get(position);
        if(ptVanChuyen == null)
        {
            return;
        }
        if(position == 0 && lastSelectedPositon == -1){
            holder.rdBtnPTVC.setChecked(true);
            onCallBackPTVC.onRadioButtonClick(position);
        }
        else if(position == lastSelectedPositon){
            holder.rdBtnPTVC.setChecked(true);
            onCallBackPTVC.onRadioButtonClick(position);
        }
        else{
            holder.rdBtnPTVC.setChecked(false);
        }
        Locale locale = new Locale("vi","VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        double phiMienPhi = Double.parseDouble(phiVanChuyen.getMienPhiVC());
        if(tongTien >= phiMienPhi){
            holder.rdBtnPTVC.setText(ptVanChuyen.getTenPTVC() + ": " + numberFormat.format(0));
        }
        else{
            holder.rdBtnPTVC.setText(ptVanChuyen.getTenPTVC() + ": " + numberFormat.format(Double.parseDouble(ptVanChuyen.getPhiVC())));
        }
    }

    @Override
    public int getItemCount() {
        if(phiVanChuyen.getPTVanChuyen() != null){
            return phiVanChuyen.getPTVanChuyen().size();
        }
        return 0;
    }

    public class PTVCViewHolder extends RecyclerView.ViewHolder {
        RadioButton rdBtnPTVC;
        public PTVCViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            rdBtnPTVC = itemView.findViewById(R.id.rdBtnPTVC);
            //
            rdBtnPTVC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPositon = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
