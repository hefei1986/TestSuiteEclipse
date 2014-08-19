package com.alibaba.test.TestSuiteEclipse;



import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by hefei.hfei on 2014/8/19.
 */
public class GuavaCacheSingletonTest extends TestCase{

    public GuavaCacheSingletonTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(CacheSingletonTest.class);
    }

    public void testCase1 () {
        ICache a = GuavaCacheSingleton.getInstance(1L);
        a.set("key1", "value");
        a.set("key2", "value");
        a.set("key3", "value");

        System.out.println(a.get("key1"));
        a.free("key1");
        assertTrue(!a.get("key1").equals("value"));
        assertTrue(a.get("key2").equals("value"));
        a.clear();
        assertTrue(!a.get("key2").equals("value"));
        assertTrue(!a.get("key3").equals("value"));

        a.set("key1", "value");
        assertTrue(a.get("key1").equals("value"));
    }
}
