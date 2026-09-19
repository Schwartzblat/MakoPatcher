package com.smali_generator;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Entry point of the patch.
 *
 * Stitch adds this class to the target app's AndroidManifest as
 *   <provider android:name="com.smali_generator.InitProviderMako"
 *             android:authorities="<target package>.com.smali_generator.InitProviderMako"
 *             android:exported="false"
 *             android:initOrder="2147483647"/>
 * so onCreate() runs before the host Application's onCreate, where the old
 * invoke-static line used to fire from every entry point.
 */
@SuppressWarnings("unused")
public class InitProviderMako extends ContentProvider {

    @Override
    public boolean onCreate() {
        Log.i("PATCH", "InitProviderMako: onCreate called");
        try {
            TheAmazingPatch.on_load();
        } catch (Throwable throwable) {
            // on_load() only catches Exception, and we now run before the host
            // Application: a NoClassDefFoundError out of ArtHooks would take the
            // app down before a line of its own code had run.
            Log.e("PATCH", "Patch failed to load", throwable);
        }
        return true;
    }

    @Override public Cursor query(@NonNull Uri u, String[] p, String s, String[] a, String o) { return null; }
    @Override public String getType(@NonNull Uri u) { return null; }
    @Override public Uri insert(@NonNull Uri u, ContentValues v) { return null; }
    @Override public int delete(@NonNull Uri u, String s, String[] a) { return 0; }
    @Override public int update(@NonNull Uri u, ContentValues v, String s, String[] a) { return 0; }
}
