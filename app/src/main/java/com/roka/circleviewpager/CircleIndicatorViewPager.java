package com.roka.circleviewpager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by roka on 2016. 8. 16..
 */
public class CircleIndicatorViewPager extends FrameLayout {


    private Context mContext;
    private RadioGroup mRadioGroup;
    private CustomViewPager mCustomViewPager;
    private LeakPreventHandler mHandler;


    private int mAdapterCount = 0;
    private boolean mMotionFlagBl = true;
    private int mAutoChangeDelay = 4000;

    public CircleIndicatorViewPager(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public CircleIndicatorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mCustomViewPager = new CustomViewPager(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mCustomViewPager.setLayoutParams(params);
        this.addView(mCustomViewPager);
        this.mHandler = new LeakPreventHandler(this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;
        if (wrapHeight) {
            int width = getMeasuredWidth(), height = getMeasuredHeight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

            if (getChildCount() > 0) {
                View firstChild = getChildAt(0);
                firstChild.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
                height = firstChild.getMeasuredHeight();
            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setAdapter(@NonNull PagerAdapter pagerAdapter, @DrawableRes int id, boolean isUpPosition) {
        mCustomViewPager.setAdapter(pagerAdapter);
        mCustomViewPager.addOnPageChangeListener(mOnPageChangeListener);
        addIndicator(mAdapterCount = pagerAdapter.getCount(), id, isUpPosition);
    }

    public void setAdapter(@NonNull PagerAdapter pagerAdapter, boolean isUpPosition) {
        mCustomViewPager.setAdapter(pagerAdapter);
        mCustomViewPager.addOnPageChangeListener(mOnPageChangeListener);
        addIndicator(mAdapterCount = pagerAdapter.getCount(), R.drawable.xml_radio_indicator_, isUpPosition);
    }


    public void setDelayPageChange(int delay) {
        // TODO : 페이지 체인지 속도를 변경한다. millseconds
        _changePagerScroller(delay);
    }

    public void startAutoScroll(int delay) {
        // TODO : 페이지 오토 체인지 속도를 변경한다. millseconds
        this.mAutoChangeDelay = delay;
        this.mCustomViewPager.setOnTouchListener(mOnTouchListener);
        handleMessage(0);
    }

    public void stopAutoScroll() {
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public int getCurrentItem() {
        return mCustomViewPager.getCurrentItem();
    }

    public void setCurrentItem(int item) {
        mCustomViewPager.setCurrentItem(item);
    }



    private void _changePagerScroller(int delay) {
        // TODO : 스크롤을 느리게한다.
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);

            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(mContext);
            viewPagerScroller.setScrollDuration(delay);
            mScroller.set(mCustomViewPager, viewPagerScroller);
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    @TargetApi(21)
    private void addIndicator(int pageCount, int id, boolean isUpPosition) {
        if (pageCount <= 1) return;

        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(width, height);

        mRadioGroup = new RadioGroup(mContext);
        RadioGroup.LayoutParams radioGroupParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRadioGroup.setOrientation(mRadioGroup.HORIZONTAL);
        mRadioGroup.setLayoutParams(radioGroupParams);
        if (isUpPosition) {
            mRadioGroup.setGravity(Gravity.CENTER | Gravity.TOP);
            params.setMargins(8, 40, 8, 0);

        } else {
            mRadioGroup.setGravity(Gravity.CENTER | Gravity.BOTTOM);
            params.setMargins(8, 0, 8, 8);
        }
        RadioButton button;

        for (int i = 0; i < pageCount; i++) {
            button = new RadioButton(mContext);
            //button.setBackground(mDrawable);
            if (VersionCheckHelper.isLollpop()) {
                button.setBackground(getResources().getDrawable(id, null));
                button.setButtonDrawable(getResources().getDrawable(R.color.transparent, null));
            } else {
                button.setBackground(getResources().getDrawable(id));
                button.setButtonDrawable(getResources().getDrawable(R.color.transparent));
            }

            button.setLayoutParams(params);
            button.setClickable(false);
            mRadioGroup.addView(button);
        }

        RadioButton first = (RadioButton) mRadioGroup.getChildAt(0);
        first.setChecked(true);

        addView(mRadioGroup);
    }



    private void handleMessage(int what) {
        switch (what) {
            case 0:
                if (mMotionFlagBl) {
                    int nextCount = this.mCustomViewPager.getCurrentItem();
                    nextCount++;
                    nextCount %= mAdapterCount;
                    this.mCustomViewPager.setCurrentItem(nextCount);
                }
                this.mHandler.sendEmptyMessageDelayed(0, mAutoChangeDelay);
                break;
        }

    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO : 터치를 하고 있는상황에서는 페이지가 변경되지 않는다.
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMotionFlagBl = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    mMotionFlagBl = false;
                    break;
                case MotionEvent.ACTION_UP:
                    mMotionFlagBl = true;
                    break;
            }
            return false;
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // TODO : 인디케이터 위치 변경 리스너
            RadioButton first = (RadioButton) mRadioGroup.getChildAt(position);
            first.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private static class LeakPreventHandler extends Handler {
        private final WeakReference<CircleIndicatorViewPager> viewPagerWeakReference;

        public LeakPreventHandler(CircleIndicatorViewPager activity) {
            viewPagerWeakReference = new WeakReference<CircleIndicatorViewPager>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CircleIndicatorViewPager viewPager = viewPagerWeakReference.get();
            if (viewPager != null) {
                viewPager.handleMessage(msg.what);
            }
        }
    }
}
