package cn.com.ragnarok.elysion.common.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import cn.com.ragnarok.elysion.common.util.ConfigLoader;

public class DBConnect2 extends DBConnect{
	
	
	public static DBConnect getInstance() {
		if (instance == null) {
			instance = new DBConnect2();
			instance.init("database2");
		}
		return instance;
	}
}
