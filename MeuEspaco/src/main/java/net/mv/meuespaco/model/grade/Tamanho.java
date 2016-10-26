package net.mv.meuespaco.model.grade;

/**
 * Tamanho de peças.
 * 
 * @author Sidronio
 *
 */
public enum Tamanho {
	
	TAM_1("1"),
	TAM_2("2"),
	TAM_3("3"),
	TAM_4("4"),
	TAM_5("5"),
	TAM_6("6"),
	TAM_7("7"),
	TAM_8("8"),
	TAM_9("9"),
	TAM_10("10"),
	TAM_11("11"),
	TAM_12("12"),
	TAM_13("13"),
	TAM_14("14"),
	TAM_15("15"),
	TAM_16("16"),
	TAM_17("17"),
	TAM_18("18"),
	TAM_19("19"),
	TAM_20("20"),
	TAM_21("21"),
	TAM_22("22"),
	TAM_23("23"),
	TAM_24("24"),
	TAM_25("25"),
	TAM_26("26"),
	TAM_27("27"),
	TAM_28("28"),
	TAM_29("29"),
	TAM_30("30"),
	TAM_31("31"),
	TAM_32("32"),
	TAM_33("33"),
	TAM_P("P"),
	TAM_M("M"),
	TAM_G("G");
	
	private String descricao;
	
	private Tamanho(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public Tamanho[] tipos() {
		return Tamanho.values();
	}
	
}
