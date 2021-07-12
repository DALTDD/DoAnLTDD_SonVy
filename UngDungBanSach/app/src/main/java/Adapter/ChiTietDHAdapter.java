package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ungdungbansach.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import Model.ChiTietDH;

public class ChiTietDHAdapter extends RecyclerView.Adapter<ChiTietDHAdapter.CTDHViewHolder> {
    ArrayList<ChiTietDH> arrayList;
    Context context;

    public ChiTietDHAdapter(ArrayList<ChiTietDH> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CTDHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_row_detail_order, parent, false);
        return new CTDHViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull CTDHViewHolder holder, int position) {
        ChiTietDH chiTietDH = arrayList.get(position);
        if(chiTietDH != null) {
            holder.txtTenSachDH.setText(chiTietDH.getTenSach());
            holder.txtSoLuonDH.setText(chiTietDH.getSoLuong());
            Locale locale = new Locale("vi", "VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            holder.txtGiaSachDH.setText(numberFormat.format(Double.parseDouble(chiTietDH.getDonGia())));
            Picasso.with(context)
                    .load(chiTietDH.getAnh())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.imgDH);
        }
    }

    @Override
    public int getItemCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    public class CTDHViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenSachDH ,txtGiaSachDH ,txtSoLuonDH;
        ImageView imgDH;
        public CTDHViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            txtTenSachDH = itemView.findViewById(R.id.txtTenSachDH);
            txtGiaSachDH = itemView.findViewById(R.id.txtGiaSachDH);
            txtSoLuonDH = itemView.findViewById(R.id.txtSoLuonDH);
            imgDH = itemView.findViewById(R.id.imgDH);
        }
    }
}
