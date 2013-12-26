dojo.ready(function() {
    var throbberNode = dojo.byId("throbber");
    
    dojo.subscribe("/dojo/io/start", function(e) {
        
        dojo.style(throbberNode, "display", "block");
    });
    
    dojo.subscribe("/dojo/io/stop", function(e) {
       
        dojo.style(throbberNode, "display", "none");
    });
    
    /*dojo.subscribe("/dojo/io/error", function(err, ioArgs){
    	 
    	
    	switch(err.ioArgs.xhr.status){
    	    case 403:
    	      alert("Your session expired!");
    	      break;
    	    case 500:
    	      alert("Server is down, please try again in 5 minutes.");
    	      break;
    	  }
    	});*/
    

	
	
	dojo.declare('stuff', null, {
		
		addAjax:function(){
			
			
			Spring.addDecoration(new Spring.AjaxEventDecoration({
				elementId : "id_nomadeLink",
				event : "onclick",
				params : {
					fragments : "body, topheader, id_subfooter"
				}
			}));

			Spring.addDecoration(new Spring.AjaxEventDecoration({
				elementId : "id_itineraireLink",
				event : "onclick",
				params : {
					fragments : "body, topheader, id_subfooter"
				}
			}));

			Spring.addDecoration(new Spring.AjaxEventDecoration({
				elementId : "id_carnetLink",
				event : "onclick",
				params : {
					fragments : "body, topheader, id_subfooter"
				}
			}));
			
			Spring.addDecoration(new Spring.AjaxEventDecoration({
				elementId : "profil_id",
				event : "onclick",
				params : {
					fragments : "body, topheader, id_subfooter"
				}
			}));

			Spring.addDecoration(new Spring.AjaxEventDecoration({
				elementId : "allFriend",
				event : "onclick",
				params : {
					fragments : "smallTile, topheader"
				}
			}));
		
			
		},
		
		addAjaxPublicPage:function(){
			
			
			Spring.addDecoration(new Spring.AjaxEventDecoration({
				elementId : "id_nomadeLink",
				event : "onclick",
				params : {
					fragments : "smallTile, topheader, id_subfooter"
				}
			}));
	
			Spring.addDecoration(new Spring.AjaxEventDecoration({
				elementId : "id_itineraireLink",
				event : "onclick",
				params : {
					fragments : "smallTile, topheader, id_subfooter"
				}
			}));
	
			Spring.addDecoration(new Spring.AjaxEventDecoration({
				elementId : "id_carnetLink",
				event : "onclick",
				params : {
					fragments : "smallTile, topheader, id_subfooter"
				}
			}));
	
			Spring.addDecoration(new Spring.AjaxEventDecoration({
				elementId : "profil_id",
				event : "onclick",
				params : {
					fragments : "body, topheader, id_subfooter"
				}
			}));
		
		}
		
	});
	
}); 


Spring.addDecoration(new Spring.AjaxEventDecoration({
	elementId : "picManager_id",
	event : "onclick",
	params : {
		fragments : "body, topheader, id_subfooter"
	}
}));

jQuery.noConflict();
(function($) {
	
	$(document)
			.ready(
					function() {
						
						
						$('.datepicker').datepicker();
						
					});
});