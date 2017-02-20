package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.controller.filtro.FiltroCliente;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cpf;
import net.mv.meuespaco.model.loja.Documento;
import net.mv.meuespaco.util.Paginator;

/**
 * Abstração da camada DAO da entidade Cliente.
 * 
 * @author Sidronio
 * 22/12/2015
 */
public interface ClienteDAO extends GenericDAO<Cliente, Long> {

	/**
	 * Pesquisa um cliente pelo seu Usuário (Login).
	 * 
	 * @param usuario Usuário utilizado para loga no sistema.s
	 * @return Cliente localizado.
	 */
	public Cliente buscaClientePeloUsuario(Usuario usuario);

	/**
	 * Busca um cliente pelo código siga.
	 * 
	 * @param codigoSiga Código Siga para pesquisa.
	 * @return Cliente.
	 */
	public Cliente buscarPeloCodigoSiga(String codigoSiga);

	/**
	 * Busca um cliente pelo Cpf.
	 * 
	 * @param cpf
	 * @return
	 */
	public Cliente buscarPeloCpf(Documento cpf);

	/**
	 * Filtra os registros de Cliente pelo Codigo Siga, 
	 * Nome ou Cpf.
	 * 
	 * @param codigoSiga
	 * @param cpf
	 * @param nome
	 * @return 
	 */
	public List<Cliente> filtrarPelaPesquisa(String codigoSiga, Cpf cpf, String nome);

	/**
	 * Busca por um outro cliente com o mesmo código siga da pesquisa.
	 * 
	 * @param codigoSiga
	 * @return
	 */
	public Cliente buscarOutroPeloCodigoSiga(Long codigo, String codigoSiga);

	/**
	 * Inativa os clientes que não estão na listagem de código.
	 * 
	 * @param codigos siga dos clientes que estão ativos.
	 */
	public void inativaClientesQueNaoEstaoNaListagem(List<String> codigos);
	
	/**
	 * Filtra os registros de cliente de acordo com o filtro de Cpf, 
	 * Nome ou Código Siga.
	 * 
	 * @param filtro código siga, nome ou cpf.
	 * @param paginator
	 * @return lista de clientes com paginação.
	 */
	public List<Cliente> filtraPeloModoEspecifico(FiltroCliente filtro, Paginator paginator);
} 
