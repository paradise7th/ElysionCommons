package cn.com.ragnarok.elysion.common.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 配合数据源的DBConnect
 * 当executeclose为true时(默认查询后关闭链接),每次SQL请求新的链接
 * 当executeclose为false时,所有SQL请求使用相同链接,需要显式关闭链接
 */
public class DSConnect extends DBConnect {
	private final static org.apache.log4j.Logger log = org.apache.log4j.LogManager
			.getLogger(DSConnect.class);
	


	 private DataSource dataSource;
	 private boolean executeclose=true;
	 
	 public DSConnect(DataSource ds){
	       this(ds,true);
	    }


    public DSConnect(DataSource ds, boolean executeclose){
        this.dataSource=ds;
        this.executeclose=executeclose;
        this.setCloseAfterExcute(executeclose);
       
    }
    
    public static DBConnect getInstance() {
    	log.warn("single mode not suppport for DSConnect..");
		return null;
	}
	
	public DataSource getDataSource(){
	     return dataSource;
    }
	

    @Override
    public Connection getDBConnect() {
        try {
             if(executeclose){
                 return dataSource.getConnection();
             }else{
                 if(m_Con==null || m_Con.isClosed()  ){
                     m_Con= dataSource.getConnection();
                 }
                 return m_Con;
             }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
       
    }

}
