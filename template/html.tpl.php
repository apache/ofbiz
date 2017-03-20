<?php
/**
 * OFBiz website top level HTML template
 * arguments:
 *   $argv[1]
 */
  $page_name = $argv[1];
  
  ob_start();
  include_once($page_name);
  $content = ob_get_clean();
  
  ob_start();
  include_once('region/head.tpl.php');
  $head = ob_get_clean();
  
  ob_start();
  include_once('region/header.tpl.php');
  $header = ob_get_clean();
  
  ob_start();
  include_once('region/footer.tpl.php');
  $footer = ob_get_clean();
  
  ob_start();
  include_once('region/scripts.tpl.php');
  $scripts = ob_get_clean();
?>
<!DOCTYPE html>
<html lang="en">
<!--[if lt IE 7 ]><html class="ie ie6" lang="en"> <![endif]-->
<!--[if IE 7 ]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if (gte IE 9)|!(IE)]><!-->
<head>
<?php print $head; ?>
</head>
<body>
<?php print $header; ?>
<!-- globalWrapper -->
<div id="globalWrapper">
<?php print $content; ?>
<?php print $footer; ?>
</div>
<!-- globalWrapper -->
<?php print $scripts; ?>
</body>
</html>
