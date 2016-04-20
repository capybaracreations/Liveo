package com.patrykkrawczyk.liveo.fragments;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.SwitchPageEvent;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnTouch;

public class MenuFragment extends AnimatedFragment {

    @Bind(R.id.driverButton) TableRow driverButton;
    @Bind(R.id.passengersButton) TableRow passengersButton;
    @Bind(R.id.iceButton) TableRow iceButton;
    private boolean firstTimeRun = true;


    public MenuFragment() {
        super(R.layout.fragment_menu);
    }


    @Override
    public void onStart() {
        super.onStart();

        if (firstTimeRun == true) {
            firstTimeRun = false;
            guide = GuideManager.setGuideOnView(getActivity(), driverButton, "Getting started", "Provide your personal details");
        }

    }

    @Override
    public void onResume() {
        eventBus.register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        eventBus.unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onEvent(ScrollStoppedEvent event) {
        if (GuideManager.showGuide) {
            if (GuideManager.tutorialStage == 0) guide = GuideManager.setGuideOnView(getActivity(), driverButton, "Getting started", "Provide your personal details");
            else if (GuideManager.tutorialStage == 1) guide = GuideManager.setGuideOnView(getActivity(), iceButton, "Getting started", "Decide who should be contacted");
            else if (GuideManager.tutorialStage == 2) guide = GuideManager.setGuideOnView(getActivity(), passengersButton, "Getting started", "Select number of additional passengers in car");
            else GuideManager.showGuide = false;
        }
    }

    @OnTouch(R.id.driverButton)
    public boolean onTouchDriver (View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (guide != null) guide.hide();
            setIconColor(view);
            rippleChangePage(event, Page.DRIVER);
        }
        return true;
    }

    @OnTouch(R.id.iceButton)
    public boolean onTouchIce (View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (guide != null) guide.hide();
            setIconColor(view);
            rippleChangePage(event, Page.ICE);
        }
        return true;
    }

    @OnTouch(R.id.passengersButton)
    public boolean onTouchPassengers (View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (guide != null) guide.hide();
            setIconColor(view);
            rippleChangePage(event, Page.PASSENGERS);
        }
        return true;
    }

    @Override
    protected void setIconColor(View view) {
        LinearLayout viewGroup = (LinearLayout)((ViewGroup) view).getChildAt(0);
        MaterialIconView icon = (MaterialIconView) ((TableRow)(viewGroup.getChildAt(0))).getChildAt(0);
        TextView textView = (TextView) ((TableRow)(viewGroup.getChildAt(1))).getChildAt(0);
        //icon.setColor(color);
        //textView.setTextColor(color);
    }
}
