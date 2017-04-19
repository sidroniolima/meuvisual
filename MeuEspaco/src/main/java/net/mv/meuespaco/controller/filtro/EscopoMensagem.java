package net.mv.meuespaco.controller.filtro;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.loja.Cliente;

/**
 * Escopo da geração da mensagem: cliente (usuário), semana ou região.
 * 
 * @author sidronio
 * @created 30/03/2017 
 */
public class EscopoMensagem 
{
	
	private String codigoCliente;
	private Cliente cliente;
	
	private Semana semana;
	
	private String codigoRegiao;
	private Regiao regiao;
	
	private boolean usuariosLogados;
	
	private boolean isDefinido;
	
	/**
	 * Valida um escopo: apenas uma opção selecionada.
	 * 
	 * @throws RegraDeNegocioException
	 */
	public void valida() throws RegraDeNegocioException
	{
		if ( (null == cliente || codigoCliente.isEmpty())
				&& (null == regiao)
				&& (null == semana)
				&& !usuariosLogados)
		{
			throw new RegraDeNegocioException("O escopo para geração de mensagem deve ser definido.");
		}
		
		this.isDefinido = true;
	}

	public String getCodigoCliente() {
		return this.codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Semana getSemana() {
		return semana;
	}
	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	public String getCodigoRegiao() {
		return codigoRegiao;
	}
	public void setCodigoRegiao(String codigoRegiao) {
		this.codigoRegiao = codigoRegiao;
	}

	public Regiao getRegiao() {
		return regiao;
	}
	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public boolean isUsuariosLogados() {
		return usuariosLogados;
	}
	public void setUsuariosLogados(boolean usuariosLogados) {
		this.usuariosLogados = usuariosLogados;
	}
	
	public boolean isDefinido() 
	{
		return isDefinido;
	}

	@Override
	public String toString() {
		return "EscopoMensagem [cliente=" + cliente + ", semana=" + semana + ", regiao=" + regiao + ", usuariosLogados="
				+ usuariosLogados + ", isDefinido=" + isDefinido + "]";
	}
}
