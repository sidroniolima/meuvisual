package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.service.ProdutoService;

@Named
@RequestScoped
public class IndexBean implements Serializable {

	private static final long serialVersionUID = -4430184650971162637L;
	
	@Inject
	private ProdutoService produtoSrvc;
	
	private List<Produto> destaques;
	
	@PostConstruct
	public void init() 
	{
		if (null == destaques) {
			destaques = produtoSrvc.ultimosDezProdutosCadastrados();
		}
	}

	/**
	 * @return the destaques
	 */
	public List<Produto> getDestaques() {
		return destaques;
	}

}
