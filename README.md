# CircleIndicatorViewPager
안드로이드 라이브러리 Indicator viewpager

인디케이터가 있는 뷰페이저를 만들었음

![myimage-alt-tag](http://lycle.co.kr/images/t_1475678267095_user_event.png)



#사용용도
<pre>
이미지 배너 제작에 적합, 또는 커스텀 배너 제작에 적합.
</pre>


#Gradle
<pre>
repositories {
    ...  
    maven {
        url "https://jitpack.io" 
    } 
}
</pre>

<pre>
dependencies {
    compile 'com.github.roka88:indicatorviewpager:0.0.6'
}
</pre>


#사용법
<pre>
<ol>
<li>CircleIndicatorViewPager를 넣는다.</li>
<li>Adapter를 만든다.</li>
<li>CircleIndicatorViewPager에 Adapter를 Set</li>
</ol>
</pre>

#Example

<h5> activity_main.xml</h5>
<pre>
<xmp>
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/id_rootview"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    
    <com.roka.circleviewpager.CircleIndicatorViewPager
        android:id="@+id/viewpager"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
        
</LinearLayout></xmp>
</pre>

<h5> test.xml</h5>
<pre>
<xmp>
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <TextView
        android:id="@+id/number"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:gravity="center"
        android:textSize="20sp"
        android:background="@color/colorPrimaryDark"
        android:text="1"/>
</LinearLayout>
</xmp>
</pre>

<h5> xml_indicator.xml (when indicator change)</h5>
<pre>
<xmp>
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android" >
    <item android:state_checked="true" android:drawable="@mipmap/dot_blue_02" ></item>
    <item android:state_checked="false" android:drawable="@mipmap/dot_blue_01"></item>
</selector>
</xmp>
</pre>

<h5> MainActivity.java</h5>
<pre>
<?xml version="1.0" encoding="utf-8"?>
<xmp>
public class MainActivity extends AppCompatActivity {

    private Activity mActivity;
    private CircleIndicatorViewPager mViewpager;
    private CalendarPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;

        mViewpager = (CircleIndicatorViewPager) findViewById(R.id.viewpager);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");

        TestPagerAdapter pagerAdapter = new TestPagerAdapter(this, list);
        mViewpager.setAdapter(pagerAdapter, false);
        // TODO : 인디케이터 변경 시, 3번째 파라미터는 Indicator를 위에 위치할 것인지 아닌지에 대한 flag
        //mViewPager.setAdapter(pagerAdapter, R.drawable.xml_indicator, false);

        // TODO : 페이지를 넘길 때 걸리는 시간
        //mViewpager.setDelayPageChange(1000);

        // TODO : 페이지를 자동으로 넘기는 시간의 텀
        //mViewpager.startAutoScroll(1000);

        // TODO : Scroll이 있는 뷰 내부에서 사용할 시, 높이 사이즈를 직접 지정해줘야함.
        //DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        //int width = metrics.widthPixels;
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width / 6 * 3);
        //mViewpager.setLayoutParams(params);

    }
}
</xmp>
</pre>

<h5>TestPagerAdapter.java</h5>

<pre>
<xmp>
public class TestPagerAdapter extends PagerAdapter {

    private LayoutInflater mInflater;
    private Activity mActivity;
    private List<String> mList;

    public TestPagerAdapter(Activity activity, List<String> list) {
        this.mActivity = activity;
        this.mList = list;
        this.mInflater = activity.getLayoutInflater();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((com.roka.circleviewpager.CustomViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = mInflater.inflate(R.layout.test, view, false);
        TextView numTv = (TextView) imageLayout.findViewById(R.id.number);
        numTv.setText(mList.get(position));
        ((com.roka.circleviewpager.CustomViewPager) view).addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View container) {
    }
}
</xmp>
</pre>



#License
<pre>
Copyright 2016 Roka

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
