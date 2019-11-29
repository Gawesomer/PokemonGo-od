package com.example.pokemongo_od;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.view.Display;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ModelTest {

    @Rule
    public ActivityTestRule<TitleScreen> activityRule
            = new ActivityTestRule<>(TitleScreen.class);
    private Model model;

    @Test
    public void init() {
        // Delete database to simulate first run
        CurrentApplication currentApplication = new CurrentApplication();
        currentApplication.setCurrActivity(activityRule.getActivity());
        currentApplication.getCurrActivity().getDatabasePath(DBHelper.DATABASE_NAME);
        File dbFile = currentApplication.getCurrActivity().getDatabasePath(DBHelper.DATABASE_NAME);
        dbFile.delete();
        assertFalse(dbFile.exists());
//        model = Model.getInstance();
//        model.init(currentApplication);
//        assertTrue(dbFile.exists());
//        assertTrue(model.isFirstRun());
//
//        checkStarterChosen();
//
//        catchPokemon();
    }

    private void checkStarterChosen() {
        Pokemon[] team = model.getTeam();
        for (Pokemon pokemon : team) {
            assertEquals(null, pokemon);
        }
        // Change to choose starter activity
        Intent intent = new Intent(activityRule.getActivity(), ChooseStarter.class);
        activityRule.getActivity().startActivity(intent);
        Pokemon starter = new Pokemon(1);
        model.addToStorage(starter);
        team = model.getTeam();
        assertEquals(model.getPokemonInfo(1, DBContract.PokedexDB.POKEMON_NAME), team[0].getName());
        for (int i = 1; i < team.length; i++) {
            assertEquals(null, team[i]);
        }
        assertEquals("CAUGHT", model.getPokemonInfo(1, DBContract.PokedexDB.CATCH_STATE));
    }

    private void catchPokemon() {
        // Change to map activity
//        Intent intent = new Intent(activityRule.getActivity(), MapsActivity.class);
//        activityRule.getActivity().startActivity(intent);
//        WildPokemon wild = model.getWildPokemons()[0];
//        LatLng currCoords = new LatLng(model.getCurrLocation().getLatitude(), model.getCurrLocation().getLongitude());
//        wild.setCoordinates(currCoords);
//        Pokemon[] team = model.getTeam();
//        assertEquals(model.getPokemonInfo(wild.getNumber(), DBContract.PokedexDB.POKEMON_NAME), team[1].getName());
    }
}