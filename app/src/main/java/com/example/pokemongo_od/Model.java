package com.example.pokemongo_od;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class Model {

    private static Model mInstance = null;
    private CurrentApplication currentApplication;
    private boolean firstRun;
    private Location currLocation, defaultLocation;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted
    private final LatLng defaultCoordinates = new LatLng(-33.8523341, 151.2106085);
    private boolean locationPermissionGranted = false;
    private WildPokemon[] wildPokemons;
    private final int numWildPokemon = 5;
    private Pokemon pokemonEncountered;
    private int firstAvailableTeamSlot = 0;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private List<PropertyChangeListener> listeners = new ArrayList<>();
    public enum Properties { CURRLOCATION, WILDPOKEMON, ACTIVITY }
    public enum ActivityNames { TITLE, MAP, MENU, TEAM, POKEDEX, STORAGE, STARTER, POKEDEX_ENTRY }


    protected Model() {

    }

    void init(CurrentApplication newCurrentApplication) {
        currentApplication = newCurrentApplication;

        dbHelper = new DBHelper(currentApplication.getCurrActivity());
        copyDB();

        db = dbHelper.getWritableDatabase();

        wildPokemons = new WildPokemon[numWildPokemon];

        defaultLocation = new Location(LocationManager.GPS_PROVIDER);
        defaultLocation.setLatitude(defaultCoordinates.latitude);
        defaultLocation.setLongitude(defaultCoordinates.longitude);

        currLocation = defaultLocation;

        if (teamIsEmpty()) {
            firstRun = true;
            firstAvailableTeamSlot = 1;
        } else {
            firstRun = false;
            Pokemon[] team = getTeam();
            for (int i = 0; i < team.length; i++) {
                if (team[i] == null) {
                    firstAvailableTeamSlot = i+1;
                }
            }
            if (firstAvailableTeamSlot == 0) {
                firstAvailableTeamSlot = 7;
            }
        }
    }

    private void copyDB() {
        if (!databaseExists(DBHelper.DATABASE_NAME)) {
            db = dbHelper.getWritableDatabase();
            try {
                InputStream dbAssets = currentApplication.getCurrActivity().getAssets().open("Pokedex.db");
                String dbPathName = currentApplication.getCurrActivity().getApplicationInfo().dataDir + "/databases/" + DBHelper.DATABASE_NAME;
                copyFile(dbAssets, dbPathName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            db.close();
        }
    }

    // Checks if database exists
    private boolean databaseExists(String dbName) {
        File dbFile = currentApplication.getCurrActivity().getDatabasePath(dbName);
        return dbFile.exists();
    }

    // Used to copy database from assets to internal storage
    private void copyFile(InputStream toCopy, String destPath) {
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(destPath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = toCopy.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            toCopy.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Model getInstance() {
        if (mInstance == null) {
            mInstance = new Model();
            Controller.getInstance();
        }
        return mInstance;
    }

    Activity getCurrActivity() {
        return currentApplication.getCurrActivity();
    }

    void notifyActivityChange() {
        notifyListeners(Properties.ACTIVITY.toString(), null, null);
    }

    boolean isFirstRun() {
        return firstRun;
    }

    Location getCurrLocation() {
        return currLocation;
    }

    void setCurrLocation(Location newLocation) {
        if (newLocation != null) {
            notifyListeners(Properties.CURRLOCATION.toString(), currLocation, newLocation);
            currLocation = newLocation;
        }
    }

    Location getDefaultLocation() {
        return defaultLocation;
    }

    boolean locationPermissionGranted() {
        return locationPermissionGranted;
    }

    void setLocationPermissionGranted(boolean newPermission) {
        locationPermissionGranted = newPermission;
    }

    WildPokemon[] getWildPokemons() {
        return wildPokemons;
    }

    void setWildPokemonCoordinates(int index, LatLng newCoordinates) {
        wildPokemons[index].setCoordinates(newCoordinates);
    }

    int getNumWildPokemon() {
        return numWildPokemon;
    }

    void respawnWildPokemon(int index) {
        wildPokemons[index] = new WildPokemon();
        notifyListeners(Properties.WILDPOKEMON.toString(), index, index);
    }

    Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(currentApplication.getCurrActivity().getResources(),
                currentApplication.getCurrActivity().getResources()
                        .getIdentifier(iconName, "drawable", currentApplication.getCurrActivity().getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    private void notifyListeners(String property, Object oldValue, Object newValue) {
        for (PropertyChangeListener listener : listeners) {
            Log.d("myTag", "\t- " + listener.toString());
            listener.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
        }
    }

    void addChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    String getPokemonInfo(Integer number, String field) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                field,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DBContract.PokedexDB._ID + " = ?";
        String[] selectionArgs = { number.toString() };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = field + " DESC";

        Cursor cursor = db.query(
                DBContract.PokedexDB.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        if (cursor.getCount() <= 0) {
            return "";
        }
        cursor.moveToFirst();
        int colIndex = cursor.getColumnIndex(field);
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    int setPokemonInfo(Integer number, String field, String value) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(field, value);

        // Which row to update, based on the title
        String selection = DBContract.PokedexDB._ID + " = ?";
        String[] selectionArgs = { number.toString() };

        return db.update(
                DBContract.PokedexDB.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    boolean wasSeen(Integer number) {
        String catchState = getPokemonInfo(number, DBContract.PokedexDB.CATCH_STATE);
        return !catchState.equals("UNSEEN");
    }

    int getPokemonFrontSprite(int number) {
        number--;
        String imageFileNamePrefix;
        if (number < 10) {
            imageFileNamePrefix = "tile00";
        } else if (number < 100) {
            imageFileNamePrefix = "tile0";
        } else {
            imageFileNamePrefix = "tile";
        }
        return currentApplication.getCurrActivity().getResources().getIdentifier(imageFileNamePrefix + number,
                "drawable",
                currentApplication.getCurrActivity().getPackageName());
    }

    private boolean teamIsEmpty() {
        Pokemon[] team = getTeam();
        return team[0] == null;
    }

    Pokemon[] getTeam() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                DBContract.PokemonStorage.POKEMON_NUMBER,
                DBContract.PokemonStorage.TEAM_INDEX,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DBContract.PokemonStorage.TEAM_INDEX + " != ?";
        String[] selectionArgs = { "0" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DBContract.PokemonStorage.TEAM_INDEX + " ASC";

        Cursor cursor = db.query(
                DBContract.PokemonStorage.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        Pokemon[] team = new Pokemon[6];
        if (cursor.getCount() <= 0) {
            return team;
        }
        int colIndex;
        int pokemonNum;
        int i = 0;
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && i < 6) {
            colIndex = cursor.getColumnIndex(DBContract.PokemonStorage.POKEMON_NUMBER);
            pokemonNum = cursor.getInt(colIndex);
            team[i] = new Pokemon(pokemonNum);
            i++;
            cursor.moveToNext();

        }
        cursor.close();
        return team;
    }

    List<Pokemon> getStorage() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                DBContract.PokemonStorage.POKEMON_NUMBER,
                DBContract.PokemonStorage.TEAM_INDEX,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DBContract.PokemonStorage.TEAM_INDEX + " = ?";
        String[] selectionArgs = { "0" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DBContract.PokemonStorage.TEAM_INDEX + " ASC";

        Cursor cursor = db.query(
                DBContract.PokemonStorage.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        List<Pokemon> stored = new ArrayList<>();
        if (cursor.getCount() <= 0) {
            return stored;
        }
        int colIndex;
        int pokemonNum;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            colIndex = cursor.getColumnIndex(DBContract.PokemonStorage.POKEMON_NUMBER);
            pokemonNum = cursor.getInt(colIndex);
            stored.add(new Pokemon(pokemonNum));
            cursor.moveToNext();

        }
        cursor.close();
        return stored;
    }

    void addToStorage(Pokemon pokemon) {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DBContract.PokemonStorage.POKEMON_NUMBER, pokemon.getNumber());
        if (firstAvailableTeamSlot <= 6) {
            values.put(DBContract.PokemonStorage.TEAM_INDEX, firstAvailableTeamSlot);
            firstAvailableTeamSlot++;
        } else {
            values.put(DBContract.PokemonStorage.TEAM_INDEX, 0);
        }

        db.insert(DBContract.PokemonStorage.TABLE_NAME, null, values);
    }

}
