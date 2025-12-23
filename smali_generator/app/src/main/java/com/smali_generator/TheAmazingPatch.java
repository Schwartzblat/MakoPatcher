package com.smali_generator;

import android.util.Log;

import com.smali_generator.patches.UserManagerHook;

import java.util.concurrent.atomic.AtomicBoolean;


@SuppressWarnings("unused")
public class TheAmazingPatch {


    static Hook[] hooks = {
            new UserManagerHook(),
    };

    static AtomicBoolean is_loaded = new AtomicBoolean(false);

    public static void on_load() {
        if (is_loaded.getAndSet(true)) {
            return;
        }

        Log.e("PATCH", "Patch loaded!");
        try {
            for (Hook hook : hooks) {
                hook.load();
            }
        } catch (Exception e) {
            Log.e("PATCH", "Error: " + e.getMessage());
        }
    }
}