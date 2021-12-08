package cn.com.ragnarok.elysion.common.baseobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageBean implements Serializable {   
	  
    public static int DEFAULT_PAGE_SIZE = 10;   
  
    private int pageSize = PageBean.DEFAULT_PAGE_SIZE; // 每页的记录数   
  
    private int start; // 当前页第一条数据在List中的位置,从0开始   
  
    private List data; // 当前页中存放的记录,类型一般为List   
  
    private int totalCount; // 总记录数   
  
  
  
    /**  
     * 构造方法，只构造空页.  
     */  
    public PageBean() {   
        this(0, 0, PageBean.DEFAULT_PAGE_SIZE, new ArrayList());   
    }   
  
    /**  
     * 默认构造方法.  
     *   
     * @param page  
     *            本页页数  
     * @param totalSize  
     *            数据库中总记录条数  
     * @param pageSize  
     *            本页容量  
     * @param data  
     *            本页包含的数据  
     */  
    public PageBean(int page,  int pageSize,int totalSize, List data) {   
        this.pageSize = pageSize;
        if(pageSize<=0)pageSize=data.size();
        this.start = (page-1)*pageSize;
        if(start<=0 || start>totalSize)start=0;
        totalCount = totalSize;   
        this.data = data;   
    }
    
    public String toString(){
    	int rows=0;
    	String classname="";
    	if(this.getData()!=null){
    		rows=this.getData().size();
    		if(rows>0){
    			classname=this.getData().get(0).getClass().toString();
    		}
    	}
    	return "PageBean:[ 数据 "+rows+" 行 |  第 "+this.getCurrentPageNo()+" 页/共 "+this.getTotalPage()+" 页"+
    	" | 每页 "+this.getPageSize()+" 行 /共 "+this.getTotalCount()+" 行] 数据类型:"+classname;
    	
    }
  
    /**  
     * 取总记录数.  
     */  
    public int getTotalCount() {   
        return totalCount;   
    }   
  
    /**  
     * 取总页数.  
     */  
    public int getTotalPageCount() {   
        if (totalCount % pageSize == 0)   
            return totalCount / pageSize;   
        else  
            return totalCount / pageSize + 1;   
    }   
  
    /**  
     * 取每页数据容量.  
     */  
    public int getPageSize() {   
        return pageSize;   
    }   
  
    /**  
     * 取当前页中的记录.  
     */  
    public List getData() {   
        return data;   
    }   
  
    /**  
     * 取该页当前页码,页码从1开始.  
     */  
    public int getCurrentPageNo() {
        return start / pageSize + 1;   
    }   
  
    /**  
     * 该页是否有下一页.  
     */  
    public boolean hasNext() {   
        return getCurrentPageNo() < getTotalPageCount() - 1;   
    }   
  
    /**  
     * 该页是否有上一页.  
     */  
    public boolean hasPrev() {   
        return getCurrentPageNo() > 1;   
    }   
  
    /**  
     * 获取任一页第一条数据在数据集的位置，每页条数使用默认值.  
     *   
     * @see #getStartOfPage(int,int)  
     */  
    protected static int getStartOfPage(int pageNo) {   
        return PageBean.getStartOfPage(pageNo, PageBean.DEFAULT_PAGE_SIZE);   
    }   
  
    /**  
     * 获取任一页第一条数据在数据集的位置.  
     *   
     * @param pageNo  
     *            从1开始的页号  
     * @param pageSize  
     *            每页记录条数  
     * @return 该页第一条数据  
     */  
    public static int getStartOfPage(int pageNo, int pageSize) {   
        return (pageNo - 1) * pageSize;   
    }   
       
    public int getCurrentPage() {   
        return this.getCurrentPageNo();   
    }   
       
    public int getTotalPage() {   
        return this.getTotalPageCount();   
    }   
    public int getStart() {    
        return this.start;   
    } 
    
}

