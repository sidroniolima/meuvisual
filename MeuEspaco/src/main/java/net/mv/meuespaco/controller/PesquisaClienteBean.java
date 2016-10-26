package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroCliente;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Camada View da Pesquisa de Clientes.
 * 
 * @author Sidronio
 *
 */
@Named
@ViewScoped
public class PesquisaClienteBean extends PesquisaSingle implements Serializable {

	private static final long serialVersionUID = 8988323677565551976L;

	@Inject
	private ClienteService clienteSrvc;
	
	private List<Cliente> clientes;
	private Cliente clienteSelecionado;
	
	private FiltroCliente filtro;
	
	@Override
	@PostConstruct
	void init() 
	{
		filtro = new FiltroCliente();

		if (null == clientes)
		{
			listarComPaginacao();			
		}
	}
	
	/**
	 * Filtra os registros de Cliente.
	 */
	public void filtra() {
		clientes = this.clienteSrvc.filtraPeloModoEspecifico(filtro, this.getPaginator());
	}
	
	/**
	 * Efetiva um cadastro tornando-o de fato cliente e
	 * gerando seu login para acesso ao site.
	 */
	public void efetivaCadastro() {

		clienteSelecionado = clienteSrvc.buscaCompletoPeloCodigo(clienteSelecionado.getCodigo());
		
		try {
		
			this.clienteSrvc.efetivaCadastro(clienteSelecionado);
			
			FacesUtil.addSuccessMessage(
					String.format("Cliente %s efetivado e login gerado.", clienteSelecionado.getNome()));
			
			listarComPaginacao();
		
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(
					String.format("Não foi possível efetivar ou gerar o login do cliente. %s", e.getMessage()));
		}
	}
	
	@Override
	public void excluir() {
		
		try {
			
			clienteSrvc.exclui(this.clienteSelecionado.getCodigo());
			
			clientes.remove(clienteSelecionado);
		
			FacesUtil.addSuccessMessage(this.getMensagemDeExclusaoOk());
			
		} catch (RegraDeNegocioException | DeleteException e) {

			FacesUtil.addErrorMessage(getMensagemDeErroDeExclusao(e.getMessage()));
		}
	}

	@Override
	public void listarComPaginacao() {
		
		if (filtro.isPreenchido()) {
			
			this.filtra();
			
		} else {
		
			clientes = this.clienteSrvc.listarComPaginacao(
					this.getPaginator(),
					Arrays.asList("codigoSiga"), 
					Arrays.asList("regiao"), 
					null);
		}
	}
	
	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("Cliente %s excluído com sucesso.", this.descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("Não foi possível excluir o cliente %s. %s", this.descricaoDoRegistro(), msgError);
	}

	@Override
	public String descricaoDoRegistro() {
		return clienteSelecionado.getNome();
	}

	/**
	 * @return the clientes
	 */
	public List<Cliente> getClientes() {
		return clientes;
	}
	/**
	 * @param clientes the clientes to set
	 */
	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	/**
	 * @return the clienteSelecionado
	 */
	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}
	/**
	 * @param clienteSelecionado the clienteSelecionado to set
	 */
	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	/**
	 * @return the filtro
	 */
	public FiltroCliente getFiltro() {
		return filtro;
	}
	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(FiltroCliente filtro) {
		this.filtro = filtro;
	}
	
}
