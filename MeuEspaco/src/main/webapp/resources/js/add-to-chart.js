var cod = '';

function adicionaProduto(codigoProduto) 
{
	commandAdicionaProduto({ produto : codigoProduto });
	cod = codigoProduto;
}			

function atualizaForm(codigoProduto)
{
	msgModal = $('#span-msg-modal').text();
	
	if (msgModal)
	{
		$('#modal-body-msg').html(msgModal);
		$("#modal-saldo-insuficiente").modal('show');
	} else 
	{	
		qtd_carrinho = $("#messages_counter").text();
		qtd_carrinho_collapsed = $("#messages_counter_collapsed").text();
		
		$("#messages_counter").text(++qtd_carrinho);
		$("#messages_counter_collapsed").text(++qtd_carrinho_collapsed);
		
		$("#check_"+cod).fadeTo(0,1).fadeTo(550,0);
	} 				
}

function showModalFotos(codigoInterno, descricao, idFoto, path)
{
	console.log(codigoInterno, descricao, idFoto, path);
	$('#modal-img').attr('src', path+'/imagens/meu-espaco/' + idFoto);
	$('#modal-fotos-header-text').text(codigoInterno);
	$('#modal-fotos-body-text').text(descricao);
	$('#modal-fotos-produto').modal('show');
	$('#ex1').zoom({magnify: 1.1});
}