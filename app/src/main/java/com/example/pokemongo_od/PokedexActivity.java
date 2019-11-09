package com.example.pokemongo_od;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Comparator;

public class PokedexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        final ListView listView = findViewById(R.id.listview);
        String[] list = new String[151];
        for (int i = 0; i < list.length; i++) {
            list[i] = i + " ";
            list[i] += Pokedex.getInstance(this).getPokemonByNumber(i).getName();
            list[i] +=  "\tCatch State: " + Pokedex.getInstance(this).getPokemonByNumber(i).getCatchState();
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(arrayAdapter);
    }
}
