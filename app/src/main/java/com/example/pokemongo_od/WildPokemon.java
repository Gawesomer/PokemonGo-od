package com.example.pokemongo_od;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.Random;

public class WildPokemon extends Pokemon {

    private Model model;

    private LatLng coordinates;
    private final int spawnRadius = 1200;

    public WildPokemon() {
        super();

        model = Model.getInstance();

        Random r = new Random();

        Location currLocation = model.getCurrLocation();
        coordinates = SphericalUtil.computeOffset(
                new LatLng(currLocation.getLatitude(), currLocation.getLongitude()),
                r.nextInt(spawnRadius),
                r.nextInt(360));

    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng newCoordinates) {
        coordinates = newCoordinates;
    }
}
