jQuery(function(){ 
	adapterScreen();
	$(window).resize(function() {
		adapterScreen();
	});
	/*
	$(window).scroll(function() {
		adapterScreen();
	});
	*/
});

function adapterScreen() {
	var windowHeight = $(window).height();
	var bodyHeight =  $(document.body).outerHeight(true);
	if(windowHeight > bodyHeight) {
		if($.fn.jquery == '3.2.1') {
			//$('#footer-wrapper').css('width', $(window).width());
			$('#footer-wrapper').affix({offset: {bottom:2}});
		}
	}
}