package com.fanyunlv.xialei.rythm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.fanyunlv.xialei.rythm.MainActivity;
import com.fanyunlv.xialei.rythm.R;
import com.fanyunlv.xialei.rythm.sharedpreference.SharePrefUtil;
import com.fanyunlv.xialei.rythm.viewpager.WelcomPagerAdapter;

/**
 * Created by xialei on 2018/11/23.
 */
public class WelcomeFragment extends Fragment implements OnClickListener, ViewPager.OnPageChangeListener{
    private static final String TAG = WelcomeFragment.class.getSimpleName();
    private View comtentview;
    private Context mContext;

    private Button previous;
    private Button nextStep;
    private WelcomPagerAdapter adapter;
    private ViewPager viewPager;

    private int mCurrentpostion=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();


        //hide action bar
        ((MainActivity)mContext).hideActionbar(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        comtentview = inflater.inflate(R.layout.welcome, container, false);
        viewPager = comtentview.findViewById(R.id.welcome_pager);
        previous = comtentview.findViewById(R.id.previous);
        nextStep = comtentview.findViewById(R.id.next);
        previous.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        adapter = new WelcomPagerAdapter(mContext);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        return comtentview;
    }


    @Override
    public void onClick(View v) {
        if (v == previous) {
            Log.i(TAG, "onClick ");
            viewPager.setCurrentItem(mCurrentpostion-1,true);
        } else {
            Log.i(TAG, "LineNum:66  Method:onClick--> ");
            if (mCurrentpostion + 1 == adapter.getCount()) {
                //this means we can start app now
                SharePrefUtil.getInstance(mContext).setFirstOpen(false);
                ((MainActivity)getActivity()).welComeFinished();
            }else {
                viewPager.setCurrentItem(mCurrentpostion + 1, true);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i(TAG, "onPageScrolled position="+position);
        if (position>0&&previous.getVisibility() == View.GONE) {  //change previous button visibility
            previous.setVisibility(View.VISIBLE);
        }else if (position==0){
            previous.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.i(TAG, "onPageSelected position="+position);
        mCurrentpostion = position;
        if (position + 1 == adapter.getCount()) {
            nextStep.setText(R.string.begin);
        } else {
            nextStep.setText(R.string.next);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i(TAG, "onPageScrollStateChanged ");
    }
}
