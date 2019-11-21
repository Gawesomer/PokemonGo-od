package com.example.pokemongo_od;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void teamButtonClicked(View view) {
        Intent intent = new Intent(this, Team.class);
        startActivity(intent);
    }

    public void pokedexButtonClicked(View view) {
        Intent intent = new Intent(this, PokedexActivity.class);
        startActivity(intent);
    }
}
