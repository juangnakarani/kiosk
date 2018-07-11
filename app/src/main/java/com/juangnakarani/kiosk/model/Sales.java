package com.juangnakarani.kiosk.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class Sales {
    private BigInteger ID;
    private BigDecimal Total;
    private Date date;

    public Sales(BigInteger ID, BigDecimal total, Date date) {
        this.ID = ID;
        Total = total;
        this.date = date;
    }

    public BigInteger getID() {
        return ID;
    }

    public void setID(BigInteger ID) {
        this.ID = ID;
    }

    public BigDecimal getTotal() {
        return Total;
    }

    public void setTotal(BigDecimal total) {
        Total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
