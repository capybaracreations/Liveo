package com.patrykkrawczyk.liveo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.patrykkrawczyk.liveo.events.BackKeyEvent;
import com.patrykkrawczyk.liveo.managers.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.events.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.events.SwitchPageEvent;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnTouch;

public class IceFragment extends AnimatedFragment {

    private static final int PICK_CONTACT = 4444;
    private int lastPickedContact = 0;
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


        ice1Name.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        ice2Name.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        ice3Name.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        ice1Phone.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        ice2Phone.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        ice3Phone.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        loadData();
    }

    private void loadData() {
        SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        String s1n = sharedPref.getString(getString(R.string.LIVEO_ICE_1_NAME),  "");
        String s2n = sharedPref.getString(getString(R.string.LIVEO_ICE_2_NAME),  "");
        String s3n = sharedPref.getString(getString(R.string.LIVEO_ICE_3_NAME),  "");
        String s1p = sharedPref.getString(getString(R.string.LIVEO_ICE_1_PHONE), "");
        String s2p = sharedPref.getString(getString(R.string.LIVEO_ICE_2_PHONE), "");
        String s3p = sharedPref.getString(getString(R.string.LIVEO_ICE_3_PHONE), "");

        if (!s1n.isEmpty()) ice1Name.setText(s1n);
        if (!s2n.isEmpty()) ice2Name.setText(s2n);
        if (!s3n.isEmpty()) ice3Name.setText(s3n);
        if (!s1p.isEmpty()) ice1Phone.setText(s1p);
        if (!s2p.isEmpty()) ice2Phone.setText(s2p);
        if (!s3p.isEmpty()) ice3Phone.setText(s3p);
    }


    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
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
                //setIconColor(view);
                ripple.setEnabled(false);
                rippleChangePage(event, Page.MENU);
            } else {
                performRippleNoSwitch(event);
            }
        }
        return true;
    }

    @OnTouch(R.id.contact1Button)
    public boolean onContact1Button(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            onContactButton((MaterialIconView) view);
        }
        return true;
    }

    @OnTouch(R.id.contact2Button)
    public boolean onContact2Button(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            onContactButton((MaterialIconView) view);
        }
        return true;
    }

    @OnTouch(R.id.contact3Button)
    public boolean onContact3Button(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            onContactButton((MaterialIconView) view);
        }
        return true;
    }

    private void onContactButton(MaterialIconView view) {
        Log.d("PATRYCZEK", view.getTag().toString());
        try {
            lastPickedContact = Integer.parseInt(view.getTag().toString());
            Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts/people"));
            startActivityForResult(intent, PICK_CONTACT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getActivity().getContentResolver().query(contactData, null, null, null, null);
                    getActivity().startManagingCursor(c);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndexOrThrow(Contacts.People.NAME));
                        String number = c.getString(c.getColumnIndexOrThrow(Contacts.People.NUMBER));

                        MaterialEditText nameET, numberET;
                        if (lastPickedContact == 1) {
                            nameET = ice1Name;
                            numberET = ice1Phone;
                        } else if (lastPickedContact == 2) {
                            nameET = ice2Name;
                            numberET = ice2Phone;
                        } else {
                            nameET = ice3Name;
                            numberET = ice3Phone;
                        }
                        nameET.setText(name);
                        numberET.setText(number);
                    }
                }
                break;
        }
    }

    private int validateFields() {
        int validFields = 0;
        List<View> empties = new ArrayList<>();

        if (!ice1Name.getText().toString().isEmpty()) {
            if (ice1Phone.getText().toString().isEmpty() || !android.util.Patterns.PHONE.matcher(ice1Phone.getText().toString()).matches()) {
                empties.add(ice1Phone);
                } else validFields++;
        } else {
            empties.add(ice1Name);

            if (!ice2Name.getText().toString().isEmpty()) {
                if (ice2Phone.getText().toString().isEmpty() || !android.util.Patterns.PHONE.matcher(ice2Phone.getText().toString()).matches()) {
                    empties.add(ice2Phone);
                } else validFields++;
            } else {
                empties.add(ice2Name);

                if (!ice3Name.getText().toString().isEmpty()) {
                    if (ice3Phone.getText().toString().isEmpty() || !android.util.Patterns.PHONE.matcher(ice3Phone.getText().toString()).matches()) {
                        empties.add(ice3Phone);
                    } else validFields++;
                } else empties.add(ice3Name);
            }
        }

        for (View view:empties) {
            YoYo.with(Techniques.Shake)
                    .interpolate(new AccelerateInterpolator())
                    .duration(1000)
                    .playOn(view);
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


    @Subscribe
    public void onBackKeyEvent(BackKeyEvent event) {
//        if (validateFields() > 0) {
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
                //setIconColor(getActivity().findViewById(R.id.confirmButton));
//            if (GuideManager.getStage() == 1) GuideManager.incrementStage();
//            saveIce();
//            ripple.setEnabled(false);
            eventBus.post(new SwitchPageEvent(Page.MENU));
   //     }
    }

}
