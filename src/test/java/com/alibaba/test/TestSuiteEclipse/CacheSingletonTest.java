package com.alibaba.test.TestSuiteEclipse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Created by hefei.hfei on 2014/8/19.
 */

public class CacheSingletonTest extends TestCase{

    public CacheSingletonTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(CacheSingletonTest.class);
    }

    public void testCase1 () {
        ICache a = CacheSingleton.getInstance(1024L*16L);
        a.set("key1", "value");
        a.set("key2", "value");
        a.set("key3", "value");

        a.free("key1");
        assertNull(a.get("key1"));
        assertTrue(a.get("key2").equals("value"));
        a.clear();
        assertNull(a.get("key2"));
        assertNull(a.get("key3"));

        a.set("key1", "value");
        assertTrue(a.get("key1").equals("value"));
        a.clear();
    }
}
