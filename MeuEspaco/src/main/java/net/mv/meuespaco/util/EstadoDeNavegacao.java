package net.mv.meuespaco.util;

import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;

/**
 * Encapsula o estado de navegação da listagem de produtos para 
 * retornar de onde se parou quando continuar a escolha após a inserção de 
 * um item no carrinho. Abstração para Consignação e Venda.
 * 
 * @author Sidronio
 * 04/03/2016
 */
public abstract class EstadoDeNavegacao {

	private Estado estado;
	
	/**
	 * Salva um estado de navegação.
	 * 
	 * @param estado
	 */
	public void salvaEstado(
			Departamento dep, 
			Grupo grupo, 
			Subgrupo subgrupo, 
			FiltroListaProduto filtro, 
			Paginator paginator) {
		
		this.estado.setDep(dep);
		this.estado.setGrupo(grupo);
		this.estado.setSubgrupo(subgrupo);
		this.estado.setFiltro(filtro);
		this.estado.setPaginator(paginator);
		
	}
	
	/**
	 * Instancia o Estado.
	 */
	public void criaEstado() {
		this.estado = new Estado();
	}
	
	/**
	 * Verifica se há um estado já salvo, isto é, o cliente 
	 * já navegou pela listagem de produto.
	 * 
	 * @return
	 */
	public boolean isEstadoCriado() {
		return null != estado;
	}
	
	public class Estado {
		
		private FiltroListaProduto filtro;
		private Departamento dep;
		private Grupo grupo;
		private Subgrupo subgrupo;
		private Paginator paginator;
		
		/**
		 * Construtor padrão.
		 */
		public Estado() {	}
	
		public Estado(FiltroListaProduto filtro, 
				Departamento dep, 
				Grupo grupo, 
				Subgrupo subgrupo, 
				Paginator paginator) {
			
			this.filtro = filtro;
			this.dep = dep;
			this.grupo = grupo;
			this.subgrupo = subgrupo;
			this.paginator = paginator;
		}
		
		/**
		 * @return the filtro
		 */
		public FiltroListaProduto getFiltro() {
			return filtro;
		}
		/**
		 * @param filtro the filtro to set
		 */
		public void setFiltro(FiltroListaProduto filtro) {
			this.filtro = filtro;
		}
	
		/**
		 * @return the dep
		 */
		public Departamento getDep() {
			return dep;
		}
		/**
		 * @param dep the dep to set
		 */
		public void setDep(Departamento dep) {
			this.dep = dep;
		}
	
		/**
		 * @return the grupo
		 */
		public Grupo getGrupo() {
			return grupo;
		}
		/**
		 * @param grupo the grupo to set
		 */
		public void setGrupo(Grupo grupo) {
			this.grupo = grupo;
		}
	
		/**
		 * @return the subgrupo
		 */
		public Subgrupo getSubgrupo() {
			return subgrupo;
		}
		/**
		 * @param subgrupo the subgrupo to set
		 */
		public void setSubgrupo(Subgrupo subgrupo) {
			this.subgrupo = subgrupo;
		}
	
		/**
		 * @return the paginator
		 */
		public Paginator getPaginator() {
			return paginator;
		}
		/**
		 * @param paginator the paginator to set
		 */
		public void setPaginator(Paginator paginator) {
			this.paginator = paginator;
		}

		@Override
		public String toString() {
			return "Estado [filtro=" + filtro + ", dep=" + dep + ", grupo=" + grupo + ", subgrupo=" + subgrupo
					+ ", paginator=" + paginator + "]";
		}
		
	}

	/**
	 * @return the estado
	 */
	public Estado getEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
}
