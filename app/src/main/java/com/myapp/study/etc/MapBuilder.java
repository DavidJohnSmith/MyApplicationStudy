package com.myapp.study.etc;

import android.support.v4.util.ArrayMap;

import java.util.Collections;
import java.util.Map;

/**
 * 关于map的操作
 */
public class MapBuilder<K, V> {
    private Map<K, V> map;

    public MapBuilder() {
        map = new ArrayMap<>();
    }

    public MapBuilder<K, V> add(K key, V value) {
        map.put(key, value);
        return this;
    }

    public Map<K, V> getUnmodifiableMap() {
        return Collections.unmodifiableMap(map);
    }

    public Map<K, V> getMap() {
        return map;
    }

}
