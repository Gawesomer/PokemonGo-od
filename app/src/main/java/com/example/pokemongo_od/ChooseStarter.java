package com.example.pokemongo_od;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseStarter extends AppCompatActivity {

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_starter);

        model = Model.getInstance();
    }

    public void chooseBulbasaur(View view) {
        chooseStarter(1);
    }

    public void chooseCharmander(View view) {
        chooseStarter(4);
    }

    public void chooseSquirtle(View view) {
        chooseStarter(7);
    }

    private void chooseStarter(int number) {
        Pokemon starter = new Pokemon(number);
        model.addToStorage(starter);
        //model.setPokemonInfo(starter.getNumber(), DBContract.PokedexDB.CATCH_STATE, "CAUGHT");
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}