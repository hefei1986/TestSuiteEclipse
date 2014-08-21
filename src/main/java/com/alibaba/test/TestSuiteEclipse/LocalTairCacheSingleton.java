package com.alibaba.test.TestSuiteEclipse;

import java.io.Serializable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.taobao.tair.ResultCode;
import com.taobao.tair.extend.impl.DefaultExtendTairManager;
import org.apache.directmemory.DirectMemory;
import org.apache.directmemory.cache.CacheService;

public class LocalTairCacheSingleton implements ICache<String, Object>{


    private DefaultExtendTairManager ctrTairManager;

	private static LocalTairCacheSingleton theInstance = null;

	private LocalTairCacheSingleton(DefaultExtendTairManager cacheService) {
        this.ctrTairManager = cacheService;
	}

	public Object get(String key) {
		Object ret;
		try{
			ret = this.ctrTairManager.get(1, key);
		} catch(Exception e) {
			return null;
		} finally {
		}
		return ret;
	}

    public void free(String key) {
        try{
            this.ctrTairManager.delete(1, key);
        } catch(Exception e) {
        }finally {
        }
    }

    public void clear() {
        try {
        } catch(Exception e) {
        } finally {
        }
    }

    public boolean set(String key, Serializable obj) {
        return this.setTair(key, obj);
	}

    public boolean setTair(String key, Serializable obj) {
        ResultCode rc = new ResultCode(2);
        try {
            rc = this.ctrTairManager.put(1, key, obj);
        } catch(Exception e){
            return false;
        } finally {
            if(rc != null) {
                return rc.equals(ResultCode.SUCCESS) ? true : false;
            } else {
                return false;
            }
        }
    }

    public String getStatics() {
        return "not statics for DirectMemory";
    }

	public synchronized static LocalTairCacheSingleton getInstance(long size) {
        if(theInstance == null) {
            DefaultExtendTairManager cacheService = new DefaultExtendTairManager();
            cacheService.setDataServer("10.101.84.140:5191");
            cacheService.setTimeout(500);
            cacheService.init();

            theInstance = new LocalTairCacheSingleton(cacheService);
        }
        return theInstance;
	}
}
