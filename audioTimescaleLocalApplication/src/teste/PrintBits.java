package teste;
import java.io.FileWriter;

import util.data.Bits;
import util.data.SharedBuffer;
import util.io.FileInputTools;

public class PrintBits {
	
	public static void main (String arg[]) throws Exception {
		String file = "C:\\Temp\\teste\\ronaldinhoGerado6-1.mpg";
		String fileSaida = "C:\\Temp\\teste\\ronaldinhoGerado6-1.mpg.txt";
		
		SharedBuffer buffer = new SharedBuffer(file, 10000, 0);
		
		FileInputTools fit = new FileInputTools (buffer, 1000); 
		
		//Produtor
		Thread t1 = new Thread(fit);
		t1.start();
		
		//Consumidor
		FileWriter writer = new FileWriter(fileSaida);
		byte bytes[] = new byte[1000];
		int result = 0;
		
		while (result!=-1) {
			result = buffer.read(bytes, 0, bytes.length);
			if (result !=-1) {
				
				if (result!=bytes.length) {
					byte[] newArray = new byte[result];
					for (int i=0; i<result; i++) {
						newArray[i] = bytes[i];
					}
					bytes = newArray;
				}
				
				Bits bits = new Bits (bytes);
				String strBits = bits.toString();
				writer.write(strBits);
			}
		}
		
		writer.close();
		System.out.println("FIM - gerou txt");
	}

}
