
package com.patrykkrawczyk.liveo;

import android.content.Context;
import android.content.SharedPreferences;

public class Driver {

    private static Driver currentDriver;

    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String ageGroup;
    private String registrationNumber;

    public Driver() {
    }

    public Driver(String id, String firstName, String lastName, String gender, String ageGroup, String registrationNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.registrationNumber = registrationNumber;
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
     * @return The registrationNumber
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * @param registrationNumber The registrationNumber
     */
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public static Driver getCurrentDriver(Context context) {
        if (currentDriver == null) {
            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);

            String cId = preferences.getString(context.getString(R.string.LIVEO_FIELD_ID), "");
            String cFirstName = preferences.getString(context.getString(R.string.LIVEO_FIELD_FIRSTNAME), "");
            String cLastName = preferences.getString(context.getString(R.string.LIVEO_FIELD_LASTNAME), "");
            String cGender = preferences.getString(context.getString(R.string.LIVEO_FIELD_GENDER), "");
            String cAgeGroup = preferences.getString(context.getString(R.string.LIVEO_FIELD_AGEGROUP), "");
            String cRegistration = preferences.getString(context.getString(R.string.LIVEO_FIELD_REGISTRATION), "");

            Driver current = new Driver(cId, cFirstName, cLastName, cGender, cAgeGroup, cRegistration);

            if (validateDriver(current)) currentDriver = current;
            else currentDriver = null;
            return currentDriver;
        } else return currentDriver;
    }


    public static void setCurrentDriver(Context context, Driver driver) {
        if (validateDriver(driver)) {
            currentDriver = driver;

            SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.LIVEO_INFORMATIONS), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            //editor.putString(getString(R.string.PASSENGERS_COUNT), count);
            //editor.apply();
        }
    }

    public boolean validateDriver() {
        return validateDriver(this);
    }

    public static boolean validateDriver(Driver driver) {
        boolean okInformation = true;

        if (driver.getId().isEmpty() || driver.getFirstName().isEmpty() || driver.getLastName().isEmpty() ||
            driver.getGender().isEmpty() || driver.getLastName().isEmpty() || driver.getRegistrationNumber().isEmpty()) okInformation = false;

        return okInformation;
    }

}