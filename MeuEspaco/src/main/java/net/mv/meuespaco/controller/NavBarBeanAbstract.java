package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.consulta.MenuPorDepartamento;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.service.DepartamentoService;
import net.mv.meuespaco.service.GrupoService;

/**
 * Abstração da barra (nav) de navegação utilizado nas implementações 
 * da navegação para Consigando e Venda.
 * 
 * @author Sidronio
 * @create 10/08/2016
 */
public abstract class NavBarBeanAbstract implements Serializable {
	
	private static final long serialVersionUID = -2882194940330116877L;

	@Inject
	private GrupoService grupoSrvc;
	
	@Inject
	private DepartamentoService depSrvc;
	
	private String pesquisa;
	
	@PostConstruct
	public void init() 
	{
	}	
	
	/**
	 * Criação do menu de forma dinâmica e performatica, evitando querys.
	 * 
	 * @return Menu completo.
	 */
	public Map<Departamento, Map<Grupo, Map<Subgrupo, List<MenuPorDepartamento>>>> criaMenus()
	{
		return this.getDepSrvc().listaAtivosComGruposESubgrupos(this.getFinalidade());
	}
	
	/**
	 * Define a finalidade do Menu.
	 * @return finalidade.
	 */
	public abstract Finalidade getFinalidade(); 

	/**	 * Redireciona a pesquisa para a listagem de produtos.
	 * 
	 * @return
	 */
	public String pesquisar() 
	{
		if (pesquisa != null && !pesquisa.isEmpty()) { 

			return 
				new StringBuilder()
					.append(getUrl())
					.append(pesquisa).toString();
		}
		
		return null;
		
	}

	/**
	 * Informa a URL de resposta da pesquisa.
	 * 
	 * @return URL da pesquisa.
	 */
	protected abstract String getUrl();
	
	/**
	 * @return the pesquisa
	 */
	public String getPesquisa() {
		return pesquisa;
	}
	/**
	 * @param pesquisa the pesquisa to set
	 */
	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public GrupoService getGrupoSrvc() {
		return grupoSrvc;
	}
	
	public DepartamentoService getDepSrvc() 
	{
		return depSrvc;
	}
}
