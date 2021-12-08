package cn.com.ragnarok.elysion.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class DateUtil {
	public static String formatDate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	public static String formatDateTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static String formatDateTime(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static Date parseDateTime(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(date);
		} catch (ParseException e) {
		}
		return null;
	}

	public static Date parseDateTime(String date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(date);
		} catch (ParseException e) {
		}
		return null;
	}

	public static Date parseDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(date);
		} catch (ParseException e) {
		}
		return null;
	}
	
	public static int getDateField(Date date,int field){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(field);
	}
	
	public static int getDateField(String datestr,String pattern,int field){
		Date date=parseDateTime(datestr, pattern);
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(field);
	}
	
	public static int getDateFieldMax(Date date,int field){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(field);
	}
	
	public static int getDateFieldMin(Date date,int field){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.getActualMinimum(field);
	}
	
	
	public static List<String> generateDateCodeList(Date start,Date end,String pattern,int field,int amount,boolean ignoreSameCode){
		List<String> result=new Vector<String>();
		if(start==null || end==null || start.after(end)){
			return result;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(start);
		
		Calendar c2=Calendar.getInstance();
		c2.setTime(end);
		
		//result.add(formatDateTime(c.getTime(), pattern));
		while(true){
			String code=formatDateTime(c.getTime(), pattern);
			if(! (ignoreSameCode && result.contains(code)) ){
				result.add(code);
			}
			
			c.add(field, amount);
			if(c.getTime().after(end)){
				String currentcode=formatDateTime(c.getTime(),pattern);
				String endcode=formatDateTime(end,pattern);
				if(currentcode.equals(endcode)){
					if(! (ignoreSameCode && result.contains(currentcode)) ){
						result.add(currentcode);
					}
				}
				break;
			}
		}	
		return result;
	}
	
	
	
	public static void main(String[] args) {
		Date start=parseDate("2011-11-29");
		Date end=parseDate("2012-12-03");
		List list=generateDateCodeList(start, end, "yyyyMM", Calendar.MONTH, 1,false);
		for (Object object : list) {
			System.out.println(object);
		}
		long time=24*60*60*1000*5+60*60*1000*20+200000;
		System.out.println(DateUtil.formatTimeSpan(time, "DD天HH小时MM分SS秒"));
		System.out.println(DateUtil.formatTimeSpan(time, "HH小时MM分SS秒"));
		System.out.println(DateUtil.formatTimeSpan(time, "MM分SS秒"));
		System.out.println(DateUtil.formatTimeSpan(time, "SS秒"));
		System.out.println(DateUtil.formatTimeSpan(time, "DD天HH小时"));
		System.out.println(DateUtil.formatTimeSpan(time, "HH小时"));
		System.out.println(DateUtil.formatTimeSpan(time, "HHH-MMM:SSS"));
		
		
		
		
	}

	public static Date addTime(Date date, int field, int amount) {
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(field, amount);
		return c.getTime();
	}
	
	public static String addTimeCode(String datecode,String pattern, int field, int amount) {
		Calendar c=Calendar.getInstance();
		c.setTime(parseDateTime(datecode, pattern));
		c.add(field, amount);
		return formatDateTime(c.getTime(),pattern);
	}

	/**
	 * 格式化时间间隔字符串,
	 * @param time 时间,单位毫秒
	 * @param pattern 模板 DD 天数 HH 小时 MM 分钟 SS秒 例:"DD天HH:MM:SS"
	 * @return
	 */
	public static String formatTimeSpan(long time, String pattern) {
		long day=time/(24*60*60*1000L);
		long hour=time/(60*60*1000L);
		long min=time/(60*1000L);
		long sec=time/1000L;
		
		if(pattern.contains("MM")){
			sec=sec-min*60;
		}
		if(pattern.contains("HH")){
			min=min-hour*60;
		}
		if(pattern.contains("DD")){
			hour=hour-day*24;
		}
		
		String result=pattern.replace("DD", day+"");
		result=result.replace("HH", hour<10?"0"+hour:""+hour);
		result=result.replace("MM", min<10?"0"+min:""+min);
		result=result.replace("SS", sec<10?"0"+sec:""+sec);
		
		return result;
	}

}
