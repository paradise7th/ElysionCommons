package cn.com.ragnarok.elysion.common.db;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

public class ModelMetaData {
	
	private static Map<Class<? extends Model>,ModelMetaData> metadataMap=new HashMap<Class<? extends Model>, ModelMetaData>();
	
	
	public Map<String,String> colmapping=new LinkedHashMap<String, String>();
	public Map<String,String> idmapping=new LinkedHashMap<String, String>();
	public String tablename;
	public String insertSql="";
	public String updateSql="";
	public String deleteSql="";
	
	
	public static ModelMetaData getMetaData(Class<? extends Model> dataclass) {
		ModelMetaData meta=metadataMap.get(dataclass);
		if(meta==null) {
			meta=createMetaData(dataclass);
			metadataMap.put(dataclass, meta);
		}
		return meta;
	}
	
	
	private static ModelMetaData createMetaData(Class<? extends Model> dataclass) {
		ModelMetaData info=new ModelMetaData();
		
		
		
		Table tableinfo=(Table) dataclass.getAnnotation(Table.class);
		if(tableinfo!=null) {
			String schema=tableinfo.schema();
			String name=tableinfo.name();
			if(schema!=null && schema.length()>0){
				info.tablename=schema+"."+name;
			}else{
				info.tablename=name;				
			}
			
			
		}else {
			info.tablename=dataclass.getSimpleName();
		}
		
		StringBuilder sbinsert=new StringBuilder("insert into ")
		  .append(info.tablename)
		  .append(" (");
		
		StringBuilder sbupdate=new StringBuilder("update ")
		  .append(info.tablename)
		  .append(" set ");
		
		StringBuilder sbdelete=new StringBuilder("delete from ")
		  .append(info.tablename);
		
		StringBuilder sbwhere=new StringBuilder(" where ");
		StringBuilder sbvalues=new StringBuilder(" values (");
		  
		
		  
		  Field[] fs=dataclass.getFields();
		  for (int i = 0; i < fs.length; i++) {
			  Field f=fs[i];
			  Transient tr=f.getAnnotation(Transient.class);
			  if(tr!=null) {
				  continue;
			  }
			  
			  String fieldname=f.getName();
			  String colname=fieldname;
			  
			  Column colinfo=f.getAnnotation(Column.class);
			  if(colinfo!=null) {
				  colname=colinfo.name();
			  }
			  
			  info.colmapping.put(fieldname, colname);
			  
			  Id idinfo=f.getAnnotation(Id.class);
			  if(idinfo!=null) {
				  info.idmapping.put(fieldname, colname);
				  sbwhere.append(colname+"=? and ");
			  }else{
				  sbupdate.append(colname+"=?,");
			  }
			  
			  sbinsert.append(colname+",");
			  sbvalues.append("?,");
		  }
		  if(info.colmapping.size()>0) {
				sbinsert.deleteCharAt(sbinsert.length()-1);
				sbvalues.deleteCharAt(sbvalues.length()-1);
				sbinsert.append(")");
				sbvalues.append(")");
				
				sbinsert.append(sbvalues);
				info.insertSql=sbinsert.toString();
				
				
				sbupdate.deleteCharAt(sbupdate.length()-1);
		  }
		  if(info.idmapping.size()>0) {
			  sbwhere.delete( sbwhere.length()-4,sbwhere.length()-1);
			  sbupdate.append(sbwhere);
			  sbdelete.append(sbwhere);
			  
			  info.updateSql=sbupdate.toString();
			  info.deleteSql=sbdelete.toString();
		  }
		  
			
		  
		return info;
	}
	
	
	
	
	
	
		

}
