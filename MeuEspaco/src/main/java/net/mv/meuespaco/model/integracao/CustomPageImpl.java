package net.mv.meuespaco.model.integracao;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class CustomPageImpl<T> {

	@XmlAttribute
	private int size;
	
	@XmlAttribute
	private int totalElements;
	
	@XmlAttribute
	private int totalPages;
	
	@XmlAttribute
	private int number;
	
	@XmlAttribute(name="content")
	private MessageList content;
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public MessageList getContent() {
		return content;
	}
	public void setContent(MessageList content) {
		this.content = content;
	}
	
}
