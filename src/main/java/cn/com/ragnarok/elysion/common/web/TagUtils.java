package cn.com.ragnarok.elysion.common.web;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
/**
 * JSTL标签扩展类 需要配合TLD文件使用
 * @author Elysion
 *
 */
public class TagUtils {
	
	public static boolean contains(List list,Object obj) {
		if(list!=null){
			return list.contains(obj);
		}else{
			return false;
		}
	}
	
	public static String concat(Object a,Object b){
		if(a==null)a="";
		if(b==null)b="";
		return a.toString()+b.toString();
	}
	
	public static String concats(Object... objs){
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < objs.length; i++) {
			Object obj = objs[i];
			if(obj!=null){
				sb.append(obj);				
			}
		}
		return sb.toString();
	}
	
	public static String fnum(Object obj){
		if(obj==null)return "";
		if(obj instanceof Double || obj instanceof Float){
			return MessageFormat.format("{0,number,##0.###}", obj);
		}else{
			return obj.toString();
		}
	}
	
	public static String fnum0(Object obj){
		String r=fnum(obj);
		//System.out.println("r="+r);
		if("0".equals(r) || "0.0".equals(r)){
			return "";
		}else{
			return r;
		}
	}
	
	public static String fnum(Map hash,Object key){
		if(hash==null)return "";
		Object obj=hash.get(key);
		return fnum(obj);
	}
	
	public static String fnum0(Map hash,Object key){
		if(hash==null)return "";
		Object obj=hash.get(key);
		return fnum0(obj);
	}
	
	public static String encodeURI(Object uri){
		try {
			return URLEncoder.encode(uri.toString(),"utf-8");
		} catch (Exception e) {
			return "";
		}
	}

}
