package com.transactioncurrency.infra.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StringUtil {

    public static BigDecimal parseBigDecimalWithScale(String value, int scale) {
        //To round currency values, it is recommended to use the RoundingMode.HALF_EVEN, aka 'Bankers rounding'
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN);
    }

}
