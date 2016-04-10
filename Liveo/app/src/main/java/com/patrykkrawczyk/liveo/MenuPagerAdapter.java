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

    private List<AnimatedFragment> mList = null;
    private MenuFragment menuFragment;
    private DriverSettings driverFragment;
    private PassengerSelection passengerFragment;
    private IceSettings iceFragment;


    public MenuPagerAdapter(FragmentManager fm) {
        super(fm);
        mList = new ArrayList<>();
        menuFragment = new MenuFragment();
        passengerFragment = new PassengerSelection();
        driverFragment = new DriverSettings();
        iceFragment = new IceSettings();
        mList.add(menuFragment);
        mList.add(passengerFragment);
        //mList.add(driverFragment);
        //mList.add(iceFragment);
    }

    public void switchState(AnimatedFragment.Page page) {
//        AnimatedFragment newFragment;
//        if (page == AnimatedFragment.Page.PASSENGERS) newFragment = passengerFragment;
//        else if (page == AnimatedFragment.Page.DRIVER) newFragment = driverFragment;
//        else newFragment = iceFragment;

        if (page == AnimatedFragment.Page.PASSENGERS) mList.set(1, passengerFragment);
        else if (page == AnimatedFragment.Page.DRIVER) mList.set(1, driverFragment);
        else mList.set(1, iceFragment);


        //add(newFragment);

        //AnimatedFragment oldFragment = getItem(1);
        //if (oldFragment.equals(newFragment)) return;

        //int position = getItemPosition(newFragment);
        //mList.set(1, newFragment);
        //mList.set(position, oldFragment);
        //notifyDataSetChanged();
    }

    public void add(AnimatedFragment fragment)
    {
        mList.add(fragment);
        notifyDataSetChanged();
    }

    public void cleanFragments()
    {
        while(mList.size() > 1) {
            mList.remove(getCount() - 1);
            notifyDataSetChanged();
        }
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