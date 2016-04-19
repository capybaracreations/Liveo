package com.patrykkrawczyk.liveo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.patrykkrawczyk.liveo.fragments.AnimatedFragment;
import com.patrykkrawczyk.liveo.fragments.DriverSettings;
import com.patrykkrawczyk.liveo.fragments.IceSettings;
import com.patrykkrawczyk.liveo.fragments.MenuFragment;
import com.patrykkrawczyk.liveo.fragments.PassengerSelection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk Krawczyk on 10.04.2016.
 */
public class MenuPagerAdapter extends FragmentStatePagerAdapter {

    //static private MenuFragment menuFragment;
    //static private DriverSettings driverFragment;
    //static private PassengerSelection passengerFragment;
    //static private IceSettings iceFragment;
    private List<AnimatedFragment> mList = null;

    public MenuPagerAdapter(FragmentManager fm, AnimatedFragment.Page page) {
        super(fm);

        //menuFragment      = new MenuFragment();
        //passengerFragment = new PassengerSelection();
        //driverFragment    = new DriverSettings();
        //iceFragment       = new IceSettings();

        mList = new ArrayList<>(2);

        if (page == AnimatedFragment.Page.DRIVER) mList.add(new DriverSettings());
        else if (page == AnimatedFragment.Page.ICE) mList.add(new IceSettings());
        else if (page == AnimatedFragment.Page.PASSENGERS) mList.add(new PassengerSelection());
        else {
            mList.add(new MenuFragment());
            mList.add(new PassengerSelection());
            //mList.add(driverFragment);
            //mList.add(iceFragment);
        }
    }

    public void switchPage(AnimatedFragment.Page page) {
        AnimatedFragment newFragment;
        int position = 1;

        if (page == AnimatedFragment.Page.PASSENGERS) newFragment = new PassengerSelection();
        else if (page == AnimatedFragment.Page.DRIVER) newFragment = new DriverSettings();
        else if (page == AnimatedFragment.Page.ICE) newFragment = new IceSettings();
        else {
            newFragment = new MenuFragment();
            position = 0;
        }

        mList.set(position, newFragment);
        notifyDataSetChanged();


        //if (page == AnimatedFragment.Page.PASSENGERS) mList.set(1, passengerFragment);
        //else if (page == AnimatedFragment.Page.DRIVER) mList.set(1, driverFragment);
        //else mList.set(1, iceFragment);

        //int position = getItemPosition(newFragment);
        //mList.set(1, newFragment);
        //mList.set(position, oldFragment);
        //notifyDataSetChanged();
    }

    @Override
    public AnimatedFragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        int position = mList.indexOf(object);
        return position == -1 ? POSITION_NONE : position;
    }
}