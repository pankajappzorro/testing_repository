package com.driver.cabscout.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.driver.cabscout.model.CSPreferences;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

import app.driver.cabscout.R;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private GoogleMap mMap;
    private static String TAG = HomeScreenActivity.class.getSimpleName();
    Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;
    DrawerLayout drawerLayout;
    Activity activity = this;
    double source_latitude, source_longitude;
    Circle mCircle;
    private float lastSpan = -1;
    private Handler handler = new Handler();
    private ScaleGestureDetector gestureDetector;
    static final int MAX_DURATION = 200;
    double lat, lang;

    float zoomLevel;
    Location mLastLocation;
    LatLng latLng;
    private long lastZoomTime = 0;

    private int fingers = 0;
    long startTime;

    String location;
    MenuItem shareditem;
    LocationManager locationManager;
    private int STORAGE_PERMISSION_CODE = 23;
    Switch myswitch;
    TextView txtstatus;
    View mapView;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initViews();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, STORAGE_PERMISSION_CODE);

        initNavigationDrawer(); // Drwaer initialize call

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        myswitch =(Switch)findViewById(R.id.checkbox);
        txtstatus =(TextView)findViewById(R.id.txtstatus);
        if (myswitch.isChecked()){
            txtstatus.setText("Online");
        }
        else {
            txtstatus.setText("Offline");
        }
        myswitch.setChecked(true);
      myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if (b){
                  txtstatus.setText("Online");
              }
              else {
                  txtstatus.setText("Offline");
              }
          }
      });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
        mMap.setMyLocationEnabled(true);
        //mMap.setPadding(left, top, right, bottom);
        mMap.setPadding(100,800, 20, 100);

    }    //  END CODE HERE

    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.map:
                Intent intentMap = new Intent(this, HomeScreenActivity.class);
                startActivity(intentMap);
                break;

            case R.id.rideHistory:
                Intent intHistory = new Intent(this, HistoryActivity.class);
                startActivity(intHistory);
                break;
            case R.id.myBooking:
                Intent intentMybooking= new Intent(this,BookingActivity.class);
                startActivity(intentMybooking);
                break;
            case R.id.settings:
                Intent intentSetting= new Intent(this,SettingActivity.class);
                startActivity(intentSetting);
                break;

            case R.id.contact:
                Intent intentContact= new Intent(this,ContactActivity.class);
                startActivity(intentContact);
                break;

            case R.id.logout:
                Intent intLogout = new Intent(this, CabCompanyActivity.class);
                startActivity(intLogout);
                finish();
                CSPreferences.clearPref(activity);
                CSPreferences.putString(activity, "login_status", "false");
                break;
            }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        Log.e("on connected","");

        initCamera(mLastLocation);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.disconnect();
    }

    // Current location
    private void initCamera(Location mLocation) {


       //
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        CameraPosition position;

        if (mLocation != null) {
            Log.e(TAG, "Current latitude: "+mLocation.getLatitude());
            position = CameraPosition
                    .builder().target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                    .zoom(15f)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();
            Log.e(TAG, "Position ="+position);
        }
        else {
            Log.e(TAG, "lOCATION IS EMPTY");
            position = CameraPosition
                    .builder().target(new LatLng(40.707204,
                            50.688556))
                    .zoom(15f)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();
        }


        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("HomeScreen Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
}
