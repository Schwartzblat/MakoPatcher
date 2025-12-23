package com.smali_generator.patches;

import android.util.Log;

import com.smali_generator.Hook;

import java.lang.reflect.Method;
import java.util.Objects;

import lab.galaxy.yahfa.HookMain;


public class UserManagerHook implements Hook {

    static Class<?> VideoAdsPolicyClass = null;

    static boolean is_paying_user_backup(Object instance) {
        return true;
    }

    static boolean is_paying_user(Object instance) {
        return true;
    }

    static Object get_users_ads_policy_backup(Object instance) {
        return null;
    }

    static Object get_users_ads_policy(Object instance) {
        Object ret = null;
        try {
            ret = Objects.requireNonNull(VideoAdsPolicyClass.getEnumConstants())[0];
        } catch (Exception e) {
            Log.e("PATCH", "ActivityHook: Error creating VideoAdsPolicy instance:" + e.getMessage());
        }
        if (ret == null) {
            ret = get_users_ads_policy_backup(instance);
        }
        return ret;
    }

    static boolean get_ads_allowed(Object instance) {
        return false;
    }
    public void load() {
        Log.i("PATCH", "ActivityHook: Patch loaded");
        try {
            VideoAdsPolicyClass = Class.forName("com.mako.kscore.ksplayer.helpers.VideoAdsPolicy");
        } catch (Exception e) {
            Log.e("PATCH", "ActivityHook: Error finding VideoAdsPolicy class:" + e.getMessage());
        }
        try {
            Class<?> UserManager = Class.forName("com.mako.kscore.user.UserManager");
            Method to_patch = UserManager.getMethod("isUserSubscribed");
            Method patch = UserManagerHook.class.getDeclaredMethod("is_paying_user", Object.class);
            Method patch_backup = UserManagerHook.class.getDeclaredMethod("is_paying_user_backup", Object.class);
            HookMain.backupAndHook(to_patch, patch, patch_backup);

            to_patch = UserManager.getMethod("getUserAdsPolicy");
            patch = UserManagerHook.class.getDeclaredMethod("get_users_ads_policy", Object.class);
            patch_backup = UserManagerHook.class.getDeclaredMethod("get_users_ads_policy_backup", Object.class);
            HookMain.backupAndHook(to_patch, patch, patch_backup);

            Class<?> InterstitialManager = Class.forName("com.keshet.mako.VOD.utils.managers.InterstitialManager");
            to_patch = InterstitialManager.getDeclaredMethod("getAdsAllowed");
            patch = UserManagerHook.class.getDeclaredMethod("get_ads_allowed", Object.class);
            HookMain.hook(to_patch, patch);

        } catch (Exception e) {
            Log.e("PATCH", "ActivityHook: Error:" + e.getMessage());
        }
    }

    public void unload() {
        Log.i("PATCH", "ActivityHook: Patch unloaded");
    }
}
