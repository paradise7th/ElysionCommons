package cn.com.ragnarok.elysion.common.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
/**
 * Excel模板生成工具
 * <p>Title: Common Tool Util</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Elysion
 * @version 1.0
 */
public class ExcelTemplate {
  private final static org.apache.log4j.Logger log = org.apache.log4j.
      LogManager.getLogger(ExcelTemplate.class);
  private Workbook wb = null;
  private int sheetIndex = 0;
  private Hashtable mapCells = new Hashtable();
  private Hashtable lineCells = new Hashtable();
  private Hashtable mapData = new Hashtable();
  private Vector tableData = new Vector();
  private int lineIndex = -1;

  public ExcelTemplate() {
  }
  /**
   * 设置模板工作表编号
   * @param index int
   */
  public void setSheetIndex(int index) {
    sheetIndex = index;
  }
  /**
   * 设置映射数据
   * @param data Hashtable
   */
  public void setMapData(Hashtable data) {
    this.mapData = data;
  }
  /**
   * 设置表格数据
   * @param data Vector
   */
  public void setTableData(Vector data) {
    this.tableData = data;
  }
  /**
   * 添加映射字段位置
   * @param key String
   * @param col int
   * @param row int
   */
  public void addMapInfo(String key, int col, int row) {
    mapCells.put(key, new CellInfo(col, row));
  }
  /**
   * 添加列表字段映射位置
   * @param key String
   * @param col int
   * @param row int
   */
  public void addLineInfo(String key, int col, int row) {
    lineCells.put(key, new CellInfo(col, row));
  }
  /**
   * 添加字段映射位置
   * @param key String
   * @param loc String EXCEL单元格编号
   */
  public void addMapTemplate(String key, String loc) {
   mapCells.put(key, new CellInfo(loc));
 }
 /**
  * 添加列表字段映射位置
  * @param key String
  * @param loc String EXCEL单元格编号
  */
 public void addLineTemplate(String key, String loc) {
   lineCells.put(key, new CellInfo(loc));
 }

 /**
  * 移除字段映射
  * @param key String
  */
 public void removeMapInfo(String key) {
    mapCells.remove(key);
  }
  /**
   * 移除列表字段映射
   * @param key String
   */
  public void removeLineInfo(String key) {
    lineCells.remove(key);
  }
  /**
   * 初始化模板
   * @param filename String
   * @param sheet int
   * @throws Exception
   */
  public void initFromFile(String filename,int sheet) throws Exception {
    setSheetIndex(sheet);
    initFromFile(filename);
  }
  /**
   * 初始化模板
   * @param filename String
   * @throws Exception
   */
  public void initFromFile(String filename) throws Exception {
    try {
      wb = Workbook.getWorkbook(new File(filename));
    }
    catch (Exception ex) {
      log.error("读取模板失败", ex);
      return;
    }
    int count = wb.getNumberOfSheets();
    log.info("读取模板:" + filename + " 工作表数:" + count);
    Sheet sheet = wb.getSheet(sheetIndex);
    int rows = sheet.getRows();
    int cols = sheet.getColumns();
    log.info("取得工作表模板:" + sheet.getName() + " 行:" + rows + " 列:" + cols);

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Cell c = sheet.getCell(j, i);
        String content = c.getContents();
        if (content.startsWith("#:")) {
          mapCells.put(content.substring(2), new CellInfo(c));
        }
        else if (content.startsWith("*:")) {
          lineCells.put(content.substring(2), new CellInfo(c));
          lineIndex = c.getRow(); //确定数据开始行
        }
      }
    }
    //复制时不能关闭模板
    //wb.close();
    log.info("模板读取完成: 字段:" + mapCells.size() + " 列表字段:" + lineCells.size() +
             " 列表起始行:" + lineIndex);
  }
  /**
   * 输出文件
   * @param filename String
   * @throws Exception
   */
  public void output(String filename)throws Exception{
    OutputStream os=new FileOutputStream(filename);
    output(os);
    log.info("输出完毕:" + filename);
  }

  /**
   * 输出文件流
   * @param os OutputStream
   * @throws Exception
   */
  public void output(OutputStream os) throws Exception {
    log.info("开始应用模板: 字段:" + mapCells.size() + " 列表字段:" + lineCells.size() +
             " 列表起始行:" + lineIndex);

    WritableWorkbook wwb = Workbook.createWorkbook(os, wb);
    WritableSheet wsheet = wwb.getSheet(sheetIndex);

    for (Iterator it = mapCells.keySet().iterator(); it.hasNext(); ) {
      String key = (String) it.next();
      CellInfo cinfo = (CellInfo) mapCells.get(key);
      Object value = mapData.get(key);
      if (value != null) {
        updateCell(wsheet, cinfo, value);
      }
      else {
        updateCell(wsheet, cinfo, "");
      }
    }

    //确定第一行位置
    int dataIndex = lineIndex;
    for (Iterator it = tableData.iterator(); it.hasNext(); ) {
      Hashtable row = (Hashtable) it.next();
      for (Iterator it2 = lineCells.keySet().iterator(); it2.hasNext(); ) {
        String key = (String) it2.next();
        CellInfo cinfo = (CellInfo) lineCells.get(key);

        Object value = row.get(key);
        if (value != null) {
          addOrUpdateLineCell(wsheet, cinfo, dataIndex, value);
        }
        else {
          addOrUpdateLineCell(wsheet, cinfo, dataIndex, "");
        }
      }
      dataIndex++;

    }
    log.info("生成数据表行:"+(dataIndex-lineIndex));

    if(tableData.size()==0){
      wsheet.removeRow(lineIndex);
    }

    wwb.write();
    wwb.close();
    os.close();


  }

  /**
   * addOrUpdateLineCell
   *
   * @param wsheet WritableSheet
   * @param cinfo CellInfo
   * @param dataIndex int
   * @param value Object
   */
  private void addOrUpdateLineCell(WritableSheet wsheet,
                                   CellInfo cinfo,
                                   int dataIndex, Object value) {
    WritableCell c = wsheet.getWritableCell(cinfo.col, cinfo.row);
    if (cinfo.row == dataIndex) {
      updateCell(wsheet, cinfo, value);
      return;
    }

    WritableCell wc = null;

    if (c.getType() == CellType.LABEL) { //标签
      wc = new Label(cinfo.col, dataIndex, value.toString(), c.getCellFormat());
    }
    else if (c.getType() == CellType.NUMBER) { //数字
      try {
        wc = new jxl.write.Number(cinfo.col, dataIndex,
                                  new Double(value.toString()).doubleValue(),
                                  c.getCellFormat());
      }
      catch (NumberFormatException ex) {}
    }
    else if (c.getType() == CellType.DATE) { //日期
      try {
        wc = new jxl.write.DateTime(cinfo.col, dataIndex, (Date) value,
                                    c.getCellFormat());
      }
      catch (Exception ex) {}
    }
    else {
      log.info("单元格[" + cinfo.col + "," + dataIndex + "]:" + c.getType() +
               " 已忽略:" + value);
    }

    try {
      if(wc!=null){
        wsheet.addCell(wc);
      }
    }
    catch (WriteException ex) {
      log.error("单元格添加失败 [" + cinfo.col + "," + dataIndex + "]:" + c.getType() +
                " 值:" + value);
    }

  }

  /**
   * updateCell
   *
   * @param wsheet WritableSheet
   * @param cinfo CellInfo
   * @param value Object
   */
  private void updateCell(WritableSheet wsheet,
                          CellInfo cinfo,
                          Object value) {
    WritableCell c = wsheet.getWritableCell(cinfo.col, cinfo.row);
    if (c != null) {
      if (c.getType() == CellType.LABEL) { //标签
        Label l = (Label) c;
        l.setString(value.toString());
      }
      else if (c.getType() == CellType.NUMBER) { //数字
        jxl.write.Number n = (jxl.write.Number) c;
        try {
          n.setValue(new Double(value.toString()).doubleValue());
        }
        catch (NumberFormatException ex) {}
      }
      else if (c.getType() == CellType.DATE) { //日期
        jxl.write.DateTime d = (jxl.write.DateTime) c;
        try {
          d.setDate( (Date) value);
        }
        catch (Exception ex) {}
      }
      else {
        log.info("单元格[" + cinfo.col + "," + cinfo.row + "]:" + c.getType() +
                 " 值:" + value +" 使用默认LABEL" );
        Label lbl=new Label(cinfo.col,cinfo.row,value.toString());
//        lbl.setCellFormat(c.getCellFormat());
        try {
			wsheet.addCell(lbl);
		} catch (Exception e) {
			log.info("添加默认label失败,放弃");
		}
      }

    }

  }

  private class CellInfo {

    public CellInfo(int col, int row) {
      this.row = row;
      this.col = col;
    }

    public CellInfo(Cell c) {
      row = c.getRow();
      col = c.getColumn();
    }

    public CellInfo(String location){
      row=jxl.biff.CellReferenceHelper.getRow(location);
      col=jxl.biff.CellReferenceHelper.getColumn(location);
    }

    public int row;
    public int col;
  }

  public static void main(String[] args) throws Exception {
    ExcelTemplate template = new ExcelTemplate();
    template.initFromFile("c:/temp.xls");
    template.addMapTemplate("age","B2");
    template.addMapTemplate("date", "B3");
    template.addLineTemplate("date","C6");
    template.addLineTemplate("age","B6");

    Hashtable info = new Hashtable();
    info.put("name", "李海洋2");
    info.put("age", "27");
    info.put("note", "hello world");
    info.put("date", new Date());

    Hashtable line1 = new Hashtable();
    line1.put("name", "李海洋");
    line1.put("age", "27");
    line1.put("note", "hello world");
    line1.put("date", new Date());

    Hashtable line2 = new Hashtable();
    line2.put("name", "李海洋2");
    line2.put("age", "27");
    line2.put("note", "hello world");
    line2.put("date", new Date());

    Vector data=new Vector();
    data.add(line1);
    data.add(line2);


    template.setMapData(info);
    template.setTableData(data);

    template.output("c:/output2.xls");
    log.warn("111");

  }

}
