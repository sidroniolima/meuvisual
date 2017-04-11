package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import net.mv.meuespaco.controller.filtro.FiltroCliente;
import net.mv.meuespaco.dao.ClienteDAO;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cpf;
import net.mv.meuespaco.model.loja.Documento;
import net.mv.meuespaco.util.Paginator;

/**
 * Implementação da camada DAO da entidade Cliente.
 * 
 * @author Sidronio
 * 22/12/2015
 */
@Stateless
public class HibernateClienteDAO extends HibernateGenericDAO<Cliente, Long> implements ClienteDAO, Serializable {

	private static final long serialVersionUID = 4520454579820001530L;
	
	@Override
	public Cliente buscaClientePeloUsuario(Usuario usuario) {
		Criteria query = this.getSession().createCriteria(Cliente.class);
		
		query.setFetchMode("usuario", FetchMode.JOIN);
		query.setFetchMode("regiao", FetchMode.JOIN);
		query.setFetchMode("regiao.semana", FetchMode.JOIN);
		
		query.add(Restrictions.eq("usuario", usuario));

		return (Cliente) query.uniqueResult();
	}

	
	@Override
	public Cliente buscarPeloCodigoSiga(String codigoSiga) {

		Criteria query = this.getSession().createCriteria(Cliente.class);
		
		query.add(Restrictions.eq("codigoSiga", codigoSiga));
		
		return (Cliente) query.uniqueResult();
	}
	
	@Override
	public Cliente buscarPeloCpf(Documento cpf) 
	{
		Criteria query = this.getSession().createCriteria(Cliente.class);
		query.setFetchMode("usuario", FetchMode.JOIN);
		query.setFetchMode("regiao", FetchMode.JOIN);
		
		query.add(Restrictions.eq("cpf", cpf));
		
		return (Cliente) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cliente> filtrarPelaPesquisa(String codigoSiga, Cpf cpf, String nome) {
		
		Criteria query = this.getSession().createCriteria(Cliente.class);
		query.setFetchMode("regiao", FetchMode.JOIN);
		
		if (null != codigoSiga && !codigoSiga.isEmpty()) {
			query.add(Restrictions.eq("codigoSiga", codigoSiga));
		}
		
		if (null != cpf) {
			query.add(Restrictions.eq("cpf", cpf));
		}
		
		if (null != nome && !nome.isEmpty()) {
			query.add(Restrictions.like("nome", nome, MatchMode.ANYWHERE));
		}
		
		return query.list();
		
	}
	
	@Override
	public Cliente buscarOutroPeloCodigoSiga(Long codigo, String codigoSiga) {
		Criteria query = this.getSession().createCriteria(Cliente.class);
		
		query.add(Restrictions.eq("codigoSiga", codigoSiga));
		query.add(Restrictions.ne("codigo", codigo));
		
		return (Cliente) query.uniqueResult();
	}
	
	@Override
	public void inativaClientesQueNaoEstaoNaListagem(List<String> codigosSiga) {
		
		Query query = this.getSession().createQuery(
				"UPDATE Cliente c set c.status = 'INATIVO' WHERE c.codigoSiga not in :codigosSiga and c.status = 'ATIVO'");
		query.setParameterList("codigosSiga", codigosSiga);
		
		query.executeUpdate();
	}
	
	@Override
	public List<Cliente> filtraPeloModoEspecifico(FiltroCliente filtro, Paginator paginator) 
	{
		Criteria criteriaSublist = this.getSession().createCriteria(Cliente.class);
		criteriaSublist.setFetchMode("regiao", FetchMode.JOIN);
		
		if (null != filtro.getCodigoSiga() && !filtro.getCodigoSiga().isEmpty()) {
			criteriaSublist.add(Restrictions.eq("codigoSiga", filtro.getCodigoSiga()));
		}
		
		if (null != filtro.getCpf()) {
			criteriaSublist.add(Restrictions.eq("cpf", filtro.getCpf()));
		}
		
		if (null != filtro.getNome() && !filtro.getNome().isEmpty()) {
			criteriaSublist.add(Restrictions.like("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		if (null != filtro.getRegiao())
		{
			criteriaSublist.add(Restrictions.eq("regiao", filtro.getRegiao()));
		}

		if (null != filtro.getSemana())
		{
			criteriaSublist.add(Restrictions.eq("semana", filtro.getSemana()));
		}
		
		if (null != filtro.getStatus())
		{
			criteriaSublist.add(Restrictions.eq("status", filtro.getStatus()));
		}		
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		criteriaSublist.addOrder(Order.asc("codigoSiga"));
		
		List<Cliente> registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty()) {
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
				
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);	
		
		Criteria criteria = getSession().createCriteria(Cliente.class);		
		criteria.setFetchMode("regiao", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.asc("codigoSiga"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	
	@Override
	public List<Cliente> filtraPeloModoEspecifico(FiltroCliente filtro) 
	{
		Criteria criteria = this.getSession().createCriteria(Cliente.class);
		criteria.setFetchMode("regiao", FetchMode.JOIN);
		
		criteria.createAlias("regiao.semana", "semana");
		
		if (null != filtro.getCodigoSiga() && !filtro.getCodigoSiga().isEmpty()) 
		{
			criteria.add(Restrictions.eq("codigoSiga", filtro.getCodigoSiga()));
		}
		
		if (null != filtro.getCpf()) 
		{
			criteria.add(Restrictions.eq("cpf", filtro.getCpf()));
		}
		
		if (null != filtro.getNome() && !filtro.getNome().isEmpty()) 
		{
			criteria.add(Restrictions.like("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		
		if (null != filtro.getRegiao())
		{
			criteria.add(Restrictions.eq("regiao", filtro.getRegiao()));
		}

		if (null != filtro.getSemana())
		{
			criteria.add(Restrictions.eq("semana", filtro.getSemana()));
		}
		
		if (null != filtro.getStatus())
		{
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}
		
		criteria.addOrder(Order.asc("codigoSiga"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
}
