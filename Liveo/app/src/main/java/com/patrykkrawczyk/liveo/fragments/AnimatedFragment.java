package com.patrykkrawczyk.liveo.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.SwitchPageEvent;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import butterknife.ButterKnife;
import it.sephiroth.android.library.viewrevealanimator.ViewRevealAnimator;

/**
 * Created by Patryk Krawczyk on 10.04.2016.
 */
public class AnimatedFragment extends Fragment {

    protected final int ANIMATION_SPEED = 500;

    public enum Page {
        MENU, PASSENGERS, DRIVER, ICE;
    }

    protected MaterialRippleLayout ripple;
    protected int layoutId;

    public AnimatedFragment() {}
    public AnimatedFragment(int layoutId) {
        this.layoutId = layoutId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);

        ArrayList<View> viewList = getAllChildren(view);
        for (View v:viewList) {
            if (v.getId() == R.id.rippleView) {
                addRippleEffect(v);
                break;
            }
        }

        return view;
    }

    protected void rippleChangePage(MotionEvent event, Page page) {
        Point point = new Point((int) event.getRawX(), (int) event.getRawY());
        ripple.performRipple(point);
        EventBus.getDefault().post(new SwitchPageEvent(page));
    }

    protected void addRippleEffect(View view) {
        ripple = MaterialRippleLayout.on(view)
                    .rippleOverlay(true)
                    .rippleAlpha((float)0.75)
                    .ripplePersistent(true)
                    .rippleDuration((int)(ANIMATION_SPEED*0.75))
                    .rippleDelayClick(false)
                    .rippleFadeDuration(100)
                    .create();
    }

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
