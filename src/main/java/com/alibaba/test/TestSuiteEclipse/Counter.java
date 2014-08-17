package com.alibaba.test.TestSuiteEclipse;

public class Counter {
	private int count = 0;
	
	public void incr() {
		synchronized(this) {
			this.count ++;
		}
	}
	
	public int getCount() {
		return this.count;
	}

}
