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
		elementId : "picManager_id",
		event : "onclick",
		params : {
			fragments : "body, topheader, id_subfooter"
		}
	}));
	
	dojo.declare('stuff', null, {
		
	
		replaceJs:function(_22){
		
		var _29 = "(?:<script(.|[\n|\r])*?>)((\n|\r|.)*?)(?:</script>)";
		var _2a = [];
		var _2b = new RegExp(_29, "img");
		var _2c = new RegExp(_29, "im");
		var _2d = _22.match(_2b);
		if (_2d != null) {
			for ( var i = 0; i < _2d.length; i++) {
				var _2f = (_2d[i].match(_2c) || [ "", "", "" ])[2];
				_2f = _2f.replace(/<!--/mg, "").replace(/\/\/-->/mg, "").replace(/<!\[CDATA\[(\/\/>)*/mg, "").replace(/(<!)*\]\]>/mg, "");
				_2a.push(_2f);
			}
		}
		_22 = _22
				.replace(_2b,
						"<script> // Original script removed to avoid re-execution </script>");
		return _22;
	}
	
	});
	
}); 

jQuery.noConflict();
(function($) {

	$(document)
			.ready(
					function() {
						
						$('.datepicker').datepicker();
						
					});
});