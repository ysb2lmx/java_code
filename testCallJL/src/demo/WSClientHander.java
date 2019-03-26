package demo;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class WSClientHander {

	/** 缓存远程调用 **/
	private static ThreadLocal<HashMap<String, Call>> threadLocal = new ThreadLocal<HashMap<String, Call>>();
	
	/**
	 * 根据传入地址调用函数
	 * 
	 * @param address
	 *            服务地址
	 * @param opName
	 *            函数名
	 * @param paras
	 *            参数
	 * @return 函数返回值
	 */
	public static Object callMethodByAddress(String address, String opName,
			Object... paras) {
//		Object retObj = null;
//		if (null == address || address.length() == 0)
//			throw new EVoucherException(ExceptionConstant.EVS902);
//		if (null == opName || opName.length() == 0)
//			throw new EVoucherException(ExceptionConstant.EVS903);
		Call call = WSClientHander.getCall(address);
//		retObj = WSClientHander.invokeMethod(call, opName, paras);
		return WSClientHander.invokeMethod(call, opName, paras);
	}
	
	/**
	 * 根据传入地址调用函数
	 * 
	 * @param address
	 *            服务地址
	 * @param opName
	 *            函数名
	 * @param paras
	 *            参数
	 * @return 函数返回值
	 */
	public static Object callMethodByAddressAndUser(String address, String opName,String userCode,String password,
			Object... paras) {
		Object retObj = null;
//		if (null == address || address.length() == 0)
//			throw new EVoucherException(ExceptionConstant.EVS902);
//		if (null == opName || opName.length() == 0)
//			throw new EVoucherException(ExceptionConstant.EVS903);
		Call call = WSClientHander.getCall(address);
		
		call.addHeader(new SOAPHeaderElement("Authorization","username",userCode));
		call.addHeader(new SOAPHeaderElement("Authorization","password",password));
		
		retObj = WSClientHander.invokeMethod(call, opName, paras);
		return retObj;
	}

	/**
	 * 反射调用
	 * 
	 * @param call
	 *            远程调用对象
	 * @param opName
	 *            函数名
	 * @param paras
	 *            参数集
	 * @return 函数函数值
	 */
	private static Object invokeMethod(Call call, String opName,
			Object... paras) {
//		System.out.println();
		
		Object retObj =  null;
		call.setOperationName(new QName("http://evoucher.com/", opName));
		Object[] objs = new Object[paras.length];
		int a = 0;
		for (Object objpara : paras) {
			objs[a] = objpara;
			a++;
		}
		//设置参数类型  
		// TODO 暂时处理此2个方法，只有此回调方法有参数值
		/*if(opName.equals("generatePdfBill")){
			call.addParameter(new QName("", "requestMessage"),XMLType.XSD_BASE64, ParameterMode.IN);
			call.setReturnType(org.apache.axis.encoding.XMLType.SOAP_LONG);
		}else if(opName.equals("generatePdfBill1")){
			call.addParameter(new QName("", "admDivCode"),XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter(new QName("", "stYear"),XMLType.XSD_INT, ParameterMode.IN);
			call.setEncodingStyle(null);
			call.addParameter(new QName("", "vtCode"),XMLType.XSD_BYTE, ParameterMode.IN);
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_ANY);
		}*/
		try {		
			retObj = call.invoke(objs);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return retObj;
	}
	
	
	

	/**
	 * 根据地址获取远程调用对象
	 * 
	 * @param address
	 *            服务地址
	 * @return 远程调用对象
	 */
	private static Call getCall(String address) {
		Call call = null;
		HashMap<String,Call> callHash = threadLocal.get();
		if(callHash == null || !callHash.containsKey(address)){
			try {
				Service service = new Service();
				call = (Call) service.createCall();
				call.setTargetEndpointAddress(address);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			if(callHash == null){
				callHash = new HashMap<String,Call>();
				threadLocal.set(callHash);
			}
			callHash.put(address, call);
			
		}else{
			call = callHash.get(address);
		}
		// 清空操作
		call.clearOperation();
		// 清空头
		call.clearHeaders();
		return call;
	}
	
}