$(function() {
    // Side Bar Toggle
    $('.hide-sidebar').click(function() {
	  $('#sidebar').hide('fast', function() {
	  	$('#content').removeClass('span10');
	  	$('#content').addClass('span11');
	  	$('.hide-sidebar').hide();
	  	$('.show-sidebar').show();
	  });
	});

	$('.show-sidebar').click(function() {
		$('#content').removeClass('span11');
	   	$('#content').addClass('span10');
	   	$('.show-sidebar').hide();
	   	$('.hide-sidebar').show();
	  	$('#sidebar').show('fast');
	});
	
	$('.product-base-down').click(function() {
	  	$('.product-base-down').hide();
	  	$('.product-base-up').show();
	 });

	$('.product-base-up').click(function() {
	   	$('.product-base-up').hide();
	   	$('.product-base-down').show();
	});
	
	$('.product-introduce-down').click(function() {
	  	$('.product-introduce-down').hide();
	  	$('.product-introduce-up').show();
	 });

	$('.product-introduce-up').click(function() {
	   	$('.product-introduce-up').hide();
	   	$('.product-introduce-down').show();
	});
	
	$('.product-safe-down').click(function() {
	  	$('.product-safe-down').hide();
	  	$('.product-safe-up').show();
	 });

	$('.product-safe-up').click(function() {
	   	$('.product-safe-up').hide();
	   	$('.product-safe-down').show();
	});
});