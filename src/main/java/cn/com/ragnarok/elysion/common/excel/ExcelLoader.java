
package cn.com.ragnarok.elysion.common.excel;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import cn.com.ragnarok.elysion.common.util.DateUtil;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
/**
 *
 * <p>Title: Common Tool Util</p>
 *
 * <p>Description:EXCEL加载工具,能够将EXCEL读取为Vector(Hashtable),或者映射成对象数组 </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: </p>
 *
 * @author 李海洋
 * @version 1.0
 */
public class ExcelLoader {
    private final static org.apache.log4j.Logger log = org.apache.log4j.
            LogManager.getLogger(ExcelLoader.class);

    private Hashtable mapping=new Hashtable();
    private int rowStart=0;
    private int rowEnd=65535;
    private String filename;
    private int sheetIndex=0;

    /**
     *
     * @param filename String 读取文件名,默认为第一个表单
     */
    public ExcelLoader(String filename){
        this(filename,0);
    }
    /**
     *
     * @param filename String 读取文件名
     * @param sheetIndex int 表单编号
     */
    public ExcelLoader(String filename,int sheetIndex) {
        this.filename=filename;
        this.sheetIndex=sheetIndex;
    }

    /**
     * 读取为字符串二维数组
     * @return String[][]
     * @throws Exception
     */
    public String[][] loadExcel() throws Exception {
        log.info("开始读取文件:"+filename );
        Workbook wb=Workbook.getWorkbook(new File(filename));
        Sheet sheet=wb.getSheet(sheetIndex);
        log.info("开始读取Sheet:"+sheetIndex+" ["+sheet.getName()+"]");
        int cols=sheet.getColumns();
        int rows=sheet.getRows();
        int end=rows<rowEnd?rows:rowEnd;
        int start=rowStart>rows?rows:rowStart;
        String[][] data=new String[end-start][cols];
        log.info("初始化读取:"+"行="+rows +" 列="+cols+" 范围:["+start+" - "+end+"]行  预设:["+rowStart+" - "+rowEnd+"]行");
        for (int r = start; r < end; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell=sheet.getCell(c,r);
                if(cell.getType()==CellType.DATE_FORMULA ||cell.getType()==CellType.DATE ){
                	DateCell dc=(DateCell) cell;
                	if(dc.getContents()!=null){
                		data[r-start][c]=DateUtil.formatDateTime(new Date(dc.getDate().getTime()-8*60*60*1000));
                	}
                }else{
                	data[r-start][c]=cell.getContents();                	
                }
                //log.info((r-start)+"-"+c+" "+cell.getContents());
            }
        }
        wb.close();

        return data;
    }
    /**
     * 映射为Vector
     * @param enableColNameMapping boolean 是否按列名映射,(映射后排除第一行)
     * @return Vector
     * @throws Exception
     */
    public Vector mappingData(boolean enableColNameMapping) throws Exception {
        String[][] data=loadExcel();
        Hashtable currentMapping=(Hashtable)mapping.clone();
        int start=0;
        if(enableColNameMapping){
            for (int c = 0; c < data[0].length; c++) {
                if(currentMapping.containsKey(data[0][c])){
                    currentMapping.put(c,currentMapping.get(data[0][c]));
                }
            }
            start=1;
        }
        log.info("映射字段:"+currentMapping);

        Vector vec=new Vector();
        for (int r = start; r < data.length; r++) {
            Hashtable row=new Hashtable();
            for (int c = 0; c < data[0].length; c++) {
                String mappingKey=(String)currentMapping.get(c);
                if(mappingKey!=null){
                    row.put(mappingKey,data[r][c]);
                }
            }
            vec.add(row);
        }

        return vec;
    }
    /**
     * 映射为对象集合
     * @param objClass Class 对象类型,必须实现MappingObject接口
     * @param enableColNameMapping boolean 是否按列名映射,(映射后排除第一行)
     * @return Vector
     * @throws Exception
     */
    public Vector mappingObject(Class objClass,boolean enableColNameMapping) throws
    Exception {
		Vector vec=new Vector();
		if(!ExcelLoaderMappingObject.class.isAssignableFrom(objClass)){
		    log.info("无法映射,未实现接口 ExcelLoaderMappingObject: "+objClass);
		    return vec;
		}
        Vector mappingdata=mappingData(enableColNameMapping);
        for (Iterator it = mappingdata.iterator(); it.hasNext(); ) {
            Hashtable row = (Hashtable) it.next();
            ExcelLoaderMappingObject obj=(ExcelLoaderMappingObject)objClass.newInstance();
            obj.mappingObject(row);
            vec.add(obj);
        }
        return vec;
    }
    /**
     * 增加列映射
     * @param col int 列序号
     * @param mappingName String 映射字段
     */
    public void addColMapping(int col,String mappingName){
        mapping.put(col,mappingName);
    }
    /**
     * 增加列映射
     * @param colCode String 列编号(A,B,C...AA,)
     * @param mappingName String 映射字段
     */
    public void addColMapping(String colCode,String mappingName){
        int col=jxl.biff.CellReferenceHelper.getColumn(colCode);
        mapping.put(col,mappingName);
    }

    /**
     * 增加列名映射
     * @param colname String 列名(第一行名字)
     * @param mappingName String 映射字段
     */
    public void addColNameMapping(String colname,String mappingName){
        mapping.put(colname,mappingName);
    }
    /**
     * 清除映射
     */
    public void clearMapping(){
        mapping.clear();
    }
    /**
     * 设置读取开始行
     * @param start int
     */
    public void setRowStart(int start){
        this.rowStart=start;
    }
    /**
     * 设置读取结束行
     * @param end int
     */
    public void setRowEnd(int end){
        this.rowEnd=end;
    }
    /**
     * 设置文件名
     * @param filename String
     */
    public void setFilename(String filename){
        this.filename=filename;
    }
    /**
     * 设置表单序号
     * @param index int
     */
    public void setSheetIndex(int index){
        this.sheetIndex=index;
    }


    public static void main2(String[] args) throws Exception {
        String[][] data=new ExcelLoader("c:/test.xls").loadExcel();
        for (int r = 0; r < data.length; r++) {
            for (int c = 0; c < data[0].length; c++) {
               System.out.print(data[r][c]+" ");
            }
            System.out.println("");
        }

    }

    public static void main3(String[] args) throws Exception {
        ExcelLoader loader=new ExcelLoader("c:/test.xls");
        loader.addColMapping(1,"groupname");
        loader.addColMapping(3,"area");
        loader.addColNameMapping("工单流水号","tid");
        loader.setRowEnd(60);
        loader.setRowStart(0);
        Vector data=loader.mappingData(false);
        for (Iterator it = data.iterator(); it.hasNext(); ) {
            Hashtable row = (Hashtable) it.next();
            System.out.println(row);
        }
    }

    public static void main(String[] args) throws Exception {
        ExcelLoader loader=new ExcelLoader("c:/test.xls");
        loader.addColMapping("B","groupname");
        loader.addColMapping(3,"area");
        loader.addColNameMapping("工单流水号","tid");
        loader.setRowEnd(60);
        loader.setRowStart(0);
        Vector data=loader.mappingObject(Ticket.class,true);
        for (Iterator it = data.iterator(); it.hasNext(); ) {
                   System.out.println(it.next());
        }
    }

    public static class Ticket implements ExcelLoaderMappingObject{
        private String id;
        private String area;
        private String group;

        public void mappingObject(Hashtable mappingrow) {
            id=(String)mappingrow.get("tid");
            area=(String)mappingrow.get("area");
            group=(String)mappingrow.get("groupname");
        }

        public String toString(){
            return id+":["+area+"]"+" "+group;
        }

    }
}
