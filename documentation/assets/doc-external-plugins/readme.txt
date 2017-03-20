------------------------------------------
---- AJAX PHP Contact Form; Extended -----
---- Version - 1.6 -----------------------
---- Author - Jigowatt Ltd ---------------
---- URL - www.jigowatt.co.uk ------------
------------------------------------------


-------- Description ---------------------

A self-contained AJAX powered HTML / PHP contact form with Twitter Direct Messaging, easily integrated with already formed HTML or PHP pages.

-------- Quick Installation --------------

Below you will find documentation for a stand-alone install or integration into your existing website.


- Step 1

Unzip the files, be sure to check it's contents against the above file list.

-- NOTE --

Here at Jigowatt, we use a selection of Mac Desktops and Macbooks, occasionally Windows doesn't like the Files we zip up! If your download seems corrupt don't worry, please contact envato@jigowatt.co.uk and we'll send over a fresh copy of them for you.


-- BACKUP BACKUP BACKUP --

It is important you keep a fresh copy of the script files incase of corruption or mistake, we do not offer ongoing support for lost file packages.


- Step 2 - Insert the Form

This might look like a simple step, but it's an important one! Read carefully.

To include the form on your existing HTML page, open index.html in a text / web editor (Notepad / Dreamweaver) copy the code between the opening <body> and closing </body> tags and Paste it into the relevant position in your existing page.

-- This Contact Form can be integrated into any HTML or PHP page.

If your integrating into an existing PHP page, be sure to end PHP code tags ?> paste the above then open your PHP tags again <?php or if you have previous experience, you could format the form into PHP format.


- Step 3 - Include the Style / Javascript

In the header of your website be sure to include the contact form CSS Style and Javascript files.

Copy the code below between your <head> and </head> tags either in your 'header' file or the top of the document.


	<link href="assets/css/contact.css" rel="stylesheet" type="text/css" /> <!-- AJAX Contact Form Stylesheet -->

	<script type="text/javascript" src="//code.jquery.com/jquery-latest.js"></script>
	<script type="text/javascript" src="assets/js/jquery.jigowatt.js"></script><!-- AJAX Form Submit -->

Suggestion:

Instead of including the CSS Style line above, why not copy the style for the contact page into your existing stylesheet, merging / removing any conflicts i.e. body or html tags?

If you don't fancy merging the two, make sure you open up the file assets/css/contact.css and Remove the body and html css tags so they don't conflict with your existing ones! (See Optional Step 6)


- Step 4 - Configuration

File contact.php Aprox Line 12 find..

$address = "example@themeforest.net";

Edit this to contain the email address that you want the form information sent to, this is the main and important configuration option.

------------------------------------------

File contact.php apron line 17 find..

$twitter_active	= 0;
$twitter_user	= "";

This enables / disables posting contact form notifications to Twitter. e.g. $twitter_active	= 1; will enable Twitter integration. $twitter_user contains your username.

You will then see:

$consumer_key = "";
$consumer_secret = "";
$token = "";
$secret = "";

Follow the instructions in the config file to obtain these from Twitter.

- Step 5

Upload the changed and configured files to your server.


- Step 6 (Optional) - Styling Changes

Remove any conflicting CSS tags.

For the demonstration we've had to use body and html tags for demo style purposes. If you include the contact form style in your page, these tags will 'overwrite' your existing ones. Just simply remove the following from the style/contact.css file.

	/* Remove this when pasting into your Stylesheet! */

	html, body { border: 0; margin: 0; padding: 0; }
	body { font: 62.5% "Lucida Grande", "Lucida Sans Unicode", Arial, sans-serif; min-width: 100%; min-height: 101%; color: #666; background:#eee; }

	/* END Remove This */

With this removed you should have no Style conflict issues!


- Finished, how easy was that?!

Note: Send a test email using the form to test the installation. Enjoy the script.

-------- CHANGE LOG ----------------------

2012-11-10 - v1.7
 * Fix: Verification code is incorrect message

2012-09-12 - v1.6
 * New: Added checkbox + radio sample
 * Tweak: Organize files into folders
 * Tweak: Update contact.php to be more clear to edit
 * Tweak: Serialize all form values via JS, don't have to define them each in JS anymore

13/11/2011 - 1.5	Now 100% xHTML W3C valid. Change spaces to tabs. Approve .me for email validation. Make image verification easier to read. Various other code cleaning.
31/10/2011 - 1.4.2	Fix "Array" as subject for lower IE versions.
05/05/2011 - 1.4.1	Fixed JS button disabled error.
11/04/2011 - 1.4	Cookie fallback for verification.
11/03/2011 - 1.3	Improvements to the image verification.
23/09/2010 - 1.2	OAuth added so twitter DM's work. Requires configuration.
31/08/2010 - 1.1	Added utf-8 support.
21/01/2010 - 1.0 	Released.

-------- FAQ -----------------------------

There are several other configuration options in contact.php these are highlighted and described
near each line similar to the following.

// Advanced Configuration Option.
// Description and example of the configured option.

Feel free to change these options if you have previous PHP experience, but be sure to BACKUP before you edit the script files as we cannot support such modifications.

