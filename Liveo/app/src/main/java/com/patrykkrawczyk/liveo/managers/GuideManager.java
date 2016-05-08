package com.patrykkrawczyk.liveo.managers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.patrykkrawczyk.liveo.R;

/**
 * Created by Patryk Krawczyk on 20.04.2016.
 */
public class GuideManager implements OnShowcaseEventListener {

    private int           tutorialStage = 0;
    private boolean       showGuide     = true;
    private ShowcaseView        guide;

    private static GuideManager instance = new GuideManager();

    public static int getStage() {
        return instance.tutorialStage;
    }

    public static boolean getShowGuide() {
        return instance.showGuide;
    }

    public static void loadGuideState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        int     stage = preferences.getInt(context.getString(R.string.LIVEO_GUIDE_STAGE), 0);
        boolean show  = preferences.getBoolean(context.getString(R.string.LIVEO_GUIDE_SHOW), true);

        instance.tutorialStage = stage;
        instance.showGuide     = show;
    }

    public static ShowcaseView showGuide(Activity activity, View view) {
        final String[] descriptions = activity.getResources().getStringArray(R.array.guide_description);


        TextPaint titlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setTextSize(activity.getResources().getDimension(R.dimen.abc_text_size_display_1_material));
        titlePaint.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/RobotoCondensed-Regular.ttf"));

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(activity.getResources().getDimension(R.dimen.abc_text_size_headline_material));
        textPaint.setTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/RobotoCondensed-Light.ttf"));


        instance.guide = new ShowcaseView.Builder(activity)
                                .setTarget(new ViewTarget(view))
                                .setContentTitle("Getting started")
                                .setContentText(descriptions[instance.tutorialStage])
                                .withNewStyleShowcase()
                                .setContentTextPaint(textPaint)
                                .setContentTitlePaint(titlePaint)
                                .setShowcaseEventListener(instance)
                                .setStyle(R.style.ShowcaseTheme)
                                .build();

        instance.guide.hideButton();
        instance.guide.setDetailTextAlignment(Layout.Alignment.ALIGN_CENTER);
        instance.guide.setTitleTextAlignment(Layout.Alignment.ALIGN_CENTER);
        instance.guide.forceTextPosition(ShowcaseView.BELOW_SHOWCASE);

        if (instance.tutorialStage == 2) instance.guide.forceTextPosition(ShowcaseView.ABOVE_SHOWCASE);

        instance.saveState(activity);

        return instance.guide;
    }

    private void saveState(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(activity.getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(activity.getString(R.string.LIVEO_GUIDE_STAGE), instance.tutorialStage);
        editor.putBoolean(activity.getString(R.string.LIVEO_GUIDE_SHOW), instance.showGuide);

        editor.apply();
    }

    public static void hideGuide() {
        if (instance.guide != null) instance.guide.hide();
    }


    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
        ViewGroup parent = (ViewGroup) showcaseView.getParent();
        parent.removeView(instance.guide);
        instance.guide = null;
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) { }
    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) { }
    @Override
    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) { }

    public static void finishGuide(Activity activity) {
        instance.tutorialStage = 4;
        instance.showGuide = false;
        instance.saveState(activity);
    }

    public static void resetGuide(Activity activity) {
        instance.tutorialStage = 0;
        instance.showGuide = true;
        instance.saveState(activity);
    }

    public static void incrementStage() {
        instance.tutorialStage++;
    }
}
