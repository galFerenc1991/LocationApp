package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.locationapp.presentation.screens.map.MapFragment_;

import org.androidannotations.annotations.EActivity;

@EActivity
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragment();
    }

    public void replaceFragment() {
        Fragment fragment = MapFragment_.builder().build();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flFragmentContent_AD, fragment)
                .commit();
    }
}
