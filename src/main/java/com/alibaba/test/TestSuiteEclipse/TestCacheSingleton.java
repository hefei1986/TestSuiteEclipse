package com.alibaba.test.TestSuiteEclipse;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TestCacheSingleton implements ITest {

    long baseCycles = 100;
	long cycles = 10000;

	class MyReadThread implements Runnable {
		private ICache<String, Object> cacheService;
		private int id;
		private int cycles;
		
		public MyReadThread(ICache<String, Object> service, int id, int cycles) {
			this.cacheService = service;
			this.id = id;
			this.cycles = cycles;
		}
		public void run() {
			long start = System.currentTimeMillis();
			int i = 0;
            int j;
            for(j = 0; j < baseCycles ; j++) {
                for(i = 0; i < this.cycles; i++) {
                    String ret = (String) cacheService.get("Key_" + (i + 1000000));
                    if (ret == null) {
                        //TODO
                        continue;
                    }
                    if (ret.isEmpty()) {
                        //TODO
                        continue;
                    }
                }
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("thread #" + this.id + ", " +"read " + i+ ":" + j + " costs " + duration + "ms," + " average " + (float)i/duration);
		}
	}
	
	class MyWriteThread implements Runnable {
		
		private ICache<String, Object> cacheService;
        private MockDAO mockDAO = new MockDAO();
		private int id;
        private int baseCycles;
		private int cycles;

		
		public MyWriteThread(ICache<String, Object> service, int id, int cycles, String value) {
			this.cacheService = service;
			this.id = id;
			this.cycles = cycles;
		}
		
		public void run() {
			long start = System.currentTimeMillis();
			int i = 0;
            int j;
            for(j = 0; j < baseCycles ; j++) {
                for (i = 0; i < this.cycles; i++) {
                    Object ret = cacheService.set("Key_" + (i + 1000000), mockDAO.getData(i));
                    if (ret == null) {
                        System.out.println("thread #" + this.id + ", " + "write failed at " + i);
                        break;
                    }
                }
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("thread #" + this.id + ", " +"write " + i  + ":" + j + " costs " + duration + "ms," + " average " + (float)i/duration);
		}
	}

	public void runWrite(ICache<String, Object> cacheService, String value, int cycles) {
		int i;
		long start = System.currentTimeMillis();
		value = value + getRandomStr(100);
        for(i = 0; i < cycles; i++) {
        	Object ret = cacheService.set("Key_" + (i + 1000000), value);
        	if(ret == null) {
        		System.out.println("failed at " + i);
        		break;
        	}
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("writing " + cycles + " costs " + duration + "ms," + " average " + (float)i/duration);
	
	}
	
	public String getRandomStr(int n) {
		
		Random random = new Random();
		int len = random.nextInt(n);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i<len; i++) {
			sb.append("s");
		}
		return sb.toString();
	}
	
	 public void run() throws InterruptedException {
        int nwt = 2; //number of write threads
        int nrt = 4; //number of read threads
		int cycles = 1024 * 2; // 2048
		long cache_size = 1024 * 1024 * 1024 * 3L;
 		StringBuffer sb = new StringBuffer();
        for(int i = 0; i<1024; i++) {
        	sb.append("+");
        }	
        String strTest = sb.toString();
        List<Thread> listT = new LinkedList<Thread>();
        //CacheSingleton cacheService = CacheSingleton.getInstance(cache_size);
        GuavaCacheSingleton cacheService = GuavaCacheSingleton.getInstance(cache_size);

		System.out.println("initing cacheService...");
        runWrite(cacheService, strTest, cycles);
        System.out.println("finished initing cacheService...");
        
        String abc = (String)cacheService.get("Key_" + (1000000 + cycles - 1));
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

        for(Thread tttt:listT) {
        	tttt.join();
        }
        
        System.out.println("ALL DONE");
	} 
}
