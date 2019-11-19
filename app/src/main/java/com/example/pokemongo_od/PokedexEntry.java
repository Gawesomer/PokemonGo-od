package com.example.pokemongo_od;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PokedexEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex_entry);

        int number = getIntent().getIntExtra("number", 0);

        displayPokemonName(number);
        displayPokemonImage(number);
    }

    private void displayPokemonName(int number) {
        TextView pokemonNameView = findViewById(R.id.pokemonName);
        pokemonNameView.setText(Pokedex.getInstance(this).getPokemonInfo(number, DBContract.PokedexDB.POKEMON_NAME));
    }

    private void displayPokemonImage(int number) {
        number--;
        ImageView pokemonImageView = findViewById(R.id.pokemonImage);
        int id;
        if (number < 10) {
            id = getResources().getIdentifier("tile00" + number, "drawable", getPackageName());
        } else if (number < 100) {
            id = getResources().getIdentifier("tile0" + number, "drawable", getPackageName());
        } else {
            id = getResources().getIdentifier("tile" + number, "drawable", getPackageName());
        }
        pokemonImageView.setImageResource(id);
    }
}
