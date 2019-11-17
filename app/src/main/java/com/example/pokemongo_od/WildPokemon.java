package com.example.pokemongo_od;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.Random;

public class WildPokemon extends Pokemon {

    private Marker marker;

    public WildPokemon(@NonNull MapsActivity activity, @NonNull GoogleMap map, @NonNull Location currentLocation) {
        super(activity);
        Random r = new Random();

        LatLng position = SphericalUtil.computeOffset(
                new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                r.nextInt(1200),
                r.nextInt(360));

        // Enables marker dragging used for debugging
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

        marker = map.addMarker(new MarkerOptions()
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(activity.resizeMapIcons("wild_pokemon", 100, 100)))
                .anchor(0.5f, 0.5f)
                //.draggable(true)      // Enables marker dragging used for debugging
                .position(position));
    }

    public LatLng getPosition() {
        return marker.getPosition();
    }

    public void setPosition(LatLng position) {
        marker.setPosition(position);
    }

    public void close() {
        marker.remove();
    }
}
