package com.patrykkrawczyk.liveo.managers;

import android.net.Uri;

/**
 * Created by Patryk Krawczyk on 30.05.2016.
 */
public class IceContact {
    public String id;
    public String name;
    public String number;
    public String photoUri;

    public IceContact() {
        this(null, null, null, null);
    }

    public IceContact(String id, String name, String number) {
        this(id, name, number, null);
    }

    public IceContact(String id, String name, String number, String photoUri) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.photoUri = photoUri;
    }

    public boolean validate() {
        boolean result = true;
        if (id.isEmpty() || name.isEmpty() || number.isEmpty()) result = false;
        return result;
    }
}
