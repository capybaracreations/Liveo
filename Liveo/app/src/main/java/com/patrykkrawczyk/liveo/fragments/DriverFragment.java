package com.patrykkrawczyk.liveo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.patrykkrawczyk.liveo.INetwork;
import com.patrykkrawczyk.liveo.events.BackKeyEvent;
import com.patrykkrawczyk.liveo.Driver;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverFragment extends AnimatedFragment implements Callback<ResponseBody> {


    @Bind(R.id.firstNameEditText)   MaterialEditText firstNameEditText;
    @Bind(R.id.lastNameEditText)   MaterialEditText lastNameEditText;
    @Bind(R.id.registrationNumberEditText)   MaterialEditText registrationNumberEditText;
    @Bind(R.id.maleSelection)       MaterialIconView maleSelection;
    @Bind(R.id.femaleSelection)     MaterialIconView femaleSelection;
    @Bind(R.id.confirmButton)       MaterialIconView confirmButton;
    @Bind(R.id.teenSelection)       TextView teenSelection;
    @Bind(R.id.adultSelection)      TextView adultSelection;
    @Bind(R.id.seniorSelection)     TextView seniorSelection;

    private EventBus eventBus;

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }

    enum Gender{
        MALE, FEMALE
    }

    Gender gender;
    enum AgeGroup{
        TEEN, ADULT, SENIOR
    }

    AgeGroup ageGroup;
    String id;

    public DriverFragment() {
        super(R.layout.fragment_driver_settings);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) eventBus.register(this);

        firstNameEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        lastNameEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        registrationNumberEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        loadData();
    }

    private void loadData() {
        SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        id = sharedPref.getString(getString(R.string.LIVEO_DRIVER_ID),  "");
        String fn = sharedPref.getString(getString(R.string.LIVEO_DRIVER_FIRSTNAME),  "");
        String ln = sharedPref.getString(getString(R.string.LIVEO_DRIVER_LASTNAME),  "");
        String rn = sharedPref.getString(getString(R.string.LIVEO_DRIVER_REGISTRATION),  "");
        String g = sharedPref.getString(getString(R.string.LIVEO_DRIVER_GENDER), "");
        String ag = sharedPref.getString(getString(R.string.LIVEO_DRIVER_AGEGROUP), "");

        if (!fn.isEmpty()) firstNameEditText.setText(fn);
        if (!ln.isEmpty()) lastNameEditText.setText(ln);
        if (!rn.isEmpty()) registrationNumberEditText.setText(rn);
        if (!g.isEmpty()) {
            if (g.equals("male")) {
                gender = Gender.MALE;
                femaleSelection.setColor(Color.WHITE);
                maleSelection.setColor(getResources().getColor(R.color.colorAccent));
            } else if (g.equals("female")) {
                gender = Gender.FEMALE;
                maleSelection.setColor(Color.WHITE);
                femaleSelection.setColor(getResources().getColor(R.color.colorAccent));
            }
        }
        if (!ag.isEmpty()) {
            if (ag.equals("teen")) {
                ageGroup = AgeGroup.TEEN;
                adultSelection.setTextColor(Color.WHITE);
                teenSelection.setTextColor(getResources().getColor(R.color.colorAccent));
                seniorSelection.setTextColor(Color.WHITE);
            } else if (ag.equals("adult")) {
                ageGroup = AgeGroup.ADULT;
                teenSelection.setTextColor(Color.WHITE);
                adultSelection.setTextColor(getResources().getColor(R.color.colorAccent));
                seniorSelection.setTextColor(Color.WHITE);
            } else if (ag.equals("senior")) {
                ageGroup = AgeGroup.SENIOR;
                teenSelection.setTextColor(Color.WHITE);
                seniorSelection.setTextColor(getResources().getColor(R.color.colorAccent));
                adultSelection.setTextColor(Color.WHITE);
            }
        }
    }

    @OnTouch(R.id.confirmButton)
    public boolean onTouchConfirm(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && touchEnabled) {
            performCheck(event);
        }
        return true;
    }

    @OnTouch(R.id.menuLayout)
    public boolean onMenuTouch(View v, MotionEvent event) {
        return false;
    }

    @Subscribe
    public void onScrollStoppedEvent(ScrollStoppedEvent event) {
        touchEnabled = true;
    }

    @Subscribe
    public void onBackKeyEvent(BackKeyEvent event) {
//        Driver driver = validateData();

//        if (driver != null)  {
//            if (GuideManager.getStage() == 0) GuideManager.incrementStage();
//            confirmButton.setColor(getResources().getColor(R.color.colorAccent));
//            saveDriverData();
//            Driver.setCurrentDriver(getContext(), driver);
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
            eventBus.post(new SwitchPageEvent(Page.MENU));
//        }
    }

    private void performCheck(MotionEvent event) {
        Driver driver = validateData();

        if (driver == null) {
            //confirmButton.setColor(Color.RED);
            performRippleNoSwitch(event);
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.LIVEO_API_URL))
                    .build();
            INetwork iNetwork = retrofit.create(INetwork.class);
            Call<ResponseBody> call = iNetwork.modify(driver.getId(), driver.getFirstName(),
                                                driver.getLastName(), driver.getRegisterNumber(),
                                                driver.getGender(), driver.getAgeGroup());
            call.enqueue(this);

            if (GuideManager.getStage() == 0) GuideManager.incrementStage();
            confirmButton.setColor(getResources().getColor(R.color.colorAccent));
            saveDriverData();
            Driver.setCurrentDriver(getContext(), driver);
            rippleChangePage(event, Page.MENU);
            if (eventBus.isRegistered(this)) eventBus.unregister(this);
            touchEnabled = false;
        }
    }

    private Driver validateData() {
        Driver driver = new Driver();
        String sGender = "";
        String sAgeGroup = "";

        if (gender == Gender.MALE) sGender = "male";
        else if (gender == Gender.FEMALE) sGender = "female";

        if (ageGroup == AgeGroup.TEEN) sAgeGroup = "teen";
        else if (ageGroup == AgeGroup.ADULT) sAgeGroup = "adult";
        else if (ageGroup == AgeGroup.SENIOR) sAgeGroup = "senior";

        driver.setId(id);
        driver.setFirstName(firstNameEditText.getText().toString());
        driver.setLastName(lastNameEditText.getText().toString());
        driver.setRegisterNumber(registrationNumberEditText.getText().toString());
        driver.setGender(sGender);
        driver.setAgeGroup(sAgeGroup);

        if (Driver.validateDriver(driver)) return driver;
        else {
            List<View> empties = new ArrayList<>();
            if (driver.getFirstName().isEmpty()) {
                empties.add(firstNameEditText);
            }
            if (driver.getLastName().isEmpty()) {
                empties.add(lastNameEditText);
            }
            if (driver.getRegisterNumber().isEmpty()) {
                empties.add(registrationNumberEditText);
            }
            if (driver.getGender().isEmpty()) {
                empties.add(maleSelection);
                empties.add(femaleSelection);
            }
            if (driver.getAgeGroup().isEmpty()) {
                empties.add(teenSelection);
                empties.add(adultSelection);
                empties.add(seniorSelection);
            }
            for (View view:empties) {
                YoYo.with(Techniques.Shake)
                        .interpolate(new AccelerateInterpolator())
                        .duration(1000)
                        .playOn(view);
            }
            return null;
        }
    }

    private void saveDriverData() {
        SharedPreferences sharedPref    = getActivity().getSharedPreferences(getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String sGender;
        String sAgeGroup;

        if (gender == Gender.MALE) sGender = "male";
        else sGender = "female";

        if (ageGroup == AgeGroup.TEEN) sAgeGroup = "teen";
        else if (ageGroup == AgeGroup.ADULT) sAgeGroup = "adult";
        else sAgeGroup = "senior";

        editor.putString(getString(R.string.LIVEO_DRIVER_ID),            id);
        editor.putString(getString(R.string.LIVEO_DRIVER_FIRSTNAME),     firstNameEditText.getText().toString());
        editor.putString(getString(R.string.LIVEO_DRIVER_LASTNAME),      lastNameEditText.getText().toString());
        editor.putString(getString(R.string.LIVEO_DRIVER_REGISTRATION),  registrationNumberEditText.getText().toString());
        editor.putString(getString(R.string.LIVEO_DRIVER_GENDER),        sGender);
        editor.putString(getString(R.string.LIVEO_DRIVER_AGEGROUP),      sAgeGroup);

        editor.apply();
    }

    @OnTouch(R.id.maleSelection)
    public boolean onTouchMale(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && touchEnabled) {
            gender = Gender.MALE;
            femaleSelection.setColor(Color.WHITE);
            maleSelection.setColor(getResources().getColor(R.color.colorAccent));
            performRippleNoSwitch(event);
        }
        return true;
    }

    @OnTouch(R.id.femaleSelection)
    public boolean onTouchFemale(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && touchEnabled) {
            gender = Gender.FEMALE;
            femaleSelection.setColor(getResources().getColor(R.color.colorAccent));
            maleSelection.setColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }

    @OnTouch(R.id.teenSelection)
    public boolean onTouchTeen(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && touchEnabled) {
            ageGroup = AgeGroup.TEEN;
            teenSelection.setTextColor(getResources().getColor(R.color.colorAccent));
            adultSelection.setTextColor(Color.WHITE);
            seniorSelection.setTextColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }

    @OnTouch(R.id.adultSelection)
    public boolean onTouchAdult(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && touchEnabled) {
            ageGroup = AgeGroup.ADULT;
            teenSelection.setTextColor(Color.WHITE);
            adultSelection.setTextColor(getResources().getColor(R.color.colorAccent));
            seniorSelection.setTextColor(Color.WHITE);
            performRippleNoSwitch(event);
        }
        return true;
    }

    @OnTouch(R.id.seniorSelection)
    public boolean onTouchSenior(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && touchEnabled) {
            ageGroup = AgeGroup.SENIOR;
            teenSelection.setTextColor(Color.WHITE);
            adultSelection.setTextColor(Color.WHITE);
            seniorSelection.setTextColor(getResources().getColor(R.color.colorAccent));
            performRippleNoSwitch(event);
        }
        return true;
    }



}
