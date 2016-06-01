package com.patrykkrawczyk.liveo.fragments;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.LiveoApplication;
import com.patrykkrawczyk.liveo.activities.CalibrateActivity;
import com.patrykkrawczyk.liveo.events.BackKeyEvent;
import com.patrykkrawczyk.liveo.events.SwitchPageEvent;
import com.patrykkrawczyk.liveo.managers.IceContact;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerViewManager;
import com.patrykkrawczyk.liveo.managers.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.events.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.events.ShowGuideEvent;
import com.patrykkrawczyk.liveo.activities.HubActivity;
import com.patrykkrawczyk.liveo.managers.StateManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.OnTouch;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuFragment extends AnimatedFragment {

    @Bind(R.id.driverButton)  Button   driverButton;
    @Bind(R.id.triggerButton) FrameLayout triggerButton;
    @Bind(R.id.iceButton1)    LinearLayout iceButton1;
    @Bind(R.id.iceButton2)    LinearLayout iceButton2;
    @Bind(R.id.iceButton3)    LinearLayout iceButton3;

    private int pickedContact = -1;
    private static final int PICK_CONTACT = 4444;
    private static boolean firstTimeRun = true;
    private EventBus eventBus;

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
                    GuideManager.showGuide(getActivity(), iceButton2);
                else GuideManager.finishGuide(getActivity());
            }
        }

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);

        loadIceViews();
        /*driverName = (TextView) driverButton.findViewById(R.id.driverText);
        Driver driver = Driver.getLocalDriver(getContext());
        if (driver == null) driverName.setText("Driver");
        else driverName.setText(driver.getFirstName().toUpperCase() + " " + driver.getLastName().toUpperCase());*/
    }

    @Subscribe
    public void onShowGuideEvent(ShowGuideEvent event) {
        if (GuideManager.getShowGuide()) {
            if (GuideManager.getStage() == 0) GuideManager.showGuide(getActivity(), driverButton);
            else if (GuideManager.getStage() == 1) GuideManager.showGuide(getActivity(), iceButton2);
            else GuideManager.finishGuide(getActivity());
        }
    }

    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        touchEnabled = true;
    }


    private void onButtonTouch(MotionEvent event, Page page, View view) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            animateViewTouch(view);
            touchEnabled = false;
            GuideManager.hideGuide();
            eventBus.post(new SwitchPageEvent(page));
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
        }
    }

    private void onIceClick(MotionEvent event, int contact, View view) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            animateViewTouch(view);
            pickedContact = contact;
            IceContact iceContact = LiveoApplication.iceContactList.get(pickedContact-1);

            if (iceContact == null) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            } else {
                LinearLayout layout = null;
                if (pickedContact == 1) layout = iceButton1;
                else if (pickedContact == 2) layout = iceButton2;
                else if (pickedContact == 3) layout = iceButton3;
                TextView label = (TextView) layout.getChildAt(1);

                if (label.getText().equals("ADD")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                } else {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("")
                        .setCancelText("DELETE")
                        .setConfirmText("MODIFY")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                startActivityForResult(intent, PICK_CONTACT);
                                sDialog.cancel();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                LiveoApplication.iceContactList.set(pickedContact - 1, null);
                                loadIceViews();
                                saveIceViews();
                                sDialog.cancel();
                            }
                        })
                        .show();
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getActivity().managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String number = "";

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            c = getActivity().getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, null, null);
                            c.moveToFirst();
                            number = c.getString(c.getColumnIndex("data1"));
                        }

                        Uri photoUri = getPhotoUri(id);

                        
                        IceContact contact = new IceContact(id, name, number, photoUri.toString());
                        if (!contact.validate()) {
                            contact = null;
                        }

                        if (GuideManager.getStage() == 1) {
                            GuideManager.incrementStage();
                            eventBus.post(new ShowGuideEvent());
                        }
                        LiveoApplication.iceContactList.set(pickedContact - 1, contact);
                        loadIceViews();
                        saveIceViews();
                    }
                }
                break;
        }
    }

    private void saveIceViews() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        List<IceContact> list = LiveoApplication.iceContactList;
        IceContact ice1 = list.get(0);
        IceContact ice2 = list.get(1);
        IceContact ice3 = list.get(2);

        if (ice1 == null) ice1 = new IceContact();
        if (ice2 == null) ice2 = new IceContact();
        if (ice3 == null) ice3 = new IceContact();

        editor.putString(getString(R.string.LIVEO_ICE_1_ID), ice1.id);
        editor.putString(getString(R.string.LIVEO_ICE_1_URI), ice1.photoUri);
        editor.putString(getString(R.string.LIVEO_ICE_1_NAME), ice1.name);
        editor.putString(getString(R.string.LIVEO_ICE_1_PHONE), ice1.number);

        editor.putString(getString(R.string.LIVEO_ICE_2_ID), ice2.id);
        editor.putString(getString(R.string.LIVEO_ICE_2_URI), ice2.photoUri);
        editor.putString(getString(R.string.LIVEO_ICE_2_NAME), ice2.name);
        editor.putString(getString(R.string.LIVEO_ICE_2_PHONE), ice2.number);

        editor.putString(getString(R.string.LIVEO_ICE_3_ID), ice3.id);
        editor.putString(getString(R.string.LIVEO_ICE_3_URI), ice3.photoUri);
        editor.putString(getString(R.string.LIVEO_ICE_3_NAME), ice3.name);
        editor.putString(getString(R.string.LIVEO_ICE_3_PHONE), ice3.number);

        editor.apply();
    }

    private void loadIceViews() {
        List<IceContact> list = LiveoApplication.iceContactList;
        IceContact ice1 = list.get(0);
        IceContact ice2 = list.get(1);
        IceContact ice3 = list.get(2);

        if (ice1 == null) markEmpty(iceButton1);
        else markFilled(iceButton1, ice1);
        if (ice2 == null) markEmpty(iceButton2);
        else markFilled(iceButton2, ice2);
        if (ice3 == null) markEmpty(iceButton3);
        else markFilled(iceButton3, ice3);
    }

    private void markFilled(LinearLayout layout, IceContact contact) {
        SelectableRoundedImageView icon = (SelectableRoundedImageView) layout.getChildAt(0);
        TextView label = (TextView) layout.getChildAt(1);

        label.setTextColor(getActivity().getResources().getColor(R.color.newFont));
        label.setText(contact.name);

        icon.setBorderColor(getActivity().getResources().getColor(R.color.newFont));

        if (contact.photoUri != null) {
            icon.setImageURI(Uri.parse(contact.photoUri));
        } else {
            icon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.account));
        }

        if (icon.getDrawable() == null) {
            icon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.account));
        }
    }

    private void markEmpty(LinearLayout layout) {
        SelectableRoundedImageView icon = (SelectableRoundedImageView) layout.getChildAt(0);
        TextView label = (TextView) layout.getChildAt(1);

        label.setTextColor(getActivity().getResources().getColor(R.color.newAccent));
        label.setText("ADD");

        icon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.plus));
        icon.setBorderColor(getActivity().getResources().getColor(R.color.newAccent));
    }

    @OnTouch(R.id.triggerButton)
    public boolean onTouchTrigger(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && touchEnabled) {
            if (AccelerometerViewManager.isCalibrated()) {
                animateViewTouch(view);
                Intent intent = new Intent(getContext(), HubActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                onButtonTouch(event, Page.CALIBRATION, view);
            }
        }
        return true;
    }

    @OnTouch(R.id.driverButton)
    public boolean onTouchDriver(View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 0)) {
            onButtonTouch(event, Page.DRIVER, view);
        }
        return true;
    }

    @OnTouch(R.id.iceButton1)
    public boolean onTouchIce1(View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 1)) {
            onIceClick(event, 1, view);
        }
        return true;
    }
    @OnTouch(R.id.iceButton2)
    public boolean onTouchIce2(View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 1)) {
            onIceClick(event, 2, view);
        }
        return true;
    }
    @OnTouch(R.id.iceButton3)
    public boolean onTouchIce3(View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 1)) {
            onIceClick(event, 3, view);
        }
        return true;
    }

    @Subscribe
    public void onBackKeyEvent(BackKeyEvent event) { }

    public Uri getPhotoUri(String id) {
        try {
            Cursor cur = getActivity().getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
                            + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
                    null);
            if (cur != null) {
                if (!cur.moveToFirst()) {
                    return null; // no photo
                }
            } else {
                return null; // error in cursor process
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                .parseLong(id));
        return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }
}
