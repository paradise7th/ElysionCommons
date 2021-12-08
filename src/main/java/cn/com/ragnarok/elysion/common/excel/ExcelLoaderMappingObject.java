package cn.com.ragnarok.elysion.common.excel;

import java.util.Hashtable;
/**
 *
 * <p>Title: Common Tool Util</p>
 *
 * <p>Description:ExcelLoader的映射接口,实现接口可以将EXCEL数据映射为对象</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: </p>
 *
 * @author 李海洋
 * @version 1.0
 */
public interface ExcelLoaderMappingObject {
    void mappingObject(Hashtable mappingrow);
}
