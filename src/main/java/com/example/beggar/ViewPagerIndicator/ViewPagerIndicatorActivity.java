package com.example.beggar.ViewPagerIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.beggar.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Beggar on 2016/7/25.
 */
public class ViewPagerIndicatorActivity extends FragmentActivity {

    @Bind(R.id.vp_indicator)
    ViewPagerIndicator mIndicator;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private List<String> mTitles = Arrays.asList("短信", "电话", "游戏1", "游戏2", "游戏3", "游戏4", "游戏5");
    private List<ViewPagerFragment> mContents = new ArrayList<ViewPagerFragment>();

    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpagerindicato_layout);
        ButterKnife.bind(this);

        mIndicator.setTabTitle(mTitles);

        initData();

        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 三角形的偏移量
                // tabWidth * positionOffset + position * tabWidth
                mIndicator.scroll(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        for (String title : mTitles) {
            ViewPagerFragment fragment = ViewPagerFragment.newInstance(title);
            mContents.add(fragment);
        }
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }
        };
    }
}
