package com.patrykkrawczyk.liveo.managers.location;

import android.location.Location;

/**
 * Created by Patryk Krawczyk on 18.05.2016.
 */
public class LocationEvent {
    public Location location;
    public LocationEvent(Location location) { this.location = location; }
}
