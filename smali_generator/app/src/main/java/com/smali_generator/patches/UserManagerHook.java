package com.smali_generator.patches;

import android.util.Log;

import com.smali_generator.Hook;

import java.lang.reflect.Method;
import java.util.Objects;

import com.arthooks.ArtHooks;


public class UserManagerHook implements Hook {

    static Class<?> VideoAdsPolicyClass = null;

    // Backups must be native with no body: ArtHooks (1.0.5+) refuses a backup with a Java body for an
    // instance target, because dex2oat inlines that body and the backup never reaches the original.
    static native boolean is_paying_user_backup(Object instance);

    static boolean is_paying_user(Object instance) {
        return true;
    }

    static native Object get_users_ads_policy_backup(Object instance);

    static Object get_users_ads_policy(Object instance) {
        Object ret = null;
        try {
            ret = Objects.requireNonNull(VideoAdsPolicyClass.getEnumConstants())[0];
        } catch (Exception e) {
            Log.e("PATCH", "UserManagerHook: Error creating VideoAdsPolicy instance:" + e.getMessage());
        }
        if (ret == null) {
            ret = get_users_ads_policy_backup(instance);
        }
        return ret;
    }

    static boolean get_ads_allowed(Object instance) {
        return false;
    }

    // hook_function() reports failure by returning false, not by throwing.
    static void log_hook(String name, boolean hooked) {
        if (hooked) {
            Log.i("PATCH", "UserManagerHook: hooked " + name);
        } else {
            Log.e("PATCH", "UserManagerHook: failed to hook " + name + ", see the ArtHooks tag");
        }
    }

    public void load() {
        Log.i("PATCH", "UserManagerHook: Patch loaded");
        try {
            VideoAdsPolicyClass = Class.forName("com.mako.kscore.ksplayer.helpers.VideoAdsPolicy");
        } catch (Exception e) {
            Log.e("PATCH", "UserManagerHook: Error finding VideoAdsPolicy class:" + e.getMessage());
        }
        try {
            Class<?> UserManager = Class.forName("com.mako.kscore.user.UserManager");
            Method to_patch = UserManager.getMethod("isUserSubscribed");
            Method patch = UserManagerHook.class.getDeclaredMethod("is_paying_user", Object.class);
            Method patch_backup = UserManagerHook.class.getDeclaredMethod("is_paying_user_backup", Object.class);
            log_hook("isUserSubscribed", ArtHooks.hook_function(to_patch, patch, patch_backup));

            to_patch = UserManager.getMethod("getUserAdsPolicy");
            patch = UserManagerHook.class.getDeclaredMethod("get_users_ads_policy", Object.class);
            patch_backup = UserManagerHook.class.getDeclaredMethod("get_users_ads_policy_backup", Object.class);
            log_hook("getUserAdsPolicy", ArtHooks.hook_function(to_patch, patch, patch_backup));

            Class<?> InterstitialManager = Class.forName("com.keshet.mako.VOD.utils.managers.InterstitialManager");
            to_patch = InterstitialManager.getDeclaredMethod("getAdsAllowed");
            patch = UserManagerHook.class.getDeclaredMethod("get_ads_allowed", Object.class);
            log_hook("getAdsAllowed", ArtHooks.hook_function(to_patch, patch));

        } catch (Exception e) {
            Log.e("PATCH", "UserManagerHook: Error:" + e.getMessage());
        }
    }

    public void unload() {
        Log.i("PATCH", "UserManagerHook: Patch unloaded");
    }
}
