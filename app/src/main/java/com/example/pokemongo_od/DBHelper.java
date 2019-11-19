package com.example.pokemongo_od;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Pokedex.db";

    private static final String POKEDEX_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.PokedexDB.TABLE_NAME + " (" +
                    DBContract.PokedexDB._ID + " INTEGER PRIMARY KEY," +
                    DBContract.PokedexDB.POKEMON_NAME + " TEXT," +
                    DBContract.PokedexDB.CATCH_STATE + " TEXT)";

    private static final String POKEDEX_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.PokedexDB.TABLE_NAME;

    private static final String STORAGE_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.PokemonStorage.TABLE_NAME + " (" +
                    DBContract.PokemonStorage._ID + " INTEGER PRIMARY KEY," +
                    DBContract.PokemonStorage.POKEMON_NAME + " TEXT," +
                    DBContract.PokemonStorage.INTEAM + " INTEGER)";

    private static final String STORAGE_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.PokemonStorage.TABLE_NAME;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(POKEDEX_CREATE_ENTRIES);
            db.execSQL(STORAGE_CREATE_ENTRIES);
        } catch (SQLiteException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(STORAGE_DELETE_ENTRIES);
        onCreate(db);
    }
}
