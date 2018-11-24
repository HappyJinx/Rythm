package com.fanyunlv.xialei.rythm.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.viewpager.WelcomPagerAdapter;

/**
 * Created by xialei on 2018/11/23.
 */
public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        ViewPager viewPager = findViewById(R.id.welcome_pager);
        WelcomPagerAdapter pagerAdapter = new WelcomPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
    }

}
