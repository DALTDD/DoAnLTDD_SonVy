package com.example.ungdungbansach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Model.Cart;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayoutMain;
    static BottomNavigationView bottomNav;
    Cart cart;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Lien ket control
        linkControl();
        //
        setSupportActionBar(toolbar);
        //
        //Su kien Selected item BottomNavigation
        bottomNav.setOnNavigationItemSelectedListener(navItemSelected);
        bottomNav.setSelectedItemId(R.id.mnNavHome);
        //Thay doi mau ActionBar
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.color_gradient));
//        }
        //Load so luong icon giohang
        loadNumberCart();
    }

    public void loadNumberCart() {
        int numberCart = cart.getInstanceCart().size();
        if (numberCart > 0) {
            showNumberIconNav(numberCart);
        } else {
            hideNumberIconNav();
        }
    }

    private void linkControl() {
        frameLayoutMain = findViewById(R.id.frameLayoutMain);
        bottomNav = findViewById(R.id.bottomNav);
        toolbar = findViewById(R.id.toolbar);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navItemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.mnNavHome: {
                    if (getSupportActionBar() != null) {
                        //getSupportActionBar().setTitle("");
                        getSupportActionBar().hide();
                    }
                    Fragment fragmentCheckHome = getSupportFragmentManager().findFragmentByTag("Home");
                    if (fragmentCheckHome == null) {
                        fragment = new HomeFragment();
                        displayFragment(fragment, "Home");
                    }
                    return true;
                }
                case R.id.mnNavDashboard: {
                    Fragment fragmentCheckDashboard = getSupportFragmentManager().findFragmentByTag("Dashboard");
                    if (fragmentCheckDashboard == null) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(R.string.ttDashboard);
                            getSupportActionBar().show();
                        }
                        fragment = new DashboardFragment();
                        displayFragment(fragment, "Dashboard");
                    }
                    return true;
                }
                case R.id.mnNavCart: {

                    Fragment fragmentCheckCart = getSupportFragmentManager().findFragmentByTag("Cart");
                    if (fragmentCheckCart == null) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(R.string.ttCart);
                            getSupportActionBar().show();
                        }
                        fragment = new CartFragment();
                        displayFragment(fragment, "Cart");
                    }
                    return true;
                }
                case R.id.mnNavNoti: {

                    Fragment fragmentCheckNoti = getSupportFragmentManager().findFragmentByTag("Noti");
                    if (fragmentCheckNoti == null) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(R.string.ttNoti);
                            getSupportActionBar().show();
                        }
                        fragment = new NotificationFragment();
                        displayFragment(fragment, "Noti");
                    }
                    return true;
                }
                case R.id.mnNavAccount: {

                    Fragment fragmentCheckAccount = getSupportFragmentManager().findFragmentByTag("Account");
                    if (fragmentCheckAccount == null) {
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setTitle(R.string.ttAccount);
                            getSupportActionBar().show();
                        }
                        if (checkLogin()) {
                            fragment = new AccountFragment();
                        }
                        else {
                            fragment = new LoginFragment();
                        }
                        displayFragment(fragment, "Account");
                    }
                    return true;
                }
            }
            return false;
        }
    };

    private void displayFragment(Fragment fragment, String tag) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_right_to_left);
        transaction.replace(R.id.frameLayoutMain, fragment, tag);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
//    private void showNumberIconNav(int number,int menuItemId){
//        BadgeDrawable badge = bottomNav.getOrCreateBadge(menuItemId);
//        badge.setBackgroundColor(Color.parseColor("#E23C3C"));
//        badge.setBadgeTextColor(Color.WHITE);
//        if(badge.isVisible() == false) {
//            badge.setVisible(true);
//        }
//        badge.setNumber(number);
//    }
//    private void hideNumberIconNav(int menuItemId){
//        BadgeDrawable badge = bottomNav.getOrCreateBadge(menuItemId);
//        if(badge != null) {
//            badge.setVisible(false);
//            badge.clearNumber();
//        }
//    }

    public static void showNumberIconNav(int number) {
        BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.mnNavCart);
        badge.setBackgroundColor(Color.parseColor("#E23C3C"));
        badge.setBadgeTextColor(Color.WHITE);
        if (badge.isVisible() == false) {
            badge.setVisible(true);
        }
        badge.setNumber(number);
    }

    public static void hideNumberIconNav() {
        BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.mnNavCart);
        if (badge != null) {
            badge.setVisible(false);
            badge.clearNumber();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadNumberCart();
    }

    @Override
    public void onBackPressed() {
        if (bottomNav.getSelectedItemId() == R.id.mnNavHome) {
            super.onBackPressed();
            finish();
        } else {
            bottomNav.setSelectedItemId(R.id.mnNavHome);
        }

    }

    public Boolean checkLogin() {
        SharedPreferences p = getSharedPreferences("caches", Context.MODE_PRIVATE);
        return p.getBoolean("Login", false);
    }
}