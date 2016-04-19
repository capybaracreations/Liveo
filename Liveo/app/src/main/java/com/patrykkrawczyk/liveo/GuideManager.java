package com.patrykkrawczyk.liveo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by Patryk Krawczyk on 20.04.2016.
 */
public class GuideManager {

    public static int       tutorialStage = 0;
    public static boolean   showGuide     = true;

    public static void loadGuideState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        int     stage = preferences.getInt(context.getString(R.string.LIVEO_GUIDE_STAGE), 0);
        boolean shown = preferences.getBoolean(context.getString(R.string.LIVEO_GUIDE_SHOWN), false);

        tutorialStage = stage;
        showGuide     = !shown;
    }

    public static ShowcaseView setGuideOnView(Activity activity, View view, String contentTitle, String contentText) {
        ShowcaseView showcaseView = new ShowcaseView.Builder(activity)
                                    .setTarget(new ViewTarget(view))
                                    .setContentTitle(contentTitle)
                                    .setContentText(contentText)
                                    .setStyle(R.style.ShowcaseTheme)
                                    .build();

        return showcaseView;
    }

}
