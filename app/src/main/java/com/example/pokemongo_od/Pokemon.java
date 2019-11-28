package com.example.pokemongo_od;

import java.util.Random;

public class Pokemon {

    private int number;

    private enum CatchState {
        UNSEEN, SEEN, CAUGHT
    }

    public Pokemon() {
        Random r = new Random();

        // Random number [1-151]
        number = r.nextInt(151)+1;
    }

    public Pokemon(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPokemonSeen() {
        Model.getInstance().setPokemonInfo(number, DBContract.PokedexDB.CATCH_STATE, "SEEN");
    }

    public void setPokemonCaught() {
        Model.getInstance().setPokemonInfo(number, DBContract.PokedexDB.CATCH_STATE, "CAUGHT");
    }

    public String getCatchState() {
        return Model.getInstance().getPokemonInfo(number, DBContract.PokedexDB.CATCH_STATE);
    }

    public boolean wasSeen() {
        return !getCatchState().equals("UNSEEN");
    }

    public String getName() {
        return Model.getInstance().getPokemonInfo(number, DBContract.PokedexDB.POKEMON_NAME);
    }
}
