package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.GrupoDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.service.GrupoService;

/**
 * Implementação da camada Service da entidade Grupo.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Stateless
public class GrupoServiceImpl extends SimpleServiceLayerImpl<Grupo, Long> implements GrupoService, Serializable {

	private static final long serialVersionUID = 8399726525699256280L;
	
	@Inject
	private GrupoDAO grupoDAO;
	
	@Override
	public void validaInsercao(Grupo grupo) throws RegraDeNegocioException {
		grupo.valida();
	}

	@Override
	public void validaExclusao(Grupo grupo) throws RegraDeNegocioException {
		// TODO Auto-generated method stub
	}

	@Override
	public GenericDAO getDAO() {
		return grupoDAO;
	}

	@Override
	public Grupo buscaGrupoComFamilia(Long codigo) {
		return grupoDAO.buscarPeloCodigoComRelacionamento(codigo, Arrays.asList("familia"));
	}

	@Override
	public List<Grupo> listaTodosComFamilia() {
		return grupoDAO.filtrar(
				null, 
				null, 
				Arrays.asList("familia"), 
				Arrays.asList("familia.descricao", "descricao"),
				Arrays.asList("familia"));
	}
	
	@Override
	public List<Grupo> buscaTodosComSubGrupos() {
		return this.grupoDAO
				.filtrar(new Grupo(), 
						null, 
						Arrays.asList("subgrupos"), 
						Arrays.asList("descricao"), 
						null);
	}
	
	@Override
	public List<Grupo> listaGruposPorDepartamento(Departamento dep) {
		return grupoDAO.listaGrupoComSubgruposPorDepartamento(dep);
	}

	@Override
	public List<Grupo> listaGruposPorDepartamentoParaConsignado(Departamento dep) {
		return grupoDAO.listaGrupoComSubgruposPorDepartamentoEFinalidade(dep, Finalidade.CONSIGNADO);
	}
	
	@Override
	public List<Grupo> listaGruposPorDepartamentoParaVenda(Departamento dep) {
		return grupoDAO.listaGrupoComSubgruposPorDepartamentoEFinalidade(dep, Finalidade.VENDA);
	}
}
