package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.mv.meuespaco.controller.filtro.FiltroEscolha;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.model.loja.StatusEscolha;
import net.mv.meuespaco.relatorio.RelatorioEscolha;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.util.FacesUtil;
import net.mv.meuespaco.util.IConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class PesquisaEscolhaBean extends PesquisaSingle implements Serializable {

	private static final long serialVersionUID = -7189450014704767797L;

	@Inject
	private EscolhaService escolhaSrvc;
	
	private List<Escolha> escolhas;
	
	private Escolha escolhaSelecionada;
	
	private FiltroEscolha filtro;
	
	private StatusEscolha[] status;

	private String relatorioPath = IConstants.CAMINHO_DOS_RELATORIOS;
	
	@PostConstruct
	@Override
	public void init() {
		
		status = StatusEscolha.values();
		
		filtro = new FiltroEscolha();
		
		if (null == escolhas) {
			listarComPaginacao();
		}
		
	}

	@Override
	public void excluir() {
		
		try {
			
			this.escolhaSrvc.exclui(escolhaSelecionada.getCodigo());
			
			escolhas.remove(escolhaSelecionada);
			
			FacesUtil.addSuccessMessage(getMensagemDeExclusaoOk());
			
			escolhaSelecionada = null;
			
		} catch (RegraDeNegocioException | DeleteException e) {
			
			FacesUtil.addErrorMessage(getMensagemDeErroDeExclusao(e.getMessage()));
		}
	}

	@Override
	public void listarComPaginacao() {
		
		if (filtro.isPreenchido()) {
			
			filtrar();
	
		} else {
		
			escolhas = this.escolhaSrvc.listarComPaginacao(
					getPaginator(), 
					Arrays.asList("data"), 
					Arrays.asList("itens","cliente","cliente.regiao"), 
					null);
		}
	}

	/**
	 * Filtra os registros de acordo com os dados de pesquisa. 
	 */
	private void filtrar() {
		
		escolhas = this.escolhaSrvc.filtraPelaPesquisa(filtro, getPaginator());
		
		/*if (filtro.forPeloStatus() && null != escolhas) {
			List<Escolha> escolhasFiltradas = new ArrayList<Escolha>();
			
			escolhasFiltradas = escolhas
					.stream()
					.filter(e -> e.getStatus().equals(filtro.getStatus()))
					.collect(Collectors.toList());
			
			escolhas = escolhasFiltradas;
			getPaginator().setTotalDeRegistros(escolhas.size());
		}*/
	}
	
	/**
	 * Geração do relatório.
	 */
	public void geraRelatorio() {
		
		List<RelatorioEscolha> inforacoesDoRelatorio = 
				escolhaSrvc.geraRelatorioDeEscolha(escolhaSelecionada.getCodigo());
		URL url;
		
		try {
			url = new URL(relatorioPath);
			
			URLConnection connection = url.openConnection();
	        connection.setReadTimeout(5000);
	        
	        JasperReport jasper = JasperCompileManager.compileReport(connection.getInputStream());
	        
			JasperPrint jasperPrint= JasperFillManager.fillReport( 
					jasper,
					null,
			        new JRBeanCollectionDataSource( inforacoesDoRelatorio));
			
			HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

			httpServletResponse.setContentType("application/pdf");

			ServletOutputStream servletStream = httpServletResponse.getOutputStream();
			
			JasperExportManager.exportReportToPdfStream(jasperPrint, servletStream);
			
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (JRException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("A escolha %s foi excluída com sucesso.", this.descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("A escolha %s não pode ser excluída. %s", 
				this.descricaoDoRegistro(),
				msgError);
	}

	@Override
	public String descricaoDoRegistro() {
		
		StringBuilder strBuilder = new StringBuilder();
		
		strBuilder.append(escolhaSelecionada.codigoFormatado());
		strBuilder.append(" do cliente ");
		strBuilder.append(escolhaSelecionada.getCliente().getNome());
		
		return strBuilder.toString();
	}

	/**
	 * @return the escolhaSelecionada
	 */
	public Escolha getEscolhaSelecionada() {
		return escolhaSelecionada;
	}
	/**
	 * @param escolhaSelecionada the escolhaSelecionada to set
	 */
	public void setEscolhaSelecionada(Escolha escolhaSelecionada) {
		this.escolhaSelecionada = escolhaSelecionada;
	}

	/**
	 * @return the escolhas
	 */
	public List<Escolha> getEscolhas() {
		return escolhas;
	}

	/**
	 * @return the filtro
	 */
	public FiltroEscolha getFiltro() {
		return filtro;
	}
	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(FiltroEscolha filtro) {
		this.filtro = filtro;
	}

	/**
	 * @return the status
	 */
	public StatusEscolha[] getStatus() {
		return status;
	}
	
}
