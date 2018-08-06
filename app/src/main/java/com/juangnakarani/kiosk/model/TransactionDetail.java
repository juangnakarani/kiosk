package com.juangnakarani.kiosk.model;

public class TransactionDetail {
    private int id;
    private int transactionID;
    private int productID;
    private int categoryID;
    private int price;
    private int ordered;

    public TransactionDetail() {
    }

    public TransactionDetail(int id, int transactionID, int productID, int categoryID, int price, int ordered) {
        this.id = id;
        this.transactionID = transactionID;
        this.productID = productID;
        this.categoryID = categoryID;
        this.price = price;
        this.ordered = ordered;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }
}
