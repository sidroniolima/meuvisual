package net.mv.meuespaco.model.grade;

public enum Cor {

	AMETISTA_ROSA("Ametista rosa","FFCCFF"),
	AMARELO("Amarelo","FFFF00"),
	AMETISTA_VERDE("Ametista verde","999999"),
	AMETISTA("Ametista","9966CC"),
	APATITA("Apatita","0099CC"),
	AZUL_CLARO("Azul claro", "1E90FF"),
	AZUL_ESCURO("Azul escuro", "00008B"),
	BRANCO_PEROLA("Branco pérola", "F2F2F2"),
	BRANCO_LEITOSO("Branco Leitoso","FFFFF0"),
	BRANCO("Branco", "FAFAFA"),
	BRONZE("Bronze", "886A08"),
	C_ROSA_LEITOSO("C rosa leitoso","FFCCCC"),
	CALCEDONIA("Calcedônia","EEE0E5"),
	CARAMELO("Caramelo", "B45F04"),
	CB_LEITOSO("Cb leitoso","FFFFFF"),
	CHOCOLATE("Chocolate","CD853F"),
	CINZA("Cinza", "CDC0B0"),
	CINZA_MESCLA("Cinza Mescla","CDC5BF"),
	CITRINO_CHAMPAGNE("Citrino champagne","FFFFCC"),
	CITRINO_CONHAQUE("Citrino conhaque","663300"),
	CITRINO_LIMAO("Citrino limão","CCCC66"),
	CITRINO("Citrino","FF6600"),
	COBRE("Cobre", "8A4B08"),
	CRISTAL("Cristal","CCCCCC"),
	ESMERALDA("Esmeralda","006600"),
	FUME("Fumê","666633"),
	FURTA_COR("Furta-cor", "FFFFE0"),
	JADE("Jade", "00a86b"),
	JADE_CLARA("Jade clara", "00d285"),
	JADE_ESCURA("Jade escura", "149365"),
	JASPE_MARROM("Jaspe marrom", "8B4513"),
	JADE_VERDE("Jade Verde","3CB371"),
	KUNZITA("Kunzita", "810886"),
	LARANJA("Laranja", "FFA500"),
	LILÁS("Lilás", "B23AEE"),
	LILAS_VINTAGE("Lilás Vintage", "DB7093"),
	MARFIM("Marfim", "F8ECE0"),
	MORGANITA("Morganita","FF9966"),
	NUDE("Nude","d9c8aa"),
	ONIX("Onix","000000"),
	PARAIBA_BRASIL("Paraíba brasil","33CCCC"),
	PARAIBA_VERDE("Paraíba verde","66FF99"),
	PARAIBA("Paraíba","33CCCC"),
	PINK("Pink", "FF0040"),
	PRETO("Preto", "000000"),
	RODOLITA("Rodolita","FF6699"),
	ROSA_BEBE("Rosa bebê", "F6CED8"),
	ROSA_LEITOSO("Rosa Leitoso","FFF5EE"),
	ROSA("Rosa", "F781BE"),
	RUBELITA("Rubelita","FF0066"),
	RUBI("Rubi","660033"),
	RUTILADO("Rutilado","FF9933"),
	SAFIRA_ROSA("Safira rosa","FF3399"),
	SAFIRA("Safira","0000FF"),
	SAFIRA_LIGHT("Light Safira", "2c6bcd"),
	TANZANITA("Tanzanita","9999FF"),
	TOPAZIO_IMPERIAL("Topazio imperial", "DF3A01"),
	TOPAZIO_LONDON("Topázio london","006699"),
	TOPAZIO_SKY("Topázio sky","00CCFF"),
	TOPAZIO_SWISS("Topázio swiss","0099FF"),
	TOPAZIO("Topazio", "0489B1"),
	TURMALINA_BRASIL("Turmalina Brasil","6B8E23"),	
	TURMALINA_VERDE("Turmalina verde","003300"),
	TURQUESA("Turquesa", "00CED1"),
	VERDE_ESCURO("Verde escuro", "008B00"),
	VERDE("Verde", "00FF00"),
	VERDE_LEITOSO("Verde Leitoso","F5FFFA"),
	VERMELHO("Vermelho","FF0000"),
	VINHO("Vinho","8B0000");

	private String descricao;
	private String codigoHtml;
	
	private Cor(String descricao, String codigoHtml) {
		this.descricao = descricao;
		this.codigoHtml = codigoHtml;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public String getCodigoHtml() {
		return codigoHtml;
	}

	public Cor[] tipos() {
		return Cor.values();
	}
	
}
