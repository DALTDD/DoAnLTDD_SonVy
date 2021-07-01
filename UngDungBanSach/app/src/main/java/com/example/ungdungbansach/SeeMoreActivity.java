package com.example.ungdungbansach;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SeeMoreActivity extends AppCompatActivity {
    //AutoCompleteTextView auto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);
        //
        //auto = findViewById(R.id.autoComplete);
        //
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                //android.R.layout.simple_spinner_dropdown_item, COUNTRIES);

        //auto.setAdapter(adapter);
    }

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
    private boolean _isHidden = false;
    public boolean onOptionsItemSelected(MenuItem item) {
        // ...

        View menuItemView = findViewById(R.id.menu_overflow); // SAME ID AS MENU ID
        PopupMenu popupMenu = new PopupMenu(this, menuItemView);
        popupMenu.inflate(R.menu.popup_menu_sort);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.three:
                        Toast.makeText(SeeMoreActivity.this, "One", Toast.LENGTH_SHORT).show();
                        item.setCheckable(true);
                        item.setChecked(true);


                }
                return false;
            }
        });
        // ...
        popupMenu.show();
        // ...



        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

}