/**
 * Essa classe faz parte do projeto de ajuste elastico de audio comprimido
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (25/05/2005) 
 */

package timescale.audio.format.generalFormat;


/**
 * Essa classe extrai informacoes de dados dentro de frames.
 */
public abstract class AMainDataHandler {

	protected FramesCollection frames;
	
	/** 
	 * Controi instancia da classe.
	 * @param frames quadros a serem manipulados 
	 */		
	public AMainDataHandler (FramesCollection frames) {
		this.frames = frames;	
	}
	
	public static AMainDataHandler getMainDataHandler(FramesCollection frames) {
		return new DataFieldHandler (frames);
	}

	/**
	 * Recupera string formada dos caracteres encontrados partindo da tuplaindex. 
	 * Formara string de tamanho, NO MÁXIMO, igual a length. Pode ser diferente no
	 * caso de nao existir mais bits a serem recuperados.
	 * @param ti index de inicio de onde dados serão buscados, inclusive
	 * @param lengthInBytes tamanho MAXIMO da string a ser retornada
	 * @return string de tamanho length formada dos caracteres encontrados 
	 * partindo da tuplaindex.   
	 */	
    public abstract byte[] getData (TuplaIndex ti, int lengthInBytes);
    
	/**
	 * Modifica dados fisicos dos frames a partir do index especificado 
	 * utilizando dado tambem passado como argumento.
	 * Caso acabem os bits dos frames, os dados que sobrarem do newData serao ignorados.
	 * @param ti index inicial a ser modificado
	 * @param byteNewData dado a ser utilizado para modificar os bits 
	 */    
    public abstract void setData (TuplaIndex ti, byte[] byteNewData);
    
	/**
	 * Retorna novo index considerando um incremento de delta na TuplaIndex
	 * passada como argumento.
	 * @param ti index que deve ser incrementado
	 * @param delta incremento dado a tupla em bytes
	 * @return index incrementado por delta  
	 */    
    public abstract TuplaIndex incrementTuplaIndex (TuplaIndex ti, int delta);
    
	/**
	 * Retorna o numero de bytes contido entre as duas tuplas.
	 * @param begin tupla de inicio do intervalo, inclusive
	 * @param end tupla de final do intervalo, exclusive
	 * @return numero de bytes contido entre as duas tuplas.
	 */    
    public abstract int lengthInBytesBetweenTuplas (TuplaIndex begin, TuplaIndex end);
    
	/**
	 * Recupera TuplaIndex equivalente ao inicio do dado lógico 
	 * do frame cujo index e passado como argumento.
	 * @param index index do frame que se deseja analisar
	 * @return TuplaIndex equivalente ao inicio do dado logico do frame cujo 
	 * index e passado como argumento.
	 */	    
    public abstract TuplaIndex findIndexBeginLogicalData (int index);
    
    /**
     * Essa classe representa um indice dentro do conjunto de main data de frames.
     */
    public class TuplaIndex {

    	private int indexFrameInCollection; 
    	private int indexPhysicalDataInBytes;
    		    		
    	/**
    	 * Controi instancia da classe.
    	 * @param indexFrameInCollection index do frame em uma colecao
    	 * @param indexPhysicalDataInBytes index do dado fisico do frame indicado em indexFrame
    	 */
    	public TuplaIndex (int indexFrameInCollection, int indexPhysicalDataInBytes) {
    		this.indexFrameInCollection = indexFrameInCollection;
    		this.indexPhysicalDataInBytes = indexPhysicalDataInBytes;
    	}
    		
    	/**
    	 * Retorna index do frame referenciado por TuplaIndex.
    	 * @return index do frame referenciado por TuplaIndex
    	 */
    	public int getIndexFrameInCollection() {
    		return this.indexFrameInCollection;
    	}
    	
    	/**
    	 * Retorna index do dado físico do frame (indicado em indexFrame) referenciado por TuplaIndex.
    	 * @return index do dado físico do frame (indicado em indexFrame) referenciado por TuplaIndex
    	 */		
    	public int getIndexPhysicalDataInBytes() {
    		return this.indexPhysicalDataInBytes;
    	}			
    }
    
}
