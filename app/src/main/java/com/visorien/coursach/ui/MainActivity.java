package com.visorien.coursach.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.visorien.coursach.R;
import com.visorien.coursach.data.local.PreferencesHelper;
import com.visorien.coursach.ui.auth.AuthActivity;
import com.visorien.coursach.ui.schedule.ScheduleFragment;
import com.visorien.coursach.ui.schedule.ScheduleViewPagerFragment;
import com.visorien.coursach.ui.settings.SettingsFragment;
import com.visorien.coursach.ui.subjects.SubjectsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView navName;
    TextView navGroup;
    TextView avatarLetter;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sharedPref = new PreferencesHelper(getApplicationContext()).getPref();
        editor = sharedPref.edit();

        // Check if user is already logged in or not
        boolean isAuthorized = sharedPref.getBoolean("isAuthorized", false);
        if (!isAuthorized) {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
            finish();
        }
        View headerView = navigationView.getHeaderView(0);
        navName = (TextView) headerView.findViewById(R.id.nav_name);
        navGroup = (TextView) headerView.findViewById(R.id.nav_group);
        avatarLetter = (TextView) headerView.findViewById(R.id.avatar_letter);
        setUserMenuData();//show name and group at navigation drawer

        openScheduleMenuItem();//open default fragment on start
        navigationView.getMenu().getItem(0).setChecked(true);//mark it in menu as selected
    }

    private void openScheduleMenuItem() {
        Fragment fragment = new ScheduleViewPagerFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();
    }

    private void setUserMenuData() {
        String userName = sharedPref.getString("name", "none");
        String userGroup = sharedPref.getString("group", "none");

        String role = sharedPref.getString("role", "none");

        if(!userName.equals("none")) {
            navName.setText(userName);
            avatarLetter.setText(String.valueOf(userName.charAt(0)));
        } else {
            navName.setText("Error");
        }
        if(!userGroup.equals("none")) {
            navGroup.setText(userGroup);
        }
        if(!role.equals("student")) {
            navGroup.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_shedule) {
            fragment = new ScheduleViewPagerFragment();
        } else if (id == R.id.nav_subjects) {
            fragment = new SubjectsFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_logout) {
            editor.clear().commit();
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
            finish();
        }
        if(fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
