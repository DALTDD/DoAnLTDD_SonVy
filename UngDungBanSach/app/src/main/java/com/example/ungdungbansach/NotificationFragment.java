package com.example.ungdungbansach;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ungdungbansach.R;

import Model.Cart;
import Model.CartItem;


public class NotificationFragment extends Fragment {
    Cart cart;
    public NotificationFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("KRT", "So luong noti " + cart.getInstanceCart().size());
        for (CartItem item : cart.getInstanceCart()){
            Log.d("KRT", "Duyet carrt " + item.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }
}