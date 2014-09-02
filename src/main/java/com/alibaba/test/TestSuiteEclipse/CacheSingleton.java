package com.alibaba.test.TestSuiteEclipse;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;

public class CacheSingleton implements ICache<String, Object>{
	private static CacheSingleton theInstance = null;
	private static CacheService<String, Object> cacheService = null;
	final private static int steps = 32;

	private CacheSingleton(CacheService cacheService) {
		this.cacheService = cacheService;
	}
	
	public Object get(String key) {
		Object ret;
		try{
			ret = this.cacheService.retrieve(key);
		} catch(Exception e) {
			return null;
		} finally {
		}
		return ret;
	}

    public void free(String key) {
        try{
            this.cacheService.free(key);
        } catch(Exception e) {
        }finally {
        }
    }

    public void clear() {
        try {
            this.cacheService.clear();
        } catch(Exception e) {
        } finally {
        }
    }

    public boolean set(String key, Serializable obj) {
		Object ret;
		try{
			ret = this.cacheService.put(key, obj);
		} catch(Exception e) {
			ret = null;
		} finally {
		}
		return ret == null ? false : true;
	}

    public String getStatics() {
        return "not statics for DirectMemory";
    }

    public boolean setTair(String key, Serializable obj)
    {
        return true;
    }
	public synchronized static CacheSingleton getInstance(long size) {
        if(theInstance == null) {
            CacheService cacheService = new DirectMemory<String, Object>()
                    .setNumberOfBuffers(steps)
                    .setSize((int) (size / steps))
                    .setInitialCapacity(2000000)
                    .setConcurrencyLevel(4)
                    .newCacheService();
            theInstance = new CacheSingleton(cacheService);
        }
        return theInstance;
	}
}
