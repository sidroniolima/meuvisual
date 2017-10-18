var cronometroId;

must = '${loginBean.mustShowPriorityMessages}';
countMsg = '${loginBean.numberOfMessages()}';
idIntervalTimeOut = null;
lastUpdate = new Date().getTime();
registrouTimeOut = false;
	
list = [];

pullFromBean();

modalBody = $('#modalBody');

function registraCronometro()
{		
	cronometroId = setInterval(() => {
		pullFromBean();				
		
		$('.txt_count').text(countMsg);
	
		if (must == 'yes')
		{
			modalBody.html("");
			
			list.map(function(msg) 
			{
				row = '<div class="row">' + 			      				      	
							'<div class="col-md-2 col-xs-2">' +
								'	<p align="center"><span style="font-size:32px"><i class="fa fa-exclamation"></i></span></p>' +
							'</div>' +
							'<div class="col-md-10 col-xs-10">' +				
								'	<p>' + msg + '</p>' +
							'</div>' + 
						'</div>' +
						'<hr/>';

				modalBody.append(row);
				
			});
	      	
			$("#myModalMsgs").modal('show');
			clearInterval(cronometroId);						
		}

	}, 600000);
};
	
$("#myModalMsgs").on("hide.bs.modal", function () 
{
	pullFromBean();
	$('.txt_count').text(countMsg);
	registraCronometro();
});

$(function () 
{
	registraCronometro();	
	intervalTimeOut();
});		

$(document).click(function(){
     lastUpdate = new Date().getTime();
     mostrouModal = false;
});

function intervalTimeOut() 
{
	minutes = 600000; //1000 (1 second) * 60 (1 minute) * 10 = 600000 10 minutos
	
   	if (!registrouTimeOut)
	{
	   	mostrouModal = false;	   	

	   	idIntervalTimeOut = setInterval(function() 
		{
			if (new Date().getTime() - lastUpdate > minutes)
			{
				if (mostrouModal)
				{
					logout();
				} else
				{
					$('#myModalTimeout').modal('show');
					mostrouModal = true;
				}						 
			}
			
		}, minutes);

	   	registrouTimeOut = true;
	}
}