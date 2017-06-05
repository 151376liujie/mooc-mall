package com.mall.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Author: jonny
 * Time: 2017-05-28 17:31.
 */
public class BigDecimalUtil {

    private BigDecimalUtil(){}

    public static BigDecimal add(double a,double b){
        BigDecimal bigDecimal = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(b));
        return bigDecimal.add(bigDecimal2);
    }

    public static BigDecimal sub(double a,double b){
        BigDecimal bigDecimal = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(b));
        return bigDecimal.subtract(bigDecimal2);
    }

    public static BigDecimal multi(double a,double b){
        BigDecimal bigDecimal = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(b));
        return bigDecimal.multiply(bigDecimal2);
    }

    public static BigDecimal div(double a,double b){
        BigDecimal bigDecimal = new BigDecimal(Double.toString(a));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(b));
        return bigDecimal.divide(bigDecimal2,2, RoundingMode.HALF_UP);
    }

}
