package com.example.pokemongo_od;

import android.provider.BaseColumns;

public final class DBContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DBContract() {}

    /* Inner class that defines the table contents */
    public static class PokedexDB implements BaseColumns {
        public static final String TABLE_NAME = "pokedex";
        public static final String POKEMON_NAME = "pokemon_name";
        public static final String CATCH_STATE = "catch_state";
    }

    public static class PokemonStorage implements BaseColumns {
        public static final String TABLE_NAME = "pokemon_storage";
        public static final String POKEMON_NUMBER = "pokemon_number";
        public static final String TEAM_INDEX = "team_index";   // 0 if not in team else [1-6] represents position in team
    }

}
