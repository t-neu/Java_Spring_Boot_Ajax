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

$('#location-search').keypress(function(e) {
	var key = e.which;
	if (key == 13) // the enter key code
	{
		$('#location-search-btn').click();
		//$('#location-search').bind("keypress");
		return false;
	}
});

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

