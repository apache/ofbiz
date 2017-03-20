jQuery(document).ready(function() {

	$('#contactform').submit(function() {

		var action = $(this).attr('action');
		var values = $(this).serialize();

		$('#submit').attr('disabled', 'disabled').after('<img src="js-plugin/ajax-contact-extend/assets/img/ajax-loader.gif" class="loader" />');

		$("#message").slideUp(750, function() {

			$('#message').hide();

			$.post(action, values, function(data) {
                            
				$('#message').html(data);
                                
				$('#message').slideDown('slow');
                                
				$('#contactform img.loader').fadeOut('fast', function() {
					$(this).remove()
				});
				$('#submit').removeAttr('disabled');
				if (data.match('success') != null) $('#contactform').slideUp('slow');

			});

		});

		return false;

	});

});