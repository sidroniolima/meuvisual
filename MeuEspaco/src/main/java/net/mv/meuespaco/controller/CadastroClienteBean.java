package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.StatusCliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.RegiaoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller do cadastro da entidade Departamento responsável 
 * pela inclusão e alteração.
 * 
 * @author Sidronio
 * 03/05/2016
 */
@Named
@ViewScoped
public class CadastroClienteBean extends CadastroSingle implements Serializable {

	private static final long serialVersionUID = -1663278016337412059L;
	
	@Inject
	private ClienteService clienteSrvc;
	
	@Inject
	private RegiaoService regiaoSrvc;
	
	private Cliente cliente;
	private StatusCliente[] status;
	private List<Regiao> regioes;

	@Inject @Param
	private Long paramCodigo;
	
	@Override
	@PostConstruct
	public void init() {
		
		status = StatusCliente.values();
		regioes = regiaoSrvc.buscaTodas();
		
		if (isEdicao()) {
			
			cliente = clienteSrvc.buscaPeloCodigoComRegiao(paramCodigo);
		} else {
			
			novoRegistro();
		}
	}

	@Override
	void novoRegistro() {
		cliente = new Cliente();
	}

	@Override
	public void salvar() {
		
		try {

			clienteSrvc.salva(cliente);
			
			if (isEdicao()) {
				
				FacesUtil.addSuccessMessage(this.getMensagemDeAlteracao(cliente.getNome()));
			} else {
				
				FacesUtil.addSuccessMessage(this.getMensagemDeInclusao(cliente.getNome()));
			}
			
			novoRegistro();
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(String.format("Não foi possível salvar o cliente. %s", e.getMessage()));
		}
	}

	@Override
	String getMensagemDeInclusao(String registro) {
		return String.format("Cliente %s incluído com sucesso.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) {
		return String.format("Cliente %s alterado com sucesso.", registro);
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}
	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the status
	 */
	public StatusCliente[] getStatus() {
		return status;
	}

	/**
	 * @return the regioes
	 */
	public List<Regiao> getRegioes() {
		return regioes;
	}
	
}
