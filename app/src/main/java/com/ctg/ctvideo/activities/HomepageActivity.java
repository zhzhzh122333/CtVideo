package com.ctg.ctvideo.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.adapter.ViewPagerAdapter;
import com.ctg.ctvideo.fragments.ClassFragment;
import com.ctg.ctvideo.fragments.LiveFragment;
import com.ctg.ctvideo.fragments.MyFragment;
import com.ctg.ctvideo.fragments.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {
    private RadioGroup tabGroup;
    private RadioButton tabRecommend;
    private RadioButton tabClass;
    private RadioButton tabLive;
    private RadioButton tabMy;
    private ViewPager viewPager;
    private List<Fragment> tabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabGroup = (RadioGroup) findViewById(R.id.homepage_tab_group);
        tabRecommend = (RadioButton) findViewById(R.id.homepage_tab_recommend);
        tabClass = (RadioButton) findViewById(R.id.homepage_tab_class);
        tabLive = (RadioButton) findViewById(R.id.homepage_tab_live);
        tabMy = (RadioButton) findViewById(R.id.homepage_tab_my);

        viewPager = (ViewPager) findViewById(R.id.homepage_content);
        viewPager.setOffscreenPageLimit(4);

        tabFragment = new ArrayList<Fragment>();
        tabFragment.add(new RecommendFragment());
        tabFragment.add(new ClassFragment());
        tabFragment.add(new LiveFragment());
        tabFragment.add(new MyFragment());
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), tabFragment));

        // 设置按钮点击切换屏幕
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (v.getId()) {
                    case R.id.homepage_tab_recommend:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.homepage_tab_class:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.homepage_tab_live:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.homepage_tab_my:
                        viewPager.setCurrentItem(3);
                        break;
                }
                ft.commit();
            }
        };
        tabRecommend.setOnClickListener(clickListener);
        tabClass.setOnClickListener(clickListener);
        tabLive.setOnClickListener(clickListener);
        tabMy.setOnClickListener(clickListener);

        // 滑动屏幕事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentButton = tabGroup.getCheckedRadioButtonId();
                switch (position) {
                    case 0:
                        if (currentButton != tabRecommend.getId()) {
                            tabRecommend.setChecked(true);
                        }
                        break;
                    case 1:
                        if (currentButton != tabClass.getId()) {
                            tabClass.setChecked(true);
                        }
                        break;
                    case 2:
                        if (currentButton != tabLive.getId()) {
                            tabLive.setChecked(true);
                        }
                        break;
                    case 3:
                        if (currentButton != tabMy.getId()) {
                            tabMy.setChecked(true);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
