package cn.com.ragnarok.elysion.common.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DSConnect extends DBConnect {
	private final static org.apache.log4j.Logger log = org.apache.log4j.LogManager
			.getLogger(DSConnect.class);
	


	 private DataSource dataSource;
	 
	 public DSConnect(DataSource ds){
	       this(ds,true);
	    }


    public DSConnect(DataSource ds, boolean executeclose){
        this.dataSource=ds;
        this.setCloseAfterExcute(executeclose);
       
    }
    
    public static DBConnect getInstance() {
    	log.warn("single mode not suppport for DSConnect..");
		return null;
	}
	

    @Override
    public Connection getDBConnect() {
        try {
            if(m_Con==null || m_Con.isClosed()  ){
                m_Con= dataSource.getConnection();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return m_Con;
    }

}
