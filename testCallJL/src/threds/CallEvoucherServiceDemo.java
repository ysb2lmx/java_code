package threds;

//import net.sf.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import demo.Arithmetic;
import demo.ReadFile;
import demo.WSClientHander;
import demo.ZipUtil;

/**
 * Created by lkk on 2017/10/11.
 */
public class CallEvoucherServiceDemo {

    public static void main(String[] args) throws IOException {
    	String ser_ctj_cloud = "http://140.210.2.29:9080/realware/services/EDocService";
//    	String ser_ctj_cloud = "http://127.0.0.1:8081/realware/services/EDocService";
    	String vtCode = "0011";
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssmmmm");
		String voucherNo = df.format(new Date());
    	String generateFile = "F:\\all_text_pdf\\generate.pdf";
    	String signatureFile = "F:\\all_text_pdf\\signature.pdf";
    	String signStampFile = "F:\\all_text_pdf\\signStamp.pdf";
    	
    	
		//  以下调用接口
    	callGeneratePdfInterface(ser_ctj_cloud,voucherNo,vtCode,generateFile); 
    	
    	callSignature(ser_ctj_cloud,voucherNo,vtCode,signatureFile);
    	
    	signStampByServer(ser_ctj_cloud,voucherNo,vtCode,signStampFile);
    	
    }
    
    private static void callGeneratePdfInterface(String serUrl,String voucherNo,String vtCode,String file){

    	 String _0011_xml  = "<?xml version=\"1.0\" encoding=\"GBK\"?><Voucher>" +
					"<tradeOrg>远大支行</tradeOrg><tradeDate>20180719</tradeDate><tradeName>取息</tradeName><flowNo>15616841654</flowNo><customName>亚历山大</customName>"+
					"<cardNo>60226245126435468435</cardNo><productName>定期</productName><valueDate>20100101</valueDate><expiryDate>20180808</expiryDate>"+
					"<payInterestPeriod>10</payInterestPeriod><rate>5</rate><cashTransFlag>现金</cashTransFlag><getInterestSum>6000</getInterestSum>"+
					"<agentName>拿破仑</agentName><agentCertType>身份证</agentCertType><agentCertNo>130316841348946548</agentCertNo><remark></remark>"+
					"<authorizeTeller>2201</authorizeTeller><handleTeller>3215</handleTeller>"
					+"</Voucher>";
    	 System.out.println("voucherNo  "+voucherNo);
    	 String xml = null;
		try {
			xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><MOF>" +
			         "<VoucherBody VoucherNo=\"" +
			         voucherNo +
			         "\"><Voucher>" +
			         Arithmetic.encode(_0011_xml)
						+ "</Voucher></VoucherBody></MOF>";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

         byte[] pdfData = (byte[])WSClientHander.callMethodByAddress(serUrl,"generatePdfBill",
        		 "test",2018,vtCode,xml);

         writeFile(pdfData,file);
         
    }
    
    private static void callSignature(String serUrl,String voucherNo,String vtCode,String file){
//    	String file = "F:\\all_text_pdf\\signature.pdf";
    	CallEvoucherServiceDemo callEvoucherServiceDemo = new CallEvoucherServiceDemo();
    	
    	String sign = callEvoucherServiceDemo.getSign();
    	byte[] pdfData = (byte[])WSClientHander.callMethodByAddress(serUrl,
                "signatureByServer"
                ,"test",2018,vtCode,voucherNo, "zkjn",sign);
    	writeFile(pdfData,file);
    	
			
    }
    
    private static void signStampByServer(String serUrl,String voucherNo,String vtCode,String file){
//    	String file = "F:\\all_text_pdf\\signStamp.pdf";
    	String stamp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MOF><Stamp No=\"qzwz\">test</Stamp></MOF>";
    	byte[] pdfData = (byte[])WSClientHander.callMethodByAddress(serUrl,
                "signStampByServer"
                ,"test",2018,vtCode,voucherNo,stamp);
    	writeFile(pdfData,file);
    }
    
    private static void writeFile(byte[] pdfData,String file){
    	
    	FileOutputStream fileOutputStream;
        ZipUtil eUtil=new ZipUtil();
		
			byte[] unZipData= eUtil.unZip(pdfData);
			try {
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(unZipData);
		         fileOutputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("文件夹不存在" + file + " cause by :"+e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("写出道文件异常 cause by:"+e.getMessage());
			}
    }
    
    public  String getSign(){
    	String file = ReadFile.class.getClassLoader().getResource("122.txt").getFile();
		StringBuilder result = new StringBuilder();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(file)));
			String s = null;
			while((s = br.readLine())!=null){
				result.append(s);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
//		signaturePic =result.toString();
    	return result.toString();
    }
}
