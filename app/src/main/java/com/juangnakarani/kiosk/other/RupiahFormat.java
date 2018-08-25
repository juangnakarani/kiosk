package com.juangnakarani.kiosk.other;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class RupiahFormat {
    public RupiahFormat(BigDecimal total) {
    }

    public static String formatRupiah(Object o){
        String pattern = "#,###";
        DecimalFormat df = new DecimalFormat(pattern);
        
        return df.format(o);
    }
}
