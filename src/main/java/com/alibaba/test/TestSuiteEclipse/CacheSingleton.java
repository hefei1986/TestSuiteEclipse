package com.alibaba.test.TestSuiteEclipse;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;

public class CacheSingleton {
	private static CacheSingleton theInstance = null;
	private static CacheService<String, String> cacheService = null;
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
	
	public boolean set(String key, String obj) {
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
		CacheService cacheService  = new DirectMemory<String, String> ()
				.setNumberOfBuffers(steps)
				.setSize((int)(size/steps))
				.setInitialCapacity(100000)
				.setConcurrencyLevel(4)
				.newCacheService();	
		return new CacheSingleton(cacheService);
	}
	
	

}
