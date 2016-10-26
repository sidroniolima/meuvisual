package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;
import org.primefaces.model.UploadedFile;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class FotoProdutoBean implements Serializable{

	private static final long serialVersionUID = -8291863139907727373L;

	@Inject
	private ProdutoService produtoSrvc;
	
	@Param @Inject
	private Long paramCodigo;
	
	private Produto produto;
	private UploadedFile file;
	
	private String fotoSelecionada;
	
	private Produto produtoRelacionado;
	private List<Produto> produtos;
	
	private Produto relacionamentoSelecionado;
	
	@PostConstruct
	public void init() {
		
		if (null != paramCodigo) {
			
			produto = produtoSrvc.buscaProdutoComInfoParaOSite(paramCodigo);
			
		} else {
			
			FacesUtil.addErrorMessage("Não foi possível localizar o produto.");
			
		}
		
		produtos = produtoSrvc.buscaTodas();
		novoProdutoRelacionado();
	}
	
	/**
	 * Cria uma nova instância para o produto relacionado.
	 */
	public void novoProdutoRelacionado() {
		produtoRelacionado = new Produto();
	}
	
	/**
	 * Faz o upload da imagem em disco utilizando a camada 
	 * de serviço de Produto.
	 */
	public void uploadFoto() throws IOException {

		try {
			
			produtoSrvc.adicionaFotoAoProduto(produto, file);
			
			FacesUtil.addSuccessMessage("Foto adicionada com sucesso.");

		} catch (IOException | RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage("Não foi possível adicionar a foto. " + e.getMessage());
		}
	}
	
	/**
	 * Remove a foto selecionada do produto.
	 */
	public void removeFoto() {
		
		try {
		
			produtoSrvc.removeFotoDoProduto(produto, fotoSelecionada);
			
			FacesUtil.addSuccessMessage("Foto removida do produto.");
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(
					String.format("Foto não pode ser removida do produto. %s", e.getMessage()));
		}
	}
	
	/**
	 * Relaciona o produto selecionado ao produto 
	 * em edição. 
	 */
	public void adicionaRelacaoAoProduto() {
		
		try {
			
			produto.adicionaRelacao(produtoRelacionado);
			
			FacesUtil.addSuccessMessage(
					String.format("Produto %s relacionado.", produtoRelacionado.getCodigoInterno()));
			
			novoProdutoRelacionado();
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(String.format("Não foi possível incluir o produto. %s", e.getMessage()));
			
		}
		
	}
	
	/**
	 * Remove um relacionamento entre produtos.
	 */
	public void removeRelacionamento() {
		
		try {
			
			produto.removeRelacionamento(relacionamentoSelecionado);
			
			FacesUtil.addSuccessMessage(
					String.format("O produto %s não está mais relacionado.", relacionamentoSelecionado.getCodigoInterno()));
	
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(String.format("Não foi possível remover a relação. %s", e.getMessage()));
		}
		
	}
	
	/**
	 * Salva o produto atualizando as informações de fotos 
	 * e produtos relacionados.
	 */
	public void atualizaInformacoes() {
			
		try {
			
			this.produtoSrvc.salva(produto);
			
			FacesUtil.addSuccessMessage(String.format("Produto %s atualizado com sucesso.", produto.getCodigoInterno()));
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(e.getMessage());
		}
		
	}

	/**
	 * @return the produto
	 */
	public Produto getProduto() {
		return produto;
	}

	/**
	 * @return the file
	 */
	public UploadedFile getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(UploadedFile file) {
		this.file = file;
	}

	/**
	 * @return the fotoSelecionada
	 */
	public String getFotoSelecionada() {
		return fotoSelecionada;
	}
	/**
	 * @param fotoSelecionada the fotoSelecionada to set
	 */
	public void setFotoSelecionada(String fotoSelecionada) {
		this.fotoSelecionada = fotoSelecionada;
	}

	/**
	 * @return the produtoRelacionado
	 */
	public Produto getProdutoRelacionado() {
		return produtoRelacionado;
	}

	/**
	 * @param produtoRelacionado the produtoRelacionado to set
	 */
	public void setProdutoRelacionado(Produto produtoRelacionado) {
		this.produtoRelacionado = produtoRelacionado;
	}
	/**
	 * @return the produtos
	 */
	public List<Produto> getProdutos() {
		return produtos;
	}

	/**
	 * @return the relacionamentoSelecionado
	 */
	public Produto getRelacionamentoSelecionado() {
		return relacionamentoSelecionado;
	}
	/**
	 * @param relacionamentoSelecionado the relacionamentoSelecionado to set
	 */
	public void setRelacionamentoSelecionado(Produto relacionamentoSelecionado) {
		this.relacionamentoSelecionado = relacionamentoSelecionado;
	}
	
}
