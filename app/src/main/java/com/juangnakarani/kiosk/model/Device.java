package com.juangnakarani.kiosk.model;

public class Device {
    private String address;
    private String name;
    private  boolean selected;

    public Device(String address, String name, boolean selected) {
        this.address = address;
        this.name = name;
        this.selected = selected;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
