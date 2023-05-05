package cn.com.ragnarok.elysion.common.db;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Druid连接池快速查询类,使用getInstance返回一个DSconnect对象
 * 可在启动前配置属性文件,或单独产生DSconnect
 */
public class DruidConnect  {
    private static final Logger log = Logger.getLogger(DruidConnect.class);
    private static String defPropFile="./config/db.properties";
    private static DSConnect dsConnect;
    
    public static void setDefaultProperties(String prop){
        defPropFile=prop;
    }
    
    
    
    private static  DataSource getDruidDataSource(String propfile){
        try {
            InputStream is=null;
            if(new File(propfile).exists()){
                is=new FileInputStream(propfile);
            }else{
                String classpath=propfile;
                if(classpath.startsWith("..")){
                    classpath=classpath.substring(2);
                }else if(classpath.startsWith(".")){
                    classpath=classpath.substring(1);
                }
                is=DruidConnect.class.getClassLoader().getResourceAsStream(classpath);
            }
            
            
            Properties properties = new Properties();
            properties.load(is);
            return DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            log.error("get druid datasource fail:",e);
            return null;
        }
    }
    
    public static DSConnect getInstance(){
        if(dsConnect==null){
            dsConnect=new DSConnect(getDruidDataSource(defPropFile));
        }
        return dsConnect;
    }
    
    public static DSConnect createDSConnect(String propfile){
        return new DSConnect(getDruidDataSource(propfile));
    }
    
    

   
}
