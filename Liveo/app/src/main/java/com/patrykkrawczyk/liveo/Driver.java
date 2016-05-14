
package com.patrykkrawczyk.liveo;

import android.content.Context;
import android.content.SharedPreferences;

public class Driver {

    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String ageGroup;
    private String registerNumber;

    public Driver() {
    }

    public Driver(String id, String firstName, String lastName, String gender, String ageGroup, String registerNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.registerNumber = registerNumber;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return The ageGroup
     */
    public String getAgeGroup() {
        return ageGroup;
    }

    /**
     * @param ageGroup The ageGroup
     */
    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    /**
     * @return The registerNumber
     */
    public String getRegisterNumber() {
        return registerNumber;
    }

    /**
     * @param registerNumber The registerNumber
     */
    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public static Driver getLocalDriver(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

        String cId = preferences.getString(context.getString(R.string.LIVEO_DRIVER_ID), "");
        String cFirstName = preferences.getString(context.getString(R.string.LIVEO_DRIVER_FIRSTNAME), "");
        String cLastName = preferences.getString(context.getString(R.string.LIVEO_DRIVER_LASTNAME), "");
        String cGender = preferences.getString(context.getString(R.string.LIVEO_DRIVER_GENDER), "");
        String cAgeGroup = preferences.getString(context.getString(R.string.LIVEO_DRIVER_AGEGROUP), "");
        String cRegistration = preferences.getString(context.getString(R.string.LIVEO_DRIVER_REGISTRATION), "");

        Driver driver = new Driver(cId, cFirstName, cLastName, cGender, cAgeGroup, cRegistration);

        if (validateDriver(driver)) return driver;
        else return null;
    }

    public static boolean setCurrentDriver(Context context, Driver driver) {
        if (validateDriver(driver)) {
            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(context.getString(R.string.LIVEO_DRIVER_ID), driver.getId());
            editor.putString(context.getString(R.string.LIVEO_DRIVER_FIRSTNAME), driver.getFirstName());
            editor.putString(context.getString(R.string.LIVEO_DRIVER_LASTNAME), driver.getLastName());
            editor.putString(context.getString(R.string.LIVEO_DRIVER_GENDER), driver.getGender());
            editor.putString(context.getString(R.string.LIVEO_DRIVER_AGEGROUP), driver.getAgeGroup());
            editor.putString(context.getString(R.string.LIVEO_DRIVER_REGISTRATION), driver.getRegisterNumber());
            editor.apply();

            return true;
        } else return false;
    }

    public boolean validateDriver() {
        return validateDriver(this);
    }
    public static boolean validateDriver(Driver driver) {
        boolean okInformation = true;

        if (driver.getId().isEmpty() || driver.getFirstName().isEmpty() || driver.getLastName().isEmpty() ||
            driver.getGender().isEmpty() || driver.getAgeGroup().isEmpty() || driver.getRegisterNumber().isEmpty()) okInformation = false;

        return okInformation;
    }

}