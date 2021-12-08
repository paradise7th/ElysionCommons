package cn.com.ragnarok.elysion.common.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * WEB工具类
 * @author Elysion
 *
 */
public class WebUtils {
	
    public static String getParam(HttpServletRequest request,String key,String def){
        String s=request.getParameter(key);
        if(s==null){
            s=def;
        }
        return s;
    }
    
    public static Vector<String> getParamValues(HttpServletRequest request,String key){
        Vector<String> list=new Vector<String>();
    	String[] s=request.getParameterValues(key);
        if(s!=null){
            for (int i = 0; i < s.length; i++) {
				list.add(s[i]);
			}
        }
        return list;
    }
    
    public static Vector<Long> getParamValuesLong(HttpServletRequest request,String key){
    	 Vector<Long> list=new Vector<Long>();
     	String[] s=request.getParameterValues(key);
         if(s!=null){
             for (int i = 0; i < s.length; i++) {
            	 try {
                     list.add(Long.valueOf(s[i]));
                 } catch (Exception ex) {
                 }
 			}
         }
         return list;
    }
    
    public static Vector<Double> getParamValuesDouble(HttpServletRequest request,String key){
   	 Vector<Double> list=new Vector<Double>();
    	String[] s=request.getParameterValues(key);
        if(s!=null){
            for (int i = 0; i < s.length; i++) {
           	 try {
                    list.add(Double.valueOf(s[i]));
                } catch (Exception ex) {
                }
			}
        }
        return list;
    }
    
    public static Vector<Integer> getParamValuesInt(HttpServletRequest request,String key){
      	 Vector<Integer> list=new Vector<Integer>();
       	String[] s=request.getParameterValues(key);
           if(s!=null){
               for (int i = 0; i < s.length; i++) {
              	 try {
                       list.add(Integer.valueOf(s[i]));
                   } catch (Exception ex) {
                   }
   			}
           }
           return list;
       }	

    public static String getParamUTF8(HttpServletRequest request,String key,String def){
        String s=request.getParameter(key);
        try {
			s = new String(s.getBytes("ISO-8859-1"),"UTF-8");
		} catch (Exception e) {
		}
        if(s==null){
            s=def;
        }
        return s;
    }

    public static String getParamGBK(HttpServletRequest request,String key,String def){
        String s=request.getParameter(key);
        try {
			s = new String(s.getBytes("ISO-8859-1"),"GBK");
		} catch (Exception e) {
		}
        if(s==null){
            s=def;
        }
        return s;
    }

    public static int getParamInt(HttpServletRequest request,String key,int def){
        String s=request.getParameter(key);
        int i=def;
        try {
            i=Integer.parseInt(s);
        } catch (Exception ex) {
        }
        return i;
    }

    public static long getParamLong(HttpServletRequest request,String key,int def){
        String s=request.getParameter(key);
        long i=def;
        try {
            i=Long.parseLong(s);
        } catch (Exception ex) {
        }
        return i;
    }

    public static double getParamDouble(HttpServletRequest request,String key,double def){
        String s=request.getParameter(key);
        double i=def;
        try {
            i=Double.parseDouble(s);
        } catch (Exception ex) {
        }
        return i;
    }

    public static float getParamFloat(HttpServletRequest request,String key,float def){
        String s=request.getParameter(key);
        float i=def;
        try {
            i=Float.parseFloat(s);
        } catch (Exception ex) {
        }
        return i;
    }
    
    public static Date getParamDate(HttpServletRequest request,String key,String pattern,Date def){
    	String s=getParam(request, key,null);
    	if(s==null)return def;
    	try{
    		def=new SimpleDateFormat(pattern).parse(s);
    	}catch(Exception e){
    	}
    	return def;
    	
    	
    }

    public static Object getSessionObject(HttpServletRequest request,String key,Object def){
    	HttpSession session=request.getSession(false);
    	Object obj=null;
    	if(session!=null){
    		obj=session.getAttribute(key);
    	}
    	if(obj==null){
    		obj=def;
    	}
    	return obj;
    }

    public static String getSessionString(HttpServletRequest request,String key,String def){
    	Object obj=getSessionObject(request, key, null);
    	if(obj!=null){
    		return obj.toString();
    	}else{
    		return def;
    	}
    }

    public static int getSessionInt(HttpServletRequest request,String key,int def){
    	String value=getSessionString(request, key, null);
    	int i=def;
    	if(value!=null){
    		try {
                i=Integer.parseInt(value);
            } catch (Exception ex) {
            }
    	}
    	return i;
    }
    
    public static long getSessionLong(HttpServletRequest request,String key,long def){
    	String value=getSessionString(request, key, null);
    	long i=def;
    	if(value!=null){
    		try {
                i=Long.parseLong(value);
            } catch (Exception ex) {
            }
    	}
    	return i;
    }


    public static double getSessionDouble(HttpServletRequest request,String key,double def){
    	String value=getSessionString(request, key, null);
    	double i=def;
    	if(value!=null){
    		try {
                i=Double.parseDouble(value);
            } catch (Exception ex) {
            }
    	}
    	return i;
    }
    
    public static String encodeUTF8(String uri){
    	try {
			return java.net.URLEncoder.encode(uri, "utf-8");
		} catch (Exception e) {
			return uri;
		}
    }
    
    public static String encodeGBK(String uri){
    	try {
			return java.net.URLEncoder.encode(uri, "gbk");
		} catch (Exception e) {
			return uri;
		}
    }
    
    public static String decodeUTF8(String uri){
    	try {
			return java.net.URLDecoder.decode(uri, "utf-8");
		} catch (Exception e) {
			return uri;
		}
    }
    
    public static String decodeGBK(String uri){
    	try {
			return java.net.URLDecoder.decode(uri, "gbk");
		} catch (Exception e) {
			return uri;
		}
    }


   


}
