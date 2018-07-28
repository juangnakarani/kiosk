package com.juangnakarani.kiosk.model;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private BigDecimal price; //unit price
    private int ordered;
    private Category category;

    public Product(int id, String name, BigDecimal price, int ordered, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ordered = ordered;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getOrdered() {
        return ordered;
    }

    public void setOrdered(int ordered) {
        this.ordered = ordered;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
