package com.patrykkrawczyk.liveo.events;

import com.patrykkrawczyk.liveo.fragments.AnimatedFragment.Page;

/**
 * Created by Patryk Krawczyk on 12.04.2016.
 */
public class SwitchPageEvent {
    public Page page;
    public SwitchPageEvent(Page page) {
        this.page = page;
    }
}
