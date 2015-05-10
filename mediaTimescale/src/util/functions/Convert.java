/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (20/04/2004)
 */

package util.functions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Essa classe converte um mesmo dado para diferentes tipos de dados.
 */
public class Convert {
	
	/**
	 * Converte conjunto de bytes para um string.
	 * @param bytes bytes a serem convertidos
	 * @return string que representa bytes convertidos
	 */    
	public static String bytesToString (byte[] bytes) {
		StringBuffer s = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			byte b = bytes[i];
			s.append(Convert.byteToString(b));
		}				
		return s.toString();
	}
	
	/**
	 * Converte um bit recebido para um boolean.
	 * @param bit char que representa bit a ser convertido
	 * @return true se e somente se valor do parâmetro é '1'
	 */
	public static boolean bitToBoolean (char bit) {
		boolean value = false;
		
		if (bit == '1') {
			value=true;
		}
		return value;
	}
	
	/**
	 * Converte um boolean recebido para um bit.
	 * @param b boolean a ser convertido    
	 * @return '1' se e somente se b==true
	 */
	public static char booleanToBit (boolean b) {
		char value = '0';
				
		if (b == true) {
			value = '1';
		}
		return value;
	}

	/**
	 * Converte conjunto de bits recebido para um int na base binaria.
	 * @param bits string contendo conjunto de bits a ser convertido
	 * @return inteiro que representa bits na base binaria.
	 */	
	public static int bitsToInt (String bits) {
		return Integer.parseInt(bits,2);
	}

	public static long bitsToLong (String bits) {
		return Long.parseLong(bits,2);
	}	
	
	public static int bytesToInt (byte[] bytes) {
		String str = bytesToString(bytes);
		return bitsToInt(str);
	}
	
	public static String longToString(long number, int finalLength) {
		String str = Long.toString (number,2);
		int lengthStr = str.length();
		
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < finalLength - lengthStr; i++) {
			sb.append('0');
		}
		
		return sb.toString() + str;		
	}
	
	/**
	 * Converte conjunto de bits recebido para um byte na base binaria.
	 * @param bits string contendo conjunto de bits a ser convertido
	 * @return byte que representa bits na base binaria
	 */	
	public static byte bitsToByte (String bits) {
		int i = bitsToInt(bits);
		return (byte) i;
	}		

	/**
	 * Converte conjunto de bits para um bytes.
	 * @param bits bits a serem convertidos
	 * @return bytes que representa bits convertidos
	 */ 	 	
	public static byte[] bitsToBytes (String bits) {
		int totalBytes = bits.length()/8;
		byte[] bytes = new byte[totalBytes];
		for (int index = 0; index < totalBytes; index++) {
			int indexInBits = index*8;
			String bitsOfOneByte = bits.substring(indexInBits, indexInBits+8);
			bytes[index] = (byte) bitsToInt(bitsOfOneByte); 
		}
		return bytes;
	}			
	
	/**
	 * Converte inteiro em conjunto de bits que o representa na base binaria.
	 * @param i inteiro a ser convertido
	 * @return string (tamanho variavel) contendo os bits que representam o inteiro recebido na base binaria.
	 */	
	public static String intToBits (int i) {
		return Integer.toString(i,2);
	}
	
	public static byte[] intToBytes (int number, int numberOfBytes) {
		String str = intToBits(number, numberOfBytes*8);
		byte[] bytes = new byte [numberOfBytes];
		for (int i=0; i<numberOfBytes; i++) {
			String s = str.substring(i*numberOfBytes, (i+1)*8);
			bytes[i] = bitsToByte(s);
		}		
		return bytes;
	}
	
	/**
	 * Retorna uma string com os valores dos bits que compoem o byte.
	 * @param b byte a ser convertido
	 * @return uma string com os valores dos bits que compoem o byte
	 */	 
	public static String byteToString (byte b) {
		// String do byte com, no máximo, 32 dígitos.
		// Não posssui os 0's iniciais. 
		String binaryFormatLengthUp32 = Integer.toBinaryString(b);		
		int lengthUp32 = binaryFormatLengthUp32.length();
		String binaryLength8 = "";
				
		if (lengthUp32 < 8) { 
			// Nesse caso, precisa completar com '0's na frente
			for (int j = lengthUp32; j < 8; j++) {
				binaryLength8  += "0";
			}
			binaryLength8 += binaryFormatLengthUp32;
		}
		else {
			binaryLength8 = binaryFormatLengthUp32.substring(lengthUp32-8,lengthUp32);	
		}
		return binaryLength8; 	  						
	}
	
	/**
	 * Converte inteiro em conjunto de bits que o representa na base binaria.
	 * @param i inteiro a ser convertido
	 * @param length tamanho do string a ser retornado
	 * @return string contendo os bits que representam o inteiro recebido na base binaria
	 */
	public static String intToBits(int i, int length) {
		String sValue = Convert.intToBits(i);
		int lengthValue = sValue.length();

		StringBuffer allZero = new StringBuffer();
		while (lengthValue < length) {  
			allZero.append("0");
			lengthValue++;
		}		
		sValue = allZero + sValue;
		return sValue; 
	}
	
	/**
	 * Arredonda double para uma determinada precisao.
	 * @param n double a ser arredondado
	 * @param precision precisao utilizada
	 * @return double arredondado para uma determinada precisao
	 */	
	public static String roundDouble(double n, int precision) {
		DecimalFormat format = (DecimalFormat)DecimalFormat.getInstance();
		format.setMaximumFractionDigits(precision);
		DecimalFormatSymbols dfs = format.getDecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		format.setDecimalFormatSymbols(dfs);
		return format.format(n);
	}
	
	/*
	public static void main(String[] args) {
		int i = 17000;
		/*String strI = intToBits(i,16);
		String firstByte = strI.substring(0,8);
		String secondByte = strI.substring(8,16);
		byte b1 = bitsToByte(firstByte);
		byte b2 = bitsToByte(secondByte);
		byte[] b = new byte[2];
		b[0]=b1;
		b[1]=b2;
		byte[] b = intToBytes(i,2);
		int newI = bytesToInt(b);
		
	}*/
		
}
