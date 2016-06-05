package com.patrykkrawczyk.liveo;

/**
 * Created by Patryk Krawczyk on 04.06.2016.
 */
public class ChangeHubFragmentEvent {
    public HubFragmentsEnum page;

    public ChangeHubFragmentEvent(HubFragmentsEnum page) { this.page = page; }
}
