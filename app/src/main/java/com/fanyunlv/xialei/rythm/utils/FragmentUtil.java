package com.fanyunlv.xialei.rythm.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
/**
 * Created by xialei on 2018/11/24.
 */
public class FragmentUtil {

    private FragmentManager fragmentManager;

    private String lastShowFragment;
    private Fragment currentFragment;

    public FragmentUtil(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private void setLastShowFragment(String lastShowFragment) {
        this.lastShowFragment = lastShowFragment;
    }
    private Fragment getfragment(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    public void showNewFragment(String newfragment) {
        FragmentTransaction f = fragmentManager.beginTransaction();
        if (lastShowFragment != null) {
            f.hide(getfragment(lastShowFragment));
        }
        f.show(getfragment(newfragment));
        f.commitNow();
        currentFragment = getfragment(newfragment);
        setLastShowFragment(newfragment);
    }

    public Fragment getLastFragment() {
        return getfragment(lastShowFragment);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void returnTomain() {
        FragmentTransaction f = fragmentManager.beginTransaction();
        if (lastShowFragment != null) {
            f.hide(getfragment(lastShowFragment));
        }
        f.show(getfragment("function"));
        f.commitNow();
        currentFragment = getfragment("function");
        setLastShowFragment("function");
    }

    public boolean isMainFragment() {
        return "function".equals(lastShowFragment);
    }

}
