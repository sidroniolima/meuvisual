package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.util.IConstants;
import net.mv.meuespaco.util.Paginator;

@Named
@ViewScoped
public class ListaEscolhasDoClienteBean implements Serializable {

	private static final long serialVersionUID = 2018870829458226738L;

	@Inject
	private EscolhaService escolhaSrvc;
	
	private List<Escolha> escolhas;
	
	private Paginator paginator;
	
	@Inject
	@ClienteLogado
	private Cliente cliente;
	
	@PostConstruct
	public void init() {
		
		paginator = new Paginator(IConstants.QTD_PADRAO_REGISTROS_POR_PAGINA);
		
		//cliente = clienteSrvc.buscaClientePeloUsuario(loginBean.getUserLogged());
		
		listaComPaginacao();
	}
	
	public void listaComPaginacao() {
		escolhas = escolhaSrvc.listaEscolhasDoCliente(cliente);
	}

	/**
	 * @return the paginator
	 */
	public Paginator getPaginator() {
		return paginator;
	}
	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	/**
	 * @return the escolhas
	 */
	public List<Escolha> getEscolhas() {
		return escolhas;
	}
	
}
