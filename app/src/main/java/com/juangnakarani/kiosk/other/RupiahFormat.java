package com.juangnakarani.kiosk.other;

import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class RupiahFormat {
    public RupiahFormat(BigDecimal total) {
    }

    public static String formatRupiah(BigDecimal b){
        String pattern = "#,###";
        DecimalFormat df = new DecimalFormat(pattern);
        Log.d("chk format", df.format(b));
        return df.format(b);
    }
    public static String formatRupiah(int i){
        String pattern = "#,###";
        DecimalFormat df = new DecimalFormat(pattern);
        Log.d("chk format", df.format(i));
        return df.format(i);
    }
}
