package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.events.BackKeyEvent;
import com.patrykkrawczyk.liveo.managers.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.events.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.events.SwitchPageEvent;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.OnTouch;

public class PassengerFragment extends AnimatedFragment {


    public PassengerFragment() { super(R.layout.fragment_passenger_selection); }


    private EventBus eventBus;


    @Override
    public void onStart() {
        super.onStart();

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);

        loadData();
    }

    private void buttonTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && touchEnabled) {

            MaterialIconView icon = (MaterialIconView) getActivity().findViewById(R.id.passenger0Button);
            icon.setColor(getResources().getColor(R.color.WHITE));

            icon = (MaterialIconView) getActivity().findViewById(R.id.passenger1Button);
            icon.setColor(getResources().getColor(R.color.WHITE));

            icon = (MaterialIconView) getActivity().findViewById(R.id.passenger2Button);
            icon.setColor(getResources().getColor(R.color.WHITE));

            icon = (MaterialIconView) getActivity().findViewById(R.id.passenger3Button);
            icon.setColor(getResources().getColor(R.color.WHITE));

            icon = (MaterialIconView) getActivity().findViewById(R.id.passenger4Button);
            icon.setColor(getResources().getColor(R.color.WHITE));

            icon = (MaterialIconView) getActivity().findViewById(R.id.passenger5Button);
            icon.setColor(getResources().getColor(R.color.WHITE));


            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            setIconColor(view);
            savePassengerCount(view, event);
            rippleChangePage(event, Page.MENU);
        }
    }

    @OnTouch(R.id.passenger0Button)
    public boolean onPassenger0Button(View view, MotionEvent event) {
        buttonTouch(view, event);
        return true;
    }
    @OnTouch(R.id.passenger1Button)
    public boolean onPassenger1Button(View view, MotionEvent event) {
        buttonTouch(view, event);
        return true;
    }

    @OnTouch(R.id.passenger2Button)
    public boolean onPassenger2Button(View view, MotionEvent event) {
        buttonTouch(view, event);
        return true;
    }
    @OnTouch(R.id.passenger3Button)
    public boolean onPassenger3Button(View view, MotionEvent event) {
        buttonTouch(view, event);
        return true;
    }

    @OnTouch(R.id.passenger4Button)
    public boolean onPassenger4Button(View view, MotionEvent event) {
        buttonTouch(view, event);
        return true;
    }
    @OnTouch(R.id.passenger5Button)
    public boolean onPassenger5Button(View view, MotionEvent event) {
        buttonTouch(view, event);
        return true;
    }


    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        Logger.d("onScrollStoppedEvent | DRIVER");
        touchEnabled = true;
    }

    private void savePassengerCount(View view, MotionEvent event) {
        if (GuideManager.getStage() == 2) GuideManager.incrementStage();
        String count                    = view.getTag().toString();
        SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.LIVEO_PASSENGERS_COUNT), count);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        String count = sharedPref.getString(getString(R.string.LIVEO_PASSENGERS_COUNT),  "");

        if (!count.isEmpty()) {
            if (count.equals("0"))      setIconColor(getActivity().findViewById(R.id.passenger0Button));
            else if (count.equals("1")) setIconColor(getActivity().findViewById(R.id.passenger1Button));
            else if (count.equals("2")) setIconColor(getActivity().findViewById(R.id.passenger2Button));
            else if (count.equals("3")) setIconColor(getActivity().findViewById(R.id.passenger3Button));
            else if (count.equals("4")) setIconColor(getActivity().findViewById(R.id.passenger4Button));
            else if (count.equals("5")) setIconColor(getActivity().findViewById(R.id.passenger5Button));
        }
    }

    @Subscribe
    public void onBackKeyEvent(BackKeyEvent event) {
//        SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
//        String count = sharedPref.getString(getString(R.string.LIVEO_PASSENGERS_COUNT), "");
//        if (count.isEmpty()) {
//            List<View> empties = new ArrayList<>();
//
//            empties.add(getActivity().findViewById(R.id.passenger0Button));
//            empties.add(getActivity().findViewById(R.id.passenger1Button));
//            empties.add(getActivity().findViewById(R.id.passenger2Button));
//            empties.add(getActivity().findViewById(R.id.passenger3Button));
//            empties.add(getActivity().findViewById(R.id.passenger4Button));
//            empties.add(getActivity().findViewById(R.id.passenger5Button));
//
//            for (View view:empties) {
//                YoYo.with(Techniques.Shake)
//                        .interpolate(new AccelerateInterpolator())
//                        .duration(1000)
//                        .playOn(view);
//            }
//        } else {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            eventBus.post(new SwitchPageEvent(Page.MENU));
        //}
    }

}