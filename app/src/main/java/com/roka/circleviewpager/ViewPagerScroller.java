package com.roka.circleviewpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by roka on 2016-05-02.
 */
public class ViewPagerScroller extends Scroller {
    // TODO : ViewPagerScroll의 속도를 줄이기위한 커스텀 상속
    private int mScrollDuration = 3000;



    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    public void setScrollDuration(int duration) {
        this.mScrollDuration = duration;
    }


}
