package com.patrykkrawczyk.liveo.activities;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ToxicBakery.viewpager.transforms.StackTransformer;
import com.orhanobut.logger.Logger;
import com.patrykkrawczyk.liveo.events.BackKeyEvent;
import com.patrykkrawczyk.liveo.MenuPagerAdapter;
import com.patrykkrawczyk.liveo.MyViewPager;
import com.patrykkrawczyk.liveo.R;
import com.patrykkrawczyk.liveo.events.ScrollStoppedEvent;
import com.patrykkrawczyk.liveo.events.ShowGuideEvent;
import com.patrykkrawczyk.liveo.events.SwitchPageEvent;
import com.patrykkrawczyk.liveo.fragments.AnimatedFragment;
import com.patrykkrawczyk.liveo.fragments.AnimatedFragment.Page;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.mainViewPager)
    MyViewPager mainViewPager;

    private MenuPagerAdapter mPagerAdapter;
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
        eventBus.post(new BackKeyEvent());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 0) {
            eventBus.post(new ScrollStoppedEvent());
            if (mainViewPager.getCurrentItem() == 0) {
                eventBus.post(new ShowGuideEvent());
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
