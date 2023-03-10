package com.datastat.util;

import java.util.*;

public class ArrayListUtil {
    public static ArrayList<HashMap<String, Object>> sortByType(ArrayList<HashMap<String, Object>> arrayList, String type, String order) {
        if (order.equalsIgnoreCase("asc")) {
            Collections.sort(arrayList, new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> t1, HashMap<String, Object> t2) {
                    return t1.get(type).toString().toLowerCase()
                            .compareTo(t2.get(type).toString().toLowerCase());
                }
            });
        } else {
            Collections.sort(arrayList, new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> t1, HashMap<String, Object> t2) {
                    return t2.get(type).toString().toLowerCase()
                            .compareTo(t1.get(type).toString().toLowerCase());
                }
            });
        }
        return arrayList;
    }
}
