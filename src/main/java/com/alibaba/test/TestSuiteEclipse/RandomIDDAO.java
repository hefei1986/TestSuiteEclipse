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
}
