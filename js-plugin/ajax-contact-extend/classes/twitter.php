<html>
  <head>
  </head>
  <body>
	<h2>Send Direct Messages on Twitter</h2>

	<?php if (!isset($_POST['submit'])) { ?>
	<form method="post" action="<?php echo htmlentities($_SERVER['PHP_SELF']); ?>">
	  Direct message: <br/>
	  <textarea name="message" cols="15"></textarea><br/>
	  <input type="submit" name="submit" value="Send" />
	</form>

	<?php } else {

		function twittermessage($user,$pass,$message) {
			$url = "http://twitter.com/direct_messages/new.xml";
			$ch = curl_init();
			curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
			curl_setopt($ch, CURLOPT_USERPWD, "$user:$pass");
			curl_setopt($ch, CURLOPT_URL,$url);
			curl_setopt($ch, CURLOPT_POST, 1);
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
			curl_setopt($ch, CURLOPT_POSTFIELDS,"user=$user&text=$message");
			$results = curl_exec ($ch);
			curl_close ($ch);
		}

		$user	= "jigowatt";
		$pass	= "xxxxxx";
		$message = $_POST['message'];

		twittermessage($user,$pass,$message);

	}
	?>
  </body>
</html>