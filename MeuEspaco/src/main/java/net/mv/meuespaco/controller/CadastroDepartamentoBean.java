package net.mv.meuespaco.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.service.DepartamentoService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class CadastroDepartamentoBean extends CadastroSingle {

	@Inject
	private DepartamentoService depService;
	
	private Departamento dep;
	
	@Inject @Param
	private Long paramCodigo;
	
	@PostConstruct
	@Override
	public void init() {
		
		if (isEdicao()) 
		{
			dep = depService.buscaPeloCodigo(paramCodigo);
		} else 
		{
			novoRegistro();
		}
		
	}

	@Override
	public void novoRegistro() {
		dep = new Departamento();
	}

	@Override
	public void salvar() {
		
		try 
		{
			depService.salva(dep);
			
			FacesUtil.addSuccessMessage(String.format("Depatamento %s salvo.", dep.getDescricao()));
			novoRegistro();
			
		} catch (RegraDeNegocioException e) 
		{
			FacesUtil.addErrorMessage(String.format("Não foi possível salvar o depatamento. %s", e.getMessage()));
		}
	}

	@Override
	String getMensagemDeInclusao(String registro) {
		return String.format("Departamento %s incluído.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) {
		return String.format("Departamento %s alterado.", registro);
	}

	public Departamento getDep() {
		return dep;
	}
	public void setDep(Departamento dep) {
		this.dep = dep;
	}
	
}
