package com.example.pokemongo_od;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Storage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        final ListView storageListView = findViewById(R.id.storageListView);
        List<Pokemon> stored = Model.getInstance().getStorage();
        String[] storageList = new String[stored.size()];
        for (int i = 0; i < storageList.length; i++) {
            storageList[i] = stored.get(i).getName();
        }
        final ArrayAdapter<String> storageArrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, storageList);

        storageListView.setAdapter(storageArrayAdapter);

        final ListView teamListView = findViewById(R.id.teamListView);
        String[] teamList = new String[6];
        Pokemon[] team = Model.getInstance().getTeam();
        for (int i = 0; i < teamList.length; i++) {
            if (team[i] != null) {
                teamList[i] = team[i].getName();
            } else {
                teamList[i] = "---";
            }
        }
        final ArrayAdapter<String> teamArrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teamList);

        teamListView.setAdapter(teamArrayAdapter);
    }
}
