package com.petclinic.billing.util;

import lombok.Generated;
import org.apache.commons.lang3.RandomStringUtils;

public class ShortIdGen {

    @Generated
    public ShortIdGen(){}
    private static final int LENGTH_ID = 5;

    public static Integer getShortId() {
        String shortIdString = RandomStringUtils.randomNumeric(LENGTH_ID);
        return Integer.valueOf(shortIdString);
    }
}
