dojo.ready(function() {
    var throbberNode = dojo.byId("throbber");
    
    dojo.subscribe("/dojo/io/start", function(e) {
        
        dojo.style(throbberNode, "display", "block");
    });
    
    dojo.subscribe("/dojo/io/stop", function(e) {
       
        dojo.style(throbberNode, "display", "none");
    });
    
   /* d.subscribe("/dojo/io/error", function(err, ioArgs){
    	  switch(ioArgs.xhr.status){
    	    case 403:
    	      alert("Your session expired!");
    	      break;
    	    case 500:
    	      alert("Server is down, please try again in 5 minutes.");
    	      break;
    	  }
    	});*/

}); 