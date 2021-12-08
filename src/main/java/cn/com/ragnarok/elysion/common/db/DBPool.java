package cn.com.ragnarok.elysion.common.db;

import java.util.Stack;
import java.sql.Connection;
import java.sql.*;
import java.util.Vector;
import java.util.*;
/**
 *
 * <p>Title: Common Tool Util</p>
 *
 * <p>Description: 数据库连接池类</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Elysion
 * @version 1.0
 */
public class DBPool {
  private final static org.apache.log4j.Logger log = org.apache.log4j.
      LogManager.getLogger(DBPool.class);
  private String dbname;
  private String driver;
  private String connString;
  private String userName;
  private String password;
  private int maxConnection;
  private int minConnection;
  private int activeConnection;
  private Vector freeConns;
  private Vector totalConns;
  private static int waitTime=3000;

  public DBPool(String name,String driverName,String connString,String userName,String password,int min,int max) {
    this.dbname=name;
    this.driver=driverName;
    this.connString=connString;
    this.userName=userName;
    this.password=password;
    this.maxConnection=max;
    this.minConnection=min;
    freeConns=new Vector();
    totalConns=new Vector();
    init();
  }



public synchronized Connection getConnection(){
  Connection conn=null;

  if(!freeConns.isEmpty()){
    conn = (Connection) freeConns.get(0);
    freeConns.remove(0);
    try {
      if (conn == null || conn.isClosed()) {
        log.debug("原连接失效,丢弃重新取得连接:" + dbname);
        conn = getConnection();
      }
    }
    catch (Exception ex) {
      log.debug("原连接失效,丢弃重新取得连接:" + dbname);
      conn = getConnection();
    }
    activeConnection++;

    //单例连接特殊处理
    if(maxConnection==1 ){
      freeConns.add(conn);
      activeConnection=1;
    }


  }else{
    if(maxConnection==1){
      conn=newConnection();
      if(conn!=null){
        freeConns.add(conn);
        activeConnection=1;
        totalConns.add(conn);
      }

    }else if(maxConnection==0 || activeConnection<maxConnection){
      conn=newConnection();
      if(conn!=null){
        activeConnection++;
        totalConns.add(conn);
      }
    }else{
      log.info("连接数达到上限:"+dbname+",等待重新取得:"+maxConnection+" wait="+waitTime);
      try {
        wait(waitTime);
      }
      catch (InterruptedException ex1) {
      }
      conn=getConnection();
    }
  }
  log.debug("取得连接:"+dbname+",当前连接数:"+activeConnection+" 最大:"+maxConnection+" 空闲:"+freeConns.size());
  return conn;
}

private Connection newConnection(){
  Connection conn=null;
   try {
     Class.forName(driver);
     conn=DriverManager.getConnection(connString,userName,password);
     log.debug("创建连接:"+dbname);
   }
   catch (Exception ex) {
     log.info("创建连接失败:"+dbname,ex);
   }
   return conn;

}

 public synchronized void freeConnection(Connection conn){
   if(conn!=null && !freeConns.contains(conn)){
     freeConns.add(conn);
     activeConnection--;
   }
   log.debug("释放连接:"+dbname+",当前连接数:"+activeConnection+" 最大:"+maxConnection+" 空闲:"+freeConns.size());
   notifyAll();
 }

 private void init(){
   if(minConnection>0 && minConnection<=maxConnection){
     log.debug("初始化最小连接数:"+dbname+" min="+minConnection);
     for (int i = 0; i < minConnection; i++) {
       Connection conn=newConnection();
       freeConns.add(conn);
       totalConns.add(conn);
     }
   }
 }

 public void closeConnections(){
   for (Iterator it = totalConns.iterator(); it.hasNext(); ) {
     Connection conn = (Connection) it.next();
    try {
      conn.close();
    }
    catch (Exception ex) {
    }
   }
 }


public int getFreeConnectionCount(){
  return freeConns.size();
}

  public String getConnString() {
    return connString;
  }

  public String getDbname() {
    return dbname;
  }

  public String getPassword() {
    return password;
  }

  public String getUserName() {
    return userName;
  }

  public int getMaxConnection() {
    return maxConnection;
  }

  public String getDriver() {
    return driver;
  }

  public int getCurrentConnection() {
    return activeConnection;
  }

  public void setConnString(String connString) {
    this.connString = connString;
  }

  public void setDbname(String dbname) {
    this.dbname = dbname;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setMaxConnection(int maxConnection) {
    this.maxConnection = maxConnection;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public void setCurrentConnection(int currentConnection) {
    this.activeConnection = currentConnection;
  }


}
