package com.example.pokemongo_od;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
        for (int i = 0; i < pokemonList.length; i++) {
            values.put(DBContract.PokedexDB.POKEMON_NAME, pokemonList[i].getName());
            values.put(DBContract.PokedexDB.CATCH_STATE, pokemonList[i].getCatchState());
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(DBContract.PokedexDB.TABLE_NAME, null, values);
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
