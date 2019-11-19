package com.example.pokemongo_od;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Pokedex {

    private static Pokedex mInstance = null;

    private Pokemon[] pokemonList;
    private DBHelper dbHelper;

    protected Pokedex(Context context) {
        pokemonList = new Pokemon[151];
        for (int i = 0; i < pokemonList.length; i++) {
            pokemonList[i] = new Pokemon(context, i+1);
        }
        if (!databaseExists(context, DBHelper.DATABASE_NAME)) {
            dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            try {
                InputStream dbAssets = context.getAssets().open("Pokedex.db");
                String dbPathName = context.getApplicationInfo().dataDir + "/databases/" + DBHelper.DATABASE_NAME;
                copyFile(context, dbAssets, dbPathName);
            } catch (IOException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    // Checks if database exists
    private boolean databaseExists(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    // Used to copy database from assets to internal storage
    private void copyFile(Context context, InputStream toCopy, String destPath) {
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(destPath);

            byte[] buffer = new byte[1024];
            int length = 0;
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

    public static Pokedex getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Pokedex(context);
        }
        return mInstance;
    }

    public Pokemon getPokemonByNumber(int number) {
        if (number < 0 || number >= pokemonList.length) {
            return null;
        }
        return pokemonList[number];
    }

}
