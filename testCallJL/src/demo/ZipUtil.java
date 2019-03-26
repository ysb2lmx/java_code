package demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

public class ZipUtil {
	
	public static byte[] unZip(byte[] zipDate){
		byte[] b = null;
		try {
		ByteArrayInputStream bais = new ByteArrayInputStream(zipDate);
		ZipInputStream zip = new ZipInputStream(bais);
		
			while(zip.getNextEntry() != null){
				byte[] by = new byte[1024];
				int i = 1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while((i = zip.read(by,0,by.length)) != -1){
					baos.write(by,0 ,i);
				}
				b = baos.toByteArray();
				baos.flush();
				baos.close();
			}
			zip.close();
			bais.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return b;
	}

}
