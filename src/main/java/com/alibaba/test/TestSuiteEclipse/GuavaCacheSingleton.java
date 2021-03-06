package com.alibaba.test.TestSuiteEclipse;

/**
 * Created by hefei.hfei on 2014/8/19.
 */

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

public class GuavaCacheSingleton implements ICache<String, Object>{
    private static Cache<String, Object> cacheService = null;
	private static GuavaCacheSingleton theInstance = null;
	final private static int steps = 32;
	private ReadWriteLock rwLock;
    private MockDAO mockDAO;

	private GuavaCacheSingleton(Cache cacheService) {
		this.cacheService = cacheService;
		rwLock = new ReentrantReadWriteLock(true);
        mockDAO = new MockDAO();
	}

	public Object get(final String key) {
		Object ret;
		//rwLock.readLock().lock();
		try{
			ret = this.cacheService.get(key, new Callable<String>()
            {
                public String call() {
                    return mockDAO.getData(1);
                }
            });
		} catch(Exception e) {
            e.printStackTrace();
			return null;
		} finally {
			//rwLock.readLock().unlock();
		}
		return ret;
	}

    public void free(String key) {
        //rwLock.writeLock().lock();
        try{
            this.cacheService.invalidate(key);
        } catch(Exception e) {
            e.printStackTrace();
        }finally {
            //rwLock.writeLock().unlock();
        }
    }

    public void clear() {
        //rwLock.writeLock().lock();
        try {
            this.cacheService.invalidateAll();
        } catch(Exception e) {
        } finally {
            //rwLock.writeLock().unlock();
        }
    }
	public boolean set(String key, Serializable obj) {
        boolean ret = true;
		//rwLock.writeLock().lock();
		try{
            this.cacheService.put(key, obj);
		} catch(Exception e) {
			ret = false;
		} finally {
			//rwLock.writeLock().unlock();
		}
        return ret;
	}

    public boolean setTair(String key, Serializable obj)
    {
        return true;
    }

    public String getStatics() {
        return this.cacheService.stats().toString();
    }

	public synchronized static GuavaCacheSingleton getInstance(long size) {
        if (theInstance == null) {
            Cache<String, Object> cacheService = CacheBuilder.newBuilder()
                    .maximumSize(2L * 1024L * 1024L)
                    .build();
            theInstance = new GuavaCacheSingleton(cacheService);
        }
        return theInstance;
	}

    public static Object createNewObject(String key) {
        return new Object();
    }
}