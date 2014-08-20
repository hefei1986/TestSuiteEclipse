package com.alibaba.test.TestSuiteEclipse;

import java.util.LinkedList;
import java.util.List;
import sun.management.ManagementFactory;

public class TestCacheSingleton implements ITest {

    private MockDAO mockDAO;
    private int range;
    private int cycles;
    private int nwt;
    private int nrt;
    private long cacheSize;

    public TestCacheSingleton() {
        mockDAO = new MockDAO();
        range = 1024 * 128;
        cycles =1024 * 1024 * 1600;
        nwt = 1;
        nrt = 5;
        cacheSize = 1024L * 1024L * 1024L * 2L;
    }

	public void runWrite(ICache<String, Object> cacheService, int range) {
		int i;
		long start = System.currentTimeMillis();
        for(i = 0; i < range; i++) {
            cacheService.set("Key_" + i, this.getRandomStr());
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("writing " + range + " costs " + duration + "ms," + " average " + (float)i/duration);
	}
	
	public String getRandomStr() {
        return mockDAO.getData(RandomIDDAO.getRandomId(this.range));
	}
	
	 public void run() throws InterruptedException {
         System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        List<Thread> listReadT = new LinkedList<Thread>();
         List<Thread> listClearT = new LinkedList<Thread>();
        //CacheSingleton cacheService = CacheSingleton.getInstance(1024L * 1024L * 1024L *2);
        GuavaCacheSingleton cacheService = GuavaCacheSingleton.getInstance(this.cacheSize);

		System.out.println("initing cacheService...");
        runWrite(cacheService, this.range);
        System.out.println("finished initing cacheService...");

        for(int i = 0; i< nwt; i++) {
        	ClearThread t = new ClearThread(cacheService, i, this.range, this.cycles );
        	Thread tt = new Thread(t);
        	listClearT.add(tt);
        	tt.start();
        }
        for(int i = 0; i< nrt; i++) {
            ReadThread t = new ReadThread(cacheService, i, this.range, this.cycles );
        	Thread tt = new Thread(t);
        	listReadT.add(tt);
        	tt.start();
        }

        for(Thread tttt:listReadT) {
        	tttt.join();
        }

         for(Thread tttt:listClearT) {
             tttt.interrupt();
             tttt.join();
         }

         System.out.println(cacheService.getStatics());
         System.out.println("ALL DONE");
	} 
}
