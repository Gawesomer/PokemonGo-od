package com.example.pokemongo_od;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CurrentApplication implements Application.ActivityLifecycleCallbacks {

    private Activity currActivity;

    CurrentApplication() {
    }

    Activity getCurrActivity() {
        return currActivity;
    }

    void setCurrActivity(Activity activity) {
        currActivity = activity;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Model.getInstance().notifyActivityChange();
        currActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Model.getInstance().notifyActivityChange();
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
