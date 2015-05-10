/*
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (18/02/2005)
 */
 
package util.functions;

/**
 * Essa classe disponibiliza funcoes de porposito geral.
 */
public class Functions {
	
	/**
	 * Cria copia de um array de caracteres.
	 * @param m array a ser copiado
	 * @return copia do array
	 */
    public static char[] copyParameters (char m[]) {
		int dimension = m.length;
		char newM[] = new char [dimension];
		for (int i=0; i<dimension; i++) {
			newM[i] = m[i];
		}
		return newM;
	}

	/**
	 * Cria copia de um array de bytes.
	 * @param m array a ser copiado
	 * @return copia do array
	 */
    public static byte[] copyParameters (byte m[]) {
		byte newM[] = null;
		if (m!=null) {
			int dimension = m.length;
			newM = new byte [dimension];
			for (int i=0; i<dimension; i++) {
				newM[i] = m[i];
			}
		}
		return newM;
	}
    
    public static byte[] copyParameters (byte m[], int total) {
		byte newM[] = null;
		if (m!=null) {
			newM = new byte [total];
			for (int i=0; i<total; i++) {
				newM[i] = m[i];
			}
		}
		return newM;
	}    

    public static byte[] copyParameters (byte m[], int indexBeginning, int total) {
		byte newM[] = null;
		if (m!=null) {
			newM = new byte [total];
			for (int i=0; i<total; i++) {
				newM[i] = m[indexBeginning+i];
			}
		}
		return newM;
	}        
    
	/**
	 * Cria copia de um array de string.
	 * @param m array a ser copiado
	 * @return copia do array
	 */
    public static String[] copyParameters (String m[]) {
		int dimension = m.length;
		String newM[] = new String [dimension];
		for (int i=0; i<dimension; i++) {
			newM[i] = m[i];
		}
		return newM;
	}
	
	/**
	 * Cria copia de um array de caracteres de duas dimensoes.
	 * @param m array a ser copiado
	 * @return copia do array
	 */
    public static char[][] copyParameters (char m[][]) {
		char newM[][] = null;
		if (m!=null) {
			int firstDimension = m.length;  
			int secondDimension = 0;
			secondDimension = m[0].length;
			newM = new char[firstDimension][secondDimension];
			for (int i=0; i<firstDimension; i++) {
				for (int j=0; j<secondDimension; j++) {
					newM[i][j] = m[i][j];
				}
			}
		}
		return newM;
	}

	/**
	 * Cria copia de um array de string de duas dimensoes.
	 * @param m array a ser copiado
	 * @return copia do array
	 */
    public static String[][] copyParameters (String m[][]) {
		int firstDimension = m.length;  
		int secondDimension = m[0].length; 
		String newM[][] = new String[firstDimension][secondDimension];
		for (int i=0; i<firstDimension; i++) {
			for (int j=0; j<secondDimension; j++) {
				newM[i][j] = m[i][j];
			}
		}
		return newM;
	}

	/**
	 * Concatena dois arrays.
	 * @param a1 primeiro array a ser concatenado
	 * @param a2 segundo array a ser concatenado
	 * @return um terceiro array concatenados os demais concatenados
	 */    
	public static byte[] appendArrayInAnotherArray (byte[] a1, byte[] a2) {
		byte[] result = new byte[a1.length + a2.length];
		appendArray(result, a1, 0);
		appendArray(result, a2, a1.length);
		return result;
	}

	/**
	 * Concatena dois arrays.
	 * @param a1 array que recebera dados de outro array
	 * @param a2 array a ser concatenado
	 */
	public static void appendArray (byte[] a1, byte[] a2) {
		appendArray(a1,a2,a1.length,a2.length);
	}
	
	/**
	 * Concatena dois arrays.
	 * @param a1 array que recebera dados de outro array
	 * @param a2 array a ser concatenado
	 * @param indexA1 index de a1 a partir do qual os dados de a2 serao concatenados
	 */
	public static void appendArray (byte[] a1, byte[] a2, int indexA1) {
		appendArray(a1,a2,indexA1,a2.length);
	}

	/**
	 * Concatena dois arrays.
	 * @param a1 array que recebera dados de outro array
	 * @param a2 array a ser concatenado
	 * @param indexA1 index de a1 a partir do qual os dados de a2 serao concatenados
	 * @param lengthToCopyA2 numero de elementos do array a2 a ser concatenado
	 */
	public static void appendArray (byte[] a1, byte[] a2, int indexA1, int lengthToCopyA2) {
		for (int i=0; i<lengthToCopyA2; i++) {
			a1[indexA1+i] = a2[i];
		}
	}	

	public static String getOutputFileName(String inputFile, String outputFile, double rate) {
	    if (outputFile==null || outputFile.trim().equals("")) {
	        outputFile = Functions.getOutputFileName(inputFile, rate); 
	    }
	    return outputFile;
	}
	
	/**
	 * Cria um nome de arquivo a partir do nome de outro arquivo e de uma taxa de ajuste.
	 * @param inputFile nome de um arquivo
	 * @param rate taxa de ajuste
	 * @return nome de um arquivo criado a partir do nome de outro arquivo e de uma taxa de ajuste
	 */	
	public static String getOutputFileName (String inputFile, double rate) {
		int length = inputFile.length();
		int index = length-(getExtension(inputFile).length()+1);
		String fileBeforeExtension = inputFile.substring(0,index);
		String fileAfterExtension = inputFile.substring(index,length);
		return fileBeforeExtension + "-" + Convert.roundDouble(rate,2) + fileAfterExtension;		
	}
	
	public static String getExtension(String inputFile) {
		int index = inputFile.lastIndexOf(".");	
		String extension = inputFile.substring(index+1);
		return extension;
	}
	
	/*
	public static void main(String args[]) {
		String t3 = "musica.mp3";
		t3 = getOutputFileName(t3, 10);
		String t4 = "sistema.mpeg";
		t4 = getOutputFileName(t4, 10); 
		System.out.println("t3=" + t3);
		System.out.println("t4=" + t4);
	}
	*/
}
