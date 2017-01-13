package net.mv.meuespaco.model.integracao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Encapsula uma lista de créditos para um Cliente.
 * 
 * @author sidronio
 * @created 02/01/17
 */
public class ListagemCreditos {

	private List<Credito> creditos;
	
	public ListagemCreditos() {	
		creditos = new ArrayList<Credito>();
	}
	
	public ListagemCreditos(List<Credito> creditos) 
	{
		this.creditos = creditos;
	}
	
	/**
	 * Calcula o saldo dos créditos.
	 * 
	 * @return
	 */
	public double saldo()
	{
		
		Map<Boolean, Double> agrupado = creditos
			.parallelStream()
			.collect(Collectors.groupingBy(Credito::isSoma, Collectors.summingDouble(Credito::getValor)));
		
		if (agrupado.isEmpty())
			return 0;
		
		if (!agrupado.containsKey(true))
			return 0 - agrupado.get(false);
		
		if (!agrupado.containsKey(false))
			return agrupado.get(true);
		
		return agrupado.get(true) - agrupado.get(false);
	}

	public List<Credito> getCreditos() {
		return creditos;
	}
	
}
