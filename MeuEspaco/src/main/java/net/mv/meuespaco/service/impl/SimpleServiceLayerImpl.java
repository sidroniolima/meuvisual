package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.service.SimpleServiceLayer;
import net.mv.meuespaco.util.Paginator;

/**
 * Abstração da implementação da interface SimpleServiceLayer.
 * 
 * @author Sidronio
 * 
 * 10/11/2015
 *
 * @param <T>
 * @param <ID>
 */
public abstract class SimpleServiceLayerImpl<T, ID extends Serializable> implements SimpleServiceLayer<T, ID> {

	private Class<T> classeDaEntidade;
	
	@SuppressWarnings("unchecked")
	public SimpleServiceLayerImpl() {
		
		classeDaEntidade = (Class<T>) 
				( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}	
	
	@Override
	public void salva(T entidade) throws RegraDeNegocioException {
		this.validaInsercao(entidade);
		
		getDAO().salvar(entidade);
	}

	@Override
	public void exclui(ID id) throws RegraDeNegocioException, DeleteException {
		
		try {
			
			this.validaExclusao((T) getDAO().buscarPeloCodigo(id));
			
			getDAO().excluir(id);
		
		} catch (Exception e) {
			
			throw new DeleteException(e.getMessage());
		}
	}

	@Override
	public T buscaPeloCodigo(ID id) {
		return (T) getDAO().buscarPeloCodigo(id);
	}

	@Override
	public List<T> buscaTodas() {
		return getDAO().filtrar(classeDaEntidade, null, null, Arrays.asList("descricao"), null);
	}

	public abstract GenericDAO getDAO();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> listarComPaginacao(
			Paginator paginator,
			List<String> ordenacao,
			List<String> relacionamentos, 
			List<String> aliases) {
		
		return this.getDAO().listarComPaginacao(
				paginator, 
				ordenacao,
				relacionamentos, 
				aliases);
	}
	
}
