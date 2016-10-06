package com.roka.circleviewpager;

import android.widget.RadioButton;

/**
 * Created by roka on 2016. 10. 7..
 */

public interface CustomOnPageChangeListener {

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
    void onPageSelected(int position);
    void onPageScrollStateChanged(int state);
}
