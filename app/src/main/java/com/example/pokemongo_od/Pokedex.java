package com.example.pokemongo_od;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Pokedex {

    private static Pokedex mInstance = null;

    private Pokemon[] pokemonList;

    protected Pokedex(Context context) {
        pokemonList = new Pokemon[151];
        for (int i = 0; i < pokemonList.length; i++) {
            pokemonList[i] = new Pokemon(context, i+1);
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
