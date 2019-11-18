package com.example.pokemongo_od;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBContract.PokedexDB.POKEMON_NAME, pokemonList[0].getName());
        values.put(DBContract.PokedexDB.CATCH_STATE, pokemonList[0].getCatchState());
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DBContract.PokedexDB.TABLE_NAME, null, values);

        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getAssets().open("Pokedex.db");
            String file = "/data/data/com.example.pokemongo_od/databases/Pokedex.db";
            os = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            is.close();
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
