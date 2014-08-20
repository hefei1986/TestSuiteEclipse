package com.alibaba.test.TestSuiteEclipse;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hefei.hfei on 2014/8/20.
 */
class ClearThread implements Runnable {
		private ICache<String, Object> cacheService;
        private int range = 0;
        private int cycles = 0;
        private int id;
        private int missCount = 0;
        private MockDAO mockDAO ;
        private boolean needStop = false;


        public synchronized void setStop() {
            needStop = true;
        }

		public ClearThread(ICache<String, Object> service, int id, int range, int cycles) {
			this.cacheService = service;
            this.id = id;
			this.range = range;
			this.cycles = cycles;
            this.mockDAO = new MockDAO();
            this.needStop = false;
		}
		public void run() {
			long start = System.currentTimeMillis();
            long i = 0;
            while (!Thread.interrupted() ) {
                i ++;
                int tIndex = RandomIDDAO.getRandomId(range);
                String tKey = "Key_" + tIndex;
                cacheService.free(tKey);
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("thread #" + this.id + ", " +"random clear " + i + " costs " + duration + "ms," + " average " + (float)i/(float)duration);
		}
}
