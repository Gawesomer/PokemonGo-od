package com.example.pokemongo_od;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Random;

public class Pokemon {

    private int number;
    private LineNumberReader reader;
    private String[] mData;

    private enum CatchState {
        UNSEEN, SEEN, CAUGHT
    }

    private CatchState mCatchState;

    public Pokemon(Context context) {
        Random r = new Random();

        number = r.nextInt(151)+1;

        try {
            reader = new LineNumberReader(new InputStreamReader(context.getAssets().open("pokemonDB.csv")));
        } catch (IOException e) {
            Log.e("Exception: %s", e.getMessage());
        }

        try {
            while (reader.getLineNumber() != number-1) {
                reader.readLine();
            }
            mData = reader.readLine().split(",");
        } catch (Exception e) {
            Log.e("Exception: %s", e.getMessage());
        }

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

    public String getCatchState() {
        if (mCatchState == CatchState.UNSEEN) {
            return "UNSEEN";
        }
        if (mCatchState == CatchState.SEEN) {
            return "SEEN";
        }
        return "CAUGHT";
    }

    public String getName() {
        return mData[0];
    }
}
