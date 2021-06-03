package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ungdungbansach.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Sach;

public class SachSearchAdapter extends ArrayAdapter<Sach> {
    public SachSearchAdapter(@NonNull Context context, @NonNull List<Sach> objects) {
        super(context, 0, objects);
    }
    private class ViewHolder{
        ImageView imgItemSachSearch;
        TextView txtTenSachSearch;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        Sach sach = getItem(position);
        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_row_search,parent,false);
            holder = new ViewHolder();
            //
            holder.imgItemSachSearch = convertView.findViewById(R.id.imgItemSachSearch);
            holder.txtTenSachSearch = convertView.findViewById(R.id.txtTenSachSearch);
            //
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTenSachSearch.setText(sach.getTenSach());
        Picasso.with(this.getContext())
                .load(sach.getAnh())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgItemSachSearch);
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_list_search);
        convertView.startAnimation(animation);
        return convertView;
    }
}
