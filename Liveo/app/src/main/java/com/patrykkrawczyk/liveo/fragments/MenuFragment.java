package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.activities.CalibrateActivity;
import com.patrykkrawczyk.liveo.events.BackKeyEvent;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerViewManager;
import com.patrykkrawczyk.liveo.managers.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.events.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.events.ShowGuideEvent;
import com.patrykkrawczyk.liveo.activities.HubActivity;
import com.patrykkrawczyk.liveo.managers.StateManager;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnTouch;

public class MenuFragment extends AnimatedFragment {

    @Bind(R.id.driverButton) TableRow driverButton;
    @Bind(R.id.passengersButton) TableRow passengersButton;
    @Bind(R.id.iceButton) TableRow iceButton;
    @Bind(R.id.triggerButton) TableRow triggerButton;
    @Bind(R.id.triggerText) TextView triggerText;
    @Bind(R.id.driverButtonIcon) MaterialIconView driverButtonIcon;
    @Bind(R.id.iceButtonIcon) MaterialIconView iceButtonIcon;
    @Bind(R.id.passengersButtonIcon) MaterialIconView passengersButtonIcon;
    @Bind(R.id.triggerButtonIcon) MaterialIconView triggerButtonIcon;

    TextView driverName;
    private static boolean firstTimeRun = true;
    private EventBus eventBus;
    private StateManager stateManager;

    public MenuFragment() {
        super(R.layout.fragment_menu);
    }


    @Override
    public void onStart() {
        super.onStart();

        touchEnabled = true;
        if (firstTimeRun == true) {
            firstTimeRun = false;
            if (GuideManager.getShowGuide()) {
                if (GuideManager.getStage() == 0)
                    GuideManager.showGuide(getActivity(), driverButton);
                else if (GuideManager.getStage() == 1)
                    GuideManager.showGuide(getActivity(), iceButton);
                else if (GuideManager.getStage() == 2)
                    GuideManager.showGuide(getActivity(), passengersButton);
                else GuideManager.finishGuide(getActivity());
            }
        }

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);
        stateManager = StateManager.getInstance();

        driverName = (TextView) driverButton.findViewById(R.id.driverText);
        Driver driver = Driver.getLocalDriver(getContext());
        if (driver == null) driverName.setText("Driver");
        else driverName.setText(driver.getFirstName().toUpperCase() + " " + driver.getLastName().toUpperCase());

        loadPassenger();
        checkState();

    }

    private void loadPassenger() {
        SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        String count = sharedPref.getString(getString(R.string.LIVEO_PASSENGERS_COUNT),  "");

        if (!count.isEmpty()) {
            if (count.equals("0")) passengersButtonIcon.setIcon(MaterialDrawableBuilder.IconValue.NUMERIC_0_BOX_OUTLINE);
            else if (count.equals("1")) passengersButtonIcon.setIcon(MaterialDrawableBuilder.IconValue.NUMERIC_1_BOX_OUTLINE);
            else if (count.equals("2")) passengersButtonIcon.setIcon(MaterialDrawableBuilder.IconValue.NUMERIC_2_BOX_OUTLINE);
            else if (count.equals("3")) passengersButtonIcon.setIcon(MaterialDrawableBuilder.IconValue.NUMERIC_3_BOX_OUTLINE);
            else if (count.equals("4")) passengersButtonIcon.setIcon(MaterialDrawableBuilder.IconValue.NUMERIC_4_BOX_OUTLINE);
            else if (count.equals("5")) passengersButtonIcon.setIcon(MaterialDrawableBuilder.IconValue.DOTS_HORIZONTAL);
        } else passengersButtonIcon.setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT_MULTIPLE);
    }

    private void checkState() {
        boolean state = stateManager.getMonitorState();

        if (state) {
            triggerButtonIcon.setIcon(MaterialDrawableBuilder.IconValue.PULSE);
            triggerText.setText("HUB");
        } else {
            triggerButtonIcon.setIcon(MaterialDrawableBuilder.IconValue.PLAY);
            triggerText.setText("START");
        }
    }

    @Subscribe
    public void onShowGuideEvent(ShowGuideEvent event) {
        if (GuideManager.getShowGuide()) {
            if (GuideManager.getStage() == 0) GuideManager.showGuide(getActivity(), driverButton);
            else if (GuideManager.getStage() == 1) GuideManager.showGuide(getActivity(), iceButton);
            else if (GuideManager.getStage() == 2) GuideManager.showGuide(getActivity(), passengersButton);
            else GuideManager.finishGuide(getActivity());
        }
    }

    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        touchEnabled = true;
    }



    private void onButtonTouch(View view, MotionEvent event, Page page) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            GuideManager.hideGuide();
            setIconColor(view);
            rippleChangePage(event, page);
        }
    }

    @OnTouch(R.id.triggerButton)
    public boolean onTouchTrigger (View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            setIconColor(view);
            Point point = new Point((int) event.getRawX(), (int) event.getRawY());
            ripple.setRipplePersistent(true);
            ripple.performRipple(point);

            Intent intent;
            if (AccelerometerViewManager.isCalibrated()) {
                intent = new Intent(getContext(), HubActivity.class);
            } else {
                intent = new Intent(getContext(), CalibrateActivity.class);
            }

            startActivity(intent);
            getActivity().finish();
        }
        return true;
    }

    @OnTouch(R.id.driverButton)
    public boolean onTouchDriver (View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 0)) {
            onButtonTouch(view, event, Page.DRIVER);
        }
        return true;
    }

    @OnTouch(R.id.iceButton)
    public boolean onTouchIce (View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 1)) {
            onButtonTouch(view, event, Page.ICE);
        }
        return true;
    }

    @OnTouch(R.id.passengersButton)
    public boolean onTouchPassengers (View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 2)) {
            onButtonTouch(view, event, Page.PASSENGERS);
        }
        return true;
    }

    @Subscribe
    public void onBackKeyEvent(BackKeyEvent event) { }

    @Override
    protected void setIconColor(View view) {
        LinearLayout viewGroup = (LinearLayout)((ViewGroup) view).getChildAt(0);
        MaterialIconView icon = (MaterialIconView) ((TableRow)(viewGroup.getChildAt(0))).getChildAt(0);
        TextView textView = (TextView) ((TableRow)(viewGroup.getChildAt(1))).getChildAt(0);
        icon.setColor(getResources().getColor(R.color.colorAccent));
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
    }


}
