package cn.com.ragnarok.elysion.common.util;

import java.util.Map;

public class HashUtil {


    public static String getString(Map hash, Object key, String def) {
        if (hash != null) {
            Object value =  hash.get(key);
            if (value != null) {
                return value.toString();
            }
        }
        return def;
    }

    public static String getString(Map hash, Object key) {
        return getString(hash, key, null);
    }

    public static int getInt(Map hash, Object key, int def) {
        try {
            return Integer.parseInt(getString(hash, key));
        } catch (Exception e) {
            return def;
        }

    }

    public static long getLong(Map hash, Object key, long def) {
        try {
            return Long.parseLong(getString(hash, key));
        } catch (Exception e) {
            return def;
        }

    }

    public static double getDouble(Map hash, Object key, double def) {
        try {
            return Double.parseDouble(getString(hash, key));
        } catch (Exception e) {
            return def;
        }

    }
    
    

}
