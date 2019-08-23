package com.xl.ad.util;

import java.util.Map;
import java.util.function.Supplier;

public class CommonUtils {

    public static <K,V> V getOrCreate(K k, Map<K,V> map, Supplier<V> factory){
        return map.computeIfAbsent(k,obj -> factory.get());
    }

}
