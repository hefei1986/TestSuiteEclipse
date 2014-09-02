package com.alibaba.test.TestSuiteEclipse;

import java.util.Random;

/**
 * Created by hefei.hfei on 2014/8/20.
 */
public class RandomIDDAO {
    public static int getRandomId(int range) {
        Random random = new Random();
        return random.nextInt(range);
    }
    public static int getTenNinetyRandomId(int range) {
        Random random = new Random();
        if (random.nextInt(100) < 90) {
            return random.nextInt(range/10);
        } else {
            return random.nextInt(range);
        }
    }
}
