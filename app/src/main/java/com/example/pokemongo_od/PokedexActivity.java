package com.example.pokemongo_od;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PokedexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);

        final ListView listView = findViewById(R.id.listview);
        String[] list = new String[151];
        for (int i = 0; i < list.length; i++) {
            list[i] = (i+1) + " ";
            if (Pokedex.getInstance(this).wasSeen(i+1)) {
                list[i] += Pokedex.getInstance(this).getPokemonInfo(i+1, DBContract.PokedexDB.POKEMON_NAME);
            } else {
                list[i] += "---";
            }
        }
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = arrayAdapter.getItem(position);
                assert item != null;
                if (!item.contains("---")) {
                    // Create an Intent to start the second activity
                    Intent pokedexEntry = new Intent(getApplicationContext(), PokedexEntry.class);

                    int number = Integer.parseInt(item.split(" ")[0]);

                    pokedexEntry.putExtra("number", number);

                    startActivity(pokedexEntry);
                }
            }
        });

        listView.setAdapter(arrayAdapter);
    }
}
