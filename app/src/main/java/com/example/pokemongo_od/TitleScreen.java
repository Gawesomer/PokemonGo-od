package com.example.pokemongo_od;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class TitleScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        CurrentApplication currentApplication = new CurrentApplication();
        currentApplication.setCurrActivity(this);
        getApplication().registerActivityLifecycleCallbacks(currentApplication);

        final Model model = Model.getInstance();
        model.init(currentApplication);

        Random r = new Random();

        // Display random pokemon
        int number = r.nextInt(151);
        ImageView pokemonImageView = findViewById(R.id.pokemonTitle);
        pokemonImageView.setImageResource(model.getPokemonFrontSprite(number));

        // Switch to map activity onClick
        final Activity activity = this;
        ConstraintLayout layout = findViewById(R.id.titleLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (model.isFirstRun()) {
                    intent = new Intent(activity, ChooseStarter.class);
                } else {
                    intent = new Intent(activity, MapsActivity.class);
                }
                startActivity(intent);
            }
        });
    }
}
