package net.mv.meuespaco.model.grade;

/**
 * Tamanho de peças.
 * 
 * @author Sidronio
 *
 */
public enum Tamanho {
	
	TAM_1("1", false),
	TAM_2("2", true),
	TAM_3("3", false),
	TAM_4("4", true),
	TAM_5("5", false),
	TAM_6("6", true),
	TAM_7("7", false),
	TAM_8("8", true),
	TAM_9("9", false),
	TAM_10("10", true),
	TAM_11("11", false),
	TAM_12("12", true),
	TAM_13("13", false),
	TAM_14("14", true),
	TAM_15("15", false),
	TAM_16("16", true),
	TAM_17("17", false),
	TAM_18("18", true),
	TAM_19("19", false),
	TAM_20("20", true),
	TAM_21("21", false),
	TAM_22("22", true),
	TAM_23("23", false),
	TAM_24("24", true),
	TAM_25("25", false),
	TAM_26("26", true),
	TAM_27("27", false),
	TAM_28("28", true),
	TAM_29("29", false),
	TAM_30("30", true),
	TAM_31("31", false),
	TAM_32("32", true),
	TAM_33("33", false),
	TAM_P("P", false),
	TAM_M("M", false),
	TAM_G("G", false);
	
	private String descricao;
	private boolean listar;
	
	private Tamanho(String descricao, boolean listar) 
	{
		this.descricao = descricao;
		this.listar = listar;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public boolean isListar() 
	{
		return this.listar;
	}
	
	public Tamanho[] tipos() {
		return Tamanho.values();
	}
	
}
