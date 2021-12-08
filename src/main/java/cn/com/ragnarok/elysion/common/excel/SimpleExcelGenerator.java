
package cn.com.ragnarok.elysion.common.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
/**
 *
 * <p>Title: Common Tool Util</p>
 *
 * <p>Description:简单EXCEL报表输出 </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: </p>
 *
 * @author 李海洋
 * @version 1.0
 */
public class SimpleExcelGenerator
{
  private final static org.apache.log4j.Logger log = org.apache.log4j.
    LogManager.getLogger(SimpleExcelGenerator.class);

  private String title;
  private String filename;
  private String[] colNames;
  private String[] mappingNames;
  private int[] colWidths=new int[]{};
  private Hashtable mappings=new Hashtable();
  private List data;
  private WritableCellFormat titleFormat;
  private WritableCellFormat colheadFormat;
  private String sheetName="Sheet1";
  /**
   *
   * @param title String 标题
   * @param filename String 文件名
   * @param colNames String[] 列名
   * @param mapping String[] 列名映射字段,要和列名数量一致
   * @param data List 数据,每行以Map存放
   */
  public SimpleExcelGenerator(String title,String filename,String[] colNames,String[] mapping,List data){
    this.title=title;
    this.filename=filename;
    this.colNames=colNames;
    this.mappingNames=mapping;
    for(int i = 0;i < mappingNames.length;i++)
    {
      mappings.put(mappingNames[i],i);
    }
    this.data=data;
  }

  public SimpleExcelGenerator(String filename,String[] colNames,String[] mapping){
    this(null,filename,colNames,mapping,null);
  }
  /**
   * 设置数据
   * @param data List
   */
  public void setData(List data){
    this.data=data;
  }
  /**
   * 设置文件名
   * @param name String
   */
  public void setFilename(String name){
    this.filename=name;
  }
  /**
   * 设置标题样式
   * @param cf WritableCellFormat
   */
  public void setTitleFormat(WritableCellFormat cf){
    this.titleFormat=cf;
  }
  /**
   * 设置表头样式
   * @param cf WritableCellFormat
   */
  public void setColumnHeadFormat(WritableCellFormat cf){
    this.colheadFormat=cf;
  }
  /**
   * 设置表单名称
   * @param name String
   */
  public void setSheetName(String name){
    this.sheetName=name;
  }
/**
 * 设置每列宽度
 * @param colwidth
 */
  public void setColumnWidth(int[] colwidth){
	  this.colWidths=colwidth;
  }


  /**
   * 默认标题样式
   * @return WritableCellFormat
   */
  public WritableCellFormat getDefaultTitleCellFormat(){
    WritableFont wf=new WritableFont(WritableFont.ARIAL,18,WritableFont.BOLD,false);
    WritableCellFormat cf=new WritableCellFormat(wf);
    try
    {
      cf.setAlignment(Alignment.CENTRE);
      cf.setVerticalAlignment(VerticalAlignment.CENTRE);
      cf.setBorder(Border.ALL,BorderLineStyle.THIN);
    }
    catch(WriteException ex)
    {
    }
    return cf;
  }
  /**
   * 默认表头样式
   * @return WritableCellFormat
   */
  public WritableCellFormat getDefaultColumnHeadCellFormat(){
    WritableFont wf=new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD,false);
    WritableCellFormat cf=new WritableCellFormat(wf);
    try
    {
      cf.setAlignment(Alignment.CENTRE);
      cf.setVerticalAlignment(VerticalAlignment.CENTRE);
      cf.setBackground(Colour.LIME);
      cf.setBorder(Border.ALL,BorderLineStyle.THIN);
    }
    catch(WriteException ex)
    {
    }
    return cf;

 }
/**
 * 输出EXCEL,按预设文件名
 * @throws Exception
 */
  public void generateExcel() throws Exception{
	  generateExcel(new FileOutputStream(new File(filename)));
  }



 /**
  * 输出EXCEL
  * @throws Exception
  */
 public void generateExcel(OutputStream os) throws Exception
  {
    if(data==null)return;
    WritableWorkbook wwb=Workbook.createWorkbook(os);
    WritableSheet ws=wwb.createSheet(sheetName,0);
    int cols=colNames.length;
    int rowindex=0;
    //标题
    if(title!=null && title.length()>0){
        Label lbl = new Label(0,0,title,titleFormat!=null?titleFormat:getDefaultTitleCellFormat());
        ws.addCell(lbl);
        ws.mergeCells(0,0,cols-1,rowindex);
        rowindex++;
    }

    //表头
    for(int i = 0;i < cols;i++)
    {
      Label lbl=new Label(i,rowindex,colNames[i],colheadFormat!=null?colheadFormat:getDefaultColumnHeadCellFormat());
      ws.addCell(lbl);
    }
    rowindex++;
    //表格
    for(Iterator it = data.iterator();it.hasNext();)
    {
      Map row = (Map)it.next();
      for(Iterator it2 = row.keySet().iterator();it2.hasNext();)
      {
        Object key = it2.next();  //轮询数据集,

        if(mappings.containsKey(key)){ //查看有不有和映射对应的字段
          Integer col=(Integer)mappings.get(key);
          Object obj=row.get(key); //取得数据
          //log.info("取得数据:"+key+" "+obj);
          if(obj!=null){
            ws.addCell(generateCell(col, rowindex, obj));
          }
        }
      }
      rowindex++;
    }
    
    CellView cellView = new CellView();  
    cellView.setAutosize(true); //设置自动大小  
    for(int i = 0;i < cols;i++){
    	if(i<colWidths.length && colWidths[i]>=0){
    		ws.setColumnView(i, colWidths[i]);    		     		    			
    	}else{
    		ws.setColumnView(i, cellView);//根据内容自动设置列宽  
    	}
    	
	 }
    

    
    wwb.write();
    wwb.close();
    log.info("EXCEL生成完成:"+(rowindex)+"行 " +filename);
  }
 
 
 
 
 private WritableCell generateCell(int col,int row,Object obj){
	 if(obj instanceof Number){
		 return new jxl.write.Number(col,row,((Number)obj).floatValue());
	 }else if(obj instanceof Date){
		 return new jxl.write.DateTime(col,row,(Date)obj);
	 }
//	 try{
//		 return new jxl.write.Number(col,row,Float.parseFloat(obj.toString()));
//	 }catch(Exception e){}
	 return new Label(col,row,obj.toString());
 }




  public static void main(String[] args) throws Exception
  {
    String[] cols={"AA","BB","CC"};
    String[] mapping={"name","age","date"};

    Vector data=new Vector();
    Hashtable line1=new Hashtable();
    line1.put("name","都会恢复");
    line1.put("age",12);
    line1.put("date","2009-10-01");
    data.add(line1);

    Hashtable line2=new Hashtable();
   line2.put("name","都会恢复2333333333333333333");
   line2.put("age","12");
   line2.put("date","");
   data.add(line2);

   new SimpleExcelGenerator("测试故障单","c:/text.xls",cols,mapping,data).generateExcel();


  }


}
