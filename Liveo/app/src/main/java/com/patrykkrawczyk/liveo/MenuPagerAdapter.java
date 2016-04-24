package com.patrykkrawczyk.liveo;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.patrykkrawczyk.liveo.fragments.AnimatedFragment;
import com.patrykkrawczyk.liveo.fragments.DriverFragment;
import com.patrykkrawczyk.liveo.fragments.IceFragment;
import com.patrykkrawczyk.liveo.fragments.MenuFragment;
import com.patrykkrawczyk.liveo.fragments.PassengerFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk Krawczyk on 10.04.2016.
 */
public class MenuPagerAdapter extends FragmentStatePagerAdapter {

    //static private MenuFragment menuFragment;
    //static private DriverFragment driverFragment;
    //static private PassengerFragment passengerFragment;
    //static private IceFragment iceFragment;
    public List<AnimatedFragment> mList = null;

    public MenuPagerAdapter(FragmentManager fm) {
        super(fm);

        //menuFragment      = new MenuFragment();
        //passengerFragment = new PassengerFragment();
        //driverFragment    = new DriverFragment();
        //iceFragment       = new IceFragment();

        mList = new ArrayList<>(2);

        mList.add(new MenuFragment());
        //mList.add(new PassengerFragment());

        //mList.add(driverFragment);
        //mList.add(iceFragment);
    }

    public void switchPage(AnimatedFragment.Page page) {
        AnimatedFragment newFragment;
        int position = 1;

        if (page == AnimatedFragment.Page.PASSENGERS) newFragment = new PassengerFragment();
        else if (page == AnimatedFragment.Page.DRIVER) newFragment = new DriverFragment();
        else if (page == AnimatedFragment.Page.ICE) newFragment = new IceFragment();
        else {
            newFragment = new MenuFragment();
            position = 0;
        }

        EventBus eb = EventBus.getDefault();
        AnimatedFragment oldFragment = null;

        if (mList.size() > 1) {
            oldFragment = mList.get(position);
            if (eb.isRegistered(oldFragment)) eb.unregister(oldFragment);
            mList.set(position, newFragment);
            for (AnimatedFragment fragment:mList) {
                fragment.touchEnabled = false;
            }
        } else if (page != AnimatedFragment.Page.MENU) {
            mList.add(position, newFragment);
            for (AnimatedFragment fragment:mList) {
                fragment.touchEnabled = false;
            }
        }

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