package com.example.pokemongo_od;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class MapsActivity extends FragmentActivity implements PropertyChangeListener, OnMapReadyCallback {

    // Future Reference: https://stackoverflow.com/questions/29352051/keep-map-centered-regardless-of-where-you-pinch-zoom-on-android

    private Model model;
    private GoogleMap mMap;

    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final int DEFAULT_ZOOM = 15;
    private static final int MAX_ZOOM = 20;
    private static final int MIN_ZOOM = 14;

    private Marker mPositionMarker;
    private Marker[] wildPokemonMarkers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        model = Model.getInstance();
        model.addChangeListener(this);
        wildPokemonMarkers = new Marker[model.getNumWildPokemon()];

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(Model.Properties.CURRLOCATION.toString())) {
            updateLocationUI();
        } else if (event.getPropertyName().equals(Model.Properties.WILDPOKEMON.toString())) {
            int index = (Integer)event.getNewValue();
            if (wildPokemonMarkers[index] != null) {
                wildPokemonMarkers[index].remove();
            }
            wildPokemonMarkers[index] = mMap.addMarker(new MarkerOptions()
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromBitmap(model.resizeMapIcons("wild_pokemon", 100, 100)))
                    .anchor(0.5f, 0.5f)
                    .draggable(true)      // Enables marker dragging used for debugging
                    .position( (model.getWildPokemons()[index]).getCoordinates() ));
        }
        // TODO: Switch to a switch
//        switch (event.getPropertyName()) {
//            case Model.Properties.CURRLOCATION.toString():
//                updateLocationUI();
//                break;
//            default:
//        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Disable camera panning and rotation and restrict zoom
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMaxZoomPreference(MAX_ZOOM);
        mMap.setMinZoomPreference(MIN_ZOOM);

        // Enables marker dragging used for debugging
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                for (int i = 0; i < wildPokemonMarkers.length; i++) {
                    if (marker.equals(wildPokemonMarkers[i])) {
                        model.setWildPokemonCoordinates(i, marker.getPosition());
                    }
                }
            }
        });

        // Create position marker
        mPositionMarker = mMap.addMarker(new MarkerOptions()
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(Model.getInstance().resizeMapIcons("red_sprite", 100, 100)))
                .anchor(0.5f, 0.5f)
                .position(new
                        LatLng(model.getCurrLocation().getLatitude(), model.getCurrLocation().getLongitude())));

        // Turn on the My Location layer and the related control on the map
        updateLocationUI();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(model.getCurrLocation().getLatitude(),
                        model.getCurrLocation().getLongitude()), DEFAULT_ZOOM));
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (model.locationPermissionGranted()) {
                if (model.getCurrLocation() != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(model.getCurrLocation().getLatitude(),
                                    model.getCurrLocation().getLongitude()), mMap.getCameraPosition().zoom));
                    mPositionMarker.setPosition(new
                            LatLng(model.getCurrLocation().getLatitude(), model.getCurrLocation().getLongitude()));
                }
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    public void menuMe(View view) {
        // Create and Intent to start the second activity
        Intent menuIntent = new Intent(this, Menu.class);

        startActivity(menuIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, TitleScreen.class);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
