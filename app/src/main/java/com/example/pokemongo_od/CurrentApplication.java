package com.example.pokemongo_od;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CurrentApplication implements Application.ActivityLifecycleCallbacks {

    private static CurrentApplication mInstance;
    private Activity currActivity;

    private CurrentApplication() {

    }

    public static CurrentApplication getInstance() {
        if (mInstance == null) {
            mInstance = new CurrentApplication();
        }
        return mInstance;
    }

    public Activity getCurrActivity() {
        return currActivity;
    }

    public void setCurrActivity(Activity activity) {
        currActivity = activity;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.d("myTag", "CurrentApplication: " + activity.toString());
        currActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.d("myTag", "CurrentApplication: " + activity.toString());
        currActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
