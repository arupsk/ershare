package com.Andriod.ER.com;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.Andriod.ER.R;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
//        navigationView.getMenu().getItem(0).setChecked(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case 100074:
                        Intent anIntent = new Intent(getApplicationContext(), AddBatteryActivity.class);
                        anIntent.putExtra("BatteryName", "mDeviceName");
                        startActivity(anIntent);
                        drawerLayout.closeDrawers();
                        break;
                    case 1000075:
                        SharedHelper.putKey(BaseActivity.this, "Login", "false");
                        Intent intent = new Intent(BaseActivity.this, LogIn.class);
                        startActivity(intent);
                        finishAffinity();
                }
                return false;
            }
        });
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }
}