package com.patrykkrawczyk.liveo.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.SwitchPageEvent;
import com.patrykkrawczyk.liveo.fragments.AnimatedFragment.Page;
import com.patrykkrawczyk.liveo.fragments.MenuFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.mainViewPager)
    MyViewPager mainViewPager;

    static private MenuPagerAdapter mPagerAdapter;
    private EventBus eventBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Logger.init(getString(R.string.APP_TAG));
        eventBus = EventBus.getDefault();


        mPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager());
        mainViewPager.setAdapter(mPagerAdapter);
        mainViewPager.addOnPageChangeListener(this);

        //mainViewPager.setPageTransformer(true, new ZoomOutTranformer());
        mainViewPager.setPageTransformer(true, new StackTransformer());
        //mainViewPager.setPageTransformer(true, new CubeOutTransformer());


    }

    @Subscribe
    public void onEvent(SwitchPageEvent event) {
        mPagerAdapter.switchPage(event.page);
        if (event.page != Page.MENU) {
            mainViewPager.setCurrentItem(1);
        } else {
            mainViewPager.setCurrentItem(0);
        }
    };


    @Override
    protected void onResume() {
        eventBus.register(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (mainViewPager.getCurrentItem() > 0) {
            eventBus.post(new SwitchPageEvent(Page.MENU));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mainViewPager.getCurrentItem() == 0 && state == 0) {
            eventBus.post(new ScrollStoppedEvent());
        }
    }
}
