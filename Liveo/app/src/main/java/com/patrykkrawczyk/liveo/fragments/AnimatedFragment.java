package com.patrykkrawczyk.liveo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;

import butterknife.ButterKnife;

/**
 * Created by Patryk Krawczyk on 10.04.2016.
 */
public class AnimatedFragment extends Fragment {


    public enum Page {
        MENU(0), PASSENGERS(1), DRIVER(2), ICE(3);
        private final int value;
        private Page(int value) { this.value = value; }

        public int getValue() { return value; }
    }

    protected FragmentManager fragmentManager;
    protected MenuPagerAdapter adapter;
    protected MyViewPager mainViewPager;
    private int layoutId;

    public AnimatedFragment() {}
    public AnimatedFragment(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        mainViewPager = (MyViewPager) getActivity().findViewById(R.id.mainViewPager);
        adapter = (MenuPagerAdapter) mainViewPager.getAdapter();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);

        ViewGroup viewGroup = (ViewGroup) view;
        TextView tv;
        for (int k = 0; k < viewGroup.getChildCount(); k++) {
            try {
                tv = (TextView) viewGroup.getChildAt(k);
            } catch (ClassCastException e) {
                tv = null;
            }
            if (tv != null)
                addRippleEffect(tv);
        }

        return view;
    }

    protected void switchPage(Page page) {
        switch (page) {
            case MENU:
                            mainViewPager.setCurrentItem(0, true);
                            break;
            default:
                            adapter.switchState(page);
                            mainViewPager.setCurrentItem(1, true);
                            return;
        }
    }


    protected void addRippleEffect(View view) {
            MaterialRippleLayout.on(view)
                    .rippleOverlay(true)
                    .rippleAlpha((float)0.2)
                    .rippleDuration(350)
                    .rippleDelayClick(false)
                    .rippleFadeDuration(100)
                    .create();
        }
}
