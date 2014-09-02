package com.alibaba.test.TestSuiteEclipse;

import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.common.tair.impl.DefaultTairManager;
import com.taobao.tair.extend.impl.DefaultExtendTairManager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ExtendedLocalTairCacheSingleton implements ICache<String, Object>{


    private DefaultExtendTairManager ctrTairManager;

	private static ExtendedLocalTairCacheSingleton theInstance = null;

	private ExtendedLocalTairCacheSingleton(DefaultExtendTairManager cacheService) {
        this.ctrTairManager = cacheService;
	}

	public Object get(String key) {
		com.taobao.tair.Result<com.taobao.tair.DataEntry> ret;
		try{
            ret = this.ctrTairManager.get(1, key);
            if(ret == null) {
                return null;
            }
            if(null == ret.getValue()) {
                return null;
            }
            com.taobao.tair.DataEntry dataEntry = ret.getValue();
            return dataEntry.getValue();
		} catch(Exception e) {
            e.printStackTrace();
        }
        return null;
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
        com.taobao.tair.ResultCode rc = new ResultCode(2);
        try {
            rc = this.ctrTairManager.put(1, key, obj, 0, 60);
            //rc = this.ctrTairManager.putAsync(1, key, obj);

            System.out.println(rc.toString());
        } catch(Exception e){
            e.printStackTrace();
            System.out.print(rc.toString());
            return false;
        } finally {
            if(rc != null) {
                if (!rc.equals(ResultCode.SUCCESS)) {
                    System.out.print("no exception: " + rc.toString());
                }

                return rc.equals(ResultCode.SUCCESS) ? true : false;
            } else {
                System.out.print("set rc == null");
                return false;
            }
        }
    }

    public void getMultiKeys(int range) {
        List<String>  listKey = new LinkedList<String>();
        long start = System.currentTimeMillis();
        for(int i = 0; i < range; i++) {
            listKey.add("Key_" + i);
        }
        Result<List<DataEntry>> result = this.ctrTairManager.mget(1, listKey);

        List<DataEntry> listDataEntry= result.getValue();

        long size = 0;
        for(DataEntry entry: listDataEntry) {
            String t = (String)entry.getValue();
            size += t.length();
        }
        System.out.println(size);
        System.out.println(System.currentTimeMillis() - start);
    }

    public String getStatics() {
        return "not statics for DirectMemory";
    }

	public synchronized static ExtendedLocalTairCacheSingleton getInstance(long size) {
        if(theInstance == null) {
            //DefaultExtendTairManager cacheService = new DefaultExtendTairManager();

            List<String> configServerList = new LinkedList<String>();
            configServerList.add("10.101.84.140:5198");
            DefaultExtendTairManager defaultTairManager = new DefaultExtendTairManager();

            boolean localMode  = false;

            if(localMode) {
                defaultTairManager.setDataServer("10.101.84.101:5191");
                defaultTairManager.setTimeout(500);
            } else {
                defaultTairManager.setConfigServerList(configServerList);
                defaultTairManager.setGroupName("group_ctr");
                defaultTairManager.setTimeout(5000);
            }

            System.out.println("before");
            defaultTairManager.init();
            System.out.println("after");
            theInstance = new ExtendedLocalTairCacheSingleton(defaultTairManager);
        }
        return theInstance;
	}
}
