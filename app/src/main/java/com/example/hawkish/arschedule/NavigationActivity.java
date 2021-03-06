package com.example.hawkish.arschedule;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;


public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InternetConnectivityListener {
    public static ImageView image;
    public static Switch mSwitch;
    public static BottomSheetBehavior bottomSheetBehavior;
    public static NavigationView navigationView;
    public static Fragment fragment = null;
    public static Class fragmentClass = null;
    public static boolean isCreated;
    public DrawerLayout drawer;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private InternetAvailabilityChecker mInternetAvailabilityChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        image = findViewById(R.id.imageView);
        drawer = findViewById(R.id.drawer_layout);

        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }

        });



        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getBackground().setAlpha(128);
        drawer.setScrimColor(Color.TRANSPARENT);

        hideSystemPanel();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        InternetAvailabilityChecker.init(this);
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);
        showCamera();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment instanceof ArFragment) {
                super.onBackPressed();
            } else {
                showCamera();
            }
        }
    }

    public void showCamera() {
        image.setImageResource(R.drawable.menu_black);
        fragmentClass = ArFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction().replace(R.id.Content, fragment).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_baza) {
            image.setImageResource(R.drawable.menu_black);
            fragmentClass = FragmentUz.class;
        } else if (id == R.id.nav_poradnik) {

            image.setImageResource(R.drawable.menu);
            fragmentClass = GuideFragment.class;
        } else if (id == R.id.nav_o_nas) {

            image.setImageResource(R.drawable.menu);
            fragmentClass = AboutUsFragment.class;
            return true;
        }

        fragmentManager.beginTransaction().detach(fragment);
        fragmentManager.beginTransaction().remove(fragment);

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        fragmentManager.beginTransaction().replace(R.id.Content, fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void hideSystemPanel() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isNetworkAvailable()) {
            if (!isConnected) {
                if (!isCreated) {
                    startActivity(new Intent(NavigationActivity.this, Connection.class));
                    isCreated = true;
                }
            }
        } else {
            if (!isCreated) {
                startActivity(new Intent(NavigationActivity.this, Connection.class));
                isCreated = true;
            }
        }
    }
}