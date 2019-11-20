package com.example.pokemongo_od;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class TitleScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        Random r = new Random();

        // Display random pokemon
        int number = r.nextInt(151);
        ImageView pokemonImageView = findViewById(R.id.pokemonTitle);
        pokemonImageView.setImageResource(Pokedex.getInstance(this).getPokemonFrontSprite(number));
        Log.d("myTag", "Name: "+Pokedex.getInstance(this).getPokemonInfo(number, DBContract.PokedexDB.POKEMON_NAME));

        // Switch to map activity onClick
        final Context context = this;
        ConstraintLayout layout = findViewById(R.id.titleLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Pokedex.getInstance(context).teamIsEmpty()) {
                    Log.d("myTag", "No pokemon in team");
                    intent = new Intent(context, ChooseStarter.class);
                } else {
                    Log.d("myTag", "Pokemon in team");
                    intent = new Intent(context, MapsActivity.class);
                }
                startActivity(intent);
            }
        });
    }
}
