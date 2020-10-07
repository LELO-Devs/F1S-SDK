package com.lelo.sdk.f1s.permissions.model;


public class AllowedPermissions {

    //permissions M
    private boolean ACCESS_COARSE_LOCATION = false;

    //turned on
    private boolean BLUETOOTH_ON = false;
    private boolean LOCATION_ON = false;

    public AllowedPermissions() {
    }


    public boolean isACCESS_COARSE_LOCATION() {
        return ACCESS_COARSE_LOCATION;
    }

    public void setACCESS_COARSE_LOCATION(boolean ACCESS_COARSE_LOCATION) {
        this.ACCESS_COARSE_LOCATION = ACCESS_COARSE_LOCATION;
    }

    public boolean isBLUETOOTH_ON() {
        return BLUETOOTH_ON;
    }

    public void setBLUETOOTH_ON(boolean BLUETOOTH_ON) {
        this.BLUETOOTH_ON = BLUETOOTH_ON;
    }

    public boolean isLOCATION_ON() {
        return LOCATION_ON;
    }

    public void setLOCATION_ON(boolean LOCATION_ON) {
        this.LOCATION_ON = LOCATION_ON;
    }


    public boolean areAllPermissionsAllowed() {
        if (isLOCATION_ON() && isBLUETOOTH_ON() && isACCESS_COARSE_LOCATION()) {
            return true;
        } else {
            return false;
        }
    }


}

