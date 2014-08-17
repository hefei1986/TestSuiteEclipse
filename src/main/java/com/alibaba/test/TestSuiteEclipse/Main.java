package com.alibaba.test.TestSuiteEclipse;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		run();
	}

	private static ITest getTestImpl() {
		return new TestCacheSingleton();
		//return new TestDirectMemory();
	}
	
	private static void run() throws InterruptedException {
	
		long start = 0;
		long duration = 0;
		
		ITest testImpl= getTestImpl();
		start = System.currentTimeMillis();
		testImpl.run();
		duration = System.currentTimeMillis() - start;
		System.out.println(testImpl.getClass() + ",duration: " + duration);
	}

}
