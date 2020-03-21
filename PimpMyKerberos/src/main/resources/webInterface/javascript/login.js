    
  
	
$(document).ready(function() {
		// Check if logged
		var actualLocation=document.location.pathname;
		
		
		setInterval(function() {
				//log("checkLogged");
				checkLogged()
					}, 10000);
	    
		function checkLogged()
	    {
	    	getValueFromUrl("admin?action=isLogged",true,function(isLogged){
				//log(isLogged);
	    		if ( isLogged == "false" || isLogged == "" || isLogged == "ERROR" )
    			{
					
	    			if ( actualLocation != "/index.html" )
    					{
						
						$("#disconnect").modal({
												onApprove : function() {
													getValueFromUrl("/index.html",true,function(isLogged){
														log(isLogged);
												    		if ( isLogged == "false" || isLogged == "" || isLogged == "ERROR" )
												    			{
																console.log("Still disconnected");
																}else
																{
																document.location="/index.html";																	
																}
													});
												}
						}).modal('show');
						
						
						
	    				//document.location="waiting.html?redirect=" + actualLocation ;
    					}
					
    			}
	    	});
	    } 			    
	});

	