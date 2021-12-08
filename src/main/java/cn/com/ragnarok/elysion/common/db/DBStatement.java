package cn.com.ragnarok.elysion.common.db;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * <p>Title: Common Tool Util</p>
 *
 * <p>Description:数据库语句操作,整合了PrepareStatement和ResultSet </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Elysion
 * @version 1.0
 */
public class DBStatement {
  private final static org.apache.log4j.Logger log = org.apache.log4j.
      LogManager.getLogger(DBStatement.class);
  private Connection conn;
  private Statement st;
  private PreparedStatement pst;
  private ResultSet rs;
  private int columnCase=0;
  
  public static final int UPPER_CASE=1;
  public static final int LOWER_CASE=-1;
  public static final int DEFAULT_CASE=0;
  

  public DBStatement(Connection conn) {
    this.conn = conn;
  }

  public DBStatement(Connection conn,int columnCase) {
    this.conn = conn;
    this.columnCase=columnCase;
  }

  public void setColumnCase(int b){
    this.columnCase=b;
  }

  
  private String formatColumn(String col){
	  if(col==null)return null;
	  switch (columnCase) {
		case UPPER_CASE:
			return col.toUpperCase();
		case LOWER_CASE:
			return col.toLowerCase();
		default:
			return col;
	}
  }


  /**
   * SQL更新
   * @param sql String
   * @return int
   * @throws SQLException
   */
  public int updateSQL(String sql) throws SQLException {
    st = conn.createStatement();
    log.debug("update: "+sql);
    int i = st.executeUpdate(sql);
    close();
    log.debug("result: "+i);
    return i;
  }
  /**
   * SQL查询
   * @param sql String
   * @throws SQLException
   */
  public void querySql(String sql) throws SQLException {
    st = conn.createStatement();
    log.debug("query: "+sql);
    rs = st.executeQuery(sql);
  }
  /**
   * 预备查询
   * @param sql String
   * @throws SQLException
   */
  public void prepare(String sql) throws SQLException {
    log.debug("prepare: "+sql);
    pst = conn.prepareStatement(sql);
  }
  /**
   * 预备查询,带参数组
   * @param sql String
   * @param params Object[]
   * @throws SQLException
   */
  public void prepare(String sql,Object... params) throws SQLException {
    log.debug("prepare: "+sql);
    pst=conn.prepareStatement(sql);
    if(params==null) return;
    for (int i = 0; i < params.length; i++) {
      pst.setObject(i+1,params[i]);
    }
  }
  /**
   * 预备查询更新
   * @return int
   * @throws SQLException
   */
  public int update() throws SQLException {
    return pst.executeUpdate();
  }
  /**
   * 预备查询执行
   * @throws SQLException
   */
  public void query() throws SQLException {
    rs=pst.executeQuery();
  }

  /**
   * 关闭语句
   */
  public void close() {
    if (rs != null) {
      try {
        rs.close();
      }
      catch (Exception ex) {
        log.debug("关闭数据集失败", ex);
      }
    }
    if (st != null) {
      try {
        st.close();
      }
      catch (Exception ex1) {
        log.debug("关闭语句失败", ex1);
      }
    }
    if (pst != null) {
      try {
        pst.close();
      }
      catch (Exception ex2) {
        log.debug("关闭预备语句失败", ex2);
      }
    }

  }
  /**
   * 关闭数据集
   */
  public void closeResultSet() {
    if (rs != null) {
      try {
        rs.close();
      }
      catch (SQLException ex) {
        log.debug("关闭数据集失败", ex);
      }
    }
  }
  /**
   * 查询SQL,返回字符串结果的Vector,每行一个Hashtable,包含列名和数据,数据为字符串
   * @param sql String
   * @return Vector
   * @throws SQLException
   */
  public List<Map<String,String>> queryStringSql(String sql) throws SQLException {
    this.querySql(sql);
    List<Map<String, String>> v=listString();
    close();
    return v;
  }
  /**
   * 预备查询SQL,返回字符串结果的Vector,每行一个Hashtable,包含列名和数据,数据为字符串
   * @return Vector
   * @throws SQLException
   */
  public List<Map<String,String>> queryString() throws SQLException {
    this.query();
    List<Map<String, String>> v=listString();
    return v;
  }
  /**
   * 查询SQL,返回OBJECT类型的Vector,每行一个Hashtable,包含列名和数据,数据为各自对应对象类型
   * @param sql String
   * @return Vector
   * @throws SQLException
   */
  public List<Map<String,Object>> queryObjectSql(String sql) throws SQLException {
  this.querySql(sql);
  Vector v=listAllObject();
  return v;
}
/**
 * 预备查询SQL,返回OBJECT类型的Vector,每行一个Hashtable,包含列名和数据,数据为各自对应对象类型
 * @return Vector
 * @throws SQLException
 */
public List<Map<String,Object>> queryObject() throws SQLException {
  this.query();
  Vector v=listAllObject();
  return v;
  }
  /**
   * 取得第一个结果
   * @return Object
   * @throws SQLException
   */
  public Object queryScale() throws SQLException {
  this.query();
  try {
    if (rs.next()) {
      return rs.getObject(1);
    }
    else {
      return null;
    }
  }
  catch (SQLException ex) {
    throw ex;
  }finally{
    close();
  }

}
/**
 * 取得第一个结果
 * @param sql String
 * @return Object
 * @throws SQLException
 */
public Object queryScaleSql(String sql) throws SQLException {
  this.querySql(sql);
 try {
   if (rs.next()) {
     return rs.getObject(1);
   }
   else {
     return null;
   }
 }
 catch (SQLException ex) {
   throw ex;
 }finally{
   close();
 }

}



  private List<Map<String,String>> listString() throws SQLException {
    Vector v = new Vector();
    ResultSetMetaData rsmt = rs.getMetaData();
    int count = rsmt.getColumnCount();
    String[] cols = new String[count];
    for (int i = 0; i < cols.length; i++) {
      cols[i] = rsmt.getColumnLabel(i+1);
    }
    while (rs.next()) {
      Hashtable hash = new Hashtable();
      for (int i = 0; i < cols.length; i++) {
        String s = rs.getString(i+1);
        if (s == null) {
          s = "";
        }
        String key=cols[i];
        key=formatColumn(key);
        hash.put(key, s);
      }
      v.add(hash);
    }
    close();
    return v;

  }

  private Vector listAllObject() throws SQLException {
   Vector v = new Vector();
   ResultSetMetaData rsmt = rs.getMetaData();
   int count = rsmt.getColumnCount();
   String[] cols = new String[count];
   for (int i = 0; i < cols.length; i++) {
     cols[i] = rsmt.getColumnLabel(i+1);
   }
   while (rs.next()) {
     Hashtable hash = new Hashtable();
     for (int i = 0; i < cols.length; i++) {
       Object o=rs.getObject(i+1);
       if(o==null){
         o="";
       }
       String key=cols[i];
       key=formatColumn(key);
       hash.put(key, o);

     }
     v.add(hash);
   }
   close();
   return v;
 }


//useful
 /**
  * 查询下一个
  * @return boolean
  * @throws SQLException
  */
 public boolean next() throws SQLException {
    return rs.next();
  }
  /**
   * 取得当前结果集
   * @return ResultSet
   */
  public ResultSet getResultSet() {
    return rs;
  }
  /**
   * 取得结果集信息
   * @return ResultSetMetaData
   * @throws SQLException
   */
  public ResultSetMetaData getRSMetaData() throws SQLException {
    return rs.getMetaData();
  }
  /**
   * 取得当前位置
   * @return int
   * @throws SQLException
   */
  public int rowIndex() throws SQLException {
    return rs.getRow();
  }
  /**
   * 取得列编号
   * @param col String
   * @return int
   * @throws SQLException
   */
  public int getColumnIndex(String col) throws SQLException {
    return rs.findColumn(col);
  }



//PST
  public void setString(int i, String s) throws SQLException {
    pst.setString(i, s);
  }

  public void setInt(int i, int s) throws SQLException {
    pst.setInt(i, s);
  }

  public void setDate(int i, Date s) throws SQLException {
    pst.setDate(i, s);
  }

  public void setLong(int i, long s) throws SQLException {
    pst.setLong(i, s);
  }

  public void setFloat(int i, float s) throws SQLException {
    pst.setFloat(i, s);
  }

  public void setShort(int i, short s) throws SQLException {
    pst.setShort(i, s);
  }

  public void setDouble(int i, double s) throws SQLException {
    pst.setDouble(i, s);
  }

  public void setArray(int i, Array arr) throws SQLException {
    pst.setArray(i, arr);
  }

  public void setAsciiStream(int i, InputStream x, int length) throws
      SQLException {
    pst.setAsciiStream(i, x, length);
  }

  public void setBigDecimal(int i, BigDecimal x) throws SQLException {
    pst.setBigDecimal(i, x);
  }

  public void setBinaryStream(int i, InputStream x, int length) throws
      SQLException {
    pst.setBinaryStream(i, x, length);
  }

  public void setBlob(int i, Blob x) throws SQLException {
    pst.setBlob(i, x);
  }

  public void setByte(int i, byte x) throws SQLException {
    pst.setByte(i, x);
  }

  public void setBytes(int i, byte[] x) throws SQLException {
    pst.setBytes(i, x);
  }

  public void setCharacterStream(int i, Reader reader, int length) throws
      SQLException {
    pst.setCharacterStream(i, reader, length);
  }

  public void setClob(int i, Clob x) throws SQLException {
    pst.setClob(i, x);
  }

  public void setNull(int i, int sqlType) throws SQLException {
    pst.setNull(i, sqlType);
  }

  public void setObject(int i, Object x) throws SQLException {
    pst.setObject(i, x);
  }

  public void setObject(int i, Object x, int targetSqlType) throws SQLException {
    pst.setObject(i, x, targetSqlType);
  }

  public void setRef(int i, Ref x) throws SQLException {
    pst.setRef(i, x);
  }

  public void setTime(int i, Time x) throws SQLException {
    pst.setTime(i, x);
  }

  public void setTimestamp(int i, Timestamp x) throws SQLException {
    pst.setTimestamp(i, x);
  }

  public void setURL(int i, URL x) throws SQLException {
    pst.setURL(i, x);
  }

//RS
  public String getString(int i) throws SQLException {
    return rs.getString(i);
  }

  public String getString(String s) throws SQLException {
    return rs.getString(s);
  }

  public Array getArray(int i) throws SQLException {
    return rs.getArray(i);
  }

  public InputStream getAsciiStream(int i) throws SQLException {
    return rs.getAsciiStream(i);
  }

  public BigDecimal getBigDecimal(int i) throws SQLException {
    return rs.getBigDecimal(i);
  }

  public InputStream getBinaryStream(int i) throws SQLException {
    return rs.getBinaryStream(i);
  }

  public Blob getBlob(int i) throws SQLException {
    return rs.getBlob(i);
  }

  public boolean getBoolean(int i) throws SQLException {
    return rs.getBoolean(i);
  }

  public byte getByte(int i) throws SQLException {
    return rs.getByte(i);
  }

  public byte[] getBytes(int i) throws SQLException {
    return rs.getBytes(i);
  }

  public Reader getCharacterStream(int i) throws SQLException {
    return rs.getCharacterStream(i);
  }

  public Date getDate(int i) throws SQLException {
    return rs.getDate(i);
  }

  public double getDouble(int i) throws SQLException {
    return rs.getDouble(i);
  }

  public float getFloat(int i) throws SQLException {
    return rs.getFloat(i);
  }

  public int getInt(int i) throws SQLException {
    return rs.getInt(i);
  }

  public long getLong(int i) throws SQLException {
    return rs.getLong(i);
  }

  public Object getObject(int i) throws SQLException {
    return rs.getObject(i);
  }

  public Ref getRef(int i) throws SQLException {
    return rs.getRef(i);
  }

  public short getShort(int i) throws SQLException {
    return rs.getShort(i);
  }

  public Time getTime(int i) throws SQLException {
    return rs.getTime(i);
  }

  public Timestamp getTimestamp(int i) throws SQLException {
    return rs.getTimestamp(i);
  }

  public URL getURL(int i) throws SQLException {
    return rs.getURL(i);
  }

  //RS2

  public Array getArray(String i) throws SQLException {
    return rs.getArray(i);
  }

  public InputStream getAsciiStream(String i) throws SQLException {
    return rs.getAsciiStream(i);
  }

  public BigDecimal getBigDecimal(String i) throws SQLException {
    return rs.getBigDecimal(i);
  }

  public InputStream getBinaryStream(String i) throws SQLException {
    return rs.getBinaryStream(i);
  }

  public Blob getBlob(String i) throws SQLException {
    return rs.getBlob(i);
  }

  public boolean getBoolean(String i) throws SQLException {
    return rs.getBoolean(i);
  }

  public byte getByte(String i) throws SQLException {
    return rs.getByte(i);
  }

  public byte[] getBytes(String i) throws SQLException {
    return rs.getBytes(i);
  }

  public Reader getCharacterStream(String i) throws SQLException {
    return rs.getCharacterStream(i);
  }

  public Date getDate(String i) throws SQLException {
    return rs.getDate(i);
  }

  public double getDouble(String i) throws SQLException {
    return rs.getDouble(i);
  }

  public float getFloat(String i) throws SQLException {
    return rs.getFloat(i);
  }

  public int getInt(String i) throws SQLException {
    return rs.getInt(i);
  }

  public long getLong(String i) throws SQLException {
    return rs.getLong(i);
  }

  public Object getObject(String i) throws SQLException {
    return rs.getObject(i);
  }

  public Ref getRef(String i) throws SQLException {
    return rs.getRef(i);
  }

  public short getShort(String i) throws SQLException {
    return rs.getShort(i);
  }

  public Time getTime(String i) throws SQLException {
    return rs.getTime(i);
  }

  public Timestamp getTimestamp(String i) throws SQLException {
    return rs.getTimestamp(i);
  }

  public URL getURL(String i) throws SQLException {
    return rs.getURL(i);
  }



}
