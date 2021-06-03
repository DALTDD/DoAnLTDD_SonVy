package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayoutMain;
    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Lien ket control
        linkControl();

        //Su kien Selected item BottomNavigation
        bottomNav.setOnNavigationItemSelectedListener(navItemSelected);
        bottomNav.setSelectedItemId(R.id.mnNavHome);
        //Thay doi mau ActionBar
        if(getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
        }
        showNumberIconNav(999,R.id.mnNavCart);

    }
    private void linkControl(){
        frameLayoutMain = findViewById(R.id.frameLayoutMain);
        bottomNav = findViewById(R.id.bottomNav);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navItemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch(item.getItemId()){
                case R.id.mnNavHome:{
                    if(getSupportActionBar() != null){
                        //getSupportActionBar().setTitle("");
                        getSupportActionBar().hide();
                    }
                    fragment = new HomeFragment();
                    displayFragment(fragment);
                    return true;
                }
                case R.id.mnNavDashboard:{
                    if(getSupportActionBar() != null){
                        getSupportActionBar().setTitle(R.string.ttDashboard);
                        getSupportActionBar().show();
                    }
                    fragment = new DashboardFragment();
                    displayFragment(fragment);
                    return true;
                }
                case R.id.mnNavCart:{
                    if(getSupportActionBar() != null){
                        getSupportActionBar().setTitle(R.string.ttCart);
                        getSupportActionBar().show();
                    }
                    fragment = new CartFragment();
                    displayFragment(fragment);
                    return true;
                }
                case R.id.mnNavNoti:{
                    if(getSupportActionBar() != null){
                        getSupportActionBar().setTitle(R.string.ttNoti);
                        getSupportActionBar().show();
                    }
                    fragment = new NotificationFragment();
                    displayFragment(fragment);
                    return true;
                }
                case R.id.mnNavAccount:{
                    if(getSupportActionBar() != null){
                        getSupportActionBar().setTitle(R.string.ttAccount);
                        getSupportActionBar().show();
                    }
                    fragment = new AccountFragment();
                    displayFragment(fragment);
                    return true;
                }
            }
            return false;
        }
    };
    private void displayFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_right_to_left);
        transaction.replace(R.id.frameLayoutMain, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
    private void showNumberIconNav(int number,int menuItemId){
        BadgeDrawable badge = bottomNav.getOrCreateBadge(menuItemId);
        badge.setBackgroundColor(Color.parseColor("#E23C3C"));
        badge.setBadgeTextColor(Color.WHITE);
        if(badge.isVisible() == false) {
            badge.setVisible(true);
        }
        badge.setNumber(number);
    }
    private void hideNumberIconNav(int number,int menuItemId){
        BadgeDrawable badge = bottomNav.getOrCreateBadge(menuItemId);
        if(badge != null) {
            badge.setVisible(false);
            badge.clearNumber();
        }
    }
}