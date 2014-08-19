package com.alibaba.test.TestSuiteEclipse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * Created by hefei.hfei on 2014/8/19.
 */
public class MockDAOTest extends TestCase {
        /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MockDAOTest( String testName )
    {
        super( testName );
    }

    public void testCase1() {
        MockDAO mockDAO = new MockDAO();
        String str = mockDAO.getData(123);
        System.out.println(str);
        assertNotNull(str);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

}
