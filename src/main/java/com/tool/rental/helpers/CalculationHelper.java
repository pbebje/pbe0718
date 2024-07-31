package com.tool.rental.helpers;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CalculationHelper {

    public static<N extends Number> String formatDecimal(N num, int decimalPlaces) {
        DecimalFormat df = new DecimalFormat();
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMaximumFractionDigits(decimalPlaces);
        df.setMinimumFractionDigits(decimalPlaces);
        df.setMinimumIntegerDigits(0);
        return df.format(num);
    }

    public static<N extends Number> String formatCurrency(N num) {
        DecimalFormat df = new DecimalFormat();
        df.setRoundingMode(RoundingMode.HALF_UP);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(df.format(num));
    }

    public static<N extends Number> String formatPercentage(N num, int decimalPlaces) {
        DecimalFormat df = new DecimalFormat();
        df.setRoundingMode(RoundingMode.HALF_UP);
        NumberFormat formatter = NumberFormat.getPercentInstance();
        formatter.setMaximumFractionDigits(decimalPlaces);
        return formatter.format(df.format(num));
    }
}
