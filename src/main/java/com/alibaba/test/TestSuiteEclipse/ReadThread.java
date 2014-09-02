package com.alibaba.test.TestSuiteEclipse;

/**
 * Created by hefei.hfei on 2014/8/20.
 */
class ReadThread implements Runnable {
		private ICache<String, Object> cacheService;
        private int range = 0;
        private int cycles = 0;
        private int id;
        private int missCount = 0;
        private MockDAO mockDAO ;

		public ReadThread(ICache<String, Object> service, int id, int range, int cycles) {
			this.cacheService = service;
            this.id = id;
			this.range = range;
			this.cycles = cycles;
            this.mockDAO = new MockDAO();
		}
		public void run() {
			long start = System.currentTimeMillis();
			int i;
            for(i = 0; i < cycles ; i++) {
                //int tIndex = RandomIDDAO.getRandomId(range);
                int tIndex = RandomIDDAO.getTenNinetyRandomId(range);
                String tKey = "Key_" + tIndex;
                String ret = (String) cacheService.get(tKey);
                if(ret == null) {
                    missCount ++;
                    cacheService.set(tKey, mockDAO.getData(tIndex));
                }
            }
            long duration = System.currentTimeMillis() - start;
            System.out.println("thread #" + this.id + ", " +"read " + this.cycles + " costs " + duration + "ms," + " average " + (float)i/(float)duration);
            System.out.println("thread #" + this.id + ", read miss rate:" + (float)this.missCount/(float)this.cycles );
		}
}
