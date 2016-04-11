package com.patrykkrawczyk.liveo.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.fragments.AnimatedFragment;
import com.patrykkrawczyk.liveo.fragments.DriverSettings;
import com.patrykkrawczyk.liveo.fragments.IceSettings;
import com.patrykkrawczyk.liveo.fragments.MenuFragment;
import com.patrykkrawczyk.liveo.fragments.PassengerSelection;
import com.patrykkrawczyk.liveo.fragments.AnimatedFragment.Page;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.mainViewPager)
    MyViewPager mainViewPager;


    static private MenuFragment menuFragment;
    static private DriverSettings driverFragment;
    static private PassengerSelection passengerFragment;
    static private IceSettings iceFragment;

    private MenuPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Logger.init(getString(R.string.APP_TAG));

        mPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(mPagerAdapter);
        mainViewPager.setPageTransformer(true, new CubeOutTransformer());

        menuFragment = new MenuFragment();
        passengerFragment = new PassengerSelection();
        driverFragment = new DriverSettings();
        iceFragment = new IceSettings();

        mPagerAdapter.add(menuFragment);
        mPagerAdapter.add(passengerFragment);
        mPagerAdapter.add(driverFragment);
        mPagerAdapter.add(iceFragment);
    }

    @Override
    public void onBackPressed() {
        if (mainViewPager.getCurrentItem() > 0) {
            mainViewPager.setCurrentItem(0);
        }
    }

}
