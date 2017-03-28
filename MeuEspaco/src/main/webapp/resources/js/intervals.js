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
		
		$('#frm-nav-bar\\:txt_count').text(countMsg);
	
		if (must == 'yes')
		{
			modalBody.html("");
			
			list.map(function(msg) 
			{
				row = '<div class="row">' + 			      				      	
								'<div class="col-md-2">' +
								'	<p align="center"><span style="font-size:32px"><i class="fa fa-exclamation"></i></span></p>' +
								'</div>' +
								'<div class="col-md-10">' +				
								'	<p>' + msg + '</p>' +
								'</div>' + 
								'</div>' +
								'<hr/>';

				modalBody.append(row);
				
			});
	      	
			$("#myModalMsgs").modal('show');
			clearInterval(cronometroId);						
		}

	}, 5000);
};
	
$("#myModalMsgs").on("hide.bs.modal", function () 
{
	pullFromBean();
	$('#frm-nav-bar\\:txt_count').text(countMsg);
	registraCronometro();
});

$(function () 
{
	registraCronometro();	
	intervalTimeOut();
});		

$(document).click(function(){
     lastUpdate = new Date().getTime();
     console.log(lastUpdate);
});

function intervalTimeOut() 
{
   	if (!registrouTimeOut)
	{
	   	console.log("Interval de sessão iniciado.");

	   	mostrouModal = false;	   	

	   	idIntervalTimeOut = setInterval(function() 
		{
			if (new Date().getTime() - lastUpdate > 60000)
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
			
		}, 60000);

	   	registrouTimeOut = true;
	}
}