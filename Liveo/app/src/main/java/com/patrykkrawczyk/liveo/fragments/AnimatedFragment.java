package com.patrykkrawczyk.liveo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import butterknife.ButterKnife;
import it.sephiroth.android.library.viewrevealanimator.ViewRevealAnimator;

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
        FragmentActivity activiity = getActivity();
        mainViewPager = (MyViewPager) activiity.findViewById(R.id.mainViewPager);
        adapter = (MenuPagerAdapter) mainViewPager.getAdapter();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);

       // ViewGroup viewGroup = (ViewGroup) view;
        //for (int k = 0; k < viewGroup.getChildCount(); k++)
        //    addRippleEffect(view);

        return view;
    }

    protected void switchPage(Page page) {
     //   mainViewPager.setScrollSpeed(page.getValue());
    //    mainViewPager.setCurrentItem(page.getValue(), true);
    }


    protected void addRippleEffect(View view) {
        MaterialRippleLayout.on(view)
                .rippleOverlay(true)
                .rippleAlpha((float)0.5)
                .rippleDuration(350)
                .rippleDelayClick(false)
                .rippleFadeDuration(100)
                .create();
    }
//    protected void addRippleEffect(ArrayList<View> list) {
//        for (View view:list) {
//            if (view instanceof TextView || view instanceof MaterialIconView)
//            MaterialRippleLayout.on(view)
//                    .rippleOverlay(true)
//                    .rippleAlpha((float)0.5)
//                    .rippleDuration(350)
//                    .rippleDelayClick(false)
//                    .rippleFadeDuration(100)
//                    .create();
//        }
//    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
}
