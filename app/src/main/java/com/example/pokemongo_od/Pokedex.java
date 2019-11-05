package com.example.pokemongo_od;

import android.util.Log;

public class Pokedex {

    private Pokemon[] pokemonList;

    public Pokedex() {
        pokemonList = new Pokemon[151];
        for (int i = 0; i < pokemonList.length; i++) {
            pokemonList[i] = new Pokemon();
        }
    }

    public Pokemon getPokemonByNumber(int number) {
        if (number < 0 || number >= pokemonList.length) {
            return null;
        }
        return pokemonList[number];
    }
}
