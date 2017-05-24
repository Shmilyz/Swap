package com.shmily.tjz.swap.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.shmily.tjz.swap.R;


/**
 * Created by Shmily_Z on 2017/5/3.
 */

public class ViewPagerFragmwnt extends Fragment{
    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.viewpage_fragment, container, false);
        initview();
        return rootView;

    }

    private void initview() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add(R.string.a, MainFragment.class)
                .add(R.string.b, SpecialFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

    }
}