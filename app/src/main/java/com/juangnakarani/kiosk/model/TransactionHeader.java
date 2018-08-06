package com.juangnakarani.kiosk.model;

public class TransactionHeader {
    private int id;
    private String datetime;
    private int total;
    private int received;

    public TransactionHeader() {
    }

    public TransactionHeader(int id, String datetime, int total, int received) {
        this.id = id;
        this.datetime = datetime;
        this.total = total;
        this.received = received;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }
}


