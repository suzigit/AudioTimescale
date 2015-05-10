/*
 * Essa classe faz parte do projeto de ajuste elastico de media comprimida
 * do formatador Hyperprop.
 * Todos os direitos sao reservados.
 * 
 * @Copyright: Laboratorio TeleMidia
 * @author: <a href="mailto:smbm@telemidia.puc-rio.br">Suzana Mesquita</a>
 * Creation date: (10/11/2005)
 */
package timescale.assembler;

import util.data.ContentType;

/**
 * Responsavel por extrair objetos elementares de media a partir de um fluxo binario 
 */
public interface IAssembler {
	public ContentType getType ();
}
