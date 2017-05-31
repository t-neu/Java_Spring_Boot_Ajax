var availableTagsLoop = [];
var availableTags = [];
var oldPrice = $(".finalprice").html();
var vidWidth;
var vidHeight;

//dropdown();
function dropdown() {
	availableTags = [];
	//$("#location-search").autocomplete({ source: response });
	var variable = $("#location-search").val();
	var returnVar = 0;
	$.ajax({
		url : "/dropdown/ajax",
		async : false,
		data : {
			val : variable
		},
		dataType : 'html',
		success : function(dat) {
			var json = JSON.parse(dat);
			//var arr = Object.keys(json).map(function(k) { return json[k] });
			//console.log(arr);
			for ( var i in json) {
				//console.log("-----------------");
				availableTags.push(json[i].dropdown);
				//console.log(availableTags[i]);
			}
		}
	});
	//console.log(availableTags);
	return availableTags;
}

function rebatecode() {

	var variable = $("#rebate-code").val();
	var returnVar = 0;
	var newPrice = "";
	
	$.ajax({
		url : "/check-rb",
		async : false,
		data : {
			val : variable
		},
		dataType : 'html',
		success : function(dat) {
			var json = JSON.parse(dat);
			//console.log(" 0 " + json[0] + " 1 " + json[1] + " 2 " + json[2]);
			if (json[0] != null){
				
				if (dat[2].indexOf(".") != -1) {					
					//console.log(". does exists" + dat);
				}else{
					//console.log(". doesnt exists" + dat);
					dat = dat + ".00";
					//newPrice = dat;
				}
				//var difference = parseFloat(oldprice) - parseFloat(dat);
				if(json[0] == 0){
					$(".checkout-rebate-amount").html(json[1] + "%");
				}else{
					$(".checkout-rebate-amount").html("$" + json[1] + " Off");
				}
				$(".finalprice").html(json[2]);
				$('#rebate-code').css("color", "green");
				$('#rebate-code').css('border-color', "green");
				$('#rebate-code').prop('disabled', true);
				$('#rebate-code-btn').off('click');
			}else{
				//console.log("null " + json);
				$('#rebate-code').css("color", "red");
			}
		}
	});
	return availableTags;
}

$("#rebate-code-btn").on("click",function() {
	//console.log("#rebate-code-btn click");
	rebatecode();
});
try{
	$("#location-search, #search").autocomplete({
		source : function(request, response) {
			jQuery.get("/dropdown/ajax", {
				val : request.term
			}, function(data) {
				availableTags = [];
				for ( var i in data) {
					availableTags.push(data[i].dropdown);
				}
				response(availableTags);
				//console.log(availableTags)
			});

		},
		select : function(event, ui) {
			$(event.target).val(ui.item.label);
			$('#location-search-btn').click();
			return false;
		},
		open : function(result) {

			if (navigator.userAgent.match(/(iPod|iPhone|iPad)/)) {
				$('.ui-autocomplete').off('menufocus hover mouseover');
			}
		},
		minLength : 3,
		Delay : 3,

	});
}
catch(err) {
	
}


//$('#search').clone().attr('type','tel').insertAfter('#search').prev().remove();

function getMobileOperatingSystem() {
	  var userAgent = navigator.userAgent || navigator.vendor || window.opera;

	      // Windows Phone must come first because its UA also contains "Android"
	    if (/windows phone/i.test(userAgent)) {
	        return "Windows Phone";
	    }

	    if (/android/i.test(userAgent)) {
	        return "Android";
	    }

	    // iOS detection from: http://stackoverflow.com/a/9039885/177710
	    if (/iPad|iPhone|iPod/.test(userAgent) && !window.MSStream) {
	        return "iOS";
	    }

	    return "unknown";
	}

$("#search-btn, .input-group-btn .btn").not( ".message-container .btn, #rebate-code-group .btn" )
		.on(
				"click",
				function() {
					//alert($(this).parent().parent().find("input[type!='hidden']").val());
					if ($(this).parent().parent().find("input[type!='hidden']")
							.val() != "") {
						if (window.location.href.indexOf("/") > -1) { // etc
							window.location.replace("/map/search/"
									+ $(this).parent().parent().find(
											"input[type!='hidden']").val());
						}
					}
				});
$('#location-search-btn').unbind('click').click(function() {
	//addToCart($(this).attr("id"));
});

function locationSearchAjaxCall() {
	var variable = $("#location-search").val();
	var returnVar = 0;
	$.ajax({
		url : "/location/ajax",
		async : false,
		data : {
			val : variable
		},
		dataType : 'html',
		success : function(dat) {
			var json = JSON.parse(dat);
			if (dat != "") {
				returnVar = json;
			} else {
				returnVar = "";
			}
		}
	});
	return returnVar;
}

$(".home").on("click", function() {
	moveToHome();
});

$(".my-location").on("click", function() {
	//console.log(" click my-location");
	myCurrentPosition();
});

$("#location-search-btn").on("click", function() {
	//console.log("#location-search-btn");
	var returnLocVar = locationSearchAjaxCall();
	//console.log(returnLocVar[0].latitude + ", " + returnLocVar[0].longitude);
	setLocationList(returnLocVar[0].latitude, returnLocVar[0].longitude);
	var locString = $("#location-search").val();
	$(".pre-scrollable").animate({
		scrollTop : 0
	}, "fast");
	showAllMarkers();
	clearMarkers();
});
try{
	var adTopPosition = $('.sidebar-img').offset().top;
}
catch(err) {
	
}

function SideBarFixed() {
	
	if (adTopPosition != null){
		//console.log("SideBarFixed is being called");
		if ($(window).scrollTop() > adTopPosition){
			//console.log("Add Class");
			$('.sidebar-img').addClass("sidebar-fixed");
		}else{
			$('.sidebar-img').removeClass("sidebar-fixed");
			//console.log("Remove Class");
		}
	}
}

var timeoutId = 0;
var scrollCall = 0;

$(window).scroll(function (event) {
	scrollCall++;
	//console.log("Fixed is being called " + scrollCall);
	clearTimeout(timeoutId);
	timeoutId = setTimeout(SideBarFixed, 300);
	if (scrollCall > 35){
		SideBarFixed();
		scrollCall = 0;
	}
});

$('#location-search').keypress(function(e) {
	var key = e.which;
	if (key == 13) // the enter key code
	{
		$('#location-search-btn').click();
		//$('#location-search').bind("keypress");
		return false;
	}
});

function videoDiv(){
if($('.overlay-cover').length == 0){
	vidWidth =  $(window).width() * .90;
	//console.log("vidWidth" + vidWidth);
	if (vidWidth > 600){
		vidWidth = 600;
	}
	vidHeight = vidWidth * .5625;
	 timeout = setTimeout(function(){	  
		  d = document.createElement('div');
		  adShowing = true;
		  //console.log("time out " + adShowing);
			$(d).addClass('overlay-cover')
			    .html("<div class='outer'><div class='middle'><div class='inner'><div class='video-holder' style='width:"+ vidWidth +"px'><h3 style='color: white'>How To Video</h3><iframe width='"+ vidWidth +"' height='"+ vidHeight +"' src='https://www.youtube.com/embed/_HY68NNoieE' frameborder='0' allowfullscreen></iframe></div></div></div></div>")
			    .appendTo($("body")) //main div
			.click(function () {
			    $('.overlay-cover').remove();
			    //adShowing = false;
			})
			    .hide()
			    .slideToggle(300)
			    .delay(2500)
		  }, 100000);
	 //$(".video-holder").width(vidWidth);
}
}

$("#search-btn-nav").on("click", function() {
	window.location.replace("/map/search/" + $("#search-nav").val());
});

$(".video-player-btn").on("click", function() {
	adShowing = true;
	vidWidth =  $(window).width() * .90;
	//console.log("vidWidth" + vidWidth);
	if (vidWidth > 600){
		vidWidth = 600;
	}
	vidHeight = vidWidth * .5625;
	d = document.createElement('div');
	$(d).addClass('overlay-cover')
	    .html("<div class='outer'><div class='middle'><div class='inner'><div class='video-holder' style='width:"+ vidWidth +"px'><h3 style='color: white'>How To Video</h3><iframe width='"+ vidWidth +"' height='"+ vidHeight +"' src='https://www.youtube.com/embed/_HY68NNoieE' frameborder='0' allowfullscreen></iframe></div></div></div></div>")
	    .appendTo($("body")) //main div
	.click(function () {
	    $(this).remove();
	    //adShowing = false;
	})
	    .hide()
	    .slideToggle(300)
	    .delay(2500)
	//$(".video-holder").width(vidWidth);
});

var timeout;
var adShowing = false;

if(pageTitle != "Admin"){
	
	$(document).on('touchmove',function(e){
	    touchEvent = e.originalEvent.touches[0]; //this is your usual jQuery event, with its properties such as pageX and pageY properties	    
	    //console.log('adShowing ' + adShowing);
	    if(adShowing == false){
	    	videoDiv();
	  	  }else{
	  		clearTimeout(timeout);
	  	  }
	});

	document.onmousemove = function(){
	  clearTimeout(timeout);
	  //console.log("mouse over " + adShowing);
	  if(adShowing == false){
		  videoDiv();
	  }else{
		  clearTimeout(timeout);
	  }
	}
}

$('#search-nav').keypress(function(e) {
	var key = e.which;
	if (key == 13) // the enter key code
	{
		$('#search-btn-nav').click();
		return false;
	}
});

$('#search').keypress(function(e) {
	var key = e.which;
	if (key == 13) // the enter key code
	{
		$('#search-btn').click();
		return false;
	}
});

$(window).load(function() {
	$(".search-bar").css("display", "block");
	breadcrumbSize();
	
    $("#code-txt").text("new-value");
    
	
});

$(window).resize(function() {
	layoutChange();
});

$( window ).on( "orientationchange", function( event ) {
	layoutChange();
	});

function layoutChange() {
	breadcrumbSize();
	vidWidth =  $(this).width() * .90;
	//console.log("vidWidth" + vidWidth);
	if (vidWidth > 600){
		vidWidth = 600;
	}
	vidHeight = vidWidth * .5625;
	try {
		inputPos = $("#" + inputName).position();
		$(".msg-text").html(
				inputName.charAt(0).toUpperCase() + inputName.slice(1));
		$("." + inputName + " .msg-container").css('top', inputPos.top - 35);
		$("." + inputName + ".msg-container").css('left', inputPos.left);
	} catch (e) {

	}

	if ($(this).width() < 992) {
		$(".location2").insertBefore(".location1");
	} else {
		$(".location1").insertBefore(".location2");
	}
	if ($(this).width() < 769) {
		$(".edit-button").insertAfter(".description");
		$(".mobile-divider").show();
	} else {
		$(".edit-button").insertBefore(".address");
		$(".mobile-divider").hide();
	}

	if (window.location.href.indexOf("map") > -1) { // etc
		if ($(this).width() <= 1200) {
			$("#map").css("width", "100%");
			$(".img-holder").insertBefore(".search-bar");
		} else {
			$("#map").css("width", "66.666%");
			$(".search-bar").insertBefore(".img-holder");
		}

	} else {
		$("#map").css("width", "100%");
	}
	if ($(this).width() < 769) {
		/*
		if (window.location.href.indexOf("map") > -1) {
			if($(".list-group").height() < 400){
				$(".border_box").css( "border", "none" );
				$(".border_box").css( "background-color", "transparent" );
			}
		}
		 */
		$(".remove-padding").css("padding-left", "0px");
		$(".remove-padding").css("padding-right", "0px");
		//$("#map").css( "margin-top", "20px" );
		//$("#map").css( "width", "100%" );
		$("#map").css("overflow", "hidden");
		$(".map h3").css("text-align", "center");
		$(".advertising").css("margin-bottom", "50px");
		$(".form-inline").css("width", "94%");
		$(".form-inline").css("padding-left", "3%");
		$("#dropdown-menu").css("width", "100%");
		$(".dropdown-menu").css("width", "100%");
	} else {
		$("#map").css("margin-top", "0px");
		//$("#map").css( "width", "100%" );
		$("#map").css("overflow", "hidden");
		$(".map h3").css("text-align", "left");
		$(".advertising").css("margin-bottom", "0px");
		$(".form-inline").css("padding-top", "7px");
		$("#dropdown-menu").css("width", "auto");
		$(".dropdown-menu").css("width", "auto");
	}

}

$(document).ready(function() {

	$(".phone").text(function(i, text) {
        text = text.replace(/(\d{3})(\d{3})(\d{4})/, "($1)-$2-$3");
        return text;
    });

	$("#sandbox-container .form-control").change(function() {
		$('.datepicker').hide();
	});

	$('#sandbox-container input').datepicker({
		daysOfWeekDisabled : "0",
		autoclose : true
	});

	layoutChange();
	/*
	
	if ( $(this).width() < 992 ) {
		$(".location2").insertBefore(".location1");
	}else{
		$(".location1").insertBefore(".location2");
	}
	
	 if (window.location.href.indexOf("map") > -1) { // etc
		 	if ( $(this).width() <= 1024 ) {
				$("#map").css( "width", "100%" );	
		    }else{
		    	$("#map").css( "width", "66.666%" );
		    }

	    }else{
	    	$("#map").css( "width", "100%" );
	    }
	
	if ( $(this).width() < 769 ) {
		
		if (window.location.href.indexOf("map") > -1) {
	    	if($(".list-group").height() < 400){
	    		$(".border_box").css( "border", "none" );
	    		$(".border_box").css( "background-color", "transparent" );
	    	}
		}
		
		$(".remove-padding").css( "padding-left", "0px" );
		$(".remove-padding").css( "padding-right", "0px" );
		//$("#map").css( "margin-top", "20px" );
		//$("#map").css( "width", "100%" );
		$("#map").css( "overflow", "visible" );
		$(".map h3").css( "text-align", "center" );
		$(".advertising").css( "margin-bottom", "50px" );
		$(".form-inline").css( "width", "94%" );
		$(".form-inline").css( "padding-left", "3%" );
		$("#dropdown-menu").css( "width", "100%" );
		$(".dropdown-menu").css( "width", "100%" );	
	}else{
		$("#map").css( "margin-top", "0px" );
		$("#map").css( "overflow", "hidden" );
		$(".map h3").css( "text-align", "left" );
		$(".advertising").css( "margin-bottom", "0px" );	    	
		$(".form-inline").css( "padding-top", "7px" );
		$("#dropdown-menu").css( "width", "auto" );
		$(".dropdown-menu").css( "width", "auto" );
	}
	if ( $(this).width() < 769 ) {
		$(".edit-button").insertBefore(".logo-js");
		$( ".mobile-divider" ).show();
	}else{
		$(".edit-button").insertBefore(".address");
		$( ".mobile-divider" ).hide();
	}
	 */

});

/*
$(document).ready(function(e){
    $('.search-panel .dropdown-menu').find('a').click(function(e) {
		e.preventDefault();
		var param = $(this).attr("href").replace("#","");
		var concept = $(this).text();
		$('.search-panel span#search_concept').text(concept);
		$('.input-group #search_param').val(param);
		if(param == 'zip'){
			$('#search').clone().attr('type','tel').attr('maxlength','5').insertAfter('#search').prev().remove();
		}else{
			$('#search').clone().attr('type','text').removeAttr('maxlength').insertAfter('#search').prev().remove();
		}
	});
    
});
 */

$(".details, #email-settings").on("click", function() {
	//console.log(" click details");
	$("span", this).toggleClass("glyphicon-plus glyphicon-minus");
});

$(document)
		.delegate(
				'#description',
				'keydown',
				function(e) {
					var keyCode = e.keyCode || e.which;

					if (keyCode == 9) {
						e.preventDefault();
						var start = $(this).get(0).selectionStart;
						var end = $(this).get(0).selectionEnd;

						// set textarea value to: text before caret + tab + text after caret
						$(this).val(
								$(this).val().substring(0, start) + "\t"
										+ $(this).val().substring(end));

						// put caret at right position again
						$(this).get(0).selectionStart = $(this).get(0).selectionEnd = start + 1;
					}
				});

$(document).ready(function(){
    $(".map-holder a, .map-holder .loc-button span, .loc-button-container span").tooltip({
        title : 'It works in absence of title attribute.'
    });
});

$('.bcjs').on("click", ".cd-breadcrumb .glyphicon-minus-sign", function() {
	var data = $(this).data("type");
	var url = "/cart/" + data + "/delete";
	var bc = "/breadcrumb/" + data + "/delete";
	var bcjs = "/breadcrumb/js";
	var refreshCheckout = "/refresh/checkout";
	var sitePreference = "/site-preference?site_preference=normal"
	//console.log(url);
	
	$('.breadcrumb-holder').load(url);
	//$('.checkout-location').load("/breadcrumb/" + data + "/delete");
	if(data == "location"){
		$('.checkout-location').load(bc, function() {
			
		});
	}else{
		$('.checkout-product').load(bc, function() {
			
		});
	}
	$('.checkout').load(refreshCheckout, function() {
		
	});
	/*
	$('.breadcrumb-js').load(bcjs, function() {
	});
	*/
	
	breadcrumbSize();
});

function breadcrumbSize() {
	var bcSize = 70;
	var newWidth = 0;
	var windowWidth = $(window).width();
	if(windowWidth >= 1024){
		bcSize = 56;
	}else if(768 <= windowWidth && windowWidth < 1024){
		bcSize = 56;
	}else if(600 < windowWidth && windowWidth <= 767){
		bcSize = 49.5;
	}else if(481 < windowWidth && windowWidth <= 600){
		bcSize = 46;
	}else if(359 < windowWidth && windowWidth <= 480){
		bcSize = 45;
	}else{
		bcSize = 45;
	}
	//newWidth = ($('.breadcrumb-holder').width() / 3) - bcSize;
	newWidth = (($('.breadcrumb-holder').width()) / 3) - bcSize;
	
	//console.log("bh " + ($('.breadcrumb-holder').width() / 3) + " newWidth " + newWidth);
	$('.cd-breadcrumb.triangle li > *').width(newWidth);
}

function sortStars(){
	
	//console.log("Sort Stars");
    var $wrapper = $('.company-rating, .product-rating');
    
    $($wrapper).each(function( index ) {
    	  //console.log( index );
    	  $wrapper.eq( index ).find('span').sort(function (a, b) {
    	        return +a.dataset.number - +b.dataset.number;
    	  }).appendTo( $wrapper.eq( index ) );
  		  //console.log($wrapper.eq( index ));
    });
    
    
    
}
