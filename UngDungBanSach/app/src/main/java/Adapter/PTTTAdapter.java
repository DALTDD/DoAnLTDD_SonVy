package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungbansach.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Model.OnCallBackPTTT;
import Model.PhuongThucTT;

public class PTTTAdapter extends RecyclerView.Adapter<PTTTAdapter.PTTTViewHolder> {
    Context context;
    ArrayList<PhuongThucTT> arrayList;
    OnCallBackPTTT onCallBackPTTT;
    private int lastSelectedPosition = -1;
    public PTTTAdapter(Context context, ArrayList<PhuongThucTT> arrayList, OnCallBackPTTT onCallBackPTTT){
        this.context = context;
        this.arrayList = arrayList;
        this.onCallBackPTTT = onCallBackPTTT;
    }
    @NonNull
    @Override
    public PTTTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.row_item_pttt,parent,false);
        return new PTTTViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull PTTTViewHolder holder, int position) {
        PhuongThucTT phuongThucTT = arrayList.get(position);
        if(phuongThucTT == null){
            return;
        }
        if (position == 0 && lastSelectedPosition == -1) {
            holder.rdbtnPTTT.setChecked(true);
            onCallBackPTTT.onRadioButtonClick(position);
        }
        else if(lastSelectedPosition == position){
            holder.rdbtnPTTT.setChecked(true);
            onCallBackPTTT.onRadioButtonClick(position);
        }
        else{
            holder.rdbtnPTTT.setChecked(false);
        }
        holder.txtTenPTTT.setText(phuongThucTT.getTenPTTT());
        Picasso.with(context)
                .load(phuongThucTT.getAnh())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgPTTT);
        //

    }

    @Override
    public int getItemCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    public class PTTTViewHolder extends RecyclerView.ViewHolder {
        RadioButton rdbtnPTTT;
        ImageView imgPTTT;
        TextView txtTenPTTT;
        public PTTTViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            rdbtnPTTT = itemView.findViewById(R.id.rdbtnPTTT);
            imgPTTT = itemView.findViewById(R.id.imgPTTT);
            txtTenPTTT = itemView.findViewById(R.id.txtTenPTTT);
            //
            rdbtnPTTT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
