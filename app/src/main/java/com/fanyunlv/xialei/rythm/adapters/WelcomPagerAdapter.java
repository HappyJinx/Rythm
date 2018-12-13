package com.fanyunlv.xialei.rythm.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanyunlv.xialei.rythm.R;

import java.util.ArrayList;



/**
 * Created by xialei on 2018/11/23.
 */
public class WelcomPagerAdapter extends PagerAdapter {
    private static final String TAG = WelcomPagerAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<ImageView> imageViews;

    private int[] resids = {
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5
    };

    public WelcomPagerAdapter(Context context) {
        Log.i(TAG, "WelcomPagerAdapter init");
        this.mContext = context;
        imageViews = new ArrayList<>();

        if (imageViews==null||imageViews.size()==0) {
            initAdapter();
        }
    }


    private void initAdapter() {
        Log.i(TAG, "initAdapter ");
        for (int m : resids) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(m);
            imageViews.add(imageView);
        }
    }

    @Override
    public int getCount() {
        return resids.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.i(TAG, "instantiateItem position="+position);
        ImageView imageView = imageViews.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Log.i(TAG, "destroyItem position="+position);
        container.removeView(imageViews.get(position));
    }
}
