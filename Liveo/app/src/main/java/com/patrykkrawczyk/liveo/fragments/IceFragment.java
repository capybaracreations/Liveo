package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.ScrollStoppedEvent;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnTouch;

public class IceFragment extends AnimatedFragment {

    private EventBus eventBus;
    @Bind(R.id.confirmButton)   MaterialIconView confirmButton;
    @Bind(R.id.ice1Name)     MaterialEditText ice1Name;
    @Bind(R.id.ice2Name)     MaterialEditText ice2Name;
    @Bind(R.id.ice3Name)     MaterialEditText ice3Name;
    @Bind(R.id.ice1Phone)    MaterialEditText ice1Phone;
    @Bind(R.id.ice2Phone)    MaterialEditText ice2Phone;
    @Bind(R.id.ice3Phone)    MaterialEditText ice3Phone;

    public IceFragment() {
        super(R.layout.fragment_ice_settings);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);

        ripple.setEnabled(false);
    }


    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        Logger.d("onScrollStoppedEvent | DRIVER");
        touchEnabled = true;
    }



    @OnTouch(R.id.confirmButton)
    public boolean onConfirmButton(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (validateFields() > 0) {
                if (eventBus.isRegistered(this)) eventBus.unregister(this);
                touchEnabled = false;
                if (GuideManager.getStage() == 1) GuideManager.incrementStage();
                saveIce();
                setIconColor(view);
                ripple.setEnabled(false);
                rippleChangePage(event, Page.MENU);
            } else {
                performRippleNoSwitch(event);
            }
        }
        return true;
    }

    private int validateFields() {
        int validFields = 0;

        if (!ice1Name.getText().toString().isEmpty() && !ice1Phone.getText().toString().isEmpty()) validFields++;
        if (!ice2Name.getText().toString().isEmpty() && !ice2Phone.getText().toString().isEmpty()) validFields++;
        if (!ice3Name.getText().toString().isEmpty() && !ice3Phone.getText().toString().isEmpty()) validFields++;

        if (validFields == 0) {
            List<View> empties = new ArrayList<>();
            if (ice1Name.getText().toString().isEmpty()) {
                empties.add(ice1Name);
            }
            if (ice1Phone.getText().toString().isEmpty()) {
                empties.add(ice1Phone);
            }
            if (ice2Name.getText().toString().isEmpty()) {
                empties.add(ice2Name);
            }
            if (ice2Phone.getText().toString().isEmpty()) {
                empties.add(ice2Phone);
            }
            if (ice3Name.getText().toString().isEmpty()) {
                empties.add(ice3Name);
            }
            if (ice3Phone.getText().toString().isEmpty()) {
                empties.add(ice3Phone);
            }
            for (View view:empties) {
                YoYo.with(Techniques.Shake)
                        .interpolate(new AccelerateInterpolator())
                        .duration(1000)
                        .playOn(view);
            }
        }

        return validFields;
    }

    private void saveIce() {
        SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.LIVEO_ICE_1_NAME), ice1Name.getText().toString());
        editor.putString(getString(R.string.LIVEO_ICE_2_NAME), ice2Name.getText().toString());
        editor.putString(getString(R.string.LIVEO_ICE_3_NAME), ice3Name.getText().toString());
        editor.putString(getString(R.string.LIVEO_ICE_1_PHONE), ice1Phone.getText().toString());
        editor.putString(getString(R.string.LIVEO_ICE_2_PHONE), ice2Phone.getText().toString());
        editor.putString(getString(R.string.LIVEO_ICE_3_PHONE), ice3Phone.getText().toString());

        editor.apply();
    }



}
