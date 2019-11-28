package com.example.pokemongo_od;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PokedexEntry extends AppCompatActivity {

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex_entry);

        model = Model.getInstance();
        model.setCurrActivity(this);

        int number = getIntent().getIntExtra("number", 0);

        displayPokemonName(number);
        displayPokemonImage(number);
    }

    private void displayPokemonName(int number) {
        TextView pokemonNameView = findViewById(R.id.pokemonName);
        pokemonNameView.setText(model.getPokemonInfo(number, DBContract.PokedexDB.POKEMON_NAME));
    }

    private void displayPokemonImage(int number) {
        ImageView pokemonImageView = findViewById(R.id.pokemonImage);
        pokemonImageView.setImageResource(model.getPokemonFrontSprite(number));
    }
}
