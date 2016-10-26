package net.mv.meuespaco.factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.model.loja.ItemCarrinho;

/**
 * Padrão Factory para encapsulamento da criação 
 * da escolha de um cliente utilizando o carrinho.
 * 
 * @author Sidronio
 * 08/01/2016
 */
public class EscolhaFactory {

	private Escolha instancia = new Escolha();
	
	public EscolhaFactory naData(LocalDateTime data) {
		instancia.setData(data);
		
		return this;
	}
	
	
	public EscolhaFactory comEnivoPara(LocalDateTime envio) {
		instancia.setDataEnvio(envio);
		return this;
	}
	
	public EscolhaFactory doCliente(Cliente cliente) {
		instancia.setCliente(cliente);
		return this;
	}
	
	public EscolhaFactory comOsItens(List<ItemCarrinho> itensDoCarrinho) throws RegraDeNegocioException {
		
		instancia.adicionaItensDoCarrinho(itensDoCarrinho);
		return this;
	}
	
	public EscolhaFactory comQtdMaximaDeItens(double qtdPermitidaParaEscolha) throws RegraDeNegocioException {
		
		double qtdDeItensDescontaveisDaEscolha = 0d;
		
		instancia.setQtdMaximaPermitida(new BigDecimal(qtdPermitidaParaEscolha));
		/*
		if (null != instancia.getItens()) 
		{
			for (ItemEscolha i : instancia.getItens()) {
				
				if (i.getProduto().isDescontavel()) {
					qtdDeItensDescontaveisDaEscolha += i.getQtd().doubleValue();
				}
				
			}			
		}
		
		if (qtdDeItensDescontaveisDaEscolha >= qtdPermitidaParaEscolha) {
		
			instancia.setStatus(StatusEscolha.ENVIADA);
			instancia.setDataEnvio(LocalDate.now());
		
		} else {
			instancia.setDataEnvio(instancia.getCliente().informaDataFinalDaSemana());
		}
		*/
		return this;
	}
	
	public EscolhaFactory comOValorMaximo(BigDecimal valorMaximo) {
		instancia.setValorMaximoPermitido(valorMaximo);
		return this;
	}
	
	public Escolha cria() {
		return instancia;
	}


}
