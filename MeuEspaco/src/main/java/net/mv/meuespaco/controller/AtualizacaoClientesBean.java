package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.UploadedFile;

import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.UsuarioService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class AtualizacaoClientesBean implements Serializable {

	private static final long serialVersionUID = -1452179737537356019L;

	@Inject
	private ClienteService clienteSrvc;
	
	@Inject
	private UsuarioService usuarioSrvc;
	
	private UploadedFile filePreCadastros;
	
	private UploadedFile fileLegado;

	private UploadedFile fileRegioes;
	
	private boolean desativaImplantacao = true;
	
	private int status = 0;
	
	private boolean atualizado;
	
	@PostConstruct
	public void init() {
		
	}
	
	/**
	 * Importa as informações do ERP
	 */
	public void importaPreCadastros() {
		
		if (!desativaImplantacao) {
			clienteSrvc.importaPreCadastrosDoERP(filePreCadastros);
		}
	}
	
	/**
	 * Atualiza as senhas do sistema legado.
	 */
	public void atualizaSenhasDoSistemaLegado() {
		
		if (!desativaImplantacao) {
			clienteSrvc.atualizaSenhaDosCadastrosPeloSistemaLegado(fileLegado);
		}
	}
	
	/**
	 * Atualiza as informações de Região.
	 */
	public void atualiaAsRegioes() {
		
		if (!desativaImplantacao) {
			clienteSrvc.atualizaRegiaoDosClientesPeloERP(fileRegioes);
		}
	}
	
	/**
	 * Atualiza a quantidade de peças para escolha e o status 
	 * dos clientes de acordo com a exportação do ERP.
	 */
	public void atualizaClientesPeloERP() 
	{
		List<Long> codigosDosClientesDoERP = clienteSrvc.atualizaInformacoesVindasDoErp();
		
		clienteSrvc.inativaClientesQueNaoEstaoEntreOsCodigos(codigosDosClientesDoERP);
		usuarioSrvc.inativaUsuarios();
		usuarioSrvc.reativaUsuarios();
		
		FacesUtil.addSuccessMessage("Clientes e usuários atualizados com sucesso.");
	}
	
	public void atualizaStatus() {
		status += 10;
	}

	/**
	 * @return the fileLegado
	 */
	public UploadedFile getFileLegado() {
		return fileLegado;
	}
	/**
	 * @param fileLegado the fileLegado to set
	 */
	public void setFileLegado(UploadedFile fileLegado) {
		this.fileLegado = fileLegado;
	}

	/**
	 * @return the fileRegioes
	 */
	public UploadedFile getFileRegioes() {
		return fileRegioes;
	}
	/**
	 * @param fileRegioes the fileRegioes to set
	 */
	public void setFileRegioes(UploadedFile fileRegioes) {
		this.fileRegioes = fileRegioes;
	}

	/**
	 * @return the filePreCadastros
	 */
	public UploadedFile getFilePreCadastros() {
		return filePreCadastros;
	}
	/**
	 * @param filePreCadastros the filePreCadastros to set
	 */
	public void setFilePreCadastros(UploadedFile filePreCadastros) {
		this.filePreCadastros = filePreCadastros;
	}

	/**
	 * @return the desativaImplantacao
	 */
	public boolean isDesativaImplantacao() {
		return desativaImplantacao;
	}

	public int getStatus() {
		return status;
	}

	public boolean isAtualizado() {
		return atualizado;
	}
	
}
