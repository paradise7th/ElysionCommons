package cn.com.ragnarok.elysion.common.db;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 数据库对应实体类,保存实体类与数据库表对应关系,通过读取jpa注解信息来对public字段进行映射
 * 具体注解如下
 * Transient 忽略字段
 * Column 映射对应列
 * Id 映射主键 (用于update语句)
 * 
 * 另外附有extenddata哈希表,用于携带除数据库字段外的其他信息
 * @author Elysion
 *
 */
public class Model implements Serializable {
	private Map<String, Object> extendData;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public void putExtendData(String name,Object value) {
		
		getExtendDataMap().put(name, value);
	}
	
	public Object getExtendData(String name) {
		return getExtendDataMap().get(name);
	}
	
	public Map<String, Object> getExtendDataMap() {
		if(extendData==null) {
			extendData=new HashMap<String, Object>();
		}
		return extendData;
	}
	
	public void setExtendDataMap(Map<String, Object> map) {
		extendData=map;
	}
	
	
	
	
	public static BigDecimal toBigDecimal(long i) {
		return new BigDecimal(i);
	}
	
	public static BigDecimal toBigDecimal(String str,long def) {
		
		try {
			return new BigDecimal(str);
		} catch (Exception e) {
			return new BigDecimal(def);
		}
	}
	
	public static Long toLong(long i) {
		return new Long(i);
	}
	
	public static Long toLong(String str,long def) {
		try {
			return new Long(str);
		} catch (Exception e) {
			return new Long(def);
		}
	}
	
	public static Double toDouble(double i) {
		return new Double(i);
	}
	
	public static Double toDouble(String str,double def) {
		try {
			return new Double(str);
		} catch (Exception e) {
			return new Double(def);
		}
	}
	
	
	

	


}
