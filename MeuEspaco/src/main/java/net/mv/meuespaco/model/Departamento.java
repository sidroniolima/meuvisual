package net.mv.meuespaco.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Departamento {

	INFANTIL("Infantil",true),
	MASCULINO("Masculino",true),
	FEMININO("Feminino",true),
	DIA_DAS_MAES("Dia das mães",false),
	DIA_DOS_NAMORADOS("Dia dos namorados",false),
	OLIMPIADAS_2016("Olimpíadas 2016",false),
	ANEIS_DE_FORMATURA("Anéis de formatura",true),	
	MATERIAL_DE_APOIO("Material de apoio",true);
	
	private String descricao;
	private boolean ativo;

	private Departamento(String descricao, boolean ativo) {
		this.descricao = descricao;
		this.ativo = ativo;
	}
	
	private Departamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public boolean isAtivo() {
		return ativo;
	}

	public static Departamento[] getDepartamentos() {
		return Departamento.values();
	}
	
	public static Departamento[] getAtivos() {
		
		List<Departamento> deps = Arrays.asList(Departamento.values())
				.stream()
				.filter(d -> d.isAtivo())
				.collect(Collectors.toList());
		
		return deps.toArray(new Departamento[]{});
	}
	
}
