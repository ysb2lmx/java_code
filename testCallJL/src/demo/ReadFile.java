package demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * 配置文件操作
 * 
 * @author LKK  17-8-11
 * 
 */
public class ReadFile {
	public static String getEstampPro(String estampKey) throws Exception {
		String willReturnStr = null;
		String file = ReadFile.class.getClassLoader().getResource("evoucher.properties").getFile();
		System.out.println("file**"+file);
		file=file.replace("%20"," ");//服务器在 带有空格路径下部署进行转yi
		Properties prop = new Properties(); 
		InputStream is = null;
		try {
			is=new FileInputStream(file);
			prop.clear();
			prop.load(is);
			Set keyValue = prop.keySet();
			for (Iterator it = keyValue.iterator(); it.hasNext();){
				String key = (String) it.next();
				if (key.equals(estampKey)){
					willReturnStr = prop.getProperty(key);
				}
//				if(prop.getProperty(key).contains(estampKey)){
//					willReturnStr = key;
//				}
			}
		} catch (FileNotFoundException e) {
			throw new Exception("找不到【"+ file + "】文件", e);
		} catch (IOException e) {
			throw new Exception("读取文件["+ file + "]失败", e);
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return willReturnStr;
	}
	
	public static Map<String,String> getAllMessage(String propertyName) {  
	    // 获得资源包  
	    ResourceBundle rb = ResourceBundle.getBundle(propertyName.trim());  
	    // 通过资源包拿到所有的key  
	    Enumeration<String> allKey = rb.getKeys();  
	    // 遍历key 得到 value  
	    Map<String,String> valList = new HashMap<String, String>();
	    while (allKey.hasMoreElements()) {  
	        String key = allKey.nextElement();  
	        String value = (String) rb.getString(key);  
	        valList.put(key, value);
	    }  
	    return valList;  
	}  
	
}
