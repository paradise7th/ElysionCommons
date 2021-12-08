package cn.com.ragnarok.elysion.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class StringUtil {
	
	public static String join(Collection c,String spliter){
		StringBuffer buffer = new StringBuffer();
        for (Iterator it = c.iterator(); it.hasNext();) {
			Object obj = (Object) it.next();
			buffer.append(obj);
			if(it.hasNext()){
				buffer.append(spliter);
			}
			
		}
        return buffer.toString();
	}
	
	public static List<String> split(String s,String regex){
		return new ArrayList(Arrays.asList(s.split(regex)));
	}
	
	public static int parseInt(Object o,int def){
		
		try {
			def=Integer.parseInt(o.toString());
		} catch (Exception e) {
		}
		return def;
	}
	
	public static long parseLong(Object o,long def){
		
		try {
			def=Long.parseLong(o.toString());
		} catch (Exception e) {
		}
		return def;
	}
	
   public static double parseDouble(Object o,double def){
		try {
			def=Double.parseDouble(o.toString());
		} catch (Exception e) {
		}
		return def;
	}

}
