package com.example.slezica.dailyself.utils;


import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

public class Quick {

    public static Drawable applyTint(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);

        return drawable;
    }

}
