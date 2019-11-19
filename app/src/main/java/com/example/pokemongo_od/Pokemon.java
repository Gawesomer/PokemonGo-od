package com.example.pokemongo_od;

import android.content.Context;
import android.util.Log;

import java.util.Random;

public class Pokemon {

    private int number;
    Context context;

    private enum CatchState {
        UNSEEN, SEEN, CAUGHT
    }

    public Pokemon(Context context) {
        this.context = context;
        Random r = new Random();

        // Random number [1-151]
        number = r.nextInt(151)+1;
    }

    public Pokemon(Context context, int number) {
        this.context = context;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPokemonSeen() {
        Pokedex.getInstance(context).setPokemonInfo(number, DBContract.PokedexDB.CATCH_STATE, "SEEN");
    }

    public void setPokemonCaught() {
        Pokedex.getInstance(context).setPokemonInfo(number, DBContract.PokedexDB.CATCH_STATE, "CAUGHT");
    }

    public String getCatchState() {
        return Pokedex.getInstance(context).getPokemonInfo(number, DBContract.PokedexDB.CATCH_STATE);
    }

    public boolean wasSeen() {
        return !getCatchState().equals("UNSEEN");
    }

    public String getName() {
        return Pokedex.getInstance(context).getPokemonInfo(number, DBContract.PokedexDB.POKEMON_NAME);
    }
}
