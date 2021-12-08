package cn.com.ragnarok.elysion.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ConfigLoader {
  private final static org.apache.log4j.Logger log = org.apache.log4j.
      LogManager.getLogger(ConfigLoader.class);
  private static ConfigLoader instance;

  private Hashtable topicHash;
  private String configINI = "./config/config.ini";
  private String configXML = "./config/config.xml";
  private String configClassINI = "/config/config.ini";
  private String configClassXML = "/config/config.xml";

  private ConfigLoader() {
    topicHash = new Hashtable();
    loadConfig();
  }

  public synchronized static ConfigLoader getInstance() {
    if (instance == null) {
      instance = new ConfigLoader();
    }
    return instance;
  }

  private InputStream getInputStream(String filename){
    try {
      return new FileInputStream(filename);
    }
    catch (Exception ex) {
      log.error("读取配置文件失败:"+filename);
      return null;
    }
  }

  private InputStream getInputStream(URL url){
    try {
      return url.openStream();
    }
    catch (Exception ex) {
      log.error("读取配置文件失败:"+url);
      return null;
    }
  }


  /**
   * loadConfig
   */
  private void loadConfig() {
    Properties prop = System.getProperties();
    String iniPath = prop.getProperty("ConfigLoader.ini");
    String xmlPath = prop.getProperty("ConfigLoader.xml");

    if (xmlPath != null) {
      log.debug("读取参数配置XML:"+xmlPath);
      loadXML(getInputStream(xmlPath));
    }
    else if (iniPath != null) {
      log.debug("读取参数配置INI:"+iniPath);
      loadINI(getInputStream(iniPath));
    }
    else if (new File(configXML).exists()) {
      log.debug("读取默认配置XML:"+configXML);
      loadXML(getInputStream(configXML));
    }
    else if (new File(configINI).exists()) {
      log.debug("读取默认配置INI:"+configINI);
      loadINI(getInputStream(configINI));
    }
    else {
      URL xml = getClass().getResource(configClassXML);
      URL ini = getClass().getResource(configClassINI);
      if (xml != null) {
        log.debug("读取类路径配置XML:"+configClassXML);
        loadXML(getInputStream(xml));
      }
      else if (ini != null) {
        log.debug("读取类路径配置INI:"+configClassINI);
        loadINI(getInputStream(ini));
      }
      else {
        log.info("配置文件不存在,无法载入配置");
      }
    }

  }

//  /**
//   * loadINI
//   *
//   * @param configINI String
//   */
//  private void loadINI(String iniPath) {
//    log.debug("开始载入INI配置:" + iniPath);
//    BufferedReader br = null;
//    try {
//      br = new BufferedReader(new FileReader(iniPath));
//      String line = null;
//      Hashtable option = null;
//      while ( (line = br.readLine()) != null) {
//        line = line.trim();
//        if (line.length() == 0 || line.startsWith(";") || line.startsWith("#") ||
//            line.startsWith("//") ) {
//          continue;
//        }
//        if (line.startsWith("[")) {
//          int end = line.indexOf("]");
//          String key = line.substring(1, end);
//          option = new Hashtable();
//          topicHash.put(key, option);
//        }
//        else if (line.indexOf("=") != -1 && option != null) {
//          int index=line.indexOf("=");
//          String key=line.substring(0,index);
//          String value=line.substring(index+1);
//          if(option!=null){
//            option.put(key, value);
//          }
//        }
//
//      }
//    }
//    catch (FileNotFoundException ex) {
//      log.info("未找到配置文件:" + iniPath);
//    }
//    catch (Exception ex) {
//      log.info("读取配置文件出错:" + iniPath, ex);
//    }
//    finally {
//      if (br != null) {
//        try {
//          br.close();
//        }
//        catch (IOException ex1) {
//        }
//      }
//    }
//
//  }

  private void loadINI(InputStream in) {

    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(in,"utf-8"));
      String line = null;
      LinkedHashMap option = null;
      while ( (line = br.readLine()) != null) {
        line = line.trim();
        if (line.length() == 0 || line.startsWith(";") || line.startsWith("#") ||
            line.startsWith("//") ) {
          continue;
        }
        if (line.startsWith("[")) {
          int end = line.indexOf("]");
          String key = line.substring(1, end);
          option = new LinkedHashMap();
          topicHash.put(key, option);
        }
        else if (line.indexOf("=") != -1 && option != null) {
          int index=line.indexOf("=");
          String key=line.substring(0,index);
          String value=line.substring(index+1);
          if(option!=null){
            option.put(key, value);
          }
        }

      }
    }
    catch (Exception ex) {
      log.error("读取INI配置出错" , ex);
    }
    finally {
        try {
          br.close();
        }
        catch (Exception ex1) {
        }
    }

  }


//  /**
//   * loadXML
//   *
//   * @param xmlPath String
//   */
//  private void loadXML(String xmlPath) {
//    log.debug("开始载入XML配置:" + xmlPath);
//    DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
//    fac.setIgnoringElementContentWhitespace(true);
//    Document doc = null;
//    try {
//      DocumentBuilder builder = fac.newDocumentBuilder();
//      doc = builder.parse(xmlPath);
//    }
//    catch (IOException ex) {
//      log.info("读取配置文件错误:" + xmlPath, ex);
//    }
//    catch (Exception ex) {
//      log.info("解析XML配置错误:" + xmlPath, ex);
//    }
//
//    Node root = doc.getDocumentElement();
//    NodeList options = root.getChildNodes();
//    for (int i = 0; i < options.getLength(); i++) {
//      Node n = options.item(i);
//      if (n instanceof Element) {
//        Element e = (Element) n;
//        String name = e.getTagName();
//        Hashtable hash = new Hashtable();
//        NodeList list = e.getChildNodes();
//        for (int j = 0; j < list.getLength(); j++) {
//          Node n2 = list.item(i);
//          if (n2 instanceof Element) {
//            Element e2 = (Element) n2;
//            String key = e2.getTagName();
//            String value = getTagBody(e2);
//            hash.put(key, value);
//          }
//        }
//        topicHash.put(name, hash);
//      }
//    }
//  }

  private void loadXML(InputStream in) {
    DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
    fac.setIgnoringElementContentWhitespace(true);
    Document doc = null;
    try {
      DocumentBuilder builder = fac.newDocumentBuilder();
      doc = builder.parse(in);

      Node root = doc.getDocumentElement();
      NodeList options = root.getChildNodes();
      for (int i = 0; i < options.getLength(); i++) {
        Node n = options.item(i);
        if (n instanceof Element) {
          Element e = (Element) n;
          String name = e.getTagName();
          LinkedHashMap hash = new LinkedHashMap();
          NodeList list = e.getElementsByTagName("option");
          for (int j = 0; j < list.getLength(); j++) {
            Node n2 = list.item(j);
            if (n2 instanceof Element) {
              Element e2 = (Element) n2;
              String key=e2.getAttribute("key");
              String value = getTagBody(e2);
              if(key!=null){
                hash.put(key, value);
              }
            }
          }
          topicHash.put(name, hash);
        }
      }
    } catch (Exception ex) {
      log.error("读取XML配置出错", ex);
    }finally{
      try {
        in.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  private String getTagBody(Element e) {
    String value = "";
    if (e.getChildNodes().getLength() != 0) {
      Node n = e.getChildNodes().item(0);
      if (n instanceof Text) {
        value = n.getNodeValue();
      }
    }
    return value;
  }

  public String getValue(String option, String key, String def) {
	 LinkedHashMap hash = (LinkedHashMap) topicHash.get(option);
    if (hash != null) {
      String value = (String) hash.get(key);
      if (value != null) {
        return value;
      }
    }
    return def;
  }

  public String getValue(String option, String key) {
    return getValue(option, key, null);
  }

  public int getIntValue(String option,String key,int def){
    try{
      return Integer.parseInt(getValue(option,key));
    }catch(Exception e){
      return def;
    }

  }

  public long getLongValue(String option,String key,long def){
    try{
      return Long.parseLong(getValue(option,key));
    }catch(Exception e){
      return def;
    }

  }

  public double getDoubleValue(String option,String key,double def){
    try{
      return Double.parseDouble(getValue(option,key));
    }catch(Exception e){
      return def;
    }

  }



  public Set getOptionKeys(String option) {
	  LinkedHashMap hash = (LinkedHashMap) topicHash.get(option);
    if (hash != null) {
      return hash.keySet();
    }
    else {
      return null;
    }

  }
  
  public Map getOptionMapping(String option){
	  LinkedHashMap hash = (LinkedHashMap) topicHash.get(option);
    if(hash!=null){
      return (Map)hash.clone();
    }else{
      return hash;
    }
  }
  
  
  






  public static void main(String[] args) {
    ConfigLoader c = ConfigLoader.getInstance();
    String s = c.getValue("main", "test");
    System.out.println(s);
    Map m=c.getOptionMapping("main");
    System.out.println(m);
    

  }
}
