package com.example.pokemongo_od;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Controller implements PropertyChangeListener {

    private static Controller mInstance = null;

    private Model model;

    private static final String LOGTAG = Controller.class.getSimpleName();

    private final long locationUpdateInterval = 2 * 1000;
    private final int encounterRadius = 20;
    private final int OOBRadius = 1400;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Controller() {
        model = Model.getInstance();
        if (model == null) {
            Log.e(LOGTAG, "Error in Controller(): Model is null");
        } else {
            model.addChangeListener(this);
        }
    }

    public static Controller getInstance() {
        if (mInstance == null) {
            mInstance = new Controller();
        }
        return mInstance;
    }

    private void mapController() {
        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(model.getCurrActivity());
        initLocationCallback();
        getLocationPermission();
        getDeviceLocation();
        startLocationUpdates();
    }

    private void initLocationCallback() {
        mLocationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    model.setCurrLocation(location);

                    WildPokemon[] wildPokemons = model.getWildPokemons();

                    for (int i = 0; i < wildPokemons.length; i++) {
                        if (wildPokemons[i] != null) {
                            float[] results = new float[1];
                            Location.distanceBetween(
                                    wildPokemons[i].getCoordinates().latitude,
                                    wildPokemons[i].getCoordinates().longitude,
                                    model.getCurrLocation().getLatitude(),
                                    model.getCurrLocation().getLongitude(),
                                    results);
                            if (results[0] <= encounterRadius) {
                                // Found Pokemon
                                wildPokemons[i].setPokemonSeen();
                                Model.getInstance().addToStorage(wildPokemons[i]);
                                Toast myToast = Toast.makeText(model.getCurrActivity(),
                                        "Seen: " + wildPokemons[i].getNumber(),
                                        Toast.LENGTH_SHORT);
                                myToast.show();
                                model.respawnWildPokemon(i);
                            }
                            if (results[0] >= OOBRadius) {
                                // Pokemon out of range
                                model.respawnWildPokemon(i);
                            }
                        }
                    }

                    getLocationPermission();
                }
            }
        };
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionResult.
         */
        if (!model.locationPermissionGranted()) {
            if (ContextCompat.checkSelfPermission(model.getCurrActivity().getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                model.setLocationPermissionGranted(true);
            } else {
                ActivityCompat.requestPermissions(model.getCurrActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (model.locationPermissionGranted()) {
                final WildPokemon[] wildPokemons = model.getWildPokemons();
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(model.getCurrActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device
                            model.setCurrLocation(task.getResult());
                            for (int i = 0; i < wildPokemons.length; i++) {
                                model.respawnWildPokemon(i);
                            }
                        } else {
                            Log.d(LOGTAG, "Current location is null. Using defaults.");
                            Log.e(LOGTAG, "Exception: %s", task.getException());
                            model.setCurrLocation(model.getDefaultLocation());
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(locationUpdateInterval);

        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                Looper.getMainLooper());
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(Model.Properties.ACTIVITY.toString())) {
            Activity newActivity = (Activity)event.getNewValue();
            if (newActivity.getLocalClassName().equals(MapsActivity.class.getSimpleName())) {
                mapController();
            }
        }
    }

}
