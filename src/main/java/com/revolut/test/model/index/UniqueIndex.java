package com.revolut.test.model.index;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class UniqueIndex<K, V> {

    private ConcurrentNavigableMap<K, V> map = new ConcurrentSkipListMap<>();
    private Function<V, K> extractor;
    private ConcurrentMap<K, Lock> locks = new ConcurrentHashMap<>();

    public UniqueIndex(Function<V, K> extractor) {
        this.extractor = extractor;
    }

    public boolean exists(V value) {
        return map.containsKey(extractor.apply(value));
    }

    public void put(V value) {
        map.put(extractor.apply(value), value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public Collection<V> range(K start, K end) {
        return map.subMap(start, end).values();
    }

    public Collection<V> all() {
        return map.values();
    }

    private Lock getLock(K key) {
        ReentrantLock lock = new ReentrantLock();
        Lock lock_ = locks.putIfAbsent(key, lock);
        return lock_ == null ? lock : lock_;
    }

    public void lock(V value) {
        K key = extractor.apply(value);
        getLock(key).lock();
    }

    public void unlock(V value) {
        K key = extractor.apply(value);
        locks.get(key).unlock();
    }

}
