package threds;

import demo.Base64_T;
import demo.ReadFile;
import demo.WSClientHander;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * Created by lkk on 2017/10/18.
 */
    public class TestServiceImpl {
    	 private ExecutorService executor = null;
    	public TestServiceImpl(int pools){
    		executor = Executors.newFixedThreadPool(pools);
    	}

    private static int time;

    private static  Map<String,String> map ;

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        int pools =5;
        try {
        	map = ReadFile.getAllMessage("estamp");
            time = Integer.parseInt(map.get("times"));
            pools = Integer.parseInt(map.get("threadTool"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        	System.out.println("服务地址=="+map.get("asspEStampUrl"));
        	System.out.println("pdf文件=="+map.get("pdfFile"));
        	System.out.println("签章位置=="+map.get("locDTOs"));
        	System.out.println("印章别名=="+map.get("stampId"));
        	System.out.println("柜员号=="+map.get("certID"));
        	System.out.println("生成pdf个数=="+map.get("times"));
        	System.out.println("输出文件位置=="+map.get("outFile"));
        	System.out.println("接口名=="+map.get("interfaceName"));
        	System.out.println("线程数=="+map.get("threadTool"));
        	String istt  = "不使用";
        	if ("1".equals(istt))
        		istt = "使用";
        	System.out.println(istt +"  SURE签名服务器");
        	
        
        
        TestServiceImpl testService = new TestServiceImpl(pools);
        testService.addList();

        long end = System.currentTimeMillis();

        System.out.println("共用时："+(end-start));

    }


    public void addList() {
        try {

            int times = time;
            CountDownLatch countDownLatch = new CountDownLatch(times);
            for (int a = 0 ; a < times ; a++){
                executor.execute(new addListRunnable(countDownLatch,a));//调用业务逻辑
            }
            countDownLatch.await();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            executor.shutdown();
        }

        }

        private class addListRunnable implements Runnable{

            private CountDownLatch countDownLatch;
            int a;

            public addListRunnable( CountDownLatch countDownLatch,int a){
                super();
                this.countDownLatch = countDownLatch;
                this.a = a;
            }


            @Override
            public void run() {
                String asspEStampUrl = null;
                String pdfFile =null;
                String locDTOs = null;
                String stampId = null;
                String certID = null;
                byte[] pdfByte = null;
                String outFile = null;
                String interfaceName = null;
                String isTest = null;
                try {
                    asspEStampUrl = map.get("asspEStampUrl");
                    pdfFile = map.get("pdfFile");
                    locDTOs = map.get("locDTOs");
//                    if ("0".equals(locDTOs))
//                    	locDTOs = "电子签章";
//                    System.out.println("locDTOs"+locDTOs);
                    stampId = map.get("stampId");
                    certID = map.get("certID");
                    outFile = map.get("outFile");
                    isTest = map.get("isTest");
                    if ("0".equals(isTest))
                    	isTest = "";
                    interfaceName = map.get("interfaceName");

                    File fileIn=new File(pdfFile);
                    long fileSize=fileIn.length();
                    FileInputStream fileInputStream=new FileInputStream(fileIn);
                    byte[] buffer=new byte[(int)fileSize];
                    int offset=0;
                    int numRead=0;
                    while(offset<buffer.length && (numRead=fileInputStream.read(buffer,offset,buffer.length-offset))>=0){
                        offset+=numRead;
                    }
                    fileInputStream.close();
                    pdfByte= Base64_T.encode(buffer);

                    System.out.println("当前线程:"+Thread.currentThread().getId());
                    // CSS_Java_signStampByServerPdfWithStrInfo   CSS_Java_signStampByServerMutipagePdf  CSS_Java_signStampForAttachs   CSS_Java_signStampByServerPdfForBank
//                    byte[] pdfData = (byte[])  WSClientHander.callMethodByAddress(asspEStampUrl, interfaceName,
//                            pdfByte, stampId,locDTOs,isTest,certID,13);
                    byte[] pdfData = (byte[])  WSClientHander.callMethodByAddress(asspEStampUrl, "CSS_Java_signStampByServerPdfForBank",
                            pdfByte, stampId,locDTOs,certID,"ASDF");
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssmmmm");
                    String voucherNo = df.format(new Date());

                    String aaaaaaaaaa= outFile+"/" + voucherNo+Thread.currentThread().getId()+"_"+a+"_.pdf";
                    File file=new File(aaaaaaaaaa);
                    File outFiles=new File(outFile);
                    if (outFiles.exists()){
                        FileOutputStream fileOutputStream=new FileOutputStream(file);
                        fileOutputStream.write(Base64_T.decode(pdfData));
                        fileOutputStream.close();
                    }else{
                        System.out.println("文件夹不存在"+outFile);
                    }

                } catch (Exception e) {
                    System.out.println("卧槽出错了##:"+e.getMessage());
                }finally {
                    countDownLatch.countDown();//完成�?��操作，计数减�?
                }
            }

        }
        
    }

