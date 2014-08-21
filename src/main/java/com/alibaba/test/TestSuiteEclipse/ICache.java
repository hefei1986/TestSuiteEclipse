package com.alibaba.test.TestSuiteEclipse;

import java.io.Serializable;

/**
 * Created by hefei.hfei on 2014/8/19.
 */
public interface ICache<K,V> {
    public V get(K key);
    public void free(K key);
    public void clear();
    public boolean set(K key, Serializable obj);
    public boolean setTair(K key, Serializable obj);
    public String getStatics();
}
