package org.yeah.fragmenttabhost;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioGroup;

import org.yeah.R;
import java.util.List;

/**
 * Author: harry Date: 14-5-29
 */
public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener {
    private List<Fragment> fragments; // 一个tab页面对应一个Fragment
    private RadioGroup mRadioGroup; // 用于切换tab
    private FragmentActivity mFragmentActivity; // Fragment所属的Activity
    private int mFragmentFramelayoutResId; // Activity中所要被替换的区域的id

    private int currentTab; // 当前Tab页面索引

    private OnTabCheckedChangedListener mTabCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

    public FragmentTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments,
            int fragmentContentId, RadioGroup rgs) {
        this.fragments = fragments;
        this.mRadioGroup = rgs;
        this.mFragmentActivity = fragmentActivity;
        this.mFragmentFramelayoutResId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(0));
        ft.commit();

        rgs.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedViewId) {
        Log.d("harry", "onCheckedChanged" + checkedViewId);
        for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
            if (mRadioGroup.getChildAt(i).getId() == checkedViewId) {
                Fragment fragment = fragments.get(i);
                FragmentTransaction ft = obtainFragmentTransaction(i);

                getCurrentFragment().onPause(); // 暂停当前tab
                // getCurrentFragment().onStop(); // 暂停当前tab

                if (fragment.isAdded()) {
                    // fragment.onStart(); // 启动目标tab的onStart()
                    fragment.onResume(); // 启动目标tab的onResume()
                } else {
                    ft.add(mFragmentFramelayoutResId, fragment);
                }
                //showTab(i); // 显示目标tab
                ft.show(fragment);
                ft.commit();

                // 如果设置了切换tab额外功能功能接口
                if (null != mTabCheckedChangedListener) {
                    mTabCheckedChangedListener.OnTabCheckedChanged(radioGroup,
                            checkedViewId, i);
                }
            }
        }
    }

    /**
     * 切换tab
     * 
     * @param idx
     */
    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);

            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取一个带动画的FragmentTransaction
     * 
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = mFragmentActivity.getSupportFragmentManager().beginTransaction();
        // 设置切换动画
        if (index > currentTab) {
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    public OnTabCheckedChangedListener getOnTabCheckedChangedListener() {
        return mTabCheckedChangedListener;
    }

    public void setOnTabCheckedChangedListener(
            OnTabCheckedChangedListener listener) {
        this.mTabCheckedChangedListener = listener;
    }

    /**
     * 切换tab额外功能功能接口
     */
    public interface OnTabCheckedChangedListener {
        public void OnTabCheckedChanged(RadioGroup radioGroup, int checkedViewId, int tabIndex);
    };

}
