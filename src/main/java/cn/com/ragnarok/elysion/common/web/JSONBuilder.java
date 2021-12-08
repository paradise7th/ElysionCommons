
package cn.com.ragnarok.elysion.common.web;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * JSON字符串生成类
 * @author Elysion
 *
 */
public class JSONBuilder {
    private JSONBuilder() {
    }
    /**
     * 生成JSON对象字符串,如果hash中包含hash将转化成对象字符串
     * @param hash
     * @return
     */
    public static String bulidObjectString(Map hash){
        StringBuilder sb=new StringBuilder("{");
        for (Iterator it = hash.keySet().iterator(); it.hasNext(); ) {
            Object key = (Object) it.next();
            Object value=(Object) hash.get(key);
            if(key!=null && value!=null){
                if(value instanceof Map){
                    value=bulidObjectString((Map)value);
                    sb.append("'"+key+"':"+value);
                }else if(value instanceof List){
                    value=buildDataArrayString((List)value,null);
                    sb.append("'"+key+"':"+value);
                }else{
                	String _value=value+"";
                	_value=_value.replaceAll("'","\\\\'" )
            		.replaceAll("\n","\\\\n")
            		.replaceAll("\r","\\\\r");
                    sb.append("'" + key + "':" + "'" + _value + "'");
                }
            }
            if(it.hasNext()){
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

/**
 * 生成JSON对象数组字符串
 * @param list
 * @param name
 * @return
 */
    public static String buildDataArrayString(List list,String name){
        StringBuilder sb=new StringBuilder("");
        if(name==null){
            sb.append("[");
        }else{
            sb.append("{'"+name+"':[");
        }
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map hash = (Map) it.next();
            sb.append(bulidObjectString(hash));
            if(it.hasNext()){
                sb.append(",");
            }
        }
        if(name==null){
            sb.append("]");
        }else{
            sb.append("]}");
        }

        return sb.toString();
    }


    public static String buildHashArray(Map hash){
    	if(hash == null) return "";
    	StringBuilder sb=new StringBuilder("[");
    	for (Iterator it = hash.keySet().iterator(); it.hasNext();) {
			Object key = it.next();
			Object value=hash.get(key);
			sb.append("['"+key+"'")
			.append(",")
			.append("'"+value+"'")
			.append("]");
			if(it.hasNext()){
				sb.append(",");
			}
		}
    	sb.append("]");
    	return sb.toString();
    }

    public static void main(String[] args) {
        Hashtable hash=new Hashtable();
        hash.put("age","20");
        hash.put("name","john");

        hash.put("country","earth");
//        Vector  v=new Vector();
//        v.add(hash);
//        v.add(hash);
//        System.out.println(bulidObjectString(hash));
//        System.out.println(buildDataArrayString(v,null));
        System.out.println(buildHashArray(hash));
    }

}
