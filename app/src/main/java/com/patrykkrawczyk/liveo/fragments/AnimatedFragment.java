package com.patrykkrawczyk.liveo.fragments;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.events.SwitchPageEvent;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import butterknife.ButterKnife;

/**
 * Created by Patryk Krawczyk on 10.04.2016.
 */
public class AnimatedFragment extends Fragment {

    protected final int ANIMATION_SPEED = 500;
    public boolean touchEnabled = false;

    public enum Page {
        MENU, DRIVER, CALIBRATION
    }

    protected int layoutId;

    public AnimatedFragment() {}
    @SuppressLint("ValidFragment")
    public AnimatedFragment(int layoutId) {
        this.layoutId = layoutId;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    protected void setIconColor(View view) {
        MaterialIconView icon = (MaterialIconView) view;
        icon.setColor(getResources().getColor(R.color.colorAccent));
    }

    protected void changePage(Page page) {
        EventBus.getDefault().post(new SwitchPageEvent(page));
    }



    protected void animateViewTouch(View view) {
        YoYo.with(Techniques.Pulse)
                .interpolate(new AccelerateInterpolator())
                .duration(500)
                .playOn(view);
    }
}
