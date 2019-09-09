package com.example.locationapp.presentation.utils;

import android.widget.Toast;

import com.example.locationapp.LocationApp_;


public class ToastManager {

    private static Toast mToast = Toast.makeText(LocationApp_.getInstance().getApplicationContext(), "", Toast.LENGTH_SHORT);

    public static void showToast(CharSequence message) {
        mToast.setText(message);
        mToast.show();
    }

}
