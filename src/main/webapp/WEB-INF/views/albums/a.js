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
				
*/