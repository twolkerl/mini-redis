package com.twl.miniredis.service.comparator;

import java.util.Comparator;
import java.util.Map;

public class MapValueKeyComparator<K extends Comparable<? super K>, V extends Comparable<? super V>>
        implements Comparator<Map.Entry<K, V>> {

    @Override
    public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
        int comparison = o1.getValue().compareTo(o2.getValue());
        if (comparison != 0) {
            return comparison;
        } else {
            return o1.getKey().compareTo(o2.getKey());
        }
    }
}
