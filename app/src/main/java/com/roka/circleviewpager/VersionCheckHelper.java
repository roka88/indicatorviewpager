package com.roka.circleviewpager;

import android.os.Build;

/**
 * Created by roka on 2016. 7. 22..
 */
public class VersionCheckHelper {

    public static boolean isLollpop() {
        return Build.VERSION.SDK_INT >= 21 ? true : false;
    }

}