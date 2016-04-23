package com.patrykkrawczyk.liveo.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.balysv.materialripple.MaterialRippleLayout;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.SwitchPageEvent;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import butterknife.ButterKnife;

/**
 * Created by Patryk Krawczyk on 10.04.2016.
 */
public class AnimatedFragment extends Fragment {

    protected final int ANIMATION_SPEED = 500;
    public boolean touchEnabled = false;

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


    protected void setIconColor(View view) {
        MaterialIconView icon = (MaterialIconView) view;
        icon.setColor(getResources().getColor(R.color.colorAccent));
    }

    protected void rippleChangePage(MotionEvent event, Page page) {
        Point point = new Point((int) event.getRawX(), (int) event.getRawY());
        ripple.setRipplePersistent(true);
        ripple.performRipple(point);
        EventBus.getDefault().post(new SwitchPageEvent(page));
    }

    protected void performRippleNoSwitch(MotionEvent event) {
        Point point = new Point((int) event.getRawX(), (int) event.getRawY());
        ripple.setRipplePersistent(false);
        ripple.performRipple(point);
    }

    protected void addRippleEffect(View view) {
        ripple = MaterialRippleLayout.on(view)
                    .rippleOverlay(true)
                    .rippleColor(getResources().getColor(R.color.colorRipple))
                    .rippleAlpha((float)0.20)
                    .ripplePersistent(true)
                    .rippleDuration((int)(ANIMATION_SPEED*0.75))
                    .rippleDelayClick(false)
                    .rippleFadeDuration(100)
                    .create();
    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
}
