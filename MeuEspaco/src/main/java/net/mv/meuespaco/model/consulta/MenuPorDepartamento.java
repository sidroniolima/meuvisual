package net.mv.meuespaco.model.consulta;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.loja.Departamento;

public class MenuPorDepartamento 
{
	private Departamento departamento;
	private BigInteger depCodigo;
	private String depDescricao;
	private BigInteger gruCodigo;
	private String gruDescricao;
	private BigInteger subCodigo;
	private String subDescricao;
	
	public MenuPorDepartamento() {	}
	
	public Departamento getDepartamento() 
	{
		return departamento;
	}

	public void setDepartamento(Departamento departamento) 
	{
		this.departamento = departamento;
	}
	
	public static Map<Departamento, Map<Grupo, Map<Subgrupo, List<MenuPorDepartamento>>>> 
		constroiMenu(List<MenuPorDepartamento> itens)
	{
		return itens
			.stream()
			.collect(
					Collectors.groupingBy(
						mnu -> 
							new Departamento(new Long(mnu.getDepCodigo().longValue()), mnu.getDepDescricao()),
						Collectors.groupingBy(
							gru -> new Grupo(new Long(gru.getGruCodigo().longValue()), gru.getGruDescricao()),
						Collectors.groupingBy(
							sub -> new Subgrupo(new Long(sub.getSubCodigo().longValue()), sub.getSubDescricao())
					))));
	}
	
	public MenuPorDepartamento(
			BigInteger depCodigo, 
			String depDescricao, 
			BigInteger gruCodigo, 
			String gruDescricao) 
	{
		this.depCodigo = depCodigo;
		this.depDescricao = depDescricao;
		this.gruCodigo = gruCodigo;
		this.gruDescricao = gruDescricao;
	}

	public MenuPorDepartamento(BigInteger depCodigo, 
			String depDescricao, 
			BigInteger gruCodigo, 
			String gruDescricao,
			BigInteger subCodigo, 
			String subDescricao) 
	{
		this.depCodigo = depCodigo;
		this.depDescricao = depDescricao;
		this.gruCodigo = gruCodigo;
		this.gruDescricao = gruDescricao;
		this.subCodigo = subCodigo;
		this.subDescricao = subDescricao;
	}

	public BigInteger getDepCodigo() {
		return depCodigo;
	}
	public void setDepCodigo(BigInteger depCodigo) {
		this.depCodigo = depCodigo;
	}

	public String getDepDescricao() {
		return depDescricao;
	}
	public void setDepDescricao(String depDescricao) {
		this.depDescricao = depDescricao;
	}

	public BigInteger getGruCodigo() {
		return gruCodigo;
	}
	public void setGruCodigo(BigInteger gruCodigo) {
		this.gruCodigo = gruCodigo;
	}

	public String getGruDescricao() {
		return gruDescricao;
	}
	public void setGruDescricao(String gruDescricao) {
		this.gruDescricao = gruDescricao;
	}

	public BigInteger getSubCodigo() {
		return subCodigo;
	}

	public void setSubCodigo(BigInteger subCodigo) {
		this.subCodigo = subCodigo;
	}

	public String getSubDescricao() {
		return subDescricao;
	}
	public void setSubDescricao(String subDescricao) {
		this.subDescricao = subDescricao;
	}
}
