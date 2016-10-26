package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.SubgrupoDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.service.SubgrupoService;

/**
 * Implementação da camada Service da entidade Subgrupo.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Stateless
public class SubgrupoServiceImpl extends SimpleServiceLayerImpl<Subgrupo, Long> implements SubgrupoService, Serializable {

	private static final long serialVersionUID = 6617104191636510053L;
	
	@Inject
	private SubgrupoDAO subgrupoDAO;
	
	@Override
	public void validaInsercao(Subgrupo subgrupo) throws RegraDeNegocioException {
		subgrupo.valida();
	}

	@Override
	public void validaExclusao(Subgrupo subgrupo) throws RegraDeNegocioException {
		// TODO Auto-generated method stub
	}

	@Override
	public GenericDAO getDAO() {
		return subgrupoDAO;
	}

	@Override
	public Subgrupo buscaPeloCodigoComGrupoEFamilia(Long codigo) {
		
		return subgrupoDAO.buscarPeloCodigoComRelacionamento(
				codigo,
				Arrays.asList("grupo","grupo.familia"));
	}

	@Override
	public List<Subgrupo> buscaTodosComGrupoEFamilia() {
		
		return subgrupoDAO.filtrar(
				new Subgrupo(), 
				null, 
				Arrays.asList("grupo","grupo.familia"), 
				Arrays.asList("familia.descricao", "grupo.descricao", "descricao"), 
				Arrays.asList("grupo","grupo.familia"));
	}
	
	@Override
	public List<Subgrupo> buscaPeloGrupo(Grupo grupo) {
		
		return subgrupoDAO.buscarPeloGrupo(grupo);
	}
	
	@Override
	public List<Grupo> listaSubgruposPorDepartamento(Departamento dep) {
		return subgrupoDAO.listaSubgrupoComGruposPorDepartamento(dep);
	}

	@Override
	public List<Subgrupo> listarSubgruposPorDepEGrupo(Departamento dep, Grupo grupo) {
		return subgrupoDAO.listarPorDepartamentoEGrupoDeProdutosAtivos(dep, grupo);
	}

}
