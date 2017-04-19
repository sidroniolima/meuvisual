/**
 * Read the message and update the icon.
 */

function read(id)	
	{
		readMsg({id : id});
	
		if ($('#read_' + id).children('i').hasClass('fa-envelope'))
		{					
			$('#read_' + id).children('i').removeClass('fa-envelope');
			$('#read_' + id).children('i').addClass('fa-envelope-open-o');
		}
	}