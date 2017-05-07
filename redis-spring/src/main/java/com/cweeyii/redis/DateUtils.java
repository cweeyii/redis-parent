package com.cweeyii.redis;

import java.util.Random;

/**
 * Created by wenyi on 17/5/7.
 * Email:caowenyi@meituan.com
 */
public class DateUtils {

    public static Integer getRandomExpireDays(int max, int min) {
        Random rn = new Random();
        return rn.nextInt(max - min + 1) + 1;
    }
}
