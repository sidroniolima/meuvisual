/**
 * 
 */
package net.mv.meuespaco.util;

/**
 * Constantes auxiliares às classes.
 * 
 * @author Sidronio
 * 05/11/2015
 */
public interface IConstants {

	int QTD_DE_PRODUTOS_POR_PAGINA = 40;
	int QTD_PADRAO_REGISTROS_POR_PAGINA = 40;
	String CAMINHO_DAS_FOTOS = "/home/meuvisua/var/static/images/pecas/";
	String CAMINHO_DO_ARQUIVO_DE_EXPORTACAO = "http://meuvisualsemijoias.com/integracao/";
	String NOME_DO_ARQUIVO_DE_EXPORTACAO = "integracao.csv";
	String PRODUTO_SEM_FOTO = "Produto_sem_foto.png";
	int QTD_EXIBIDA_NA_LISTAGEM_DE_PRODUTOS = 21;
	String CAMINHO_DOS_RELATORIOS = "http://meuvisualsemijoias.com/relatorios/rel-escolha.jrxml";
	final int MIN_VALOR_BRINDE = 1000; 
	final int MAX_VALOR_BRINDE = 75000; 
	final String WARN_ATINGIU_QTD = "Você atingiu a quantidade de peças permitidas.";
	final String WARN_ATINGIU_VALOR = "Você atingiu seu saldo de peças permitidas. "
			+ "Escolha peças cujo valor é menor que o seu saldo, de %s.";
}
