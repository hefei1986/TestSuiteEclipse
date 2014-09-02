package com.alibaba.test.TestSuiteEclipse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by hefei.hfei on 2014/8/19.
 */
public class MockDAO {
    final static int baseLength = 200;
    final static int randLength = 1;
    static Map<Integer, String> theMap = new HashMap<Integer, String>();

    public String getData(int key) {
        int len = getLength(baseLength, randLength);
        String str;

        if(theMap.containsKey(len)) {
            return theMap.get(len);
        } else {
            str = genStringWithLength(len);
            theMap.put(len, str);
        }
        return str;
    }

    public static int getLength(int base, int random) {
        Random randomer = new Random();
        return base +  randomer.nextInt(random);
    }

    public static String genStringWithLength(int length) {
        StringBuffer sb = new StringBuffer();
        for(int i =0; i<length; i++) {
            sb.append("X");
        }
        return sb.toString();
    }
}
