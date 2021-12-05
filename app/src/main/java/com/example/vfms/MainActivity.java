package com.example.vfms;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.vfms.tools.LocationUtils;
import com.example.vfms.ui.login.LoginActivity;
import com.example.vfms.ui.settings.SettingsActivity;
import com.github.dfqin.grantor.PermissionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.github.dfqin.grantor.PermissionsUtil;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private double _lat = 0.0d;
    private double _lng = 0.0d;
    private SharedPreferences sharedPreferences;
    public boolean isOnline = false;
    private static final String[] mPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askPermissions();
        sharedPreferences = getSharedPreferences(String.valueOf(R.string.preference_string), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (isConnected()) {
                List<Integer> quotes = loadQuotes();
                Random rand = new Random();
                Integer random = quotes.get(rand.nextInt(quotes.size()));
                Snackbar.make(view, random, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(view, R.string.not_connected, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        AppCompatDelegate.setDefaultNightMode(sharedPreferences.getInt(String.valueOf(R.string.current_mode), AppCompatDelegate.MODE_NIGHT_NO));
    }

    private List<Integer> loadQuotes() {
        return Arrays.asList(R.string.quote0, R.string.quote1, R.string.quote2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        String currentUser = sharedPreferences.getString(String.valueOf(R.string.current_username), null);
        if (currentUser != null) {
            TextView username = findViewById(R.id.display_name);
            TextView status = findViewById(R.id.display_status);
            if (!(username == null)) username.setText(currentUser);
            if (isConnected()) {
                if (!(status == null)) status.setText(R.string.nav_header_subtitle_online);
                isOnline = true;
            } else {
                if (!(status == null)) status.setText(R.string.nav_header_subtitle_offline);
                isOnline = false;
            }
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onClickImage(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                TextView username = findViewById(R.id.display_name);
                TextView connectionStatus = findViewById(R.id.display_status);
                assert data != null;
                String loginUsername = data.getStringExtra(String.valueOf(R.string.login_string));
                username.setText(loginUsername);
                connectionStatus.setText(R.string.nav_header_subtitle_online);
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void OnClickSettings(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void GetCoin(View view) {
        _lat=0.0d;
        _lng=0.0d;
        SharedPreferences sharedPreferences1 = getSharedPreferences(String.valueOf(R.string.preference_string), Context.MODE_PRIVATE);
        String username = sharedPreferences1.getString(String.valueOf(R.string.current_username), "null");
        SharedPreferences sharedPreferences2 = getSharedPreferences("keypair", Context.MODE_PRIVATE);
        String pubkey = sharedPreferences2.getString("publicKey", "null");
        String location1 = "暂无";
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        _lat=location.getAltitude();
        _lng=location.getLongitude();
        //requestPermissions();
        if(_lat==0.0d||_lng==0.0d)
        {
            Log.d("LocationError", "Error!!!!!");
        }
        Log.d("up", "username: "+username +"pubkey: "+pubkey+"loclat:"+String.valueOf(_lat)+"loclng:"+String.valueOf(_lng));
        TextView latText = (TextView) findViewById(R.id.latText);
        latText.setText("Altitude: " + _lat + "\nLongitude: " + _lng);
        BackgroundWorker backgroundWorker = new BackgroundWorker();
        backgroundWorker.execute("getcoin",username, pubkey,String.valueOf(_lat),String.valueOf(_lng));
        //String output = backgroundWorker.execute(username, pubkey,location).get();
    }
    private void askPermissions(){
        if (PermissionsUtil.hasPermission(this,mPermissions)) {
            //有访问权限
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    //用户授予了访问权限
                }
                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    //用户拒绝了访问的申请
                }
            }, mPermissions);
        }
    }
    private void requestPermissions() {
        if (PermissionsUtil.hasPermission(this,mPermissions)) {
            //有访问权限
            initLocation();
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    //用户授予了访问权限
                    initLocation();
                }
                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    //用户拒绝了访问的申请
                }
            }, mPermissions);
        }
    }
    private void initLocation(){
        LocationUtils.getInstance(this).setAddressCallback(new LocationUtils.AddressCallback() {
            @Override
            public void onGetAddress(Address address) {
                String countryName = address.getCountryName();//国家
                String adminArea = address.getAdminArea();//省
                String locality = address.getLocality();//市
                String subLocality = address.getSubLocality();//区
                String featureName = address.getFeatureName();//街道
                Log.d("定位地址",countryName+"//"+adminArea+"//"+locality+"//"+subLocality+"//"+featureName);
            }

            @Override
            public void onGetLocation(double lat, double lng) {
                _lat=lat;
                _lng=lng;
                Log.d("定位经纬度",lat+"//"+lng);
            }
        });
    }

}