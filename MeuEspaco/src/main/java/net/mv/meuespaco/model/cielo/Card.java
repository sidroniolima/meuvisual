package net.mv.meuespaco.model.cielo;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.persistence.Transient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.util.DataDoSistema;
import net.mv.meuespaco.util.Luhn;

/**
 * @author sidronio
 *
 */
public class Card {

	@SerializedName("CardNumber")
	private String cardNumber;
	
	@SerializedName("Holder")
	private String holder;
	
	@SerializedName("ExpirationDate")
	private String expirationDate;
	
	@SerializedName("SecurityCode")
	private String securityCode;
	
	@SerializedName("Brand")
	private Brand brand;
	
	@Transient
	private DataDoSistema now;

	public Card() {	
		now = new DataDoSistema();
	}
	
	/**
	 * Cria com a data do sistema.
	 * 
	 * @param now
	 */
	public Card(DataDoSistema now) 
	{
		this();
		this.now = now;
	}

	public Card(String cardNumber, String holder, String expirationDate, Brand brand) 
	{
		this();
		this.cardNumber = cardNumber;
		this.holder = holder;
		this.expirationDate = expirationDate;
		this.brand = brand;
	}

	public Card(String cardNumber, String holder, String expirationDate, String securityCode, Brand brand) 
	{
		this();
		this.setCardNumber(cardNumber);
		this.holder = holder;
		this.expirationDate = expirationDate;
		this.securityCode = securityCode;
		this.brand = brand;
	}
	
	/**
	 * Cria pelo cardNumber.
	 * 
	 * @param string
	 */
	public Card(String cardNumber) 
	{
		this();
		this.setCardNumber(cardNumber);
	}
	
	/**
	 * Cria com o número e data de expiração.
	 * 
	 * @param cardNumber
	 * @param expirationDate
	 */
	public Card(String cardNumber, String expirationDate) 
	{
		this();
		this.setCardNumber(cardNumber);
		this.expirationDate = expirationDate;
	}
	
	/**
	 * Cria com o número, data de expiração e hora. 
	 * Utilizado em testes.
	 * 
	 * @param cardNumber
	 * @param expirationDate
	 * @param now
	 */
	public Card(String cardNumber, String expirationDate, DataDoSistema now) {
		this();
		this.setCardNumber(cardNumber);
		this.expirationDate = expirationDate;
		this.now = now;
	}
	
	/**
	 * Converte um objeto Crad em um Json.
	 * 
	 * @return json.
	 */
	public String toJson()
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Card.class, new CardSerializer());
		gsonBuilder.setPrettyPrinting();
		
		Gson gson = gsonBuilder.create();
		
		return gson.toJson(this);
	}
	
	/**
	 * Cria um objeto Card a partir de um Json.
	 * 
	 * @param json
	 * @return
	 */
	public Card fromJson(String json)
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Card.class, new CardDeserializer());
		
		Gson gson = gsonBuilder.serializeNulls().create();
		
		return gson.fromJson(json, Card.class);
	}

	/**
	 * Verifica se o cartão é válido ou não, de acordo com 
	 * a análise do seu número e data de expiração.
	 * 
	 * @return válido ou inválido.
	 */
	public boolean isValid() throws RegraDeNegocioException
	{
		if ((!this.validateExpirationDate()) && (!this.validateNumber()))
		{
			return false;
		}
		
		return true;
	}

	/**
	 * Valida a data de expiração.
	 * 
	 * @return
	 * @throws RegraDeNegocioException 
	 */
	public boolean validateExpirationDate() throws RegraDeNegocioException 
	{
		
		if (this.getExpirationDate().length() != 7)
		{
			throw new RegraDeNegocioException("Formato inválido. O mês deve ter 2 dígitos e o ano 4.");
		}
		
		String month = this.getExpirationDate().substring(0, 2);
		String year = this.getExpirationDate().substring(3, 7);

		LocalDate date;
		LocalDate ultimoDiaValido;
		
		try
		{
			date = LocalDate.of(Integer.valueOf(year), Integer.valueOf(month), 1);
			ultimoDiaValido = LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth());
		} catch (DateTimeException e) {
			throw new RegraDeNegocioException("O mês ou ano de vencimento é inválido.");
		}
		
		if (ultimoDiaValido.isBefore(now.hoje()))
		{
			throw new RegraDeNegocioException("O cartão está com data de expiração vencida.");
		}

		return true;
	}

	/**
	 * Valida o número do cartão de crédito.
	 * 
	 * @return se válido ou não.
	 * @throws RegraDeNegocioException 
	 */
	public boolean validateNumber() throws RegraDeNegocioException 
	{
		if ((this.cardNumber.length() == 13) ||
			(this.cardNumber.length() == 14) ||
			(this.cardNumber.length() == 15) ||			
			(this.cardNumber.length() == 16))
		{
			if (Luhn.checkSum(this.cardNumber) == this.getDigit())
			{
				this.selecionaBandeira();
				return true;
			}
		}
		
		throw new RegraDeNegocioException("Número do cartão inválido. Tente novamente ou digite outro.");
	}

	/**
	 * Retorna o dígito verificador.
	 * 
	 * @return dígito verificador.
	 */
	protected int getDigit()
	{
		return Integer.parseInt(cardNumber.substring(cardNumber.length() -1, cardNumber.length()));
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
			
		this.selecionaBandeira();
	}
	
	/**
	 * Seleciona a Bandeira.
	 */
	private void selecionaBandeira() {
		
		if (null == this.getCardNumber())
		{
			return;
		}
		
		Pattern padraoVisa = Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$");
		Pattern padraoMaster = Pattern.compile("^5[1-5][0-9]{14}$");
		Pattern padraoDiners = Pattern.compile("^3(?:0[0-5]|[68][0-9])[0-9]{11}$");
		Pattern padraoDiscover = Pattern.compile("^6(?:011|5[0-9]{2})[0-9]{12}$");
		Pattern padraoJCB = Pattern.compile("^(?:2131|1800|35\\d{3})\\d{11}$");
		Pattern padraoAura = Pattern.compile("^(50\\d{3})\\d{11}$");
		
		Map<Brand, Pattern> padroes = new HashMap<Brand, Pattern>();
		padroes.put(Brand.Visa, padraoVisa);
		padroes.put(Brand.Master, padraoMaster);
		padroes.put(Brand.Diners, padraoDiners);
		padroes.put(Brand.Discover, padraoDiscover);
		padroes.put(Brand.JCB, padraoJCB);
		padroes.put(Brand.Aura, padraoAura);

		padroes.forEach((k,v) -> {
			if(v.matcher(this.getCardNumber()).matches())
			{
				this.setBrand(k);
			}
		});
	}

	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	
}
