package cn.com.ragnarok.elysion.common.db;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import java.net.URL;

/**
 * ConnectionManager v1.0 by elysion
 * 数据库连接管理器,读取config/db.xml
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author 李海洋
 * @version 1.0
 */
public class ConnectionManager {
  private final static org.apache.log4j.Logger log = org.apache.log4j.
      LogManager.getLogger(ConnectionManager.class);
  private String defaultFile = "./config/db.xml";
  private String defaultClassFile = "/config/db.xml";
  private String configKey = "ConnectionManager.config";
  private static ConnectionManager instance;
  private final String CONN_NODE = "connection";
  private final String DRIVER_NODE = "driver";
  private final String NAME_NODE = "name";
  private final String URL_NODE = "url";
  private final String USER_NODE = "user";
  private final String PASS_NODE = "password";
  private final String MAX_CONN_NODE = "max";
  private final String MIN_CONN_NODE = "min";
  private final int retryTime = 200;

  private HashMap infoMap;

  private ConnectionManager() {
    infoMap = new HashMap();

    Properties p = System.getProperties();
    String filename = p.getProperty(configKey);
    if (filename != null) {
      log.debug("读取参数配置文件:" + filename);
      loadInfoFromXml(getInputStream(filename));
    }
    else if (new File(defaultFile).exists()) {
      log.debug("读取默认配置文件:" + defaultFile);
      loadInfoFromXml(getInputStream(defaultFile));
    }
    else {
      URL url = getClass().getResource(defaultClassFile);
//      URL url = ClassLoader.getSystemResource(defaultClassFile);
      if (url != null) {
        log.debug("读取类库配置文件:" + defaultClassFile);
        loadInfoFromXml(getInputStream(url));
      }
      else {
        log.error("未找到数据库配置文件");
      }
    }

  }


  private InputStream getInputStream(String filename){
   try {
     return new FileInputStream(filename);
   }
   catch (Exception ex) {
     log.info("读取配置文件失败:"+filename);
     return null;
   }
 }

 private InputStream getInputStream(URL url){
   try {
     return url.openStream();
   }
   catch (Exception ex) {
     log.info("读取配置文件失败:"+url);
     return null;
   }
 }


  /**
   * 取得单例<br>
   * 读取系统参数"ConnectionManager.config"作为设置文件路径,默认为"./config/db.xml"<br>
   * 设置文件格式为<br>
   * &lt;CONNECTIONS&gt;<br>
   * &nbsp;&lt;connection&gt;<br>
   * &nbsp;&nbsp;&lt;name&gt;连接名&lt;/name&gt;<br>
   * &nbsp;&nbsp;&lt;driver&gt;驱动&lt;/driver&gt;<br>
   * &nbsp;&nbsp;&lt;url&gt;连接字符串&lt;/url&gt;<br>
   * &nbsp;&nbsp;&lt;user&gt;用户&lt;/user&gt;<br>
   * &nbsp;&nbsp;&lt;password&gt;密码&lt;/password&gt;<br>
   * &nbsp;&nbsp;&lt;max&gt;最大连接数&lt;/max&gt;<br>
   * &nbsp;&lt;/connection&gt;<br>
   * &lt;/CONNECTIONS&gt;<br>
   * @return ConnectionManager
   */

  public synchronized static ConnectionManager getInstance() {
    if (instance == null) {
      instance = new ConnectionManager();
    }
    return instance;
  }

  /**
   * 取得连接
   * @param dbname String 设置中的连接名
   * @return Connection
   */
  public synchronized Connection getConnection(String dbname) {
    DBPool pool = (DBPool) infoMap.get(dbname);
    if (pool != null) {
      return pool.getConnection();
    }
    else {
      log.error("数据库配置不存在:" + dbname);
      return null;
    }
  }

  public void freeConnection(String key, Connection conn) {
    DBPool pool = (DBPool) infoMap.get(key);
    if (pool != null) {
      pool.freeConnection(conn);
    }

  }

  public void closeConnection(String key) {
    DBPool pool = (DBPool) infoMap.get(key);
    if (pool != null) {
      pool.closeConnections();
      log.debug("关闭连接池连接:" + key);
    }

  }

  /**
   * 关闭所有连接
   */
  public void closeAllConnection() {
    for (Iterator it = infoMap.keySet().iterator(); it.hasNext(); ) {
      String key = (String) it.next();
      DBPool pool = (DBPool) infoMap.get(key);
      pool.closeConnections();
    }

    log.debug("所有连接已关闭");

  }

  /**
   * loadInfoFromXml
   * 从文件载入数据库设置
   */
  private void loadInfoFromXml(InputStream in) {
    log.info("读取数据库配置文件...");
    DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    Document doc = null;
    try {
      db = fac.newDocumentBuilder();
      doc = db.parse(in);

      Element root = doc.getDocumentElement();
      NodeList nodes = root.getElementsByTagName("connection");
      for (int i = 0; i < nodes.getLength(); i++) {
        Element e = (Element) nodes.item(i);
        String name = getInnerText(e.getElementsByTagName(NAME_NODE).item(0));
        String driver = getInnerText(e.getElementsByTagName(DRIVER_NODE).item(0));
        String url = getInnerText(e.getElementsByTagName(URL_NODE).item(0));
        String user = getInnerText(e.getElementsByTagName(USER_NODE).item(0));
        String pass = getInnerText(e.getElementsByTagName(PASS_NODE).item(0));
        String max = "1";
        String min = "0";
        NodeList nlist_max = e.getElementsByTagName(MAX_CONN_NODE);
        NodeList nlist_min = e.getElementsByTagName(MIN_CONN_NODE);
        if (nlist_max.getLength() > 0) {
          max = getInnerText(nlist_max.item(0));
        }
        if (nlist_min.getLength() > 0) {
          min = getInnerText(nlist_min.item(0));
        }
        int maxValue = Integer.parseInt(max);
        int minValue = Integer.parseInt(min);

        DBPool info = new DBPool(name, driver, url, user, pass, minValue,
                                 maxValue);
        infoMap.put(name, info);
        log.info("***********数据库配置(" + i + ")************");
        log.info("DBname: " + name);
        log.info("driver: " + driver);
        log.info("url: " + url);
        log.debug("user: " + user);
        log.debug("password: " + pass);
        log.info("minConnection: " + min);
        log.info("maxConnection: " + max);
        log.info("**********************************");
      }
    }catch (Exception e) {
     log.error("读取数据库配置失败", e);
     return;
   }finally{
    try {
      in.close();
    }
    catch (Exception ex) {
    }
   }

  }

  /**
   * 取得NODE字段
   * @param e Node
   * @return String
   */
  private String getInnerText(Node e) {
    if (e != null) {
      Node node = e.getFirstChild();
      if (node != null && node instanceof Text) {
        return ( (Text) node).getData();
      }
    }
    return "";
  }

}
