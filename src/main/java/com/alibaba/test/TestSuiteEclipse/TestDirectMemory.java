package com.alibaba.test.TestSuiteEclipse;

import java.util.LinkedList;
import java.util.List;

import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;

public class TestDirectMemory implements ITest {

	long cycles = 10000;

	
	class MyReadThread implements Runnable {
		
		private CacheService<String, String> cacheService;
		private int id;
		private int cycles;
		
		public MyReadThread(CacheService<String, String> service, int id, int cycles) {
			this.cacheService = service;
			this.id = id;
			this.cycles = cycles;
		}
		public void run() {
			long start = System.currentTimeMillis();
			int i;
            for(i = 0; i < this.cycles; i++) {
            	try {
            		String obj = cacheService.retrieve("Key_" + i);
            		if(obj == null) {
            			System.out.println("thread #" + this.id + ", " + "read failed at " + i);
            		}
            		String ret = (String)obj;
                	if(ret.isEmpty()) {
                		System.out.println("thread #" + this.id + ", " + "read failed at " + i);
                		break;
                	}
            	} catch (Exception e) {
                	System.out.println("thread #" + this.id + ", " + "read failed at " + i);
            		e.printStackTrace();
            		break;
            	} 
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("thread #" + this.id + ", " +"read " + i + " costs " + duration + "ms," + " average " + (float)i/duration);
		}
	}
	
	class MyWriteThread implements Runnable {
		
		private CacheService<String, String> cacheService;
		private int id;
		private int cycles;
		private String value;
		
		public MyWriteThread(CacheService<String, String> service, int id, int cycles, String value) {
			this.cacheService = service;
			this.id = id;
			this.cycles = cycles;
			this.value = value;
		}
		
		public void run() {
			long start = System.currentTimeMillis();
			int i;
            for(i = 0; i < this.cycles; i++) {
            	Object ret = cacheService.put("Key_" + i, this.value);
                if(ret == null) {
                	System.out.println("thread #" + this.id + ", " +"write failed at " + i);
                    break;
                }
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("thread #" + this.id + ", " +"write " + i + " costs " + duration + "ms," + " average " + (float)this.cycles/duration);
		}
	}
	
	
	
	
	public void runWrite(CacheService cacheService, String value, int cycles) {
		long start = System.currentTimeMillis();
        for(int i = 0; i < cycles; i++) {
        	Object ret = cacheService.put("Key_" + i, value);
        	if(ret == null) {
        		System.out.println("failed at " + i);
        		break;
        	}
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("writing " + cycles + " costs " + duration + "ms," + " average " + (float)cycles/duration);
	
	}
	
	public void run() throws InterruptedException {
		 
        int nwt = 20; //number of write threads
        int nrt = 1; //number of read threads
		int cycles = 100000;
 		StringBuffer sb = new StringBuffer();
        for(int i = 0; i<1024; i++) {
        	sb.append("+");
        }	
        String strTest = sb.toString();
        List<Thread> listT = new LinkedList<Thread>();
        
		CacheService cacheService  = new DirectMemory<String, String> ()
				.setNumberOfBuffers(4)
				.setSize(1024*1024*320)
				.setInitialCapacity(100000)
				.setConcurrencyLevel(32)
				.newCacheService();
		
		System.out.println("initing cacheService...");
        runWrite(cacheService, strTest, cycles);
        System.out.println("finished initing cacheService...");
        
        Object obj = cacheService.retrieve("Key_" + (cycles - 1));
        if(obj == null) {
        	System.out.println("fucking lucky");
        }
        String abc = (String)obj;
        System.out.println(abc);
        
        System.out.println("multi reading and writing begin...");
       
        for(int i = 0; i< nwt; i++) {
        	MyWriteThread t = new MyWriteThread(cacheService, i, cycles, strTest);
        	Thread tt = new Thread(t);
        	listT.add(tt);
        	tt.start();
        }
        
        for(int i = 0; i< nrt; i++) {
        	MyReadThread t = new MyReadThread(cacheService, i, cycles);
        	Thread tt = new Thread(t);
        	listT.add(tt);
        	tt.start();
        }
    
        Thread.sleep(100000);
        /*
        for(Thread ttt:listT) {
        	ttt.wait();
        }
        */
        
        System.out.println("ALL DONE");
	} 
}
