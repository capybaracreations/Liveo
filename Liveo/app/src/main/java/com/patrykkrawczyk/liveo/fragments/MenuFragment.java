package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.ShowGuideEvent;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnTouch;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuFragment extends AnimatedFragment {

    @Bind(R.id.driverButton) TableRow driverButton;
    @Bind(R.id.passengersButton) TableRow passengersButton;
    @Bind(R.id.iceButton) TableRow iceButton;
    TextView driverName;
    private static boolean firstTimeRun = true;
    private EventBus eventBus;
    SharedPreferences sharedPreferences;

    public MenuFragment() {
        super(R.layout.fragment_menu);
    }


    @Override
    public void onStart() {
        super.onStart();

        if (firstTimeRun == true) {
            firstTimeRun = false;
            touchEnabled = true;
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

        driverName = (TextView) driverButton.findViewById(R.id.driverText);
        Driver driver = Driver.getCurrentDriver(getContext());
        if (driver == null) driverName.setText("Driver");
        else driverName.setText(driver.getFirstName().toUpperCase() + " " + driver.getLastName().toUpperCase());

    }

    @Subscribe
    public void onShowGuideEvent(ShowGuideEvent event) {
        if (GuideManager.getShowGuide()) {
            if (GuideManager.getStage() == 0) GuideManager.showGuide(getActivity(), driverButton);
            else if (GuideManager.getStage() == 1) GuideManager.showGuide(getActivity(), iceButton);
            else if (GuideManager.getStage() == 2) GuideManager.showGuide(getActivity(), passengersButton);
            else GuideManager.finishGuide(getActivity());
        }
        Logger.d("onShowGuideEvent | MENU");
    }

    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        Logger.d("onScrollStoppedEvent | MENU");
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

        setIconColor(view);
        Point point = new Point((int) event.getRawX(), (int) event.getRawY());
        ripple.setRipplePersistent(true);
        ripple.performRipple(point);
//        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 0)) {
//            onButtonTouch(view, event, Page.DRIVER);
//        }
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


    @Override
    protected void setIconColor(View view) {
        LinearLayout viewGroup = (LinearLayout)((ViewGroup) view).getChildAt(0);
        MaterialIconView icon = (MaterialIconView) ((TableRow)(viewGroup.getChildAt(0))).getChildAt(0);
        TextView textView = (TextView) ((TableRow)(viewGroup.getChildAt(1))).getChildAt(0);
        icon.setColor(getResources().getColor(R.color.colorAccent));
        textView.setTextColor(getResources().getColor(R.color.colorAccent));
    }


}
