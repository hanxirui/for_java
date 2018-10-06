package com.solaris.androidstone.utils;

import android.app.Activity;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by hanxirui on 2016/12/26.
 */

public class ActivityCollector {
    public static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
