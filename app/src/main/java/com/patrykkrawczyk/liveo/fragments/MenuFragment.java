package com.patrykkrawczyk.liveo.fragments;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.joooonho.SelectableRoundedImageView;
import com.patrykkrawczyk.liveo.Driver;
import com.patrykkrawczyk.liveo.GpsStatusEvent;
import com.patrykkrawczyk.liveo.LiveoApplication;
import com.patrykkrawczyk.liveo.NetworkStateEvent;
import com.patrykkrawczyk.liveo.activities.HubActivity;
import com.patrykkrawczyk.liveo.events.BackKeyEvent;
import com.patrykkrawczyk.liveo.events.SwitchPageEvent;
import com.patrykkrawczyk.liveo.managers.IceContact;
import com.patrykkrawczyk.liveo.managers.accelerometer.AccelerometerViewManager;
import com.patrykkrawczyk.liveo.managers.GuideManager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.events.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.events.ShowGuideEvent;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.OnTouch;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuFragment extends AnimatedFragment {

    @Bind(R.id.menuConnectionIcon)
    MaterialIconView menuConnectionIcon;
    @Bind(R.id.menuGpsIcon)
    MaterialIconView menuGpsIcon;

    @Bind(R.id.driverButton)
    Button driverButton;
    @Bind(R.id.triggerButton)
    FrameLayout triggerButton;
    @Bind(R.id.iceButton1)
    LinearLayout iceButton1;
    @Bind(R.id.iceButton2)
    LinearLayout iceButton2;
    @Bind(R.id.iceButton3)
    LinearLayout iceButton3;

    @Bind(R.id.menuDriverFirstName)
    TextView menuDriverFirstName;
    @Bind(R.id.menuDriverLastName)
    TextView menuDriverLastName;
    @Bind(R.id.menuDriverAgeGroup)
    TextView menuDriverAgeGroup;
    @Bind(R.id.menuDriverGender)
    TextView menuDriverGender;
    @Bind(R.id.menuDriverRegNumber)
    TextView menuDriverRegNumber;
    @Bind(R.id.menuDriverGenderIcon)
    MaterialIconView menuDriverGenderIcon;

    LocationManager locationManager;
    private int pickedContact = -1;
    private static final int PICK_CONTACT = 4444;
    private static boolean firstTimeRun = true;
    private EventBus eventBus;
    private boolean gpsEnabled;
    private boolean networkEnabled;

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

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo       = connectivityManager.getActiveNetworkInfo();
        EventBus.getDefault().post(new NetworkStateEvent(activeNetInfo != null));

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean state = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        EventBus.getDefault().post(new GpsStatusEvent(state));

        loadDriverViews();
        loadIceViews();
    }

    private void loadDriverViews() {
        Driver driver = Driver.getLocalDriver(getActivity());

        if (!driver.getFirstName().isEmpty()) menuDriverFirstName.setText(driver.getFirstName());
        else menuDriverGender.setText("EMPTY");
        if (!driver.getLastName().isEmpty()) menuDriverLastName.setText(driver.getLastName());
        else menuDriverLastName.setText("EMPTY");
        if (!driver.getAgeGroup().isEmpty()) menuDriverAgeGroup.setText(driver.getAgeGroup());
        else menuDriverAgeGroup.setText("EMPTY");

        if (!driver.getRegisterNumber().isEmpty()) menuDriverRegNumber.setText(driver.getRegisterNumber());
        else menuDriverRegNumber.setText("EMPTY");

        if (!driver.getGender().isEmpty()) {
            menuDriverGender.setText(driver.getGender());
            if (driver.getGender().equals("MALE"))
                menuDriverGenderIcon.setIcon(MaterialDrawableBuilder.IconValue.GENDER_MALE);
            else menuDriverGenderIcon.setIcon(MaterialDrawableBuilder.IconValue.GENDER_FEMALE);
        } else
            menuDriverGender.setText("EMPTY");
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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            animateViewTouch(view);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            touchEnabled = false;
            GuideManager.hideGuide();
            eventBus.post(new SwitchPageEvent(page));
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
        }
    }

    private void onIceClick(MotionEvent event, int contact, View view) {
        if (touchEnabled) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                animateViewTouch(view);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
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
                        name = name.split(" ")[0];
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
                        String strUri = null;
                        if (photoUri != null) strUri = photoUri.toString();

                        IceContact contact = new IceContact(id, name, number, strUri);
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

        icon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.plus_circle));
        icon.setBorderColor(getActivity().getResources().getColor(R.color.newAccent));
    }

    @OnTouch(R.id.triggerButton)
    public boolean onTouchTrigger(View view, MotionEvent event) {
        if (touchEnabled) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (validateConnectivity()) {
                    if (AccelerometerViewManager.isCalibrated()) {
                        Intent intent = new Intent(getContext(), HubActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        onButtonTouch(event, Page.CALIBRATION, view);
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                animateViewTouch(view);
            }
        }
        return true;
    }

    private boolean validateConnectivity() {
        boolean result = true;

        if (!gpsEnabled) {
            YoYo.with(Techniques.Shake)
                    .interpolate(new AccelerateInterpolator())
                    .duration(1000)
                    .playOn(menuGpsIcon);
            result = false;
        }

        if (!networkEnabled) {
            YoYo.with(Techniques.Shake)
                    .interpolate(new AccelerateInterpolator())
                    .duration(1000)
                    .playOn(menuConnectionIcon);
            result = false;
        }

        return result;
    }

    @OnTouch(R.id.driverButton)
    public boolean onTouchDriver(View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 0)) {
            if (touchEnabled) {
                onButtonTouch(event, Page.DRIVER, view);
            }
        }
        return true;
    }

    @OnTouch(R.id.iceButton1)
    public boolean onTouchIce1(View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 1)) {
            if (touchEnabled) {
                onIceClick(event, 1, view);
            }
        }
        return true;
    }
    @OnTouch(R.id.iceButton2)
    public boolean onTouchIce2(View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 1)) {
            if (touchEnabled) {
                onIceClick(event, 2, view);
            }
        }
        return true;
    }
    @OnTouch(R.id.iceButton3)
    public boolean onTouchIce3(View view, MotionEvent event) {
        if (!GuideManager.getShowGuide() || (GuideManager.getShowGuide() && GuideManager.getStage() == 1)) {
            if (touchEnabled) {
                onIceClick(event, 3, view);
            }
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

    @Subscribe
    public void onGpsStatusEvent(GpsStatusEvent event) {
        if (event.state) menuGpsIcon.setColor(getResources().getColor(R.color.enabled));
        else             menuGpsIcon.setColor(getResources().getColor(R.color.disabled));
        gpsEnabled = event.state;
    }

    @Subscribe
    public void onNetworkStateEvent(NetworkStateEvent event) {
        if (event.state) menuConnectionIcon.setColor(getResources().getColor(R.color.enabled));
        else             menuConnectionIcon.setColor(getResources().getColor(R.color.disabled));
        networkEnabled = event.state;
    }
}
