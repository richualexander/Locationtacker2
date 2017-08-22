package com.example.richu.locationtacker;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.richu.locationtacker.adapter.Mapadapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;

public class Mapactivity extends AppCompatActivity  implements OnMapReadyCallback{
    LocationManager locationManager;
    private GoogleMap mMap;
    String latitudeofuser,longitudeofuser,name,phone;
    Double lat,longg;
    Marker marker;



    RecyclerView recyclerview;
    Mapadapter adapter;
    Timer t = new Timer();
    ArrayList<String> latitudes;
    ArrayList<String> longitudes;
    ArrayList<String> names ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapactivity);

        SharedPreferences preferences =getSharedPreferences("location",0);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(Mapactivity.this);
        recyclerview=(RecyclerView)findViewById(R.id.recyclermap);

//        latitudeofuser= preferences.getString("latitude","");
//        longitudeofuser= preferences.getString("longitude","");
////        name= preferences.getString("nameofuser","");
//        phone= preferences.getString("phonenoofuser","");
//
//        lat = Double.parseDouble(latitudeofuser);
//        longg = Double.parseDouble(longitudeofuser);




        getalllocation();





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showCurrentLocation();

    } private void showCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        Criteria criteria = new Criteria();
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);

        String bestProvider = locationManager.getBestProvider(criteria, true);

        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null) {
            drawMarker(location);
        }
//        locationManager.requestLocationUpdates(bestProvider, MIN_TIME_BW_UPDATES,
//                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
    }
    private void drawMarker(Location location){
//        mMap.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:"+ location.getLongitude())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("ME"));
    }
    public void getalllocation(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fddb = database.getReference("Users");
        fddb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//        ArrayList<Object> list = new ArrayList<>();
//
//        for ( DataSnapshot roomDataSnapshot : dataSnapshot.getChildren() ) {
//            locations room = roomDataSnapshot.getValue(locations.class);
//
//            list.add(room.getlocations());
////                yourAdapter.notifyDataSetChanged();
//        }




                collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void collectPhoneNumbers(Map<String,Object> users) {


mMap.clear();


        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        names = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            latitudes.add(String.valueOf(singleUser.get("locationlat")));
            longitudes.add(String.valueOf(singleUser.get("locationlong")));
            names.add(String.valueOf(singleUser.get("name")));
            for (int i = 0 ;i <longitudes.size();i++){

                LatLng sydney = new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));

                mMap.addMarker(new MarkerOptions().position(sydney)
                        .title(names.get(i)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            }
            adapter = new Mapadapter(latitudes,longitudes,names);

            RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerview.setLayoutManager(rLayoutManager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            recyclerview.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

    }
}
