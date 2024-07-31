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

    public static <T> ArrayList<T> castList(Object obj, Class<T> clazz) {
        ArrayList<T> result = new ArrayList<T>();
        if (obj instanceof ArrayList<?>) {
            for (Object o : (ArrayList<?>) obj) {
                result.add(clazz.cast(o));
            }
        }
        return result;
    }

    public static String getFilterList(ArrayList<String> queryList) {
        String result = "(";
        for (String query : queryList) {
            result = result + "\\\"" + query + "\\\",";
        }
        result = result + ")";
        return result;
    }
}
