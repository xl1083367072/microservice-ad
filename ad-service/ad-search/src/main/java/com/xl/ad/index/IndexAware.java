package com.xl.ad.index;

//索引服务接口
public interface IndexAware<K,V> {

    void add(K key,V value);

    void delete(K key,V value);

    void update(K key,V value);

    V get(K key);

}
