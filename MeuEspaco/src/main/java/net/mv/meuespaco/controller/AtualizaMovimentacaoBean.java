package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class AtualizaMovimentacaoBean implements Serializable {

	private static final long serialVersionUID = -2350393439518658167L;

	@Inject
	private EstoqueService estoqueSrvc;
	
	public void atualizaHorario()
	{
		LocalTime inicio = LocalTime.now();
		
		String msg = "Início do processo em %s e término em %s.";
		DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("hh:mm:ss");
		
		estoqueSrvc.atualizaHorario();
		LocalTime termino = LocalTime.now();
		
		FacesUtil.addSuccessMessage(String.format(msg, inicio.format(ofPattern), termino.format(ofPattern)));
	}
	
}
