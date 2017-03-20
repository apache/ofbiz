<?php //Variable declarations for region templates
  $head_title = '<title>The Apache OFBiz &trade; Project</title>';
?>

<!-- content -->
 	<div id="globalWrapper">
  <!-- page content -->
  <section id="content" class="sidebar">
    <header class="headerPage">
      <div class="container clearfix">
        <div class="row">
          <h1 class="span8">Downloads</h1>
          <div class="span4" id="navTrail"> <a href="index.html" class="homeLink">home</a><span>/</span><a href="template-columns.html">templates</a><span>/</span><span class="current">Sidebar</span> </div>
        </div>
      </div>
    </header>
    <div class="slice clearfix">
      <div class="container">
        <div class="row">
          <!-- sidebar -->
          <aside class="span4" id="sidebar">
            <section class="widget search clearfix">
              <h2>Search</h2>
              <div class="divider"><span></span></div>
              <form method="post" action="blog-home.html">
                <div class="input-append">
                  <input class="span3 searchInput" type="text">
                  <button class="btn search" type="button">Ok</button>
                </div>
              </form>
            </section>
            <section class="widget blogUpdates">
              <h2>Releases for Download</h2>
              <div class="divider"><span></span></div>
              <ul class="nav nav-tabs " id="myTab">
                <li class="active"><a href="#tabs-1" data-toggle="tab">Downloads</a></li>
                <li><a href="#tabs-2" data-toggle="tab">Release Notes</a></li>
              </ul>
              <div class="tab-content">
                <div class="tab-pane active" id="tabs-1">
                  <ul>
                    <li>
                      <h2>OFBiz 14.12.01</h2>
                      <p>Download OFBiz 14.12.</p>
                      <a href="#" class="moreLink">&rarr; read more</a> </li>
                    <li>
                      <h2>OFBiz 13.07</h2>
                      <p>Download OFBiz 13.07.</p>
                      <a href="#" class="moreLink">&rarr; read more</a> </li>
                     </ul>
                </div>
                <div class="tab-pane" id="tabs-2">
                  <ul>
                    <li>
                      <div class="row">
                        <div class="one_fourth">
                          <div class="imgWrapper"><img alt="client" src="images/client1.jpg"></div>
                        </div>
                        <div class="three_fourths last">
                          <p> Nullam sed tortor odio. Suspendisse tincidunt dictum nisi, nec convallis odio lacinia ac. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae. </p>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="row">
                        <div class="one_fourth">
                          <div class="imgWrapper"><img alt="client" src="images/client2.jpg"></div>
                        </div>
                        <div class="three_fourths last">
                          <p> Nullam sed tortor odio. Suspendisse tincidunt dictum nisi, nec convallis odio lacinia ac. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae. </p>
                        </div>
                      </div>
                    </li>
                    <li>
                      <div class="row">
                        <div class="one_fourth">
                          <div class="imgWrapper"><img alt="client" src="images/client3.jpg"></div>
                        </div>
                        <div class="three_fourths last">
                          <p> Nullam sed tortor odio. Suspendisse tincidunt dictum nisi, nec convallis odio lacinia ac. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae. </p>
                        </div>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </section>
          </aside>
          <!-- sidebar -->
          <div class="span8">
            <h2>Download Apache OFBiz</h2>
            <div class="divider"><span></span></div>
            <div class="imgWrapper"> <img src="images/img-fullwidth.jpg" alt="image fullwidth"> </div>
            <p> <strong> Use the links below to download Apache OFBiz releases from the "Apache Download Mirrors" page; On that page you'll also find instructions on how to verify the integrity of the release file using the signature and hashes (PGP, MD5, SHA512) available for each release. </strong> </p>
            <p> Despite our best efforts to maintain up to three active release branches, support for older branches can decrease because our project volunteers may be focused on other issues. We recommend using releases from the most recent branch wherever possible. </p>
            <h2>Apache OFBiz 14.12</h2>
            <div class="divider"><span></span></div>
            <p> facilisis porttitor nisl. Nulla scelerisque lectus id ipsum sollicitudin euismod. Mauris fermentum erat a ante tincidunt id condimentum lorem sodales. Nam lacus justo, porttitor sit amet egestas sit amet, scelerisque non nibh. Ut ultricies orci vitae nisl viverra quis tempus nulla ultricies. Etiam lorem tellus, porttitor nec fermentum sed, scelerisque eget sapien. Fusce quam turpis, bibendum eu pretium ut, vehicula eget mi. Aliquam erat volutpat. </p>
            <h2>Apache OFBiz 13.07</h2>
            <div class="divider"><span></span></div>
            <p> Released in May 2015, is the second release of the 13.07 series, that has been stabilized with bug fixes since July 2013. </p>
	    <p> Please note that in the 13.07 series the specialpurpose components are no more included with the only exception of the ecommerce component (because there are still some dependencies on it): the specialpurpose components may be released in a separate package in the future. </p>          
            <h2>Apache OFBiz 12.04 and earlier</h2>
            <div class="divider"><span></span></div>
            <p> Released in September 2014, is the latest bug fix release in the 12.04 series that contains all the features of the trunk up to April 2012. </p>
 	    <p> Old superseded releases can be found in the OFBiz archive</p>
	    <p> A description of each release in the history of OFBiz can be found here </p>
	    <p> Nightly snapshots can be found here</p>
            <h2>Security Vulnerabilities</h2>
            <div class="divider"><span></span></div>
            <p> <strong> We strongly encourage OfBiz users to report security problems affecting OFBiz to the private security mailing list of the ASF Security Team, before disclosing them in a public forum. </strong></p>
            <p> Please see the ASF Security Team page for further details and their contact information. </p>
            <h3>List of Knows Vulnerabilities</h3>
            <ul class="iconsList">
              	<li><i class="icon-pin"></i> CVE-2014-0232; affected releases: 12.04.03 and earlier versions (12.04.*), 11.04.04 and earlier versions (11.04.*); fixed in 12.04.04 and 11.04.05</li>
                <li><i class="icon-pin"></i> CVE-2013-2250; affected releases: 12.04.01, 11.04.02 and earlier versions (11.04.*), 10.04.05 and earlier versions (10.04.*); fixed in 12.04.02, 11.04.03 and 10.04.06</li>
		<li><i class="icon-pin"></i> CVE-2013-2137; affected releases: 12.04.01, 11.04.02 and earlier versions (11.04.*), 10.04.05 and earlier versions (10.04.*); fixed in 12.04.02, 11.04.03 and 10.04.06</li>
		<li><i class="icon-pin"></i> CVE-2013-0177; affected releases: 11.04.01, 10.04.04 and earlier versions (10.04.*); fixed in 11.04.02 and 10.04.05</li>
		<li><i class="icon-pin"></i> CVE-2012-3506; affected releases: 10.04.02, 10.04 (10.04.01); fixed in 10.04.03</li>
		<li><i class="icon-pin"></i> CVE-2012-1622; affected releases: 10.04 (10.04.01); fixed in 10.04.02</li>
		<li><i class="icon-pin"></i> CVE-2012-1621; affected releases: 10.04 (10.04.01); fixed in 10.04.02</li>
		<li><i class="icon-pin"></i> CVE-2010-0432; affected releases: 09.04; fixed in 09.04.01</li>
             </ul>
	</div>
        </div>
      </div>
    </div>
  </section>

<!-- content -->
