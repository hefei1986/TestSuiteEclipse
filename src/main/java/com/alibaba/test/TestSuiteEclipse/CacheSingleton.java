package com.alibaba.test.TestSuiteEclipse;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;

public class CacheSingleton implements ICache<String, Object>{
	private static CacheSingleton theInstance = null;
	private static CacheService<String, Object> cacheService = null;
	final private static int steps = 32;
	private ReadWriteLock rwLock;
	
	private CacheSingleton(CacheService cacheService) {
		this.cacheService = cacheService;
		rwLock = new ReentrantReadWriteLock(true);
	}
	
	public Object get(String key) {
		Object ret;
		rwLock.readLock().lock();
		try{
			ret = this.cacheService.retrieve(key);
		} catch(Exception e) {
			return null;
		} finally {
			rwLock.readLock().unlock();
		}
		return ret;
	}

    public void free(String key) {
        rwLock.writeLock().lock();
        try{
            this.cacheService.free(key);
        } catch(Exception e) {
        }finally {
            rwLock.writeLock().unlock();
        }
    }

    public void clear() {
        rwLock.writeLock().lock();
        try {
            this.cacheService.clear();
        } catch(Exception e) {
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public boolean set(String key, Object obj) {
		Object ret;
		rwLock.writeLock().lock();
		try{
			ret = this.cacheService.put(key, obj);
		} catch(Exception e) {
			ret = null;
		} finally {
			rwLock.writeLock().unlock();
		}
		return ret == null ? false : true;
	}
	
	public synchronized static CacheSingleton getInstance(long size) {
		CacheService cacheService  = new DirectMemory<String, Object> ()
				.setNumberOfBuffers(steps)
				.setSize((int)(size/steps))
				.setInitialCapacity(20000000)
				.setConcurrencyLevel(4)
				.newCacheService();	
		return new CacheSingleton(cacheService);
	}
}
