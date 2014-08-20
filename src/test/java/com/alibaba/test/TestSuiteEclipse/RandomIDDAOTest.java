package com.alibaba.test.TestSuiteEclipse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by hefei.hfei on 2014/8/20.
 */
public class RandomIDDAOTest extends TestCase{

    public RandomIDDAOTest(String name) {
        super(name);
    }

    public void testCase1()  {
        int a = RandomIDDAO.getRandomId(1000);
        int b = RandomIDDAO.getRandomId(1000);
        assertTrue(a != b);
    }


    public static Test suite() {
        return new TestSuite(RandomIDDAOTest.class);
    }

}
