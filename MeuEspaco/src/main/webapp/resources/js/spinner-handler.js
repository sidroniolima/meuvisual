function eventSpinnerHandler(data)
{
	var ajaxStatus = data.status;
	
	switch (ajaxStatus) {
	     case "begin": 
	    	 $('#modal-spinner').modal('show');
	    	 break; 		
	     case "complete": 
	    	 break;
	     case "success": 
	    	 $('#modal-spinner').modal('hide');
	    	 $("body").removeClass("modal-open");
	    	 $('.modal-backdrop').remove();
	    	 break;

    }				
}