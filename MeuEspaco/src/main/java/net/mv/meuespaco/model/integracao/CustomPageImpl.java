package net.mv.meuespaco.model.integracao;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
public class CustomPageImpl<T> {

	public class Page {
		private int size;
		private int totalElements;
		private int totalPages;
		private int number;
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
		
	}
	
	public class Embedded
	{
		private MessageList messages;
		
		public MessageList getMessages() {
			return messages;
		}
	}

	@XmlAttribute
	private Page page;
	
	@XmlAttribute
	private Embedded _embedded;
	
	public Page getPage() {
		return page;
	}
	
	public Embedded get_embedded() {
		return _embedded;
	}
}
