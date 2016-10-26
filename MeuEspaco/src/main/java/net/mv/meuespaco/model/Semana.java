package net.mv.meuespaco.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.mv.meuespaco.converter.LocalDateDBConverter;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.util.DataDoSistema;

@Entity
@Table(name="semana")
@Vetoed
public class Semana extends EntidadeModel implements Serializable {

	private static final long serialVersionUID = 7957549549015472522L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(length=60)
	private String descricao;
	
	@Convert(converter=LocalDateDBConverter.class)
	@Column(name="data_inicial", columnDefinition="DATE")
	private LocalDate dataInicial;
	
	@Convert(converter=LocalDateDBConverter.class)
	@Column(name="data_final", columnDefinition="DATE")
	private LocalDate dataFinal;
	
	@Column(name="dias_entre_ciclos")
	private int diasEntreCiclos;
	
	@OneToMany(
			fetch=FetchType.LAZY, 
			mappedBy="semana")
	private List<Regiao> regioes;
	
	@Transient
	private DataDoSistema dataDoSistema;
	
	/**
	 * Construtor padrão.
	 */
	public Semana() {	
		dataDoSistema = new DataDoSistema();
	}
	
	public Semana(Long codigo, LocalDate dataInicial, LocalDate dataFinal, DataDoSistema dataDoSistema) {
		this.codigo = codigo;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.dataDoSistema = dataDoSistema;
	}

	/**
	 * Construir a semana com datas.
	 * 
	 * @param minusDays
	 * @param now
	 */
	public Semana(LocalDate dataInicial, LocalDate dataFinal) {
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
	}

	public Semana(Long codigo, LocalDate dataInicial, LocalDate dataFinal) {
		this.codigo = codigo;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
	}

	@Override
	public void valida() throws RegraDeNegocioException {
		
		if (null == descricao || descricao.isEmpty()) {
			throw new RegraDeNegocioException("A descrição precisa ser preenchida.");
		}

		if (null == dataInicial || null == dataFinal) {
			throw new RegraDeNegocioException("O período de atendiento deve ser informado.");
		}
		
		if (!dataInicial.isBefore(dataFinal)) {
			throw new RegraDeNegocioException("A data inicial deve ser menor que a final.");
		}
		
	}
	
	/**
	 * Verifica se o ciclo de escolha está aberto para 
	 * a semana.
	 * 
	 * @return Se está ou não aberto o ciclo.
	 */
	public boolean isCicloAberto() {
		
		LocalDate hoje = dataDoSistema.hoje();
		
		return (hoje.isEqual(this.getDataInicial()) ||
				hoje.isAfter(this.getDataInicial())) &&
				
				(hoje.isBefore(this.getDataFinal()) ||
				hoje.isEqual(this.getDataFinal()));
	}
	
	/**
	 * Calcula a quantidade de dias para o próximo ciclo.
	 * 
	 * @return Dias até o próximo ciclo ou zero se o ciclo ainda está aberto.
	 */
	public int diasAteOProximoCiclo() 
	{
		return (int) dataDoSistema.hoje().until(this.dataDoProximoCiclo(), ChronoUnit.DAYS);
	}
	
	/**
	 * Informa a data do próximo ciclo.
	 * 
	 * @return
	 */
	public LocalDate dataDoProximoCiclo() {
		
		if (isCicloAberto() || isCicloComDataAntiga()) {
			return this.dataFinal.plusDays(this.diasEntreCiclos);
		}
		
		return this.dataInicial;
	}
	
	/**
	 * Se o ciclo está fechado e não há data nova para o próximo ciclo.
	 * 
	 * @return
	 */
	private boolean isCicloComDataAntiga() {
		return dataDoSistema.hoje().compareTo(this.dataFinal) > 0;
	}

	@Override
	public String toString() {
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		return String.format(this.getDescricao() + " ( %s a %s)", 
				dateFormatter.format(dataInicial), 
				dateFormatter.format(dataFinal));
	}
	
	@Override
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(LocalDate dataInicial) {
		this.dataInicial = dataInicial;
	}

	public LocalDate getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public List<Regiao> getRegioes() {
		return regioes;
	}
	public void setRegioes(List<Regiao> regioes) {
		this.regioes = regioes;
	}
	
	/**
	 * @return the diasEntreCiclos
	 */
	public int getDiasEntreCiclos() {
		return diasEntreCiclos;
	}
	/**
	 * @param diasEntreCiclos the diasEntreCiclos to set
	 */
	public void setDiasEntreCiclos(int diasEntreCiclos) {
		this.diasEntreCiclos = diasEntreCiclos;
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
		Semana other = (Semana) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
