package com.example.pokemongo_od;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Pokedex {

    private static Pokedex mInstance = null;
    private static Context mContext;
    private DBHelper dbHelper;

    protected Pokedex() {
        Log.d("myTag", "Pokedex()");
        dbHelper = new DBHelper(mContext);
        copyDB();
    }

    public void copyDB() {
        Log.d("myTag", "copyDB");
        if (!databaseExists(DBHelper.DATABASE_NAME)) {
            Log.d("myTag", "copyDB does not exist");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                InputStream dbAssets = mContext.getAssets().open("Pokedex.db");
                String dbPathName = mContext.getApplicationInfo().dataDir + "/databases/" + DBHelper.DATABASE_NAME;
                copyFile(dbAssets, dbPathName);
            } catch (IOException e) {
                Log.d("myTag", "copyDB failed");
                Log.e("Exception: %s", e.getMessage());
            }
            db.close();
        }
    }

    // Checks if database exists
    private boolean databaseExists(String dbName) {
        File dbFile = mContext.getDatabasePath(dbName);
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
            Log.d("myTag", "copyFile failed");
            e.printStackTrace();
        }
    }

    public static Pokedex getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new Pokedex();
        }
        return mInstance;
    }

    public static Pokedex getInstance() {
        if (mInstance == null) {
            return null;
        }
        return mInstance;
    }

    public String getPokemonInfo(Integer number, String field) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

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

    public int setPokemonInfo(Integer number, String field, String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(field, value);

        // Which row to update, based on the title
        String selection = DBContract.PokedexDB._ID + " = ?";
        String[] selectionArgs = { number.toString() };

        int count = db.update(
                DBContract.PokedexDB.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }

    public boolean wasSeen(Integer number) {
        String catchState = getPokemonInfo(number, DBContract.PokedexDB.CATCH_STATE);
        return !catchState.equals("UNSEEN");
    }

    public int getPokemonFrontSprite(int number) {
        number--;
        String imageFileNamePrefix;
        if (number < 10) {
            imageFileNamePrefix = "tile00";
        } else if (number < 100) {
            imageFileNamePrefix = "tile0";
        } else {
            imageFileNamePrefix = "tile";
        }
        return mContext.getResources().getIdentifier(imageFileNamePrefix + number,
                "drawable",
                mContext.getPackageName());
    }

    public boolean teamIsEmpty() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                BaseColumns._ID,
//                DBContract.PokemonStorage.INTEAM,
//        };
//
//        // Filter results WHERE "title" = 'My Title'
//        String selection = DBContract.PokemonStorage.INTEAM + " = ?";
//        String[] selectionArgs = { "1" };
//
//        // How you want the results sorted in the resulting Cursor
//        String sortOrder = BaseColumns._ID + " DESC";
//
//        Cursor cursor = db.query(
//                DBContract.PokemonStorage.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                selection,              // The columns for the WHERE clause
//                selectionArgs,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                sortOrder               // The sort order
//        );
//        Log.d("myTag", "Count: " + cursor.getCount());
//        if (cursor.getCount() <= 0) {
//            return true;
//        }
//        return false;
        Pokemon[] team = getTeam();
        if (team[0] == null) {
            return true;
        }
        return false;
    }

    public Pokemon[] getTeam() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                DBContract.PokemonStorage.POKEMON_NUMBER,
                DBContract.PokemonStorage.TEAM_INDEX,
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = DBContract.PokemonStorage.TEAM_INDEX + " = ?";
        String[] selectionArgs = { "1" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DBContract.PokemonStorage.TEAM_INDEX + " DESC";

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
        Log.d("myTag", "Count: "+cursor.getCount());
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
            team[i] = new Pokemon(mContext, pokemonNum);
            i++;
            cursor.moveToNext();

        }
        cursor.close();
        return team;
    }

    public void addToStorage(Pokemon pokemon, boolean addToTeam) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DBContract.PokemonStorage.POKEMON_NUMBER, pokemon.getNumber());
        if (addToTeam) {
            values.put(DBContract.PokemonStorage.TEAM_INDEX, 1);
        } else {
            values.put(DBContract.PokemonStorage.TEAM_INDEX, 0);
        }

        db.insert(DBContract.PokemonStorage.TABLE_NAME, null, values);
    }

}
