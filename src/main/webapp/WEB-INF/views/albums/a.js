/*$('#map').gmap3('destroy'); 
var $map = $('#map');
					$map.gmap3({ 
						 
					getroute:{
						    options:{
						        origin:'${beanManagerItineraire.start}',
						        destination:'${beanManagerItineraire.end}',
						        travelMode: google.maps.DirectionsTravelMode.DRIVING
						    },
						    callback: function(results){
						      if (!results) return; 
						      $(this).gmap3({
						        map:{
						          options:{
						            zoom: 5,
						            mapTypeId : google.maps.MapTypeId.ROADMAP,
						            //center: [-33.879, 151.235]
						          }
						        },
						        directionsrenderer:{
						          divId:"direction",
						          options:{
						            directions:results
						          } 
						        }
						      });
						    }
						  },
						  
					 marker : {	 
						 values: [
						          
						          {
						            latLng:[44.28952958093682, 6.152559438984804],
						            options:{
						              icon: "http://maps.google.com/mapfiles/marker_green.png"
						            }
						          },
						          
						          {
						            latLng:[44.28952958093682, -1.1501188139848408],
						            events:{
						              click:function(){
						                alert("I'm the last one, and i have my own click event");
						              }
						            }
						          },
						        
								{
						        address : '${beanManagerItineraire.start}',
								options : {
									draggable : true,
								},
								events : {
									dragend : function(marker, event, context) {
														geocoder.geocode({
															'latLng' : event.latLng
														},
														function(results, status) {
															if (status == google.maps.GeocoderStatus.OK) {
																if($(".adress:focus")!=null){
																	$(".adress:focus").val(results[0].formatted_address);
																	
																	var idInput = $(".adress:focus").attr("id");
																	$('#'+idInput+'Lat').val(event.latLng.lat());
																	$('#'+idInput+'Lng').val(event.latLng.lng());
																	
																}else{
																	$('#addresspicker_map')
																	.val(results[0].formatted_address);
															
																}

															}
														});
									},
								}
								}
						        ],
							}
						  
						});
				var marker;
				$map.gmap3({
					get : {
						name : 'marker',
						callback : function(obj) {
							marker = obj;
						}
					}
				});
				var geocoder = new google.maps.Geocoder();
				$('#addresspicker_map')
				.autocomplete(
						{
							source : function(request,
									response) {
								geocoder
										.geocode(
												{
													'address' : request.term
												},
												function(
														results,
														status) {
													response($.map(
																	results,
																	function(
																			item) {
																		return {
																			value : item.formatted_address,
																			location : item.geometry.location
																		};
																	}));
												});
							},
							select : function(event, ui) {
								var map = $map.gmap3('get');
								map.setCenter(ui.item.location);
								map.setZoom(5);
								marker.setPosition(ui.item.location);
							}
						});
				}
				
				
				// ---------------------------add field adress dynamicaly----------------------------------- 
								$( "#form" ).keypress(function(e) {
								      if ( e.which == 13 ) {
								         e.preventDefault();
								      }
								    });
				
				
								var next = 1;
							    $(".add-more").click(function(e){
							        e.preventDefault();
							        next=next+1;
							        var newId = "field" + next;
							        var newContent = '<div class="input-group" style="margin-bottom: 3px;" id="field'+newId+'" >'
							        	+'<input type="hidden" id="location'+newId+'Lat" name ="location'+newId+'Lat" />'
									+'<input type="hidden" id="location'+newId+'Lng" name="location'+newId+'Lng" />'
										+'<input type="text" class="form-control" id="location'+newId+'" name="location'+newId+'" autocomplete="off" data-items="8" />'
											 +'<span class="input-group-btn">'
											+'<button class="btn btn-danger remove-me" id="'+newId+'" type="button">'
											+'<i class="icon-minus"> <!-- co --></i></button></span><br/></div>';
							        var n = $("#count").val();
									$("#count").val(next);  
							        $("#field").before(newContent);
							        
							            $('.remove-me').click(function(e){
							                e.preventDefault();
							                $("#count").val($("#count").val()-1);
							                var idRem = $(this).attr("id");
							                
							                $("#field"+idRem).remove();
							               
							            });
							    });
				
*/