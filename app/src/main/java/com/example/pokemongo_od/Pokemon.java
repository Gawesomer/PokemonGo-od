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

public class Pokemon {

    private int number;

    private enum CatchState {
        UNSEEN, SEEN, CAUGHT;
    }

    private CatchState mCatchState;

    public Pokemon() {
        Random r = new Random();

        number = r.nextInt(150);

        mCatchState = CatchState.UNSEEN;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPokemonSeen() {
        mCatchState = CatchState.SEEN;
    }

    public void setPokemonCaught() {
        mCatchState = CatchState.CAUGHT;
    }
}
