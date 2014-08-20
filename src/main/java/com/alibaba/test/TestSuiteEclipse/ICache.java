package com.alibaba.test.TestSuiteEclipse;

/**
 * Created by hefei.hfei on 2014/8/19.
 */
public interface ICache<K,V> {
    public V get(K key);
    public void free(K key);
    public void clear();
    public boolean set(K key, V obj);
    public String getStatics();
}
