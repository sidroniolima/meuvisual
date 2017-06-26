package net.mv.meuespaco.model.loja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.mv.meuespaco.converter.LocalDateTimeDBConverter;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.EntidadeModel;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.util.Encryptor;

/**
 * Troca ou resgate dos produtos com finalidade de brinde.
 * 
 * @author sidronio
 * @created 03/05/2017
 */

/**
 * @author sidronio
 *
 */
@Entity
@Table(name="resgate_brinde")
public class ResgateBrinde extends EntidadeModel implements Serializable
{
	private static final long serialVersionUID = -6913155527233931069L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status_resgate")
	private StatusEscolha status;
			
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cliente_codigo")
	private Cliente cliente;
	
	@OneToMany(fetch=FetchType.LAZY,
			mappedBy="resgate",
			cascade={CascadeType.ALL},
			orphanRemoval=true)
	private List<ItemResgate> brindes;
	
	@Convert(converter=LocalDateTimeDBConverter.class)
	@Column(columnDefinition="DATETIME")	
	private LocalDateTime horario;
	
	private Long saldoAnterior;
	private Long saldoPosterior;
	
	public ResgateBrinde() 
	{
		this.horario = LocalDateTime.now();
		this.status = StatusEscolha.NOVA;
		
		brindes = new ArrayList<ItemResgate>();
		
		this.saldoAnterior = 0L;
		this.saldoPosterior = 0L;
	}
	
	public ResgateBrinde(Cliente cliente) 
	{
		this();
		this.cliente = cliente;
	}

	public ResgateBrinde(Cliente cliente, Long saldoAtual) 
	{
		this();
		this.cliente = cliente;
		this.saldoAnterior = saldoAtual;
		this.saldoPosterior = saldoAtual;
	}
	
	/* 
	 * Encripta o código.
	 */
	public String codigoEncoded()
	{
		return Encryptor.encrypt(this.codigoFormatado());
	}

	/**
	 * Substitui o status para Finalizado.
	 */
	public void finaliza() 
	{
		this.status = StatusEscolha.FINALIZADA;
	}	
	
	@Override
	public void valida() throws RegraDeNegocioException 
	{
		if (null == cliente)
		{
			throw new RegraDeNegocioException("O resgate de brinde deve ser de algum cliente.");
		}
		
		if (null == horario)
		{
			throw new RegraDeNegocioException("O horário do resgate de brinde deve ser informado.");
		}
		
		if (this.brindes.size() == 0)
		{
			throw new RegraDeNegocioException("O resgate deve conter ao menos um brinde.");
		}
	}
	
	/**
	 * Verifica se o resgate pode ser atendido se for 
	 * um Novo ou que já esteja em separação anteriormente.
	 * 
	 * @return se pode ou não ser atendido.
	 */
	public boolean isPodeSerAtendido()
	{
		return status.equals(StatusEscolha.NOVA) || status.equals(StatusEscolha.EM_SEPARACAO);
	}
	
	/**
	 * Adiciona um brinde com sua grade e quantidade.
	 * 
	 * @param brinde
	 * @param qtd
	 * @param grade
	 * @throws RegraDeNegocioException se o item não for válido.
	 */
	public void adicionaBrinde(Produto brinde, BigDecimal qtd, BigDecimal valorUnitario, Grade grade) throws RegraDeNegocioException 
	{
		this.adicionaItem(new ItemResgate(this, brinde, qtd, valorUnitario, grade));
	}

	/**
	 * Adiciona um item ao resgate.
	 * 
	 * @param itemResgate
	 * @throws RegraDeNegocioException se o item não for válido.
	 */
	private void adicionaItem(ItemResgate itemResgate) throws RegraDeNegocioException 
	{
		itemResgate.valida();
		
		this.brindes.add(itemResgate);
		
		this.saldoPosterior -= itemResgate.valorTotal().longValue();

	}

	/**
	 * Calcula o valor do resgate de acordo com os itens.
	 */
	public BigDecimal valor() 
	{
		return 
				this.brindes
					.parallelStream()
					.map(ItemResgate::valorTotal)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Calcula a quantidade de ícones.
	 * 
	 * @return
	 */
	public BigDecimal qtdDeItens()
	{
		return 
				this.brindes
					.parallelStream()
					.map(ItemResgate::getQtd)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	@Override
	public Long getCodigo() 
	{
		return this.codigo;
	}

	public StatusEscolha getStatus() {
		return status;
	}
	public void setStatus(StatusEscolha status) {
		this.status = status;
	}

	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public List<ItemResgate> getBrindes() {
		return brindes;
	}
	public void setBrindes(List<ItemResgate> brindes) {
		this.brindes = brindes;
	}
	
	public LocalDateTime getHorario() {
		return horario;
	}
	public void setHorario(LocalDateTime horario) {
		this.horario = horario;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getSaldoAnterior() {
		return saldoAnterior;
	}
	public void setSaldoAnterior(Long saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	public Long getSaldoPosterior() {
		return saldoPosterior;
	}
	public void setSaldoPosterior(Long saldoPosterior) {
		this.saldoPosterior = saldoPosterior;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResgateBrinde other = (ResgateBrinde) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
}
