package com.darshanbshah.odsystem;

/**
 * Created by iamDarshan on 22/04/17.
 */

public class DataProvider {
    private String detector;

    public DataProvider(String detector) {
        this.setDetector(detector);
    }

    public String getDetector() {
        return detector;
    }

    public void setDetector(String detector) {
        this.detector = detector;
    }
}