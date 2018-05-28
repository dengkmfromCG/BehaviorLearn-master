package com.gdut.dkmfromcg.behaviorlearn;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.gdut.dkmfromcg.behaviorlearn.behavior.UCViewHeaderBehavior;
import com.gdut.dkmfromcg.behaviorlearn.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * copy from: https://github.com/huyongli/UCMainViewForBehavior
 */
public class UcActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private CustomViewPager mViewPager;
    private UCViewHeaderBehavior mUCViewHeaderBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uc);
        mTabLayout = (TabLayout) findViewById(R.id.uc_tab_layout);
        mViewPager = findViewById(R.id.uc_content_layout);

        mUCViewHeaderBehavior = (UCViewHeaderBehavior) ((CoordinatorLayout.LayoutParams) findViewById(R.id.uc_header_layout).getLayoutParams()).getBehavior();
        initViewData();
    }

    private void initViewData() {

        List<ContentFragment> fragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fragments.add(ContentFragment.newInstance(i));
        }
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        ContentFragmentAdapter adapter = new ContentFragmentAdapter(fragments, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onBackPressed() {

        if (mUCViewHeaderBehavior.isClosed()) {
            mUCViewHeaderBehavior.openPager();
        } else {
            super.onBackPressed();
        }
    }

    public class ContentFragmentAdapter extends FragmentPagerAdapter {
        List<ContentFragment> mFragments;


        public ContentFragmentAdapter(List<ContentFragment> fragments, FragmentManager fm) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragments.get(position).getName();
        }
    }

}
