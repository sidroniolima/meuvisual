package net.mv.meuespaco.model.loja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

/**
 * Entidade para venda de produtos pelo site.
 * 
 * @author Sidronio
 * @created 23/08/2016
 */
@Entity
@Table(name="venda")
public class Venda extends EntidadeModel implements Serializable{

	private static final BigDecimal VALOR_MININO_PARCELAMENTO = new BigDecimal(150.00);

	private static final long serialVersionUID = -668988909071422226L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(name="unique_id", columnDefinition="VARCHAR(255)")
	private UUID uniqueId;
	
	@ManyToOne
	@JoinColumn(name="cliente_codigo")
	private Cliente cliente;
	
	@Column(name="horario_venda", columnDefinition="DATETIME")
	@Convert(converter=LocalDateTimeDBConverter.class)
	private LocalDateTime horarioVenda;
	
	@Column(name="horario_finalizacao", columnDefinition="DATETIME")
	@Convert(converter=LocalDateTimeDBConverter.class)
	private LocalDateTime horarioFinalizacao;
	
	@Enumerated(EnumType.STRING)
	private StatusVenda status;
	
	@Column(name="desconto_venda")
	private BigDecimal descontoVenda;
	
	@OneToMany(
			fetch=FetchType.LAZY,
			mappedBy="venda",
			cascade={CascadeType.ALL},
			orphanRemoval=true)
	List<ItemVenda> itens;
	
	@Column(name="payment_id")
	private String paymentId;
	
	@Column(name="received_date", columnDefinition="DATETIME")
	@Convert(converter=LocalDateTimeDBConverter.class)
	private LocalDateTime receivedDate;

	@Column(name="horario_cancelamento", columnDefinition="DATETIME")
	@Convert(converter=LocalDateTimeDBConverter.class)
	private LocalDateTime horarioCancelamento;
	
	/**
	 * Construtor padrão.
	 */
	public Venda() {
		uniqueId = UUID.randomUUID();
		itens = new ArrayList<ItemVenda>();
		status = StatusVenda.AGUARDANDO_PAGAMENTO;
		this.descontoVenda = BigDecimal.ZERO;
		this.horarioVenda = LocalDateTime.now();
	}
	
	public Venda(Cliente cliente) 
	{
		this();
		this.cliente = cliente;
	}

	/**
	 * Quantidade de itens da venda.
	 * 
	 * @return qtd de itens.
	 */
	public BigDecimal qtdDeItens()
	{
		return itens.stream().map(i -> i.getQtd()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Calcula o valor da compra.
	 * 
	 * @return valor da compra.
	 */
	public BigDecimal valor()
	{
		return itens.stream().map(i -> i.total()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Calcula o valor da venda com o desconto.
	 * 
	 * @return valor com desconto.
	 */
	public BigDecimal valorComDesconto()
	{
		BigDecimal valorDoDesconto = this.descontoVenda.divide(new BigDecimal(100));
		
		return this.valor().subtract(this.valor().multiply(valorDoDesconto));
	}
	
	/**
	 * Adiciona um item a venda, com produto, qtd e grade.
	 * 
	 * @param produto
	 * @param qtd
	 * @param grade
	 */
	public void addItem(Produto produto, BigDecimal qtd, Grade grade) {
		
		Optional<ItemVenda> optionalItem = this.achaItem(new ItemVenda(produto, grade));
		
		if (optionalItem.isPresent())
		{
			ItemVenda item = optionalItem.get();
			item.adicionaQtd(qtd);
		} else
		{
			ItemVenda novo = new ItemVenda(produto, qtd, grade);
			novo.setVenda(this);
			itens.add(novo);
		}
	}
	
	/**
	 * Remove um item pelo produto e grade.
	 * 
	 * @param anel
	 * @param gradeAnel
	 */
	public void removeItem(Produto produto, Grade grade) {
		
		Optional<ItemVenda> optionalItem = this.achaItem(new ItemVenda(produto, grade));
		
		if (optionalItem.isPresent())
		{
			this.itens.remove(optionalItem.get());
		}
	}
	
	/**
	 * Retorna o item para o produto com grade selecionada.
	 * 
	 * @param itemNovo
	 */
	protected Optional<ItemVenda> achaItem(ItemVenda itemNovo) {
		
		for (ItemVenda itemVenda : itens) {
			if (itemVenda.ehIgual(itemNovo))
			{
				return Optional.of(itemVenda);
			}
		}
		
		return Optional.empty();
	}
	
	/**
	 * Valida uma venda, verificando se possui a horário,   
	 * cliente e ao menos um item.
	 * 
	 * @throws RegraDeNegocioException se a venda for inválida.
	 */
	public void valida() throws RegraDeNegocioException{
		
		if (null == this.cliente)
		{
			throw new RegraDeNegocioException("A venda deve ser para um cliente.");
		}
		
		if (null == this.horarioVenda)
		{
			throw new RegraDeNegocioException("A venda deve possuir um horário.");
		}
		
		if (this.itens.isEmpty())
		{
			throw new RegraDeNegocioException("A venda deve conter ao menos um item.");
		}
		
	}
	
	/**
	 * Registra o pagamento da venda.
	 * 
	 * @param paymentId
	 * @param horario 
	 */
	public void registraPagamento(String paymentId, LocalDateTime horario)
	{
		this.paymentId = paymentId;
		this.receivedDate = horario;
		this.setStatus(StatusVenda.PAGAMENTO_CONFIRMADO);
	}
	
	/**
	 * Informa se a venda está paga verificando o status.
	 * 
	 * @return se paga ou não.
	 */
	public boolean isPaga() {
		return this.getStatus().equals(StatusVenda.PAGAMENTO_CONFIRMADO);
	}
	
	/**
	 * Verifica se uma venda pode ser cancelada.
	 */
	public boolean isCancelavel()
	{
		if (!this.isPaga())
		{
			return true;
		}
		
		int diaDoPagamento = this.receivedDate.get(ChronoField.DAY_OF_MONTH);
		int hoje = LocalDateTime.now().get(ChronoField.DAY_OF_MONTH);
		
		if (diaDoPagamento == hoje)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Cancela uma venda. 
	 * @throws RegraDeNegocioException 
	 */
	public void cancela() throws RegraDeNegocioException 
	{
		if (this.isCancelavel())
		{
			this.status = StatusVenda.CANCELADA;
			this.horarioCancelamento = LocalDateTime.now();
		} else 
		{
			throw new RegraDeNegocioException("Esta venda não pode ser cancelada. "
					+ "Apenas as vendas não pagas ou pagas hoje podem ser canceladas.");
		}
		
	}
	
	/**
	 * Verifica se a venda pode ser parcela analisando o valor bruto e 
	 * o mínimo exigido.
	 * 
	 * @return
	 */
	public boolean isParcelavel()
	{
		return this.valor().compareTo(VALOR_MININO_PARCELAMENTO) >= 0;
	}
	
	/**
	 * Calcula a quantidade de itens atendidos da venda.
	 * 
	 * @return
	 */
	public BigDecimal qtdAtendida()
	{
		return this.itens
				.stream()
				.map(ItemVenda::getQtdAtendido)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Calcula o valor atendido da venda.
	 * 
	 * @return
	 */
	public BigDecimal valorAtendido()
	{
		return this.itens
				.stream()
				.map(ItemVenda::valorAtendido)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Atende uma Venda alterando seu status para Finalizada 
	 * ou Parcialmente Finalizada.
	 */
	public void finaliza()
	{
		if (this.qtdAtendida().equals(this.qtdDeItens()))
		{
			this.setStatus(StatusVenda.FINALIZADA);
		} else
		{
			this.setStatus(StatusVenda.PARCIALMENTE_ATENDIDA);	
		}	
	}
	
	/**
	 * Verifica se a venda está finalizada.
	 * 
	 * @return
	 */
	public boolean isFinalizada()
	{
		return this.status.equals(StatusVenda.FINALIZADA);
	}
	
	/**
	 * Verifica se pode finalizar caso o pagamentos esteja confirmado.
	 * 
	 * @return pode ou não finalizar.
	 */
	public boolean isFinalizavel()
	{
		return this.status.equals(StatusVenda.PAGAMENTO_CONFIRMADO) ||
				this.status.equals(StatusVenda.PARCIALMENTE_ATENDIDA);
	}
	
	/**
	 * Atende todos os itens de uma só vez.
	 */
	public void atendeTodosOsItens()
	{
		this.itens
			.stream()
			.forEach(ItemVenda::atende);
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public UUID getUniqueId() {
		return uniqueId;
	}

	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public LocalDateTime getHorarioVenda() {
		return horarioVenda;
	}
	public void setHorarioVenda(LocalDateTime horarioVenda) {
		this.horarioVenda = horarioVenda;
	}
	
	public LocalDateTime getHorarioFinalizacao() {
		return horarioFinalizacao;
	}
	public void setHorarioFinalizacao(LocalDateTime horarioFinalizacao) {
		this.horarioFinalizacao = horarioFinalizacao;
	}

	public StatusVenda getStatus() {
		return status;
	}
	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public BigDecimal getDescontoVenda() {
		return descontoVenda;
	}

	public void setDescontoVenda(BigDecimal descontoVenda) {
		this.descontoVenda = descontoVenda;
	}

	public List<ItemVenda> getItens() {
		return itens;
	}
	public void setItens(List<ItemVenda> itens) {
		this.itens = itens;
	}
	
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public LocalDateTime getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(LocalDateTime receivedDate) {
		this.receivedDate = receivedDate;
	}
	
	public LocalDateTime getHorarioCancelamento() {
		return horarioCancelamento;
	}
	public void setHorarioCancelamento(LocalDateTime horarioCancelamento) {
		this.horarioCancelamento = horarioCancelamento;
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
		Venda other = (Venda) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
